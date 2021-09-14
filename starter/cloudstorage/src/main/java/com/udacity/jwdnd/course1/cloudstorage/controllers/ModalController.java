package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.dbFile;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("modals")
public class ModalController {

    private final FileService fileService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private final NoteService noteService;
    //This switch was implemented to reduce the number of modal pages. This field tracks which delete modal is being clicked.
    private Integer[] deleteSwitch;
    //This is used to track which file was clicked to be viewed
    private Integer fileId;

    public ModalController(FileService fileService, EncryptionService encryptionService, CredentialService credentialService,
                           NoteService noteService) {
        this.encryptionService = encryptionService;
        this.fileService = fileService;
        this.credentialService = credentialService;
        this.noteService = noteService;
    }

    @GetMapping("viewFile")
    public String viewFile(@RequestParam("id") Integer id, Model model) {
        fileId = id;
        if(fileId > 0) {
            dbFile file = fileService.getFileById(id);
            model.addAttribute("file", file);
        } else {
            model.addAttribute("file", new dbFile("Example File Name.jpg", "image/jpeg", "0.0 MB"));
        }

        return "modals/viewFileModal";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile() throws Exception {
        if(fileId > 0) {
            return fileService.downloadFile(fileId);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("deleteFileMessage")
    public String deleteFileMessage(@RequestParam("id") Integer fileId, Model model) {
        String fileName;
        if(fileId < 0) {
            fileName = "Example Name.jpg";
        } else {
            fileName = fileService.getFileById(fileId).getFileName();
        }
        model.addAttribute("deleteMessage", "Are you sure you want to delete \""+ fileName + "\"?");
        deleteSwitch = new Integer[3];
        deleteSwitch[0] = fileId;
        return "modals/deleteFileModal";
    }

    @GetMapping("deleteNoteMessage")
    public String modal4(@RequestParam("title") String title, @RequestParam("description") String description,
                         @RequestParam("id") Integer id, Model model) {
        deleteSwitch = new Integer[3];
        deleteSwitch[2] = id;
        model.addAttribute("deleteMessage", "Are you sure you want to delete?");
        model.addAttribute("noteTitle", title);
        model.addAttribute("noteDescription", description);
        return "modals/deleteNoteModal";
    }

    @GetMapping("deleteCredentialMessage")
    public String deleteCredentialMessage(@RequestParam("url") String url, @RequestParam("username") String username,
                         @RequestParam("password") String password,@RequestParam("id") Integer id,
                         @RequestParam("key") String key, Model model) {
        deleteSwitch = new Integer[3];
        deleteSwitch[1] = id;
        model.addAttribute("deleteMessage", "Are you sure you want to delete?");
        model.addAttribute("url", url);
        model.addAttribute("username", username);
        if(id > 0) {
            password = encryptionService.decryptValue(password, key);
        }
        model.addAttribute("password", password);
        model.addAttribute("deleteCredential", true);
        return "modals/deleteCredentialModal";
    }

    @GetMapping("/delete")
    public String deleteFile(Model model) {
        if(deleteSwitch[0] != null) {
            if(deleteSwitch[0] < 0) {
                model.addAttribute("test", true);
                model.addAttribute("testMessage", "This is an example result page. Since you deleted the example file no changes were made.");
                return "result";
            }
            fileService.deleteFile(deleteSwitch[0]);
            if(fileService.doesFileExists(deleteSwitch[0])) {
                model.addAttribute("failed", "Sorry there was an error deleting your file. Please try again later.");
            } else {
                model.addAttribute("success", true);
            }

        } else if (deleteSwitch[1] != null) {
            if(deleteSwitch[1] < 0) {
                model.addAttribute("test", true);
                model.addAttribute("testMessage", "This is an example result page. Since you deleted the example credentials no changes were made.");
                return "result";
            }
            credentialService.deleteCredential(deleteSwitch[1]);
            if(credentialService.credentialExists(deleteSwitch[1])) {
                model.addAttribute("failed", "Sorry there was an error deleting your credential. Please try again later.");
            } else {
                model.addAttribute("success", true);
            }

        } else if (deleteSwitch[2] != null) {
            if(deleteSwitch[2] < 0) {
                model.addAttribute("test", true);
                model.addAttribute("testMessage", "This is an example result page. Since you deleted the example note no changes were made.");
                return "result";
            }
            noteService.deleteNote(deleteSwitch[2]);
            if(noteService.noteExists(deleteSwitch[2])) {
                model.addAttribute("failed", "Sorry there was an error deleting your credential. Please try again later.");
            } else {
                model.addAttribute("success", true);
            }
        }
        return "result";
    }

    @GetMapping("/credential")
    public String credential(@RequestParam("url") String url, @RequestParam("username") String username,
                             @RequestParam("password") String password,@RequestParam("id") Integer id,
                             @RequestParam("key") String key, Model model) {
        if(id > 0) {
           password = encryptionService.decryptValue(password, key);
        }

        model.addAttribute("url", url);
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("id", id);
        model.addAttribute("key",key);
        return "modals/credentialForm";
    }

    @GetMapping("/note")
    public String note(@RequestParam("title") String title, @RequestParam("description") String description,
                             @RequestParam("id") Integer id, Model model) {

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("id", id);
        return "modals/noteForm";
    }
}



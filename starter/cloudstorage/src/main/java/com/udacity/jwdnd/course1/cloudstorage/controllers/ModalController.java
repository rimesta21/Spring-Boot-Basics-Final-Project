package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.dbFile;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private String[] deleteSwitch;
    private String viewFileName;

    public ModalController(FileService fileService, EncryptionService encryptionService, CredentialService credentialService,
                           NoteService noteService) {
        this.encryptionService = encryptionService;
        this.fileService = fileService;
        this.credentialService = credentialService;
        this.noteService = noteService;
    }

    @GetMapping("viewFile")
    public String modal1(@RequestParam("name") String fileName, Model model, Authentication authentication) {
        viewFileName = fileName;
        dbFile file = fileService.getFileByFileName(fileName, Integer.parseInt(authentication.getName()));
        model.addAttribute("fileView", file);
        return "modals/viewFileModal";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(Authentication authentication, Model model) throws Exception {
        return fileService.downloadFile(viewFileName, Integer.parseInt(authentication.getName()));
    }

    @GetMapping("deleteFileMessage")
    public String modal2(@RequestParam("name") String fileName, Model model, Authentication authentication) {
        model.addAttribute("deleteMessage", "Are you sure you want to delete \""+ fileName + "\"?");
        deleteSwitch = new String[3];
        deleteSwitch[0] = fileName;
        model.addAttribute("deleteFile", true);
        return "modals/deleteFileModal";
    }

    @GetMapping("deleteCredentialMessage")
    public String modal3(@RequestParam("url") String url, @RequestParam("username") String username,
                         @RequestParam("password") String password,@RequestParam("id") Integer id,
                         @RequestParam("key") String key, Model model, Authentication authentication) {
        deleteSwitch = new String[3];
        deleteSwitch[1] = String.valueOf(id);
        model.addAttribute("deleteMessage", "Are you sure you want to delete?");
        model.addAttribute("url", url);
        model.addAttribute("username", username);
        password = encryptionService.decryptValue(password, key);
        model.addAttribute("password", password);
        model.addAttribute("deleteCredential", true);
        return "modals/deleteCredentialModal";
    }

    @GetMapping("/delete")
    public String deleteFile(Authentication authentication, Model model) {
        //TODO: Add a failed case
        if(deleteSwitch[0] != null) {
            fileService.deleteFile(deleteSwitch[0], Integer.parseInt(authentication.getName()));
        } else if (deleteSwitch[1] != null) {
            credentialService.deleteCredential(Integer.parseInt(deleteSwitch[1]));
        } else if (deleteSwitch[2] != null) {
            noteService.deleteNote(Integer.parseInt(deleteSwitch[2]));
        }
        model.addAttribute("success", true);
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

    @GetMapping("deleteNoteMessage")
    public String modal4(@RequestParam("title") String title, @RequestParam("description") String description,
                         @RequestParam("id") Integer id, Model model) {
        deleteSwitch = new String[3];
        deleteSwitch[2] = String.valueOf(id);
        model.addAttribute("deleteMessage", "Are you sure you want to delete?");
        model.addAttribute("noteTitle", title);
        model.addAttribute("noteDescription", description);
        return "modals/deleteNoteModal";
    }



}



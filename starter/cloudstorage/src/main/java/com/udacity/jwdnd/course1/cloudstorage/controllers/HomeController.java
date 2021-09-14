package com.udacity.jwdnd.course1.cloudstorage.controllers;



import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.dbFile;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final FileService fileService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private final NoteService noteService;


    public HomeController(FileService fileService, CredentialService credentialService, EncryptionService encryptionService,
                          NoteService noteService) {
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
        this.noteService = noteService;
    }

    @GetMapping()
    public String setUpHome(Authentication authentication, Model model) {
        int userId = Integer.parseInt(authentication.getName());
        List<dbFile> files = fileService.getUserFiles(userId);
        if(files.size() > 0) {
            model.addAttribute("files", files);
        } else {
            List<dbFile> temp = new ArrayList<>();
            temp.add(new dbFile("Example File Name.jpg", -1));
            model.addAttribute("files", temp);
        }

        List<Note> notes = noteService.getAllUserNotes(userId);
        if(notes.size() > 0) {
            model.addAttribute("notes", notes);
        } else {
            List<Note> temp = new ArrayList<>();
            temp.add(new Note("Example Note Title", "Example Note Description", -1));
            model.addAttribute("notes", temp);
        }

        List<Credential> credentials = credentialService.getAllUserCredentials(userId);
        if(credentials.size() > 0) {
            model.addAttribute("credentials", credentials);
        } else {
            List<Credential> temp = new ArrayList<>();
            temp.add(new Credential("ExampleUrl.com", "examplePassword", "exampleUsername", "1234" ,-1));
            model.addAttribute("credentials", temp);
        }

        return "home";
    }

    @PostMapping("/file-upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload,
                             Authentication authentication, Model model) {
        int userId = Integer.parseInt(authentication.getName());
        if(fileService.doesFileNameExists(fileUpload.getOriginalFilename(), userId)) {
            model.addAttribute("failed", "That file name already exists. Please change the name.");
        } else {
            int success = fileService.addFile(fileUpload, userId);
            if (success == -1) {
                model.addAttribute("failed", "It seems like there was an error converting you file. Please try again.");
            } else if (success == 0) {
                model.addAttribute("failed", "It seems like there was a network error. Please try again.");
            } else {
                model.addAttribute("success", true);
            }
        }
        return "result";
    }

    @PostMapping("/addCredential")
    public String addCredential(Credential credential, Model model, Authentication authentication) {
        int userId = Integer.parseInt(authentication.getName());
        if(credentialService.credentialExists(credential.getCredentialId())) {
            credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
            credentialService.updateCredential(credential);
            if(credentialService.verifyUpdate(credential, credentialService.getCredential(credential.getCredentialId()))) {
                model.addAttribute("success", true);
            } else {
                model.addAttribute("failed", "Sorry there was an error updating your credentials. Please try again later.");
            }
        } else {
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            String encodedKey = Base64.getEncoder().encodeToString(key);
            credential.setKey(encodedKey);
            credential.setPassword(encryptionService.encryptValue(credential.getPassword(), encodedKey));
            credential.setUserId(userId);
            if (credentialService.addCredential(credential)) {
                model.addAttribute("success", true);
            } else {
                model.addAttribute("failed", "Sorry there was an error uploading your credentials. Please try again later.");
            }
        }
        return "result";
    }

    @PostMapping("/addNote")
    public String addNote(Note note, Model model, Authentication authentication) {

        int userId = Integer.parseInt(authentication.getName());
        if(noteService.noteExists(note.getNoteId())) {
            noteService.updateNote(note);
            if(noteService.verifyUpdate(note, noteService.getNote(note.getNoteId()))) {
                model.addAttribute("success", true);
            } else {
                model.addAttribute("failed", "Sorry there was an error uploading your note. Please try again later.");
            }
        } else {
            note.setUserId(userId);
            if (noteService.addNote(note)) {
                model.addAttribute("success", true);
            } else {
                model.addAttribute("failed", "Sorry there was an error uploading your note. Please try again later.");
            }
        }
        return "result";
    }




}
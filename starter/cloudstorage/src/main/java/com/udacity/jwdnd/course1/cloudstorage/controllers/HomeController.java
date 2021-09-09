package com.udacity.jwdnd.course1.cloudstorage.controllers;



import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.dbFile;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
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

    public HomeController(FileService fileService, CredentialService credentialService, EncryptionService encryptionService) {
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String firstTime(Authentication authentication, Model model) {
        int userId = Integer.parseInt(authentication.getName());
        List<String> fileName = fileService.getFileNames(userId);
        if(fileName.size() > 0) {
            model.addAttribute("files", fileName);
        } else {
            model.addAttribute("files", "ExampleFile.txt");
        }

        List<Credential> credentials = credentialService.getAllUserCredentials(userId);
        if(credentials.size() > 0) {
            model.addAttribute("credentials", credentials);
        } else {
            List<Credential> temp = new ArrayList<>();
            temp.add(new Credential("ExampleUrl.com", "examplePassword", "exampleUsername", -1));
            model.addAttribute("credentials", temp);
        }

        return "home";
    }

    @PostMapping("/file-upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload,
                             Authentication authentication, Model model) {
        int userId = Integer.parseInt(authentication.getName());
        //TODO: Add a check to see if two files have the same name
        int success = fileService.addFile(fileUpload, userId);
        if(success == -1) {
            model.addAttribute("failed", "It seems like there was an error converting you file. Please try again.");
        } else if (success == 0) {
            model.addAttribute("failed", "It seems like there was a network error. Please try again.");
        } else {
            model.addAttribute("success", true);
            model.addAttribute("files", fileService.getFileNames(userId));
        }
        return "result";
    }

    @PostMapping("/addCredential")
    public String addCredential(Credential credential,Model model, Authentication authentication) {
        //TODO: See if you can redirect to tab once done
        String addCredentialError = null;
        credential.getKey();
        int userId = Integer.parseInt(authentication.getName());
        if(credentialService.credentialExists(credential.getCredentialId())) {
            credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
            credentialService.updateCredential(credential);
            model.addAttribute("success",true);
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


}
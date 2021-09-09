package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.dbFile;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
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
    private String tempFileName;
    private Integer tempCredentialId;
    private final EncryptionService encryptionService;

    public ModalController(FileService fileService, EncryptionService encryptionService, CredentialService credentialService) {
        this.encryptionService = encryptionService;
        this.fileService = fileService;
        this.credentialService = credentialService;
        tempFileName = null;
        tempCredentialId = null;
    }

    @GetMapping("viewFile")
    public String modal1(@RequestParam("name") String fileName, Model model, Authentication authentication) {
        tempFileName = fileName;
        dbFile file = fileService.getFileByFileName(fileName, Integer.parseInt(authentication.getName()));
        model.addAttribute("fileView", file);
        return "modals/viewFileModal";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(Authentication authentication, Model model) throws Exception {
        return fileService.downloadFile(tempFileName, Integer.parseInt(authentication.getName()));
    }

    @GetMapping("deleteFileMessage")
    public String modal2(@RequestParam("name") String fileName, Model model, Authentication authentication) {
        model.addAttribute("deleteMessage", "Are you sure you want to delete \""+ fileName + "\"?");
        tempFileName = fileName;
        model.addAttribute("deleteFile", true);
        return "modals/deleteFileModal";
    }

    @GetMapping("deleteCredentialMessage")
    public String modal3(@RequestParam("url") String url, @RequestParam("username") String username,
                         @RequestParam("password") String password,@RequestParam("id") Integer id,
                         @RequestParam("key") String key, Model model, Authentication authentication) {
        tempCredentialId = id;
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
        if(tempFileName != null) {
            fileService.deleteFile(tempFileName, Integer.parseInt(authentication.getName()));
            tempFileName = null;
        } else if (tempCredentialId != null) {
            credentialService.deleteCredential(tempCredentialId);
            tempCredentialId = null;
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



}



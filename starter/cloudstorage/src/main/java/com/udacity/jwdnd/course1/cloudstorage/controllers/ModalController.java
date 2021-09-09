package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.dbFile;
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
    private String tempFileName;

    public ModalController(FileService fileService) {
        this.fileService = fileService;
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

    @GetMapping("deleteMessage")
    public String modal2(@RequestParam("name") String fileName, Model model, Authentication authentication) {
        model.addAttribute("deleteMessage", "Are you sure you want to delete \""+ fileName + "\"?");
        tempFileName = fileName;
        return "modals/deleteFileModal";
    }

    @GetMapping("/delete")
    public String deleteFile(Authentication authentication, Model model) {
        //TODO: Add a failed case
        fileService.deleteFile(tempFileName, Integer.parseInt(authentication.getName()));
        model.addAttribute("success", true);
        return "result";
    }



}



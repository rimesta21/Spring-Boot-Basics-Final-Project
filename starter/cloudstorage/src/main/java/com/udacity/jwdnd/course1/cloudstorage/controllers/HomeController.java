package com.udacity.jwdnd.course1.cloudstorage.controllers;



import com.udacity.jwdnd.course1.cloudstorage.models.dbFile;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final FileService fileService;

    public HomeController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping()
    public String firstTime(Authentication authentication, Model model) {
        List<String> fileName = fileService.getFileNames(Integer.parseInt(authentication.getName()));
        if(fileName.size() > 0) {
            model.addAttribute("files", fileName);
        } else {
            model.addAttribute("files", "ExampleFile.txt");
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

}
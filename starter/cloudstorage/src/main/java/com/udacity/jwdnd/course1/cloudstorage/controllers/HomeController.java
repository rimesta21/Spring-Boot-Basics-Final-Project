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

    @GetMapping("/view-delete")
    public String viewDeleteFile(@RequestParam(value = "action") String action,
                             Authentication authentication, Model model) {

        if(action.contains("delete")) {
            model.addAttribute("deleteFile",true);
            model.addAttribute("deleteMessage", "Are you sure you want to delete \""+ action.substring(6) + "\"?");
            model.addAttribute("deleteMessageName", action.substring(6));
            return "result";
        }
        dbFile file = fileService.getFileByFileName(action.substring(6), Integer.parseInt(authentication.getName()));
        model.addAttribute("viewFile", true);
        model.addAttribute("fileView", file);
        return "result";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "fileName") String fileName,
                                                 Authentication authentication, Model model) throws Exception {
        return fileService.downloadFile(fileName, Integer.parseInt(authentication.getName()));
    }

    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam(value = "fileName") String fileName, Authentication authentication, Model model) {
        fileService.deleteFile(fileName, Integer.parseInt(authentication.getName()));
        model.addAttribute("success", true);
        return "result";
    }

}
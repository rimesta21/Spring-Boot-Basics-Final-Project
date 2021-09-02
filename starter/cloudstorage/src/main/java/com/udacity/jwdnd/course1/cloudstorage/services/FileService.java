package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int addFile(MultipartFile mpFile, int userId) {
        try {
            File file = new File(mpFile.getOriginalFilename(), mpFile.getContentType(), String.valueOf(mpFile.getSize()), userId, mpFile.getBytes());
            if(fileMapper.insert(file) < 0) {
                //upload to the database failed
                return 0;
            }
        } catch(IOException e) {
            //access error
            return -1;
        }
        //mission success
        return 1;
    }

    public List<String> getFileNames(int userId) {
        return fileMapper.getFileNames(userId);
    }


}

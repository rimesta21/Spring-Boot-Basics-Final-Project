package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.dbFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
            dbFile file = new dbFile(mpFile.getOriginalFilename(), mpFile.getContentType(), String.valueOf(mpFile.getSize()), userId, mpFile.getBytes());
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

    public List<dbFile> getUserFiles(int userId) {
        return fileMapper.getUserFiles(userId);
    }

    public dbFile getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public boolean doesFileNameExists(String filename, Integer userId) {
        return fileMapper.getFileByName(filename, userId) != null;
    }

    public ResponseEntity<Resource> downloadFile(Integer fileId) throws Exception
    {
        try
        {
            dbFile file = fileMapper.getFileById(fileId);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName()+ "\"")
                    .body(new ByteArrayResource(file.getFileData()));
        }
        catch(Exception e)
        {
            throw new Exception("Error downloading file");
        }
    }

    public void deleteFile(Integer fileId) {
        fileMapper.delete(fileId);
    }

    public boolean doesFileExists(Integer fileId) {
        return fileMapper.getFileById(fileId) != null;
    }



}

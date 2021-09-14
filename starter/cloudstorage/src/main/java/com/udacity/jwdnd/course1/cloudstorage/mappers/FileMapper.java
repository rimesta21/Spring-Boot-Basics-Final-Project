package com.udacity.jwdnd.course1.cloudstorage.mappers;


import com.udacity.jwdnd.course1.cloudstorage.models.dbFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("INSERT INTO FILES (fileName, contentType, fileSize, userId, fileData) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(dbFile file);

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<dbFile>  getUserFiles(int userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    dbFile getFileById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE fileName = #{fileName} AND userId = #{userId}")
    dbFile getFileByName(String fileName, Integer userId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void delete(Integer fileId);

}

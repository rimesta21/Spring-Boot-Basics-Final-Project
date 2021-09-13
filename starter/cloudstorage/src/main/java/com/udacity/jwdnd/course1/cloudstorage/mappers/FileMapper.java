package com.udacity.jwdnd.course1.cloudstorage.mappers;


import com.udacity.jwdnd.course1.cloudstorage.models.dbFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("INSERT INTO FILES (fileName, contentType, fileSize, userId, fileData) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(dbFile file);

    @Select("SELECT fileName FROM FILES WHERE userId = #{userId}")
    List<String>  getFileNames(int userId);

    @Select("SELECT * FROM FILES WHERE fileName = #{fileName} AND userId = #{userId}")
    dbFile getFileByFileName(String fileName, Integer userId);

    @Delete("DELETE FROM FILES WHERE fileName = #{fileName} AND userId = #{userId}")
    void delete(String fileName, Integer userId);

}

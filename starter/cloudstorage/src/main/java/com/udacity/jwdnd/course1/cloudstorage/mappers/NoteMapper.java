package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Insert("INSERT INTO NOTES (noteTitle, noteDescription, userId) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note getNote(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getAllUserNotes(Integer userId);

    @Update("UPDATE NOTES SET noteTitle = #{noteTitle}, noteDescription = #{noteDescription} WHERE noteId = #{noteId}")
    void updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteId = #{id}")
    void delete(Integer id);

    @Select("SELECT * FROM NOTES WHERE noteTitle = #{noteTitle} AND noteDescription = #{noteDescription} AND userId = #{userId}")
    Note checkIfNoteExist(String noteTitle, String noteDescription ,Integer userId);
}

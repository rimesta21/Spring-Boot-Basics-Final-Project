package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {this.noteMapper = noteMapper;}

    public boolean addNote(Note note) {
        return noteMapper.insert(note) >= 0;
    }

    public boolean noteExists(int noteId) {
        return noteMapper.getNote(noteId) != null;
    }

    public List<Note> getAllUserNotes(int userId) {
        return noteMapper.getAllUserNotes(userId);
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }

    public void deleteNote(Integer id) {
        noteMapper.delete(id);
    }
}

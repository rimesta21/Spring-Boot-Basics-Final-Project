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

    public boolean verifyUpdate(Note newNote, Note inDBNote) {
        return newNote.getNoteTitle().equals(inDBNote.getNoteTitle())
                && newNote.getNoteDescription().equals(inDBNote.getNoteDescription());
    }


    public Note getNote(Integer id) {return noteMapper.getNote(id);}

    public int noteError(Note note, Integer userId) {
        if(note.getNoteTitle().length() > 20) {
            //title too long
            return -1;
        } else if(note.getNoteDescription().length() > 1000) {
            //description too long
            return 0;
        } else if(noteMapper.checkIfNoteExist(note.getNoteTitle(), note.getNoteDescription(), userId) != null) {
            //duplicate note
            return -2;
        }
        //It's okay to be added
        return 1;
    }

}

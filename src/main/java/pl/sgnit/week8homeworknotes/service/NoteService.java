package pl.sgnit.week8homeworknotes.service;

import org.springframework.stereotype.Service;
import pl.sgnit.week8homeworknotes.model.Note;
import pl.sgnit.week8homeworknotes.repository.NoteRepository;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public List<Note> filterNotes(String title, String note) {
        if ((title==null || title.isEmpty()) && (note ==null && note.isEmpty())) {
            return findAll();
        }
        if (title == null || title.isEmpty()) {
            return noteRepository.filterNotesByNote(note);
        }
        if (note == null || note.isEmpty()) {
            return noteRepository.filterNotesByTitle(title);
        }
        return noteRepository.filterNotesByTitleAndNote(title, note);
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }

    public void save(Note note) {
        noteRepository.save(note);
    }
}

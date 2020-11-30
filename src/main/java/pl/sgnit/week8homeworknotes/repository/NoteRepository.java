package pl.sgnit.week8homeworknotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.sgnit.week8homeworknotes.model.Note;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("select n from Note n " +
            "where lower(n.title) like '%'||:title||'%' and lower(n.note) like '%'||:note||'%'")
    List<Note> filterNotesByTitleAndNote(@Param("title") String title, @Param("note") String note);

    @Query("select n from Note n " +
            "where lower(n.title) like '%'||:title||'%'")
    List<Note> filterNotesByTitle(@Param("title") String title);

    @Query("select n from Note n " +
        "where lower(n.note) like '%'||:note||'%'")
    List<Note> filterNotesByNote(@Param("note") String note);
}

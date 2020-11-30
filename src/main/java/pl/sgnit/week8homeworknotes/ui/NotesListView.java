package pl.sgnit.week8homeworknotes.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import pl.sgnit.week8homeworknotes.model.Note;
import pl.sgnit.week8homeworknotes.service.NoteService;

@Route("")
public class NotesListView extends VerticalLayout {
    private final NoteService noteService;

    private TextField filterTitle;
    private TextField filterNote;
    private Grid<Note> grid;

    private NoteForm form;

    public NotesListView(NoteService noteService) {
        this.noteService = noteService;
        createViewLayout();
        updateNoteList();
    }

    private void createViewLayout() {
        setSizeFull();
        createFilterControls();
        createGridControl();
    }

    private void createFilterControls() {
        filterTitle = new TextField();
        filterNote = new TextField();

        configureFilterTextField(filterTitle, "Filter by title...");
        configureFilterTextField(filterNote, "Filter by note...");

        Button addNoteButton = new Button("Add note", click -> addNote());

        VerticalLayout verticalLayout = new VerticalLayout(
            new H2("Notepad"),
            new HorizontalLayout(filterTitle, filterNote, addNoteButton)
        );

        add(verticalLayout);
    }

    private void configureFilterTextField(TextField textField, String placeholder) {
        textField.setPlaceholder(placeholder);
        textField.setClearButtonVisible(true);
        textField.setValueChangeMode(ValueChangeMode.LAZY);
        textField.addValueChangeListener(event -> updateNoteList());
    }

    private void updateNoteList() {
        grid.setItems(noteService.filterNotes(filterTitle.getValue(), filterNote.getValue()));
    }

    private void createGridControl() {
        grid = new Grid<>(Note.class);
        grid.setSizeFull();
        grid.removeColumnByKey("updateTime");
        grid.removeColumnByKey("createTime");
        grid.setColumns("title", "note");
        grid.asSingleSelect().addValueChangeListener(event -> editNote(event.getValue()));

        form = new NoteForm();
        form.addListener(NoteForm.SaveEvent.class, this::saveContact);
        form.addListener(NoteForm.DeleteEvent.class, this::deleteContact);
        form.addListener(NoteForm.CloseEvent.class, event -> closeEditor());

        Div content = new Div(grid, form);
        content.getStyle().set("display", "flex");
        content.setSizeFull();

        add(content);
        closeEditor();
    }

    private void addNote() {
        grid.asSingleSelect().clear();
        editNote(new Note());
    }

    private void editNote(Note note) {
        if (note == null) {
            closeEditor();
        } else {
            form.setNote(note);
            form.setVisible(true);
        }
    }

    private void closeEditor() {
        form.setNote(null);
        form.setVisible(false);
    }

    private void deleteContact(NoteForm.DeleteEvent event) {
        noteService.delete(event.getNote());
        updateNoteList();
        closeEditor();
    }

    private void saveContact(NoteForm.SaveEvent event) {
        noteService.save(event.getNote());
        updateNoteList();
        closeEditor();
    }
}

package pl.sgnit.week8homeworknotes.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.hibernate.event.spi.DeleteEvent;
import pl.sgnit.week8homeworknotes.model.Note;

public class NoteForm extends FormLayout {

    private TextField title = new TextField("Title");
    private TextArea note = new TextArea("Note");

    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button close = new Button("Cancel");

    private Binder<Note> binder = new Binder<>(Note.class);

    public NoteForm() {

        binder.bindInstanceFields(this);
        add(title, note, createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(click -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(event -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setNote(Note note) {
        binder.setBean(note);
    }

    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<NoteForm> {
        private Note note;

        protected ContactFormEvent(NoteForm source, Note note) {
            super(source, false);
            this.note = note;
        }

        public Note getNote() {
            return note;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(NoteForm source, Note note) {
            super(source, note);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(NoteForm source, Note note) {
            super(source, note);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(NoteForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

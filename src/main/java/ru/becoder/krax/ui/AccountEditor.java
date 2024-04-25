package ru.becoder.krax.ui;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.becoder.krax.model.Account;
import ru.becoder.krax.repository.AccountRepository;

/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in MainView.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX.
 */

@SpringComponent
@UIScope
public class AccountEditor extends VerticalLayout implements KeyNotifier {

    private final AccountRepository repository;

    /**
     * The currently edited customer
     */
    private Account account;
    TextField name = new TextField("Name");
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    Binder<Account> binder = new Binder<>(Account.class);

    @Setter
    private ChangeHandler changeHandler;

    public AccountEditor(AccountRepository repository) {
        this.repository = repository;

        add(name, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editAccount(account));
        setVisible(false);
    }

    void delete() {
        repository.delete(account);
        changeHandler.onChange();
    }

    void save() {
        repository.save(account);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editAccount(Account account) {
        if (account == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = account.getId() != -1;
        if (persisted) {
            this.account = repository.findById(account.getId()).get();
        } else {
            this.account = account;
        }
        cancel.setVisible(persisted);
        binder.setBean(this.account);
        setVisible(true);
        name.focus();
    }

}
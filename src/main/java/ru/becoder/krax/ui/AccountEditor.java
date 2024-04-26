package ru.becoder.krax.ui;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;

import ru.becoder.krax.data.dto.RegisterRequest;
import ru.becoder.krax.service.AccountService;
import ru.becoder.krax.data.model.Account;
import ru.becoder.krax.service.AuthenticationService;

@SpringComponent
@UIScope
public class AccountEditor extends VerticalLayout implements KeyNotifier {

    private final AccountService accountService;
    private final AuthenticationService authenticationService;
    private Account account;
    TextField name = new TextField("Name");
    TextField balance = new TextField("Balance");
    TextField password = new TextField("Password");
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, delete);
    Binder<Account> binder = new Binder<>(Account.class);

    @Setter
    private ChangeHandler changeHandler;

    public AccountEditor(AccountService accountService, AuthenticationService authenticationService) {
        this.accountService = accountService;
        this.authenticationService = authenticationService;

        add(name, password, balance, actions);
        balance.setVisible(false);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        setVisible(false);
    }

    void delete() {
        accountService.deleteAccount(account.getId());
        changeHandler.onChange();
    }

    void save() {
        if (balance.isVisible()) {
            accountService.updateAccount(account.getId(), account.getBalance());
        } else {
            authenticationService.createAccount(
                    RegisterRequest
                            .builder()
                            .username(account.getName())
                            .password(account.getPassword())
                            .build());
        }
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
            this.account = accountService.getAccount(account.getId());
        } else {
            this.account = account;
        }
        balance.setVisible(persisted);
        password.setVisible(!persisted);
        binder.setBean(this.account);
        setVisible(true);
        name.focus();
    }

}
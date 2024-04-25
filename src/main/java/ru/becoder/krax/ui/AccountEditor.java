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
import ru.becoder.krax.dto.AccountRequest;
import ru.becoder.krax.model.Account;
import ru.becoder.krax.service.AccountService;

@SpringComponent
@UIScope
public class AccountEditor extends VerticalLayout implements KeyNotifier {

    private final AccountService accountService;
    private Account account;
    TextField name = new TextField("Name");
    TextField balance = new TextField("Balance");
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, delete);
    Binder<Account> binder = new Binder<>(Account.class);

    @Setter
    private ChangeHandler changeHandler;

    public AccountEditor(AccountService accountService) {
        this.accountService = accountService;

        add(name, balance, actions);
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
            accountService.createAccount(AccountRequest.builder().name(account.getName()).build());
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
        binder.setBean(this.account);
        setVisible(true);
        name.focus();
    }

}
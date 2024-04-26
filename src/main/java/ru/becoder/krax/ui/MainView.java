package ru.becoder.krax.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import ru.becoder.krax.data.model.Account;
import ru.becoder.krax.repository.AccountRepository;

@Route
@RolesAllowed("ADMIN")
public class MainView extends VerticalLayout {

    private final AccountRepository repository;

    private final AccountEditor editor;

    final Grid<Account> grid;

    private final Button addNewBtn;

    public MainView(AccountRepository repository, AccountEditor editor) {
        this.repository = repository;
        this.editor = editor;
        this.grid = new Grid<>(Account.class);
        this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(this.addNewBtn);
        add(actions, this.grid, editor);

        this.grid.setHeight("300px");
        this.grid.setColumns("id", "name", "balance");
        this.grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);



        this.grid.asSingleSelect().addValueChangeListener(e -> {
            this.editor.editAccount(e.getValue());
        });

        this.addNewBtn.addClickListener(e -> this.editor.editAccount(Account
                .builder()
                .id(-1L)
                .name("")
                .password("")
                .build()));

        editor.setChangeHandler(() -> {
            this.editor.setVisible(false);
            listCustomers();
        });

        listCustomers();
    }

    void listCustomers() {
        grid.setItems(repository.findAll());
    }

}
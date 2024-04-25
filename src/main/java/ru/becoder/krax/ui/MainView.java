package ru.becoder.krax.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import ru.becoder.krax.model.Account;
import ru.becoder.krax.repository.AccountRepository;

@Route("")
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

        HorizontalLayout actions = new HorizontalLayout(addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "name", "balance");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);



        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editAccount(e.getValue());
        });

        addNewBtn.addClickListener(e -> editor.editAccount(Account.builder().id(-1).name("").build()));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers();
        });

        listCustomers();
    }

    void listCustomers() {
        grid.setItems(repository.findAll());
    }

}
package org.example.transactionmanager.Controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.transactionmanager.Entity.Transaction;
import org.example.transactionmanager.Entity.TransactionType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DisplayController {

    @FXML private TableView<Transaction> transactionTableView;
    @FXML private TableColumn<Transaction, Integer> idColumn;
    @FXML private TableColumn<Transaction, String> descriptionColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> categoryColumn;
    @FXML private TableColumn<Transaction, LocalDate> dateColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TextField search;

    private List<Transaction> allTransactions;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        descriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));

        amountColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());

        categoryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory()));

        dateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDate()));

        typeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getType().toString()));

        allTransactions = FXCollections.observableArrayList();
        transactionTableView.setItems(FXCollections.observableArrayList(allTransactions));
    }

    public void setTransactions(List<Transaction> transactions) {
        this.allTransactions = transactions;
        transactionTableView.setItems(FXCollections.observableArrayList(transactions));
    }

    public void handleSearch() {
        String searchText = search.getText().toLowerCase();
        List<Transaction> filteredTransactions = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (Transaction transaction : allTransactions) {
            boolean maches = false;

            if (transaction.getDescription().toLowerCase().contains(searchText) ||
                    transaction.getCategory().toLowerCase().contains(searchText) ||
                    String.valueOf(transaction.getAmount()).contains(searchText) ||
                    transaction.getType().toString().toLowerCase().contains(searchText)) {
                filteredTransactions.add(transaction);
            }
            try {
                LocalDate searchDate = LocalDate.parse(searchText, dateFormatter);
                if (transaction.getDate().isEqual(searchDate)) {
                    maches = true;
                }
            } catch (DateTimeParseException e) {}
            if (maches) {
                filteredTransactions.add(transaction);
            }
        }

        transactionTableView.setItems(FXCollections.observableArrayList(filteredTransactions));
    }

    public void sortByAmountAscending() {
        int n = allTransactions.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (allTransactions.get(j).getAmount() < allTransactions.get(minIndex).getAmount()) {
                    minIndex = j;
                }
            }
            Transaction temp = allTransactions.get(minIndex);
            allTransactions.set(minIndex, allTransactions.get(i));
            allTransactions.set(i, temp);
        }
        transactionTableView.setItems(FXCollections.observableArrayList(allTransactions));
    }

    public void sortByAmountDescending() {
        int n = allTransactions.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (allTransactions.get(j).getAmount() < allTransactions.get(j + 1).getAmount()) {
                    Transaction temp = allTransactions.get(j);
                    allTransactions.set(j, allTransactions.get(j + 1));
                    allTransactions.set(j + 1, temp);
                }
            }
        }
        transactionTableView.setItems(FXCollections.observableArrayList(allTransactions));
    }

    public void sortByDateNew() {
        int n = allTransactions.size();
        for (int i = 1; i < n; i++) {
            Transaction key = allTransactions.get(i);
            int j = i - 1;

            while (j >= 0 && allTransactions.get(j).getDate().isBefore(key.getDate())) {
                allTransactions.set(j + 1, allTransactions.get(j));
                j = j - 1;
            }
            allTransactions.set(j + 1, key);
        }
        transactionTableView.setItems(FXCollections.observableArrayList(allTransactions));
    }

    public void sortByDateOld() {
        int n = allTransactions.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (allTransactions.get(j).getDate().isBefore(allTransactions.get(minIndex).getDate())) {
                    minIndex = j;
                }
            }
            Transaction temp = allTransactions.get(minIndex);
            allTransactions.set(minIndex, allTransactions.get(i));
            allTransactions.set(i, temp);
        }
        transactionTableView.setItems(FXCollections.observableArrayList(allTransactions));
    }
}

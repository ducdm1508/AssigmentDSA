package org.example.transactionmanager.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.transactionmanager.Entity.Transaction;
import org.example.transactionmanager.Entity.TransactionType;

import java.time.LocalDate;

public class AddController {

    @FXML private TextField descriptionField;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<TransactionType> typeComboBox;

    private MainController mainController;

    @FXML
    private void initialize() {
        categoryComboBox.setPromptText("Select Category");
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Food",
                "Transport",
                "Utilities",
                "Entertainment",
                "Health",
                "Education",
                "Shopping",
                "Salary",
                "Investment",
                "Other"
        ));
        typeComboBox.setPromptText("Select Type");
        typeComboBox.setItems(FXCollections.observableArrayList(TransactionType.values()));
        datePicker.setValue(LocalDate.now());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String description = descriptionField.getText();
        String amountText = amountField.getText();
        String category = categoryComboBox.getValue();
        LocalDate date = datePicker.getValue();
        TransactionType type = typeComboBox.getValue();

        if (description.isEmpty() || amountText.isEmpty() || category == null || date == null || type == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Amount must be a valid number.");
            return;
        }

        Transaction transaction = new Transaction(description, amount, category, date, type);
        mainController.addTransaction(transaction);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction saved successfully!");
        clearFields();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        descriptionField.clear();
        amountField.clear();
        categoryComboBox.setValue(null);
        datePicker.setValue(LocalDate.now());
        typeComboBox.setValue(null);
    }
}

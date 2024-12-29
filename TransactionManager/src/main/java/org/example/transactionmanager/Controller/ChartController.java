package org.example.transactionmanager.Controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import org.example.transactionmanager.Entity.Transaction;
import org.example.transactionmanager.Entity.TransactionType;
import org.example.transactionmanager.Service.TransactionService;
import javafx.application.Platform;

import java.time.LocalDate;
import java.util.List;

public class ChartController {

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private ComboBox<Integer> monthComboBox;
    @FXML
    private ComboBox<Integer> yearComboBox;

    private TransactionService transactionService;
    private boolean initialized = false;

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @FXML
    public void initialize() {
        for (int i = 1; i <= 12; i++) {
            monthComboBox.getItems().add(i);
        }

        for (int i = 2020; i <= 2050; i++) {
            yearComboBox.getItems().add(i);
        }

        LocalDate currentDate = LocalDate.now();
        monthComboBox.setValue(currentDate.getMonthValue());
        yearComboBox.setValue(currentDate.getYear());

        handleShowChart();
    }

    @FXML
    public void handleShowChart() {
        if (transactionService == null) {
            return;
        }

        Integer selectedMonth = monthComboBox.getValue();
        Integer selectedYear = yearComboBox.getValue();

        if (selectedMonth == null || selectedYear == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select both month and year.");
            alert.showAndWait();
            return;
        }

        List<Transaction> transactions = transactionService.getTransactionsForMonth(selectedMonth, selectedYear);
        updateChart(transactions, selectedMonth, selectedYear);
    }

    private void updateChart(List<Transaction> transactions, int month, int year) {
        barChart.getData().clear();

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Total Income");

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Total Expenses");

        double[] dailyIncome = new double[31];
        double[] dailyExpense = new double[31];

        for (Transaction transaction : transactions) {
            int day = transaction.getDate().getDayOfMonth();
            if (transaction.getType() == TransactionType.INCOME) {
                dailyIncome[day - 1] += transaction.getAmount();
            } else {
                dailyExpense[day - 1] += transaction.getAmount();
            }
        }

        for (int day = 1; day <= LocalDate.of(year, month, 1).lengthOfMonth(); day++) {
            XYChart.Data<String, Number> incomeData = new XYChart.Data<>(String.valueOf(day), dailyIncome[day - 1]);
            XYChart.Data<String, Number> expenseData = new XYChart.Data<>(String.valueOf(day), dailyExpense[day - 1]);

            incomeSeries.getData().add(incomeData);
            expenseSeries.getData().add(expenseData);
        }

        barChart.getData().addAll(incomeSeries, expenseSeries);
    }
}
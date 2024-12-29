package org.example.transactionmanager.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.example.transactionmanager.DataStructure.BinaryTree;
import org.example.transactionmanager.Entity.Transaction;
import org.example.transactionmanager.Service.TransactionService;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    private StackPane viewItem;

    private BinaryTree binaryTree = new BinaryTree();
    private TransactionService transactionService = new TransactionService(binaryTree);

    @FXML
    public void initialize() {
        LoadItem("/org/example/transactionmanager/fxml/add_transaction.fxml");
    }

    @FXML
    public void handleAddItem() {
        LoadItem("/org/example/transactionmanager/fxml/add_transaction.fxml");
    }

    @FXML
    public void handleDisplayItems() {
        LoadItem("/org/example/transactionmanager/fxml/display_item.fxml");
    }

    @FXML
    public void handleShowChart() {
        LoadItem("/org/example/transactionmanager/fxml/showchart.fxml");
    }

    @FXML
    public void handleExit() {
        System.exit(0);
    }

    public void addTransaction(Transaction transaction) {
        transactionService.addTransaction(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactionService.getAllTransactions();
    }

    public void LoadItem(String fileFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fileFxml));
            Parent root = loader.load();

            if (loader.getController() instanceof AddController) {
                AddController addController = (AddController) loader.getController();
                addController.setMainController(this);
            }

            if (loader.getController() instanceof DisplayController) {
                DisplayController displayController = (DisplayController) loader.getController();
                displayController.setTransactions(getTransactions());
            }

            if (loader.getController() instanceof ChartController) {
                ChartController chartController = (ChartController) loader.getController();
                chartController.setTransactionService(transactionService);
                chartController.handleShowChart();
            }

            viewItem.getChildren().clear();
            viewItem.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package org.example.transactionmanager.Service;

import org.example.transactionmanager.DataStructure.BinaryTree;
import org.example.transactionmanager.DataStructure.Node;
import org.example.transactionmanager.Entity.Transaction;
import org.example.transactionmanager.Entity.TransactionType;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionService {

    private BinaryTree transactionTree;
    private static final String FILE_NAME = System.getProperty("user.dir").replace("/", "\\") + "/data/transaction.txt";
    private int lastId;

    public TransactionService(BinaryTree transactionTree) {
        this.transactionTree = transactionTree;
        loadLastIdFromFile();
        loadTransactionsFromFile();
    }

    public void addTransaction(Transaction transaction) {
        transaction.setId(lastId + 1);
        lastId++; // Cập nhật ID cuối cùng
        transactionTree.insert(transaction);
        saveTransactionsToFile();
    }

    private void saveLastIdToFile(BufferedWriter writer) throws IOException {
        writer.write("lastId:" + lastId);
        writer.newLine();
    }

    private void saveTreeToFile(BufferedWriter writer, Node node) throws IOException {
        if (node == null) {
            return;
        }
        writer.write(node.transaction.getId() + "," + node.transaction.getDescription() + "," +
                node.transaction.getAmount() + "," + node.transaction.getCategory() + "," +
                node.transaction.getDate() + "," + node.transaction.getType());
        writer.newLine();
        saveTreeToFile(writer, node.left);
        saveTreeToFile(writer, node.right);
    }

    public void saveTransactionsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            saveLastIdToFile(writer);
            saveTreeToFile(writer, transactionTree.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        collectTransactions(transactionTree.getRoot(), transactions);
        return transactions;
    }

    private void collectTransactions(Node node, List<Transaction> transactions) {
        if (node != null) {
            transactions.add(node.transaction);
            collectTransactions(node.left, transactions);
            collectTransactions(node.right, transactions);
        }
    }

    public List<Transaction> getTransactionsForMonth(int month, int year) {
        List<Transaction> allTransactions = getAllTransactions();
        List<Transaction> filteredTransactions = new ArrayList<>();

        for (Transaction transaction : allTransactions) {
            if (transaction.getDate().getMonthValue() == month && transaction.getDate().getYear() == year) {
                filteredTransactions.add(transaction);
            }
        }

        return filteredTransactions;
    }

    // Load transactions from file
    private void loadTransactionsFromFile() {
        try (Scanner scanner = new Scanner(new FileReader(FILE_NAME))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0]);
                    String description = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    String category = parts[3];
                    LocalDate date = LocalDate.parse(parts[4]);
                    TransactionType type = TransactionType.valueOf(parts[5]);
                    Transaction transaction = new Transaction(description, amount, category, date, type);
                    transaction.setId(id);
                    transactionTree.insert(transaction);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLastIdFromFile() {
        try (Scanner scanner = new Scanner(new FileReader(FILE_NAME))) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("lastId:")) {
                    lastId = Integer.parseInt(line.split(":")[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
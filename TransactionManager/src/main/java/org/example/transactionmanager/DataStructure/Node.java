package org.example.transactionmanager.DataStructure;

import org.example.transactionmanager.Entity.Transaction;

public class Node {
    public Transaction transaction;
    public Node left;
    public Node right;

    public Node(Transaction transaction) {
        this.transaction = transaction;
        this.left = null;
        this.right = null;
    }
}

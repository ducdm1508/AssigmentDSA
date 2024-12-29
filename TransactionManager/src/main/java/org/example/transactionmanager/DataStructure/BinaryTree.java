package org.example.transactionmanager.DataStructure;

import org.example.transactionmanager.Entity.Transaction;

public class BinaryTree {
    private Node root; // Gốc của cây

    // Constructor
    public BinaryTree() {
        this.root = null;
    }

    // Phương thức để thêm giao dịch vào cây
    public void insert(Transaction transaction) {
        root = insertRec(root, transaction);
    }

    // Phương thức đệ quy để thêm giao dịch
    private Node insertRec(Node root, Transaction transaction) {
        if (root == null) {
            root = new Node(transaction);
            return root;
        }

        // Sắp xếp theo số tiền giao dịch
        if (transaction.getAmount() < root.transaction.getAmount()) {
            root.left = insertRec(root.left, transaction);
        } else {
            root.right = insertRec(root.right, transaction);
        }

        return root;
    }

    // Phương thức để duyệt cây (in-order traversal)
    public void inOrder() {
        inOrderRec(root);
    }

    // Phương thức đệ quy để duyệt cây
    private void inOrderRec(Node root) {
        if (root != null) {
            inOrderRec(root.left);
            System.out.println(root.transaction); // Hiển thị giao dịch
            inOrderRec(root.right);
        }
    }

    // Getter cho gốc cây
    public Node getRoot() {
        return root;
    }
}

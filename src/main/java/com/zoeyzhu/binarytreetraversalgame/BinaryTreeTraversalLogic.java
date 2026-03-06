package com.zoeyzhu.binarytreetraversalgame;
import java.util.ArrayList;

public class BinaryTreeTraversalLogic<T> {
    private ArrayList<BinaryTreeNode<T>> order = new ArrayList<>();
    private int currentIndex = 0;

    public BinaryTreeTraversalLogic(BinaryTreeNode<T> root, String mode) {
        if (mode.equals("preorder")) {
            preorderTraversal(root);
        }
        else if (mode.equals("postorder")) {
            postorderTraversal(root);
        }
        else if (mode.equals("inorder")) {
            inorderTraversal(root);
        }
    }

    private void preorderTraversal(BinaryTreeNode<T> node) {
        if (node == null) {
            return;
        }
        order.add(node);
        if (node.getLeft() != null) {
            preorderTraversal(node.getLeft());
        }
        if (node.getRight() != null) {
            preorderTraversal(node.getRight());
        }
    }

    private void postorderTraversal(BinaryTreeNode<T> node) {
        if (node == null) {
            return;
        }
        if (node.getLeft() != null) {
            postorderTraversal(node.getLeft());
        }
        if (node.getRight() != null) {
            postorderTraversal(node.getRight());
        }
        order.add(node);

    }

    private void inorderTraversal(BinaryTreeNode<T> node) {
        if (node == null) {
            return;
        }
        if (node.getLeft() != null) {
            inorderTraversal(node.getLeft());
        }
        order.add(node);
        if (node.getRight() != null) {
            inorderTraversal(node.getRight());
        }
    }

    public boolean checkNext(BinaryTreeNode<T> node) {
        if (!isComplete()) {
            if (order.get(currentIndex) == node) {
                currentIndex++;
                return true;
            }
        }
        return false;
    }

    public boolean isComplete() {
        return (currentIndex >= order.size());
    }

    public String getOrderString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < order.size(); i++) {
            sb.append(order.get(i).getData());
            if (i < order.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }
}
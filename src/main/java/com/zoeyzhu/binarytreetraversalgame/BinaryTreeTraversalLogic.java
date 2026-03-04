package com.zoeyzhu.binarytreetraversalgame;
import java.util.ArrayList;

public class BinaryTreeTraversalLogic {
    private ArrayList<BinaryTreeNode<Integer>> order = new ArrayList<>();
    private int currentIndex = 0;

    public BinaryTreeTraversalLogic(BinaryTreeNode<Integer> root, String mode) {
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

    private void preorderTraversal(BinaryTreeNode<Integer> node) {
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

    private void postorderTraversal(BinaryTreeNode<Integer> node) {
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

    private void inorderTraversal(BinaryTreeNode<Integer> node) {
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

    public boolean checkNext(BinaryTreeNode<Integer> node) {
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
}
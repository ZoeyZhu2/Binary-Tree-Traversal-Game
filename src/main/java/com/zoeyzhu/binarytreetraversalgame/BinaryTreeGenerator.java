package com.zoeyzhu.binarytreetraversalgame;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

//generates a random binary tree with random integer values for now
public class BinaryTreeGenerator {
    private BinaryTreeNode<Integer> root;
    private Random r = new Random();

    public BinaryTreeGenerator() {
        //number of nodes:
        int n = r.nextInt(3) + 19;
        Set<Integer> used = new HashSet<>();
        int generated = 0;
        while (generated < n) {
            int data = r.nextInt(1000) + 1;
            if (!used.contains(data)) {
                used.add(data);
                if (root == null) {
                    root = new BinaryTreeNode<>(data, null, null);
                }
                else {
                    insertChildren(root, data);
                }
                generated++;
            }
        }
    }

    //helper function for insertion
    private void insertChildren(BinaryTreeNode<Integer> node, int data) {
        if (data < node.getData()) {
            if (node.getLeft() == null) {
                node.setLeft(new BinaryTreeNode<>(data, null, null));
            }
            else {
                insertChildren(node.getLeft(), data);
            }
        }
        else {
            if (node.getRight() == null) {
                node.setRight(new BinaryTreeNode<>(data, null, null));
            }
            else {
                insertChildren(node.getRight(), data);
            }
        }
    }

    //to print trees in terminal via preorder traversal
    public void printAll() {
        printOne(root, 0);
    }

    private void printOne(BinaryTreeNode<Integer> node, int depth) {
        if (node == null) {
            return;
        }
        String space = "";
        for (int i = 0; i < depth; i++) {
            space += " ";
        }
        System.out.println(space + node);
        if (node.getLeft() != null) {
            printOne(node.getLeft(), depth + 1);
        }
        if (node.getRight() != null) {
            printOne(node.getRight(), depth + 1);
        }
    }

    public BinaryTreeNode<Integer> getRoot() {
        return root;
    }
}
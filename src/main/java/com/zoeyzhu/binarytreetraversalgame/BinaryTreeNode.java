package com.zoeyzhu.binarytreetraversalgame;
public class BinaryTreeNode<T> {
    private T data;
    private BinaryTreeNode<T> left;
    private BinaryTreeNode<T> right;

    public BinaryTreeNode(T data, BinaryTreeNode<T> left, BinaryTreeNode<T> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    } 

    public T getData() {
        return data;
    }

    public BinaryTreeNode<T> getLeft() {
        return left;
    }

    public BinaryTreeNode<T> getRight() {
        return right;
    }

    public void setLeft(BinaryTreeNode<T> node) {
        left = node;
    }

    public void setRight(BinaryTreeNode<T> node) {
        right = node;
    }

    //to String method for the terminal
    public String toString() {
        return data.toString();
    }
}
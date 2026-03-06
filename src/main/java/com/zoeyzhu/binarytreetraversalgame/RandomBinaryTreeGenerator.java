package com.zoeyzhu.binarytreetraversalgame;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

//generates a random binary tree with letters
public class RandomBinaryTreeGenerator {
    private BinaryTreeNode<String> root;
    private Random r = new Random();
    // private static final int TOTAL_NODES = 26; //all trees will have 26 nodes
    private int totalNodes;

    public RandomBinaryTreeGenerator() {
        totalNodes = r.nextInt(17) + 10; //making trees have 10-26 notes
        int generated = 0;
        Set<String> used = new HashSet<>();
        ArrayList<BinaryTreeNode<String>> availableNodes = new ArrayList<>();
        while (generated < totalNodes) {
            String nextLetter = generateLetter();
            if (!used.contains(nextLetter)) {
                used.add(nextLetter);
                BinaryTreeNode<String> newNode = new BinaryTreeNode<>(nextLetter, null, null);
                if (root == null) {
                    root = newNode;
                    availableNodes.add(root);
                    generated++;
                    continue;
                }
                else {
                    int size = availableNodes.size();
                    int randomIndex = r.nextInt(size);
                    BinaryTreeNode<String> parent = availableNodes.get(randomIndex);
                    if (parent.getLeft() == null) {
                        parent.setLeft(newNode);
                    }
                    else {
                        parent.setRight(newNode);
                    }
                    if (parent.getLeft() != null && parent.getRight() != null) {
                        availableNodes.remove(parent);
                    }
                }
                availableNodes.add(newNode);
                generated++;
            }
        }
        
    }

    private String generateLetter() {
        int numLetter = r.nextInt(totalNodes) + 1;
        switch (numLetter) {
            case 1:
                return "A";
            case 2:
                return "B";
            case 3:
                return "C";
            case 4:
                return "D";
            case 5:
                return "E";
            case 6:
                return "F";
            case 7:
                return "G";
            case 8:
                return "H";
            case 9:
                return "I";
            case 10:
                return "J";
            case 11:
                return "K";
            case 12:
                return "L";
            case 13:
                return "M";
            case 14:
                return "N";
            case 15:
                return "O";
            case 16:
                return "P";
            case 17:
                return "Q";
            case 18:
                return "R";
            case 19:
                return "S";
            case 20:
                return "T";
            case 21:
                return "U";
            case 22:
                return "V";
            case 23:
                return "W";
            case 24:
                return "X";
            case 25:
                return "Y";
            case 26:
                return "Z";
            default:
                return "";
        }
    }

    public BinaryTreeNode<String> getRoot() {
        return root;
    }
}
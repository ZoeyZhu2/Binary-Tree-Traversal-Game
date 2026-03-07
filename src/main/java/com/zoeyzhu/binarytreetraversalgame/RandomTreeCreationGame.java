//This class is where all the elemenets in the Random Tree Creation Game are brought together

package com.zoeyzhu.binarytreetraversalgame;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

public class RandomTreeCreationGame {
    private Stage stage;
    private RandomBinaryTreeGenerator generator;
    private TreeCanvas canvas;
    private Label feedbackLabel;
    private Label traversalLabel;

    public RandomTreeCreationGame(Stage stage) {
        this.stage = stage;
        startGame();
    }

    private void startGame() {
        generator = new RandomBinaryTreeGenerator();

        BinaryTreeTraversalLogic<String> preorder = new BinaryTreeTraversalLogic<>(generator.getRoot(), "preorder");
        BinaryTreeTraversalLogic<String> inorder = new BinaryTreeTraversalLogic<>(generator.getRoot(), "inorder");

        traversalLabel = new Label(
            "Preorder: " + preorder.getOrderString() +
            "\nInorder: " + inorder.getOrderString()
        );

        ArrayList<DraggableNode> draggableNodes = new ArrayList<>();
        ArrayList<String> letters = getLetters(generator.getRoot());
        java.util.Collections.sort(letters);

        // arrange in a grid at the TOP of the canvas
        int cols = 13;
        int spacingX = 70;
        int spacingY = 70;
        for (int i = 0; i < letters.size(); i++) {
            double x = 50 + (i % cols) * spacingX;
            double y = 50 + (i / cols) * spacingY;
            DraggableNode node = new DraggableNode(letters.get(i), x, y);
            draggableNodes.add(node);
        }

        canvas = new TreeCanvas(draggableNodes);
        canvas.setPrefSize(1200, 1200); // tall canvas so there's room to build below

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(canvas);
        scrollPane.setPrefSize(1200, 650);
        scrollPane.setPannable(true); // lets user pan by holding scroll

        feedbackLabel = new Label("Double click a node to select as parent, single click another to connect.");

        Button verifyBtn = new Button("Verify");
        Button clearBtn = new Button("Clear");
        Button resetBtn = new Button("New Game");
        Button checkBtn = new Button("Check So Far");

        verifyBtn.setOnAction(e -> verify());
        clearBtn.setOnAction(e -> reset());
        resetBtn.setOnAction(e -> startGame());
        checkBtn.setOnAction(e -> checkSoFar());

        Button menuBtn = new Button("Main Menu");
        menuBtn.setOnAction(e -> new Main().start(stage));

        HBox buttons = new HBox(10, verifyBtn, checkBtn, clearBtn, resetBtn, menuBtn);
        VBox layout = new VBox(10, traversalLabel, feedbackLabel, buttons, scrollPane);
        stage.setScene(new Scene(layout, 1200, 800));
        stage.show();
    }

    private void verify() {
        BinaryTreeNode<String> userRoot = canvas.buildUserTree();
        if (userRoot == null) {
            feedbackLabel.setText("Please build a tree first!");
            return;
        }

        BinaryTreeTraversalLogic<String> userPreorder = new BinaryTreeTraversalLogic<>(userRoot, "preorder");
        BinaryTreeTraversalLogic<String> userInorder = new BinaryTreeTraversalLogic<>(userRoot, "inorder");

        BinaryTreeTraversalLogic<String> answerPreorder = new BinaryTreeTraversalLogic<>(generator.getRoot(), "preorder");
        BinaryTreeTraversalLogic<String> answerInorder = new BinaryTreeTraversalLogic<>(generator.getRoot(), "inorder");

        boolean correct = userPreorder.getOrderString().equals(answerPreorder.getOrderString())
                    && userInorder.getOrderString().equals(answerInorder.getOrderString());

        feedbackLabel.setText(correct ? "Correct! 🎉" : "Not quite, keep trying!");
    }

    // collect all letters from the generated tree via preorder
    private ArrayList<String> getLetters(BinaryTreeNode<String> node) {
        ArrayList<String> letters = new ArrayList<>();
        collectLetters(node, letters);
        return letters;
    }

    private void collectLetters(BinaryTreeNode<String> node, ArrayList<String> letters) {
        if (node == null) return;
        letters.add(node.getData());
        collectLetters(node.getLeft(), letters);
        collectLetters(node.getRight(), letters);
    }

    private void reset() {
        canvas.clearAllConnections();
        feedbackLabel.setText("Double click a node to select as parent, single click another to connect.");
    }

    private void checkSoFar() {
        BinaryTreeNode<String> userRoot = canvas.buildUserTree();
        if (userRoot == null) {
            feedbackLabel.setText("Please build a tree first!");
            return;
        }
        canvas.highlightConnections(generator.getRoot());
        feedbackLabel.setText("Green = correct connection, Red = wrong!");
    }
}
package com.zoeyzhu.binarytreetraversalgame;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

//This class is where all the elements in the Random Tree Creation Game are brought together
public class RandomTreeCreationGame {
    private Stage stage;
    private RandomBinaryTreeGenerator generator;
    private TreeCanvas canvas;
    private Label feedbackLabel;
    private Label traversalLabel;
    private Label connectionLabel;
    private String secondTraversal = "preorder";
    private Button leftChildBtn;
    private Button rightChildBtn;

    public RandomTreeCreationGame(Stage stage) {
        this.stage = stage;
        startGame();
    }

    private void startGame() {
        generator = new RandomBinaryTreeGenerator();

        BinaryTreeTraversalLogic<String> inorder = new BinaryTreeTraversalLogic<>(generator.getRoot(), "inorder");
        BinaryTreeTraversalLogic<String> second = new BinaryTreeTraversalLogic<>(generator.getRoot(), secondTraversal);

        traversalLabel = new Label(
            "Inorder: " + inorder.getOrderString() +
            "\n" + secondTraversal.substring(0, 1).toUpperCase() + secondTraversal.substring(1) + ": " + second.getOrderString()
        );

        ArrayList<DraggableNode> draggableNodes = new ArrayList<>();
        ArrayList<String> letters = getLetters(generator.getRoot());
        java.util.Collections.sort(letters);

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
        canvas.setPrefSize(1200, 1200);

        // inline left/right buttons — replaces the Alert dialog
        connectionLabel = new Label("");
        leftChildBtn = new Button("Left Child");
        rightChildBtn = new Button("Right Child");
        leftChildBtn.setVisible(false);
        rightChildBtn.setVisible(false);
        connectionLabel.setVisible(false);

        leftChildBtn.setOnAction(e -> {
            canvas.connectSelected(true);
            hideConnectionButtons();
        });

        rightChildBtn.setOnAction(e -> {
            canvas.connectSelected(false);
            hideConnectionButtons();
        });

        // show left/right buttons when a parent is selected and child is pending
        canvas.setOnParentSelected(selectedParent -> {
            if (selectedParent != null && canvas.getPendingChild() != null) {
                connectionLabel.setText("Connect " + canvas.getPendingChild().getLetter() +
                    " as child of " + selectedParent.getLetter() + ":");
                connectionLabel.setVisible(true);
                leftChildBtn.setVisible(true);
                rightChildBtn.setVisible(true);
            } else {
                hideConnectionButtons();
            }
        });

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(canvas);
        scrollPane.setPrefSize(1200, 550);
        scrollPane.setPannable(true);

        feedbackLabel = new Label("Double click a node to select as parent, single click another to connect.");

        Button verifyBtn = new Button("Verify");
        Button clearBtn = new Button("Clear");
        Button resetBtn = new Button("New Game");
        Button checkBtn = new Button("Check So Far");
        Button menuBtn = new Button("Main Menu");

        verifyBtn.setOnAction(e -> verify());
        clearBtn.setOnAction(e -> reset());
        resetBtn.setOnAction(e -> startGame());
        checkBtn.setOnAction(e -> checkSoFar());
        menuBtn.setOnAction(e -> new Main().start(stage));

        Button preorderBtn = new Button("Inorder + Preorder");
        Button postorderBtn = new Button("Inorder + Postorder");

        preorderBtn.setOnAction(e -> { secondTraversal = "preorder"; startGame(); });
        postorderBtn.setOnAction(e -> { secondTraversal = "postorder"; startGame(); });

        if (secondTraversal.equals("preorder")) {
            preorderBtn.setStyle("-fx-background-color: lightblue;");
            postorderBtn.setStyle("");
        } else {
            postorderBtn.setStyle("-fx-background-color: lightblue;");
            preorderBtn.setStyle("");
        }

        HBox toggles = new HBox(10, new Label("Mode:"), preorderBtn, postorderBtn);
        HBox buttons = new HBox(10, verifyBtn, checkBtn, clearBtn, resetBtn, menuBtn);
        HBox connectionRow = new HBox(10, connectionLabel, leftChildBtn, rightChildBtn);

        VBox layout = new VBox(10, toggles, traversalLabel, feedbackLabel, connectionRow, buttons, scrollPane);
        stage.setScene(new Scene(layout, 1200, 800));
        stage.show();
    }

    private void hideConnectionButtons() {
        leftChildBtn.setVisible(false);
        rightChildBtn.setVisible(false);
        connectionLabel.setVisible(false);
    }

    private void verify() {
        BinaryTreeNode<String> userRoot = canvas.buildUserTree();
        if (userRoot == null) {
            feedbackLabel.setText("Please build a tree first!");
            return;
        }

        BinaryTreeTraversalLogic<String> userInorder = new BinaryTreeTraversalLogic<>(userRoot, "inorder");
        BinaryTreeTraversalLogic<String> userSecond = new BinaryTreeTraversalLogic<>(userRoot, secondTraversal);

        BinaryTreeTraversalLogic<String> answerInorder = new BinaryTreeTraversalLogic<>(generator.getRoot(), "inorder");
        BinaryTreeTraversalLogic<String> answerSecond = new BinaryTreeTraversalLogic<>(generator.getRoot(), secondTraversal);

        boolean correct = userInorder.getOrderString().equals(answerInorder.getOrderString())
                && userSecond.getOrderString().equals(answerSecond.getOrderString());

        feedbackLabel.setText(correct ? "Correct! 🎉" : "Not quite, keep trying!");
    }

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
        hideConnectionButtons();
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
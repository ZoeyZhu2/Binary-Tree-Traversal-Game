package com.zoeyzhu.binarytreetraversalgame;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;


public class BSTTraversalGame {
    private BSTGenerator generator;
    private BinaryTreeNode<Integer> root;
    private BinaryTreeDrawer drawer;
    private BinaryTreeTraversalLogic checker;
    private Label feedbackLabel;
    private Stage stage;
    private String currentMode = "preorder";
    private VBox layout;
    private Button preorderBtn;
    private Button inorderBtn;
    private Button postorderBtn;


    public BSTTraversalGame(Stage stage) {
        this.stage = stage;
        generator = new BSTGenerator();
        root = generator.getRoot();
        drawer = new BinaryTreeDrawer(root);
        checker = new BinaryTreeTraversalLogic(root, currentMode);

        feedbackLabel = new Label("Click nodes in " + currentMode + "!");
        setupClickListener();
        buildLayout();
    }

    private void switchMode(String mode) {
        currentMode = mode;
        checker = new BinaryTreeTraversalLogic(root, mode);
        setupClickListener(); // re-register with new checker
        drawer.resetHighlights();
        feedbackLabel.setText("Click nodes in " + mode + "!");

        // reset all button styles
        preorderBtn.setStyle("");
        inorderBtn.setStyle("");
        postorderBtn.setStyle("");

        // highlight active button
        if (mode.equals("preorder")) preorderBtn.setStyle("-fx-background-color: lightblue;");
        else if (mode.equals("inorder")) inorderBtn.setStyle("-fx-background-color: lightblue;");
        else if (mode.equals("postorder")) postorderBtn.setStyle("-fx-background-color: lightblue;");
    }
 
    private void resetGame() {
        generator = new BSTGenerator();
        root = generator.getRoot();
        drawer = new BinaryTreeDrawer(root);
        checker = new BinaryTreeTraversalLogic(root, currentMode);
        setupClickListener();
        buildLayout();
    }

    private void buildLayout() {
        preorderBtn = new Button("Preorder");
        inorderBtn = new Button("Inorder");
        postorderBtn = new Button("Postorder");
        Button resetBtn = new Button("Reset");

        preorderBtn.setOnAction(e -> switchMode("preorder"));
        inorderBtn.setOnAction(e -> switchMode("inorder"));
        postorderBtn.setOnAction(e -> switchMode("postorder"));
        resetBtn.setOnAction(e -> resetGame());

        HBox buttons = new HBox(10, preorderBtn, inorderBtn, postorderBtn, resetBtn);

        
        ScrollPane scrollPane = new ScrollPane(drawer);
        scrollPane.setPrefSize(1200, 700);

        layout = new VBox(10, buttons, feedbackLabel, scrollPane);
        Scene scene = new Scene(layout, 1200, 800);
        stage.setScene(scene);
        stage.show();
        preorderBtn.setStyle("");
        inorderBtn.setStyle("");
        postorderBtn.setStyle("");
        if (currentMode.equals("preorder")) preorderBtn.setStyle("-fx-background-color: lightblue;");
        else if (currentMode.equals("inorder")) inorderBtn.setStyle("-fx-background-color: lightblue;");
        else if (currentMode.equals("postorder")) postorderBtn.setStyle("-fx-background-color: lightblue;");
    }

    private void setupClickListener() {
        drawer.setNodeClickListener(node -> {
            if (checker.isComplete()) return;
            boolean correct = checker.checkNext(node);
            drawer.highlightNode(node, correct);
            if (correct) {
                feedbackLabel.setText(checker.isComplete() ? "Finished! 🎉" : "Correct!");
            } else {
                feedbackLabel.setText("Wrong! Try again.");
                new Thread(() -> {
                    try { Thread.sleep(800); } catch (Exception e) {}
                    javafx.application.Platform.runLater(() -> {
                        drawer.unhighlightNode(node);
                        feedbackLabel.setText("Click nodes in " + currentMode + "!");
                    });
                }).start();
            }
        });
    }

}
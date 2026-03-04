package com.zoeyzhu.binarytreetraversalgame;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        new BinaryTreeTraversalGame(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package com.zoeyzhu.binarytreetraversalgame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Label title = new Label("Binary Tree Game");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));

        Label subtitle = new Label("Choose a game mode:");
        subtitle.setFont(Font.font("Arial", 16));

        Button bstBtn = new Button("Traversal Game");
        Button randomBtn = new Button("Build the Tree");

        bstBtn.setPrefWidth(200);
        randomBtn.setPrefWidth(200);
        bstBtn.setStyle("-fx-font-size: 14;");
        randomBtn.setStyle("-fx-font-size: 14;");

        bstBtn.setOnAction(e -> new BSTTraversalGame(stage));
        randomBtn.setOnAction(e -> new RandomTreeCreationGame(stage));

        VBox layout = new VBox(20, title, subtitle, bstBtn, randomBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setFocusTraversable(true);

        stage.setScene(new Scene(layout, 1200, 800));
        stage.setTitle("Binary Tree Game");
        stage.show();
        layout.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package com.zoeyzhu.binarytreetraversalgame;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RandomTreeCreationGame {
    public RandomTreeCreationGame(Stage stage) {
        Pane canvas = new Pane();
        canvas.setPrefSize(1200, 700);

        DraggableNode nodeA = new DraggableNode("A", 100, 100);
        DraggableNode nodeB = new DraggableNode("B", 200, 100);
        DraggableNode nodeC = new DraggableNode("C", 300, 100);
        canvas.getChildren().addAll(nodeA, nodeB, nodeC);

        stage.setScene(new Scene(canvas, 1200, 700));
        stage.show();
    }
}
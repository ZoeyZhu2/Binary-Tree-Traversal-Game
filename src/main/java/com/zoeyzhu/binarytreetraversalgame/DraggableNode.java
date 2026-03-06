package com.zoeyzhu.binarytreetraversalgame;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class DraggableNode extends StackPane {
    private static final int RADIUS = 20;
    private String letter;
    private Circle circle;
    private double mouseOffsetX, mouseOffsetY;

    public DraggableNode(String letter, double x, double y) {
        this.letter = letter;

        circle = new Circle(RADIUS);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        Text text = new Text(letter);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        text.setX(RADIUS - text.getLayoutBounds().getWidth() / 2);
        text.setY(RADIUS + text.getLayoutBounds().getHeight() / 4);

        getChildren().addAll(circle, text);
        setLayoutX(x - RADIUS);
        setLayoutY(y - RADIUS);

        setOnMousePressed(e -> {
            mouseOffsetX = e.getSceneX() - getLayoutX();
            mouseOffsetY = e.getSceneY() - getLayoutY();
        });

        setOnMouseDragged(e -> {
            setLayoutX(e.getSceneX() - mouseOffsetX);
            setLayoutY(e.getSceneY() - mouseOffsetY);
        });
    }

    public String getLetter() { return letter; }
    public Circle getCircle() { return circle; }
    public double getCenterX() { return getLayoutX() + RADIUS; }
    public double getCenterY() { return getLayoutY() + RADIUS; }
}

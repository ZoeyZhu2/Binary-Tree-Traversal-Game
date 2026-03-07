package com.zoeyzhu.binarytreetraversalgame;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;

//This class creates the Nodes in the Tree Creation Game
public class DraggableNode extends StackPane {
    private static final int RADIUS = 20;
    private String letter;
    private Circle circle;
    private double mouseOffsetX, mouseOffsetY;
    private ArrayList<Line> connectedLines = new ArrayList<>();
    private ArrayList<Boolean> lineIsStart = new ArrayList<>();
    private double originalX;
    private double originalY;
    private boolean inDrawingArea = false;
    private double snapBoundary;
    private boolean dragged = false;
    private TreeCanvas canvas;

    private long lastClickTime = 0;
    private NodeSelectListener selectListener;

    public DraggableNode(String letter, double x, double y) {
        this.letter = letter;

        circle = new Circle(RADIUS);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        Text text = new Text(letter);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        getChildren().addAll(circle, text);
        setLayoutX(x - RADIUS);
        setLayoutY(y - RADIUS);
        originalX = getLayoutX();
        originalY = getLayoutY();
        setPrefSize(RADIUS * 2, RADIUS * 2);
        setMinSize(RADIUS * 2, RADIUS * 2);
        setMaxSize(RADIUS * 2, RADIUS * 2);

        // JPro-compatible click detection using timer instead of getClickCount()
        setOnMouseClicked(e -> {
            if (dragged) return;
            long now = System.currentTimeMillis();
            if (now - lastClickTime < 300) {
                // double click detected
                lastClickTime = 0;
                if (selectListener != null) selectListener.onDoubleClick(this);
            } else {
                // possible single click — wait to confirm it's not a double click
                lastClickTime = now;
                new Thread(() -> {
                    try { Thread.sleep(300); } catch (Exception ex) {}
                    if (lastClickTime != 0 && System.currentTimeMillis() - lastClickTime >= 300) {
                        javafx.application.Platform.runLater(() -> {
                            if (selectListener != null) selectListener.onSingleClick(this);
                        });
                    }
                }).start();
            }
        });

        setOnMousePressed(e -> {
            mouseOffsetX = e.getSceneX() - getLayoutX();
            mouseOffsetY = e.getSceneY() - getLayoutY();
            dragged = false;
            e.consume();
        });

        setOnMouseDragged(e -> {
            setLayoutX(e.getSceneX() - mouseOffsetX);
            setLayoutY(e.getSceneY() - mouseOffsetY);
            updateLines();
            dragged = true;
            e.consume();
        });

        setOnMouseReleased(e -> {
            if (dragged) {
                if (getLayoutY() + RADIUS < snapBoundary) {
                    if (canvas != null) canvas.removeNodeConnections(this);
                    snapBack();
                } else {
                    setInDrawingArea(true);
                }
            }
        });
    }

    private void updateLines() {
        for (int i = 0; i < connectedLines.size(); i++) {
            Line line = connectedLines.get(i);
            if (lineIsStart.get(i)) {
                line.setStartX(getCenterX());
                line.setStartY(getCenterY());
            } else {
                line.setEndX(getCenterX());
                line.setEndY(getCenterY());
            }
        }
    }

    public void addLine(Line line, boolean isStart) {
        connectedLines.add(line);
        lineIsStart.add(isStart);
    }

    public void removeLine(Line line) {
        int index = connectedLines.indexOf(line);
        if (index != -1) {
            connectedLines.remove(index);
            lineIsStart.remove(index);
        }
    }

    public void setHighlighted(boolean highlighted) {
        circle.setFill(highlighted ? Color.LIGHTBLUE : Color.WHITE);
    }

    public void setSelectListener(NodeSelectListener listener) {
        this.selectListener = listener;
    }

    public void clearLines() {
        connectedLines.clear();
        lineIsStart.clear();
    }

    public String getLetter() { return letter; }
    public Circle getCircle() { return circle; }
    public double getCenterX() { return getLayoutX() + RADIUS; }
    public double getCenterY() { return getLayoutY() + RADIUS; }

    public void snapBack() {
        setLayoutX(originalX);
        setLayoutY(originalY);
        inDrawingArea = false;
    }

    public void setInDrawingArea(boolean inDrawingArea) {
        this.inDrawingArea = inDrawingArea;
    }

    public boolean isInDrawingArea() { return inDrawingArea; }
    public double getOriginalX() { return originalX; }
    public double getOriginalY() { return originalY; }

    public void setSnapBoundary(double boundary) { this.snapBoundary = boundary; }
    public void setCanvas(TreeCanvas canvas) { this.canvas = canvas; }
}
package com.zoeyzhu.binarytreetraversalgame;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

//This class controls how users create the trees. Draws the Nodes and lines.
public class TreeCanvas extends Pane implements NodeSelectListener {
    private DraggableNode selectedParent = null;
    private DraggableNode pendingChild = null;
    private ArrayList<DraggableNode> nodes = new ArrayList<>();
    private HashMap<Line, DraggableNode[]> lineConnections = new HashMap<>();
    private HashMap<Line, Boolean> lineIsLeft = new HashMap<>();
    private static final double PALETTE_HEIGHT = 150;

    // callback to notify game when parent selection changes
    private Consumer<DraggableNode> onParentSelectedCallback;

    public TreeCanvas(ArrayList<DraggableNode> draggableNodes) {
        setPrefSize(1200, 1200);

        Rectangle paletteBg = new Rectangle(0, 0, 1200, PALETTE_HEIGHT);
        paletteBg.setFill(Color.LIGHTGRAY);
        getChildren().add(paletteBg);

        Rectangle drawingBg = new Rectangle(0, PALETTE_HEIGHT, 1200, 1050);
        drawingBg.setFill(Color.WHITE);
        getChildren().add(drawingBg);

        Line divider = new Line(0, PALETTE_HEIGHT, 1200, PALETTE_HEIGHT);
        divider.setStroke(Color.BLACK);
        divider.setStrokeWidth(2);
        getChildren().add(divider);

        Text paletteLabel = new Text(10, 20, "PALETTE");
        paletteLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(paletteLabel);

        Text drawingLabel = new Text(10, PALETTE_HEIGHT + 20, "DRAWING AREA");
        drawingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        getChildren().add(drawingLabel);

        for (DraggableNode node : draggableNodes) {
            node.setSelectListener(this);
            node.setSnapBoundary(PALETTE_HEIGHT);
            node.setCanvas(this);
            nodes.add(node);
            getChildren().add(node);
        }
    }

    public void setOnParentSelected(Consumer<DraggableNode> callback) {
        this.onParentSelectedCallback = callback;
    }

    @Override
    public void onDoubleClick(DraggableNode node) {
        if (!node.isInDrawingArea()) return;

        // deselect if clicking the same node again
        if (selectedParent == node) {
            selectedParent.setHighlighted(false);
            selectedParent = null;
            pendingChild = null;
            if (onParentSelectedCallback != null) onParentSelectedCallback.accept(null);
            return;
        }

        // deselect previous
        if (selectedParent != null) selectedParent.setHighlighted(false);

        selectedParent = node;
        selectedParent.setHighlighted(true);
        pendingChild = null;

        // notify game that a parent has been selected
        if (onParentSelectedCallback != null) onParentSelectedCallback.accept(selectedParent);
    }

    @Override
    public void onSingleClick(DraggableNode node) {
        if (selectedParent == null || selectedParent == node) return;
        if (!node.isInDrawingArea()) return;

        // store as pending child — game will call connectSelected() when user picks left/right
        pendingChild = node;
        if (onParentSelectedCallback != null) onParentSelectedCallback.accept(selectedParent);
    }

    // called by game when user clicks Left or Right button
    public void connectSelected(boolean isLeft) {
        if (selectedParent == null || pendingChild == null) return;
        drawConnection(selectedParent, pendingChild, isLeft);
        selectedParent.setHighlighted(false);
        selectedParent = null;
        pendingChild = null;
        if (onParentSelectedCallback != null) onParentSelectedCallback.accept(null);
    }

    public DraggableNode getPendingChild() { return pendingChild; }
    public DraggableNode getSelectedParent() { return selectedParent; }

    private void drawConnection(DraggableNode parent, DraggableNode child, boolean isLeft) {
        Line line = new Line(
            parent.getCenterX(), parent.getCenterY(),
            child.getCenterX(), child.getCenterY()
        );
        line.setStroke(isLeft ? Color.BLUE : Color.ORANGE);
        line.setStrokeWidth(4);
        lineIsLeft.put(line, isLeft);

        line.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) deleteLine(line);
        });

        parent.addLine(line, true);
        child.addLine(line, false);
        lineConnections.put(line, new DraggableNode[]{parent, child});
        getChildren().add(3, line);
    }

    private void deleteLine(Line line) {
        DraggableNode[] connection = lineConnections.get(line);
        if (connection != null) {
            connection[0].removeLine(line);
            connection[1].removeLine(line);
            lineConnections.remove(line);
            lineIsLeft.remove(line);
        }
        getChildren().remove(line);
    }

    public void clearAllConnections() {
        getChildren().removeIf(child -> child instanceof Line);
        for (DraggableNode node : nodes) node.clearLines();
        lineConnections.clear();
        lineIsLeft.clear();
    }

    public BinaryTreeNode<String> buildUserTree() {
        ArrayList<DraggableNode> drawingNodes = new ArrayList<>();
        for (DraggableNode node : nodes) {
            if (node.isInDrawingArea()) drawingNodes.add(node);
        }
        if (drawingNodes.isEmpty()) return null;

        ArrayList<DraggableNode> children = new ArrayList<>();
        for (DraggableNode[] connection : lineConnections.values()) {
            if (connection[1].isInDrawingArea()) children.add(connection[1]);
        }

        DraggableNode root = null;
        for (DraggableNode node : drawingNodes) {
            if (!children.contains(node)) {
                root = node;
                break;
            }
        }

        if (root == null) return null;
        return buildSubTree(root);
    }

    private BinaryTreeNode<String> buildSubTree(DraggableNode node) {
        BinaryTreeNode<String> treeNode = new BinaryTreeNode<>(node.getLetter(), null, null);
        for (Line line : lineConnections.keySet()) {
            DraggableNode[] connection = lineConnections.get(line);
            if (connection[0] == node) {
                boolean isLeft = lineIsLeft.get(line);
                BinaryTreeNode<String> child = buildSubTree(connection[1]);
                if (isLeft) treeNode.setLeft(child);
                else treeNode.setRight(child);
            }
        }
        return treeNode;
    }

    public void highlightConnections(BinaryTreeNode<String> answerRoot) {
        for (Line line : lineConnections.keySet()) {
            DraggableNode[] connection = lineConnections.get(line);
            boolean isLeft = lineIsLeft.get(line);
            boolean correct = checkConnection(answerRoot, connection[0].getLetter(), connection[1].getLetter(), isLeft);
            line.setStroke(correct ? Color.GREEN : Color.RED);
        }

        new Thread(() -> {
            try { Thread.sleep(3000); } catch (Exception e) {}
            javafx.application.Platform.runLater(() -> {
                for (Line line : lineConnections.keySet()) {
                    boolean isLeft = lineIsLeft.get(line);
                    line.setStroke(isLeft ? Color.BLUE : Color.ORANGE);
                }
            });
        }).start();
    }

    private boolean checkConnection(BinaryTreeNode<String> node, String parent, String child, boolean isLeft) {
        if (node == null) return false;
        if (node.getData().equals(parent)) {
            BinaryTreeNode<String> expectedChild = isLeft ? node.getLeft() : node.getRight();
            return expectedChild != null && expectedChild.getData().equals(child);
        }
        return checkConnection(node.getLeft(), parent, child, isLeft)
            || checkConnection(node.getRight(), parent, child, isLeft);
    }

    public void removeNodeConnections(DraggableNode node) {
        ArrayList<Line> toRemove = new ArrayList<>();
        for (Line line : lineConnections.keySet()) {
            DraggableNode[] connection = lineConnections.get(line);
            if (connection[0] == node || connection[1] == node) toRemove.add(line);
        }
        for (Line line : toRemove) deleteLine(line);
        getChildren().removeAll(toRemove);
    }
}
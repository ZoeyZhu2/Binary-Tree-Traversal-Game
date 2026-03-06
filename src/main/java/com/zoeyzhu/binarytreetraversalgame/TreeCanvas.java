package com.zoeyzhu.binarytreetraversalgame;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.util.ArrayList;
import java.util.Optional;
import java.util.HashMap;

public class TreeCanvas extends Pane implements NodeSelectListener {
    private DraggableNode selectedParent = null;
    private ArrayList<DraggableNode> nodes = new ArrayList<>();
    // maps each line to its parent and child node
    private HashMap<Line, DraggableNode[]> lineConnections = new HashMap<>();
    private HashMap<Line, Boolean> lineIsLeft = new HashMap<>();

    public TreeCanvas(ArrayList<DraggableNode> draggableNodes) {
        setPrefSize(1200, 700);

        for (DraggableNode node : draggableNodes) {
            node.setSelectListener(this);
            nodes.add(node);
            getChildren().add(node);
        }
    }

    @Override
    public void onDoubleClick(DraggableNode node) {
        // deselect if clicking the same node twice
        if (selectedParent == node) {
            selectedParent.setHighlighted(false);
            selectedParent = null;
            return;
        }
        // deselect previous
        if (selectedParent != null) {
            selectedParent.setHighlighted(false);
        }
        // select new parent
        selectedParent = node;
        selectedParent.setHighlighted(true);
    }

    @Override
    public void onSingleClick(DraggableNode node) {
        if (selectedParent == null || selectedParent == node) return;

        // ask left or right
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Connect Node");
        alert.setHeaderText("Add " + node.getLetter() + " as a child of " + selectedParent.getLetter());
        ButtonType leftBtn = new ButtonType("Left");
        ButtonType rightBtn = new ButtonType("Right");
        ButtonType cancelBtn = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(leftBtn, rightBtn, cancelBtn);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty() || result.get() == cancelBtn) return;

        boolean isLeft = result.get() == leftBtn;
        drawConnection(selectedParent, node, isLeft);

        // deselect parent after connecting
        selectedParent.setHighlighted(false);
        selectedParent = null;
    }

    private void drawConnection(DraggableNode parent, DraggableNode child, boolean isLeft) {
        Line line = new Line(
            parent.getCenterX(), parent.getCenterY(),
            child.getCenterX(), child.getCenterY()
        );
        line.setStroke(isLeft ? Color.BLUE : Color.RED); // blue = left, red = right
        line.setStrokeWidth(4);
        lineIsLeft.put(line, isLeft);

        // double click to delete
        line.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                deleteLine(line);
            }
        });

        // register line with both nodes so they can update it when dragged
        parent.addLine(line, true);
        child.addLine(line, false);

        // store connection info
        lineConnections.put(line, new DraggableNode[]{parent, child});

        // add line behind nodes
        getChildren().add(0, line);
    }

    private void deleteLine(Line line) {
        DraggableNode[] connection = lineConnections.get(line);
        if (connection != null) {
            connection[0].removeLine(line); // remove from parent
            connection[1].removeLine(line); // remove from child
            lineConnections.remove(line);
            lineIsLeft.remove(line);
        }
        getChildren().remove(line);
    }

    public void clearAllConnections() {
        // remove all lines from canvas
        getChildren().removeIf(child -> child instanceof Line);
        // clear line tracking from each node
        for (DraggableNode node : nodes) {
            node.clearLines();
        }
        lineConnections.clear();
        lineIsLeft.clear();
    }

    public BinaryTreeNode<String> buildUserTree() {
        // find the root — the node that is nobody's child
        ArrayList<DraggableNode> children = new ArrayList<>();
        for (DraggableNode[] connection : lineConnections.values()) {
            children.add(connection[1]); // connection[1] is always the child
        }

        DraggableNode root = null;
        for (DraggableNode node : nodes) {
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
            if (connection[0] == node) { // this node is the parent
                boolean isLeft = lineIsLeft.get(line);
                BinaryTreeNode<String> child = buildSubTree(connection[1]);
                if (isLeft) {
                    treeNode.setLeft(child);
                } else {
                    treeNode.setRight(child);
                }
            }
        }
        return treeNode;
    }
}
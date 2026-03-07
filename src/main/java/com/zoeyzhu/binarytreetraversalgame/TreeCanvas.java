//This class is controls how users create the trees. Draws the Nodes and lines.

package com.zoeyzhu.binarytreetraversalgame;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.Optional;
import java.util.HashMap;

public class TreeCanvas extends Pane implements NodeSelectListener {
    private DraggableNode selectedParent = null;
    private ArrayList<DraggableNode> nodes = new ArrayList<>();
    // maps each line to its parent and child node
    private HashMap<Line, DraggableNode[]> lineConnections = new HashMap<>();
    private HashMap<Line, Boolean> lineIsLeft = new HashMap<>();
    private static final double PALETTE_HEIGHT = 150;

    public TreeCanvas(ArrayList<DraggableNode> draggableNodes) {
        setPrefSize(1200, 1200);

        // palette background
        Rectangle paletteBg = new Rectangle(0, 0, 1200, PALETTE_HEIGHT);
        paletteBg.setFill(Color.LIGHTGRAY);
        getChildren().add(paletteBg);

        // drawing area background
        Rectangle drawingBg = new Rectangle(0, PALETTE_HEIGHT, 1200, 1050);
        drawingBg.setFill(Color.WHITE);
        getChildren().add(drawingBg);

        // dividing line
        Line divider = new Line(0, PALETTE_HEIGHT, 1200, PALETTE_HEIGHT);
        divider.setStroke(Color.BLACK);
        divider.setStrokeWidth(2);
        getChildren().add(divider);

        // labels
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

    @Override
    public void onDoubleClick(DraggableNode node) {
        if (!node.isInDrawingArea()) return;
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

        if (!node.isInDrawingArea()) return;
        
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
        line.setStroke(isLeft ? Color.BLUE : Color.ORANGE); // blue = left, red = right
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
        getChildren().add(3, line);
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
        ArrayList<DraggableNode> drawingNodes = new ArrayList<>();
        for (DraggableNode node : nodes) {
            if (node.isInDrawingArea()) drawingNodes.add(node);
        }
        if (drawingNodes.isEmpty()) return null;

        // find the root — the node that is nobody's child
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

    public void highlightConnections(BinaryTreeNode<String> answerRoot) {
        for (Line line : lineConnections.keySet()) {
            DraggableNode[] connection = lineConnections.get(line);
            boolean isLeft = lineIsLeft.get(line);
            String parentLetter = connection[0].getLetter();
            String childLetter = connection[1].getLetter();

            boolean correct = checkConnection(answerRoot, parentLetter, childLetter, isLeft);
            line.setStroke(correct ? Color.GREEN : Color.RED);
        }

        // restore original colors after 3 seconds
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
            if (connection[0] == node || connection[1] == node) {
                toRemove.add(line);
            }
        }
        for (Line line : toRemove) {
            deleteLine(line);
        }
        getChildren().removeAll(toRemove);
    }
}
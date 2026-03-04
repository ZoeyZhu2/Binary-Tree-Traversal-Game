package com.zoeyzhu.binarytreetraversalgame;


import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.HashMap;

public class BinaryTreeDrawer extends Pane {
    private static final int NODE_RADIUS = 20;

    private HashMap<BinaryTreeNode<Integer>, int[]> positions = new HashMap<>();
    private NodeClickListener listener;
    private BinaryTreeNode<Integer> root;
    private int horizontalSpacing;
    private int verticalSpacing;

    public BinaryTreeDrawer(BinaryTreeNode<Integer> root) {
        this.root = root;
        calculateSpacing(root);        
        assignPositions(root, 0, 0);
        draw();
    }

    public void setNodeClickListener(NodeClickListener listener) {
        this.listener = listener;
    }

    private void calculateSpacing(BinaryTreeNode<Integer> root) {
        int nodeCount = countNodes(root);
        int depth = getDepth(root);
        
        if (nodeCount > 20) horizontalSpacing = 45;
        else if (nodeCount > 15) horizontalSpacing = 55;
        else horizontalSpacing = 80;

        if (depth > 7) verticalSpacing = 55;
        else if (depth > 5) verticalSpacing = 65;
        else verticalSpacing = 80;
    }

    private int countNodes(BinaryTreeNode<Integer> node) {
        if (node == null) return 0;
        return 1 + countNodes(node.getLeft()) + countNodes(node.getRight());
    }
    
    private int getDepth(BinaryTreeNode<Integer> node) {
        if (node == null) return 0;
        return 1 + Math.max(getDepth(node.getLeft()), getDepth(node.getRight()));
    }

    // inorder traversal to assign x positions — naturally spaces nodes out
    private int assignPositions(BinaryTreeNode<Integer> node, int x, int depth) {
        if (node == null) return x;
        x = assignPositions(node.getLeft(), x, depth + 1);
        positions.put(node, new int[]{x * horizontalSpacing + 50, depth * verticalSpacing + 50});
        x++;
        x = assignPositions(node.getRight(), x, depth + 1);
        return x;
    }

    private void draw() {
        getChildren().clear();

        // draw edges first so they appear behind nodes
        for (BinaryTreeNode<Integer> node : positions.keySet()) {
            int[] pos = positions.get(node);
            if (node.getLeft() != null) {
                int[] childPos = positions.get(node.getLeft());
                Line line = new Line(pos[0], pos[1], childPos[0], childPos[1]);
                line.setStroke(Color.GRAY);
                line.setStrokeWidth(2);
                getChildren().add(line);
            }
            if (node.getRight() != null) {
                int[] childPos = positions.get(node.getRight());
                Line line = new Line(pos[0], pos[1], childPos[0], childPos[1]);
                line.setStroke(Color.GRAY);
                line.setStrokeWidth(2);
                getChildren().add(line);
            }
        }

        // draw nodes on top of edges
        for (BinaryTreeNode<Integer> node : positions.keySet()) {
            int[] pos = positions.get(node);
            final BinaryTreeNode<Integer> currentNode = node;

            Circle circle = new Circle(pos[0], pos[1], NODE_RADIUS);
            circle.setFill(Color.WHITE);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(2);

            Text text = new Text(pos[0] - 7, pos[1] + 5, node.getData().toString());
            text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            // click detection
            circle.setOnMouseClicked(e -> {
                if (listener != null) listener.onNodeClicked(currentNode);
            });
            text.setOnMouseClicked(e -> {
                if (listener != null) listener.onNodeClicked(currentNode);
            });

            getChildren().addAll(circle, text);
        }
    }

    // highlight a node green (correct) or red (wrong)
    public void highlightNode(BinaryTreeNode<Integer> node, boolean correct) {
        int[] pos = positions.get(node);
        if (pos == null) return;

        for (var child : getChildren()) {
            if (child instanceof Circle c) {
                if (Math.abs(c.getCenterX() - pos[0]) < 1 && Math.abs(c.getCenterY() - pos[1]) < 1) {
                    c.setFill(correct ? Color.LIGHTGREEN : Color.SALMON);
                }
            }
        }
    }

    // reset all nodes back to white
    public void resetHighlights() {
        for (var child : getChildren()) {
            if (child instanceof Circle c) {
                c.setFill(Color.WHITE);
            }
        }
    }

    public void unhighlightNode(BinaryTreeNode<Integer> node) {
        int[] pos = positions.get(node);
        if (pos == null) return;
        for (var child : getChildren()) {
            if (child instanceof Circle c) {
                if (Math.abs(c.getCenterX() - pos[0]) < 1 && Math.abs(c.getCenterY() - pos[1]) < 1) {
                    c.setFill(Color.WHITE);
                }
            }
        }
    }
}
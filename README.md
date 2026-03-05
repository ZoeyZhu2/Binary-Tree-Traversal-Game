# Binary Tree Traversal Game 🌳

An interactive browser-based game to practice binary tree traversal. Click nodes in the correct order for preorder, inorder, and postorder traversal!

🎮 **[Play here](https://binary-tree-traversal-game.onrender.com)**

> Note: The app may take ~30 seconds to load if it hasn't been visited recently (free tier cold start).

---

## How to Play

1. A random binary search tree is generated with unique integer values
2. Select a traversal mode: **Preorder**, **Inorder**, or **Postorder**
3. Click the nodes in the correct traversal order
4. Correct clicks turn **green**, wrong clicks turn **red** and auto-undo after a moment
5. Complete the full traversal to win!

## Traversal Modes

- **Preorder** — current → left → right
- **Inorder** — left → current → right (visits nodes in sorted order!)
- **Postorder** — left → right → current

---

## Built With

- Java 24
- JavaFX 24
- JPro (runs JavaFX in the browser)
- Maven

## Running Locally

### Prerequisites
- Java 24
- Maven

### Run the desktop version:
```bash
mvn javafx:run
```

### Run the browser version:
```bash
mvn jpro:run
```
Then open `http://localhost:8080` in your browser.

---

## Project Structure

```
src/main/java/com/zoeyzhu/binarytreetraversalgame/
├── Main.java                    # JavaFX entry point
├── BinaryTreeNode.java          # Generic binary tree node
├── BinaryTreeGenerator.java     # Generates random BSTs
├── BinaryTreeDrawer.java        # Draws the tree and handles clicks
├── BinaryTreeTraversalLogic.java # Computes and checks traversal order
├── BinaryTreeTraversalGame.java  # Game logic and UI
└── NodeClickListener.java       # Click callback interface
```

---

Made by Zoey Zhu# Binary Tree Traversal Game 🌳

An interactive browser-based game to practice binary tree traversal. Click nodes in the correct order for preorder, inorder, and postorder traversal!

🎮 **[Play here](https://binary-tree-traversal-game.onrender.com)**

> Note: The app may take ~30 seconds to load if it hasn't been visited recently (free tier cold start).

---

## How to Play

1. A random binary search tree is generated with unique integer values
2. Select a traversal mode: **Preorder**, **Inorder**, or **Postorder**
3. Click the nodes in the correct traversal order
4. Correct clicks turn **green**, wrong clicks turn **red** and auto-undo after a moment
5. Complete the full traversal to win!

## Traversal Modes

- **Preorder** — current → left → right
- **Inorder** — left → current → right (visits nodes in sorted order!)
- **Postorder** — left → right → current

---

## Built With

- Java 24
- JavaFX 24
- JPro (runs JavaFX in the browser)
- Maven

## Running Locally

### Prerequisites
- Java 24
- Maven

### Run the desktop version:
```bash
mvn javafx:run
```

### Run the browser version:
```bash
mvn jpro:run
```
Then open `http://localhost:8080` in your browser.

---

## Project Structure

```
src/main/java/com/zoeyzhu/binarytreetraversalgame/
├── Main.java                    # JavaFX entry point
├── BinaryTreeNode.java          # Generic binary tree node
├── BinaryTreeGenerator.java     # Generates random BSTs
├── BinaryTreeDrawer.java        # Draws the tree and handles clicks
├── BinaryTreeTraversalLogic.java # Computes and checks traversal order
├── BinaryTreeTraversalGame.java  # Game logic and UI
└── NodeClickListener.java       # Click callback interface
```

---

Made by Zoey Zhu
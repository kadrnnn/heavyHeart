package com.itlife.heavyheart.test;

public class BinaryTree {
    private Node root;

    public void insert(int data) {
        if (root == null) {
            root = new Node(data);
            return;
        }

        insertRecursive(root, data);
    }

    private void insertRecursive(Node current, int data) {
        if (data < current.data) {
            if (current.left == null) {
                current.left = new Node(data);
            } else {
                insertRecursive(current.left, data);
            }
        } else {
            if (current.right == null) {
                current.right = new Node(data);
            } else {
                insertRecursive(current.right, data);
            }
        }
    }

    public void inOrder() {
        inOrderRecursive(root);
    }

    private void inOrderRecursive(Node current) {
        if (current != null) {
            inOrderRecursive(current.left);
            System.out.print(current.data + " ");
            inOrderRecursive(current.right);
        }
    }

    private class Node {
        int data;
        Node left;
        Node right;

        Node(int data) {
            this.data = data;
        }
    }
}

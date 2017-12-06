package com.backbase.backbasereader.model;

import java.util.HashMap;

/**
 * Node class used in the Trie Implementation
 */
public class TrieNode {
    private char c;
    private TrieNode parent;
    private HashMap<Character, TrieNode> children = new HashMap<>();
    private boolean isLeaf;

    public TrieNode() {}
    public TrieNode(char c){this.c = c;}

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    public TrieNode getParent() {
        return parent;
    }

    public void setParent(TrieNode parent) {
        this.parent = parent;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public char getCharacter() {
        return c;
    }
}

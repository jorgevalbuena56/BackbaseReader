package com.backbase.backbasereader.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Implementation of a Trie to accelerate the city searches
 */

public class Trie {
    private TrieNode root;
    private ArrayList<String> words;
    private TrieNode prefixRoot;
    private String curPrefix;

    public Trie() {
        root = new TrieNode();
        words  = new ArrayList<>();
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        HashMap<Character, TrieNode> children = root.getChildren();
        TrieNode crntparent;
        crntparent = root;

        //cur children parent = root
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            TrieNode t;
            if(children.containsKey(c)){
                t = children.get(c);
            } else {
                t = new TrieNode(c);
                t.setParent(crntparent);
                children.put(c, t);
            }

            children = t.getChildren();
            crntparent = t;

            //set leaf node
            if(i == word.length() - 1) {
                t.setLeaf(true);
            }
        }
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        if (searchNode(prefix) == null) {
            return false;
        } else{
            return true;
        }
    }

    public TrieNode searchNode(String str) {
        Map<Character, TrieNode> children = root.getChildren();
        TrieNode t = null;
        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if(children.containsKey(c)) {
                t = children.get(c);
                children = t.getChildren();
            } else{
                return null;
            }
        }

        prefixRoot = t;
        curPrefix = str;
        words.clear();
        return t;
    }

    public ArrayList<String> searchPrefix(TrieNode node, int offset) {
        if(node.isLeaf()) {
            TrieNode altair;
            altair = node;
            Stack<String> hstack = new Stack<>();

            while(altair != prefixRoot) {
                hstack.push( Character.toString(altair.getCharacter()) );
                altair = altair.getParent();
            }

            StringBuilder wrd = new StringBuilder(curPrefix);
            while(!hstack.empty()) {
                wrd .append(hstack.pop());
            }

            words.add(wrd.toString());

        }

        Set<Character> kset = node.getChildren().keySet();
        Iterator itr = kset.iterator();
        ArrayList<Character> aloc = new ArrayList<>();

        while(itr.hasNext()) {
            Character ch = (Character)itr.next();
            aloc.add(ch);
        }

        for( int i=0;i<aloc.size();i++) {
            searchPrefix(node.getChildren().get(aloc.get(i)), offset + 2);
        }

        return words;
    }
}
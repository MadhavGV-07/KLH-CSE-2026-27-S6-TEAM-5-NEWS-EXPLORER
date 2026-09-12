package com.newsexplorer.dsa;

import java.util.*;

/**
 * Prefix Tree (Trie) Data Structure for Fast Lexicographical Search,
 * Multi-pattern Lookups, and Inverted Indexing of News Articles.
 *
 * Time Complexity:
 *   - Insertion: O(L) where L is the length of the keyword
 *   - Search / Prefix Query: O(L)
 * Space Complexity: O(ALPHABET_SIZE * L * N)
 */
public class TrieSearch {

    public static class TrieNode {
        public final Map<Character, TrieNode> children = new TreeMap<>();
        public boolean isEndOfWord = false;
        public int frequency = 0;
        // List of article IDs and character positions where this term appears
        public final List<Occurrence> occurrences = new ArrayList<>();
    }

    public static class Occurrence {
        public final String articleId;
        public final int position;

        public Occurrence(String articleId, int position) {
            this.articleId = articleId;
            this.position = position;
        }
    }

    public static class TrieSearchResult {
        public final String term;
        public final boolean found;
        public final int totalFrequency;
        public final List<Occurrence> occurrences;
        public final List<String> suggestions;
        public final long lookupTimeNs;

        public TrieSearchResult(String term, boolean found, int totalFrequency,
                                List<Occurrence> occurrences, List<String> suggestions,
                                long lookupTimeNs) {
            this.term = term;
            this.found = found;
            this.totalFrequency = totalFrequency;
            this.occurrences = occurrences;
            this.suggestions = suggestions;
            this.lookupTimeNs = lookupTimeNs;
        }
    }

    private final TrieNode root = new TrieNode();
    private int totalWordCount = 0;

    public TrieNode getRoot() {
        return root;
    }

    public int getTotalWordCount() {
        return totalWordCount;
    }

    /**
     * Insert a word into the Trie with its document origin and character offset.
     */
    public void insert(String word, String articleId, int position) {
        if (word == null || word.isEmpty()) return;
        word = word.toLowerCase();

        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            current = current.children.computeIfAbsent(ch, c -> new TrieNode());
        }

        if (!current.isEndOfWord) {
            current.isEndOfWord = true;
            totalWordCount++;
        }
        current.frequency++;
        if (articleId != null) {
            current.occurrences.add(new Occurrence(articleId, position));
        }
    }

    /**
     * Search for a specific word in the Trie.
     */
    public TrieSearchResult search(String term) {
        long start = System.nanoTime();
        if (term == null || term.isEmpty()) {
            return new TrieSearchResult(term, false, 0, Collections.emptyList(), Collections.emptyList(), 0);
        }

        String normalized = term.toLowerCase();
        TrieNode current = root;
        for (int i = 0; i < normalized.length(); i++) {
            char ch = normalized.charAt(i);
            current = current.children.get(ch);
            if (current == null) {
                long time = System.nanoTime() - start;
                List<String> suggestions = findPrefixCompletions(normalized.substring(0, Math.max(0, i)), 5);
                return new TrieSearchResult(term, false, 0, Collections.emptyList(), suggestions, time);
            }
        }

        long time = System.nanoTime() - start;
        List<String> suggestions = findPrefixCompletions(normalized, 8);
        boolean isMatch = current.isEndOfWord;
        int freq = isMatch ? current.frequency : 0;
        List<Occurrence> occ = new ArrayList<>(isMatch ? current.occurrences : Collections.emptyList());

        // Also aggregate prefix occurrences if term matches or is prefix of other terms
        List<Occurrence> prefixOccs = new ArrayList<>();
        collectAllOccurrences(current, prefixOccs, 50);
        if (freq == 0 && !prefixOccs.isEmpty()) {
            freq = prefixOccs.size();
            occ = prefixOccs;
        }

        return new TrieSearchResult(term, isMatch || !prefixOccs.isEmpty(), freq, occ, suggestions, time);
    }

    private void collectAllOccurrences(TrieNode node, List<Occurrence> acc, int limit) {
        if (node == null || acc.size() >= limit) return;
        acc.addAll(node.occurrences);
        for (TrieNode child : node.children.values()) {
            collectAllOccurrences(child, acc, limit);
            if (acc.size() >= limit) break;
        }
    }

    /**
     * Find auto-completion suggestions for a given prefix.
     */
    public List<String> findPrefixCompletions(String prefix, int maxResults) {
        List<String> results = new ArrayList<>();
        if (prefix == null || prefix.isEmpty()) return results;

        String normalized = prefix.toLowerCase();
        TrieNode current = root;
        for (int i = 0; i < normalized.length(); i++) {
            current = current.children.get(normalized.charAt(i));
            if (current == null) {
                return results;
            }
        }

        collectWords(current, new StringBuilder(normalized), results, maxResults);
        return results;
    }

    private void collectWords(TrieNode node, StringBuilder currentWord, List<String> results, int maxResults) {
        if (results.size() >= maxResults) return;

        if (node.isEndOfWord) {
            results.add(currentWord.toString());
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            currentWord.append(entry.getKey());
            collectWords(entry.getValue(), currentWord, results, maxResults);
            currentWord.deleteCharAt(currentWord.length() - 1);
            if (results.size() >= maxResults) return;
        }
    }

    /**
     * Builds an inverted index Trie from a list of NewsArticles.
     */
    public static TrieSearch buildCorpusIndex(List<NewsArticle> articles) {
        TrieSearch trie = new TrieSearch();
        for (NewsArticle article : articles) {
            String text = article.getFullSearchableText();
            StringBuilder token = new StringBuilder();
            int startOffset = 0;

            for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);
                if (Character.isLetterOrDigit(ch)) {
                    if (token.isEmpty()) {
                        startOffset = i;
                    }
                    token.append(ch);
                } else {
                    if (!token.isEmpty()) {
                        trie.insert(token.toString(), article.getId(), startOffset);
                        token.setLength(0);
                    }
                }
            }
            if (!token.isEmpty()) {
                trie.insert(token.toString(), article.getId(), startOffset);
            }
        }
        return trie;
    }

    /**
     * Generates a compact ASCII visualization of the Trie top levels.
     */
    public String renderAsciiTree(int maxDepth) {
        StringBuilder sb = new StringBuilder();
        sb.append("(root)\n");
        renderNodeAscii(root, "", maxDepth, 0, sb);
        return sb.toString();
    }

    private void renderNodeAscii(TrieNode node, String indent, int maxDepth, int currentDepth, StringBuilder sb) {
        if (currentDepth >= maxDepth) return;

        List<Map.Entry<Character, TrieNode>> entries = new ArrayList<>(node.children.entrySet());
        for (int i = 0; i < entries.size(); i++) {
            Map.Entry<Character, TrieNode> entry = entries.get(i);
            boolean isLast = (i == entries.size() - 1);
            String branch = isLast ? "└── " : "├── ";
            sb.append(indent).append(branch).append("'").append(entry.getKey()).append("'");
            if (entry.getValue().isEndOfWord) {
                sb.append(" [TERM, freq=").append(entry.getValue().frequency).append("]");
            }
            sb.append("\n");

            String nextIndent = indent + (isLast ? "    " : "│   ");
            renderNodeAscii(entry.getValue(), nextIndent, maxDepth, currentDepth + 1, sb);
        }
    }
}

package com.newsexplorer.dsa;

import java.util.*;

public class DocumentSimilarityGraph {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "a", "an", "the", "and", "or", "but", "is", "are", "was", "were", "be", "been",
        "in", "on", "at", "to", "for", "with", "by", "about", "against", "between", "into",
        "through", "during", "before", "after", "above", "below", "from", "up", "down", "of",
        "off", "over", "under", "again", "further", "then", "once", "here", "there", "when",
        "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other",
        "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very",
        "can", "will", "just", "should", "now", "has", "have", "had", "this", "that", "these", "those"
    ));

    public static class SimilarityNode {
        public String id;
        public String title;
        public String category;
        public List<String> keywords;

        public SimilarityNode(String id, String title, String category, List<String> keywords) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.keywords = keywords;
        }
    }

    public static class SimilarityEdge {
        public String source;
        public String target;
        public double similarity;
        public List<String> sharedTerms;

        public SimilarityEdge(String source, String target, double similarity, List<String> sharedTerms) {
            this.source = source;
            this.target = target;
            this.similarity = similarity;
            this.sharedTerms = sharedTerms;
        }
    }

    public static class CosineSimilarityResult {
        public double score;
        public List<String> sharedTerms;

        public CosineSimilarityResult(double score, List<String> sharedTerms) {
            this.score = score;
            this.sharedTerms = sharedTerms;
        }
    }

    public static class SimilarityGraphData {
        public List<SimilarityNode> nodes;
        public List<SimilarityEdge> edges;

        public SimilarityGraphData(List<SimilarityNode> nodes, List<SimilarityEdge> edges) {
            this.nodes = nodes;
            this.edges = edges;
        }
    }

    public static List<String> tokenize(String text) {
        if (text == null) return Collections.emptyList();
        String[] rawWords = text.toLowerCase().replaceAll("[^a-z0-9\\s]", " ").split("\\s+");
        List<String> tokens = new ArrayList<>();

        for (String w : rawWords) {
            if (w.length() > 2 && !STOP_WORDS.contains(w)) {
                tokens.add(w);
            }
        }
        return tokens;
    }

    public static Map<String, Double> computeTF(List<String> tokens) {
        Map<String, Double> tf = new HashMap<>();
        if (tokens.isEmpty()) return tf;

        for (String token : tokens) {
            tf.put(token, tf.getOrDefault(token, 0.0) + 1.0);
        }
        double total = tokens.size();
        for (Map.Entry<String, Double> entry : tf.entrySet()) {
            entry.setValue(entry.getValue() / total);
        }
        return tf;
    }

    public static Map<String, Double> computeIDF(List<List<String>> docsTokens) {
        Map<String, Double> idf = new HashMap<>();
        int numDocs = docsTokens.size();
        if (numDocs == 0) return idf;

        Map<String, Integer> docFreq = new HashMap<>();
        for (List<String> tokens : docsTokens) {
            Set<String> uniqueTokens = new HashSet<>(tokens);
            for (String token : uniqueTokens) {
                docFreq.put(token, docFreq.getOrDefault(token, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : docFreq.entrySet()) {
            double val = Math.log((double) numDocs / entry.getValue()) + 1.0;
            idf.put(entry.getKey(), val);
        }
        return idf;
    }

    public static CosineSimilarityResult cosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        List<String> shared = new ArrayList<>();

        for (Map.Entry<String, Double> e1 : vec1.entrySet()) {
            String term = e1.getKey();
            double v1 = e1.getValue();
            norm1 += v1 * v1;
            if (vec2.containsKey(term)) {
                double v2 = vec2.get(term);
                dotProduct += v1 * v2;
                shared.add(term);
            }
        }

        for (double v2 : vec2.values()) {
            norm2 += v2 * v2;
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return new CosineSimilarityResult(0.0, Collections.emptyList());
        }

        double score = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        score = Math.min(1.0, Math.max(0.0, score));

        // Sort shared terms by product of TF-IDF weights
        shared.sort((a, b) -> Double.compare(
            (vec1.getOrDefault(b, 0.0) * vec2.getOrDefault(b, 0.0)),
            (vec1.getOrDefault(a, 0.0) * vec2.getOrDefault(a, 0.0))
        ));

        List<String> topShared = shared.size() > 5 ? shared.subList(0, 5) : shared;

        return new CosineSimilarityResult(score, topShared);
    }

    public static SimilarityGraphData buildGraph(List<NewsArticle> articles, double threshold) {
        List<List<String>> docTokensList = new ArrayList<>();
        for (NewsArticle art : articles) {
            docTokensList.add(tokenize(art.getFullSearchableText()));
        }

        Map<String, Double> idfMap = computeIDF(docTokensList);
        List<Map<String, Double>> docTfIdfVectors = new ArrayList<>();

        for (List<String> tokens : docTokensList) {
            Map<String, Double> tfMap = computeTF(tokens);
            Map<String, Double> tfIdfMap = new HashMap<>();
            for (Map.Entry<String, Double> e : tfMap.entrySet()) {
                double idf = idfMap.getOrDefault(e.getKey(), 1.0);
                tfIdfMap.put(e.getKey(), e.getValue() * idf);
            }
            docTfIdfVectors.add(tfIdfMap);
        }

        List<SimilarityNode> nodes = new ArrayList<>();
        for (int i = 0; i < articles.size(); i++) {
            NewsArticle art = articles.get(i);
            Map<String, Double> vector = docTfIdfVectors.get(i);

            List<Map.Entry<String, Double>> entries = new ArrayList<>(vector.entrySet());
            entries.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

            List<String> topKeywords = new ArrayList<>();
            for (int k = 0; k < Math.min(4, entries.size()); k++) {
                topKeywords.add(entries.get(k).getKey());
            }

            nodes.add(new SimilarityNode(art.getId(), art.getTitle(), art.getCategory(), topKeywords));
        }

        List<SimilarityEdge> edges = new ArrayList<>();
        for (int i = 0; i < articles.size(); i++) {
            for (int j = i + 1; j < articles.size(); j++) {
                CosineSimilarityResult simRes = cosineSimilarity(docTfIdfVectors.get(i), docTfIdfVectors.get(j));
                if (simRes.score >= threshold) {
                    edges.add(new SimilarityEdge(
                        articles.get(i).getId(),
                        articles.get(j).getId(),
                        Math.round(simRes.score * 1000.0) / 1000.0,
                        simRes.sharedTerms
                    ));
                }
            }
        }

        return new SimilarityGraphData(nodes, edges);
    }
}

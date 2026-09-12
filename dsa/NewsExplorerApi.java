package com.newsexplorer.dsa;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class NewsExplorerApi {

    private static final List<NewsArticle> SAMPLE_ARTICLES = Arrays.asList(
        new NewsArticle("art-101", "Quantum Leap in Artificial Intelligence: Neural Networks Achieves Real-Time Reasoning",
            "Researchers demonstrate a groundbreaking neural network architecture...",
            "In a historic milestone for artificial intelligence, computer scientists at the Quantum AI Research Consortium have unveiled a new hybrid neural network architecture capable of performing real-time logical reasoning over trillion-parameter datasets. By combining Knuth-Morris-Pratt pattern matching algorithms for high-speed token lookup with quantum state superposition, the system achieves sub-millisecond query responses.",
            "AI", "Dr. Elena Rostova", "2026-08-28"),
        new NewsArticle("art-102", "Global Economy Faces Shift as Algorithmic High-Frequency Trading Dominates Markets",
            "Financial markets report unprecedented volume as machine learning models...",
            "Financial institutions are overhauling legacy trade execution systems in favor of hyper-optimized algorithms utilizing Rabin-Karp polynomial rolling hashes to monitor arbitrage opportunities across global exchanges. As algorithmic high-frequency trading accounts for over 82% of equity transactions worldwide, regulatory authorities are introducing new real-time auditing frameworks.",
            "Economy", "Marcus Vance", "2026-08-27"),
        new NewsArticle("art-103", "Climate Science Breakthrough: Ocean Sensor Networks Detect Deep-Sea Thermal Shifts",
            "Autonomous oceanographic sensor floats deploy advanced pattern detection...",
            "An international climate research initiative has deployed over 5,000 deep-sea autonomous sensor pods across the Atlantic Ocean. Utilizing lightweight, energy-efficient string matching algorithms onboard embedded microcontrollers, these sensors search stream telemetry for sudden thermal spikes and salinity variations.",
            "Climate", "Aria Chen", "2026-08-26"),
        new NewsArticle("art-104", "Zero-Day Cyber Threats Defeated by Dynamic Pattern Matching and Graph Neural Networks",
            "Cybersecurity teams implement dynamic fuzzy text matching and edit distance metrics...",
            "Polymorphic malware which constantly alters its byte sequence to evade traditional antivirus signature checks is facing a formidable new opponent. Cybersecurity defense platforms have integrated Levenshtein fuzzy matching and KMP string search into kernel-level memory monitoring engines.",
            "Cybersecurity", "Alexander Wright", "2026-08-25")
    );

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/api/status", new StatusHandler());
        server.createContext("/api/search", new SearchHandler());
        server.createContext("/api/kmp-trace", new KmpTraceHandler());
        server.createContext("/api/rabin-karp-trace", new RabinKarpTraceHandler());
        server.createContext("/api/fuzzy-trace", new FuzzyTraceHandler());
        server.createContext("/api/suffix-trace", new SuffixTraceHandler());
        server.createContext("/api/graph", new GraphHandler());
        server.createContext("/api/benchmark", new BenchmarkHandler());

        server.setExecutor(null);
        System.out.println("=================================================");
        System.out.println("   News Explorer Java 25 DSA Engine Active");
        System.out.println("   Server running on http://localhost:" + port);
        System.out.println("=================================================");
        server.start();
    }

    static class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            String response = "{\"status\":\"OK\", \"engine\":\"Java 25 DSA Core\", \"language\":\"Java 25 LTS\", \"version\":\"1.0.0\"}";
            sendJsonResponse(exchange, response);
        }
    }

    static class SearchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String query = params.getOrDefault("q", "algorithm");
            String algo = params.getOrDefault("algo", "KMP");

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < SAMPLE_ARTICLES.size(); i++) {
                NewsArticle art = SAMPLE_ARTICLES.get(i);
                List<Integer> matches = new ArrayList<>();
                long execTime = 0;
                int comps = 0;

                if ("KMP".equalsIgnoreCase(algo)) {
                    KmpSearch.KmpResult res = KmpSearch.search(art.getFullSearchableText(), query, false);
                    matches = res.matches;
                    execTime = res.executionTimeUs;
                    comps = res.comparisonsCount;
                } else if ("RabinKarp".equalsIgnoreCase(algo)) {
                    RabinKarpSearch.RabinKarpResult res = RabinKarpSearch.search(art.getFullSearchableText(), query, false);
                    matches = res.matches;
                    execTime = res.executionTimeUs;
                    comps = res.comparisonsCount;
                } else {
                    FuzzyEditDistance.FuzzySearchResult fRes = FuzzyEditDistance.fuzzySearchInText(query, art.getFullSearchableText(), 2);
                    for (FuzzyEditDistance.FuzzyTextMatch m : fRes.matches) {
                        matches.add(m.start);
                    }
                    execTime = fRes.executionTimeUs;
                    comps = fRes.comparisonsCount;
                }

                json.append("{")
                    .append("\"articleId\":\"").append(art.getId()).append("\",")
                    .append("\"articleTitle\":\"").append(escapeJson(art.getTitle())).append("\",")
                    .append("\"articleCategory\":\"").append(art.getCategory()).append("\",")
                    .append("\"matchIndices\":").append(matches).append(",")
                    .append("\"executionTimeUs\":").append(execTime).append(",")
                    .append("\"comparisonsCount\":").append(comps).append(",")
                    .append("\"algorithmUsed\":\"").append(algo).append("\",")
                    .append("\"engine\":\"Java 25 Core\"")
                    .append("}");

                if (i < SAMPLE_ARTICLES.size() - 1) json.append(",");
            }
            json.append("]");

            sendJsonResponse(exchange, json.toString());
        }
    }

    static class KmpTraceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String pattern = params.getOrDefault("pattern", "neural");
            String text = params.getOrDefault("text", "neural network architecture");

            KmpSearch.KmpResult res = KmpSearch.search(text, pattern, false);

            StringBuilder stepTraceJson = new StringBuilder("[");
            for (int k = 0; k < res.stepTrace.size(); k++) {
                KmpSearch.KmpStepTrace st = res.stepTrace.get(k);
                stepTraceJson.append("{")
                    .append("\"stepIndex\":").append(st.stepIndex).append(",")
                    .append("\"textPointer\":").append(st.textPointer).append(",")
                    .append("\"patternPointer\":").append(st.patternPointer).append(",")
                    .append("\"currentTextChar\":\"").append(escapeJson(st.currentTextChar)).append("\",")
                    .append("\"currentPatternChar\":\"").append(escapeJson(st.currentPatternChar)).append("\",")
                    .append("\"isMatch\":").append(st.isMatch).append(",")
                    .append("\"lpsValue\":").append(st.lpsValue).append(",")
                    .append("\"action\":\"").append(escapeJson(st.action)).append("\",")
                    .append("\"lpsArrayState\":").append(Arrays.toString(st.lpsArrayState))
                    .append("}");
                if (k < res.stepTrace.size() - 1) stepTraceJson.append(",");
            }
            stepTraceJson.append("]");

            String json = String.format(
                "{\"engine\":\"Java 25 KmpSearch\", \"algorithm\":\"KMP\", \"pattern\":\"%s\", \"lps\":%s, \"matches\":%s, \"comparisonsCount\":%d, \"executionTimeUs\":%d, \"stepTrace\":%s}",
                escapeJson(pattern), Arrays.toString(res.lps), res.matches, res.comparisonsCount, res.executionTimeUs, stepTraceJson.toString()
            );

            sendJsonResponse(exchange, json);
        }
    }

    static class RabinKarpTraceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String pattern = params.getOrDefault("pattern", "neural");
            String text = params.getOrDefault("text", "neural network architecture");

            RabinKarpSearch.RabinKarpResult res = RabinKarpSearch.search(text, pattern, false);

            StringBuilder stepTraceJson = new StringBuilder("[");
            for (int k = 0; k < res.stepTrace.size(); k++) {
                RabinKarpSearch.RabinKarpStepTrace st = res.stepTrace.get(k);
                stepTraceJson.append("{")
                    .append("\"stepIndex\":").append(st.stepIndex).append(",")
                    .append("\"windowStart\":").append(st.windowStart).append(",")
                    .append("\"windowText\":\"").append(escapeJson(st.windowText)).append("\",")
                    .append("\"windowHash\":").append(st.windowHash).append(",")
                    .append("\"patternHash\":").append(st.patternHash).append(",")
                    .append("\"hashCollision\":").append(st.hashCollision).append(",")
                    .append("\"actualMatch\":").append(st.actualMatch).append(",")
                    .append("\"action\":\"").append(escapeJson(st.action)).append("\"")
                    .append("}");
                if (k < res.stepTrace.size() - 1) stepTraceJson.append(",");
            }
            stepTraceJson.append("]");

            String json = String.format(
                "{\"engine\":\"Java 25 RabinKarpSearch\", \"algorithm\":\"Rabin-Karp\", \"patternHash\":%d, \"matches\":%s, \"comparisonsCount\":%d, \"hashCollisionsCount\":%d, \"executionTimeUs\":%d, \"stepTrace\":%s}",
                res.patternHash, res.matches, res.comparisonsCount, res.collisionsCount, res.executionTimeUs, stepTraceJson.toString()
            );

            sendJsonResponse(exchange, json);
        }
    }

    static class FuzzyTraceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String pattern = params.getOrDefault("pattern", "neural");
            String text = params.getOrDefault("text", "neuron");

            FuzzyEditDistance.EditDistanceResult res = FuzzyEditDistance.compute(pattern, text);

            StringBuilder matrixJson = new StringBuilder("[");
            for (int i = 0; i < res.matrix.length; i++) {
                matrixJson.append(Arrays.toString(res.matrix[i]));
                if (i < res.matrix.length - 1) matrixJson.append(",");
            }
            matrixJson.append("]");

            StringBuilder tbJson = new StringBuilder("[");
            for (int i = 0; i < res.traceback.size(); i++) {
                FuzzyEditDistance.EditDistanceTraceCell cell = res.traceback.get(i);
                tbJson.append("{")
                    .append("\"row\":").append(cell.row).append(",")
                    .append("\"col\":").append(cell.col).append(",")
                    .append("\"char1\":\"").append(escapeJson(cell.char1)).append("\",")
                    .append("\"char2\":\"").append(escapeJson(cell.char2)).append("\",")
                    .append("\"op\":\"").append(cell.op).append("\",")
                    .append("\"cost\":").append(cell.cost)
                    .append("}");
                if (i < res.traceback.size() - 1) tbJson.append(",");
            }
            tbJson.append("]");

            String json = String.format(
                "{\"engine\":\"Java 25 FuzzyEditDistance\", \"algorithm\":\"Fuzzy Edit Distance\", \"pattern\":\"%s\", \"target\":\"%s\", \"distance\":%d, \"similarityRatio\":%.4f, \"matrix\":%s, \"traceback\":%s, \"comparisonsCount\":%d, \"executionTimeUs\":%d}",
                escapeJson(pattern), escapeJson(text), res.distance, res.similarityRatio, matrixJson.toString(), tbJson.toString(), res.comparisonsCount, res.executionTimeUs
            );

            sendJsonResponse(exchange, json);
        }
    }

    static class SuffixTraceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String text = params.getOrDefault("text", "neural network architecture neural network");

            SuffixArrayLCP.SuffixAnalysisResult res = SuffixArrayLCP.analyzeCorpusPatterns(text);

            StringBuilder subJson = new StringBuilder("[");
            for (int i = 0; i < res.longestRepeatedSubstrings.size(); i++) {
                SuffixArrayLCP.RepeatedSubstring item = res.longestRepeatedSubstrings.get(i);
                subJson.append("{")
                    .append("\"text\":\"").append(escapeJson(item.text)).append("\",")
                    .append("\"count\":").append(item.count).append(",")
                    .append("\"length\":").append(item.length)
                    .append("}");
                if (i < res.longestRepeatedSubstrings.size() - 1) subJson.append(",");
            }
            subJson.append("]");

            String json = String.format(
                "{\"engine\":\"Java 25 SuffixArrayLCP\", \"suffixArray\":%s, \"lpsOrLcpArray\":%s, \"longestRepeatedSubstrings\":%s, \"executionTimeUs\":%d}",
                Arrays.toString(res.suffixArray), Arrays.toString(res.lcpArray), subJson.toString(), res.executionTimeUs
            );

            sendJsonResponse(exchange, json);
        }
    }

    static class GraphHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            double threshold = Double.parseDouble(params.getOrDefault("threshold", "0.15"));

            DocumentSimilarityGraph.SimilarityGraphData graphData = DocumentSimilarityGraph.buildGraph(SAMPLE_ARTICLES, threshold);

            StringBuilder nodesJson = new StringBuilder("[");
            for (int i = 0; i < graphData.nodes.size(); i++) {
                DocumentSimilarityGraph.SimilarityNode n = graphData.nodes.get(i);
                StringBuilder kwJson = new StringBuilder("[");
                for (int k = 0; k < n.keywords.size(); k++) {
                    kwJson.append("\"").append(escapeJson(n.keywords.get(k))).append("\"");
                    if (k < n.keywords.size() - 1) kwJson.append(",");
                }
                kwJson.append("]");

                nodesJson.append("{")
                    .append("\"id\":\"").append(n.id).append("\",")
                    .append("\"title\":\"").append(escapeJson(n.title)).append("\",")
                    .append("\"category\":\"").append(n.category).append("\",")
                    .append("\"keywords\":").append(kwJson.toString())
                    .append("}");
                if (i < graphData.nodes.size() - 1) nodesJson.append(",");
            }
            nodesJson.append("]");

            StringBuilder edgesJson = new StringBuilder("[");
            for (int i = 0; i < graphData.edges.size(); i++) {
                DocumentSimilarityGraph.SimilarityEdge e = graphData.edges.get(i);
                StringBuilder stJson = new StringBuilder("[");
                for (int k = 0; k < e.sharedTerms.size(); k++) {
                    stJson.append("\"").append(escapeJson(e.sharedTerms.get(k))).append("\"");
                    if (k < e.sharedTerms.size() - 1) stJson.append(",");
                }
                stJson.append("]");

                edgesJson.append("{")
                    .append("\"source\":\"").append(e.source).append("\",")
                    .append("\"target\":\"").append(e.target).append("\",")
                    .append("\"similarity\":").append(e.similarity).append(",")
                    .append("\"sharedTerms\":").append(stJson.toString())
                    .append("}");
                if (i < graphData.edges.size() - 1) edgesJson.append(",");
            }
            edgesJson.append("]");

            String json = String.format("{\"engine\":\"Java 25 DocumentSimilarityGraph Engine\", \"nodes\":%s, \"edges\":%s}",
                    nodesJson.toString(), edgesJson.toString());

            sendJsonResponse(exchange, json);
        }
    }

    static class BenchmarkHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            addCorsHeaders(exchange);
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            Map<String, String> params = parseQueryParams(exchange.getRequestURI().getQuery());
            String pattern = params.getOrDefault("pattern", "algorithm");
            String text = params.getOrDefault("text", "sample news text stream algorithm");

            List<AlgorithmBenchmark.BenchmarkMetrics> metrics = AlgorithmBenchmark.runBenchmark(text, pattern);

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < metrics.size(); i++) {
                AlgorithmBenchmark.BenchmarkMetrics m = metrics.get(i);
                json.append("{")
                    .append("\"algorithm\":\"").append(m.algorithm).append("\",")
                    .append("\"executionTimeUs\":").append(m.executionTimeUs).append(",")
                    .append("\"comparisons\":").append(m.comparisons).append(",")
                    .append("\"matchesFound\":").append(m.matchesFound).append(",")
                    .append("\"timeComplexity\":\"").append(m.timeComplexity).append("\",")
                    .append("\"spaceComplexity\":\"").append(m.spaceComplexity).append("\",")
                    .append("\"engine\":\"").append(m.engine).append("\"")
                    .append("}");
                if (i < metrics.size() - 1) json.append(",");
            }
            json.append("]");

            sendJsonResponse(exchange, json.toString());
        }
    }

    private static Map<String, String> parseQueryParams(String rawQuery) {
        Map<String, String> map = new HashMap<>();
        if (rawQuery == null || rawQuery.isEmpty()) return map;
        for (String pair : rawQuery.split("&")) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                try {
                    map.put(parts[0], URLDecoder.decode(parts[1], StandardCharsets.UTF_8.name()));
                } catch (Exception e) {
                    map.put(parts[0], parts[1]);
                }
            }
        }
        return map;
    }

    private static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    private static void sendJsonResponse(HttpExchange exchange, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private static String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}

#  News Explorer

### An Interactive News Search & Text-Analysis Engine Powered by Data Structures and Algorithms

News Explorer is an academic DSA project that demonstrates how
string-matching algorithms, dynamic programming, indexing structures,
and document-similarity techniques can be applied to a collection of
news articles.

The project combines a **Java backend** with a lightweight **HTML, CSS,
and JavaScript frontend**. Users can search articles, inspect algorithm
traces, explore article-similarity relationships, and compare algorithm
metrics through an interactive browser interface.

> **Project focus:** learning and demonstrating DSA concepts through a
> practical text-search application. Benchmark results depend on the
> machine, input data, and test conditions; run the benchmark suite
> before making performance claims.

------------------------------------------------------------------------

##  Features

###  News Explorer

-   Search the available news-article collection using the Java search
    API.
-   Choose among supported search modes.
-   View matching articles and match information in the browser.
-   Explore results through the interactive user interface.

###  DSA Visualizer Lab

-   Step-by-step KMP search trace and LPS-table explanation.
-   Rabin--Karp rolling-hash trace.
-   Edit-distance dynamic-programming trace.
-   Suffix-array/LCP analysis.
-   Algorithm-focused views intended to make internal operations easier
    to understand.

###  Article Similarity Graph

-   Represents articles as graph nodes.
-   Uses TF-IDF term weighting and cosine similarity to estimate textual
    similarity.
-   Displays similarity relationships through a graph visualization.

### Benchmark Suite

-   Compares supported search algorithms using the project's benchmark
    implementation.
-   Reports algorithm metrics such as execution time, character
    comparisons, and match counts where provided by the implementation.
-   Use consistent input data and repeated runs when collecting results
    for a report.

###  Java Source Browser

-   Lets users inspect selected Java source code from the interface.

###  Console Mode

-   Includes an interactive command-line application and a demo mode for
    exploring the algorithms without the browser interface.

------------------------------------------------------------------------

##  Algorithms & Data Structures

  ------------------------------------------------------------------------------------------------------
  Algorithm / technique  Source class                     Purpose              Complexity notes
  ---------------------- -------------------------------- -------------------- -------------------------
  Naive string matching  `NaiveSearch.java`               Baseline exact       Worst-case time:
                                                          pattern matching     `O(N × M)`

  Knuth--Morris--Pratt   `KmpSearch.java`                 Exact pattern        Search plus
  (KMP)                                                   matching using an    preprocessing: `O(N + M)`
                                                          LPS array            

  Rabin--Karp            `RabinKarpSearch.java`           Pattern matching     Expected/average behavior
                                                          with a rolling hash  can be `O(N + M)` under
                                                                               suitable assumptions;
                                                                               worst case can be
                                                                               `O(N × M)`

  Levenshtein edit       `FuzzyEditDistance.java`         Fuzzy matching and   Standard full-DP
  distance                                                edit-distance        implementation:
                                                          analysis             `O(N × M)` time and space

  Trie / prefix index    `TrieSearch.java`                Prefix search, word  Typical lookup depends on
                                                          lookup, and indexed  key length; often `O(L)`
                                                          occurrences          with suitable child
                                                                               access

  Suffix array and LCP   `SuffixArrayLCP.java`            Suffix analysis and  Depends on construction
                                                          repeated-substring   method; check
                                                          exploration          implementation-specific
                                                                               bounds

  TF-IDF and cosine      `DocumentSimilarityGraph.java`   Document-vector      Depends on corpus size,
  similarity                                              similarity and       vocabulary, and
                                                          article graph        implementation

  Benchmarking           `AlgorithmBenchmark.java`        Collects and         Depends on algorithms and
                                                          compares algorithm   test inputs
                                                          metrics              
  ------------------------------------------------------------------------------------------------------

**Notation:** `N` = text length, `M` = pattern length, `L` = word or
query length. Complexity statements describe common algorithmic bounds
and assumptions; actual runtime depends on implementation and data.

------------------------------------------------------------------------

##  Technology Stack

-   **Java:** search algorithms, data structures, corpus handling, HTTP
    API, and benchmark logic.
-   **HTML:** page structure.
-   **CSS:** styling and responsive presentation.
-   **JavaScript:** browser interactions, API requests, visualizers, and
    result rendering.
-   **JSON:** article data used by the frontend.
-   **Java built-in HTTP server:** exposes the local API on port `8080`.

The browser frontend does not require React or TypeScript. The Java
project is compiled directly with `javac`; Maven and Gradle are not
required by the supplied scripts.

------------------------------------------------------------------------

##  Project Structure

``` text
dsa 3 project/
├── index.html
├── style.css
├── app.js
├── articles.json
├── package.json
├── compile.bat
├── compile.sh
├── run.bat
├── run.sh
├── run-website.bat
├── open-website.bat
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── newsexplorer/
│                   └── dsa/
│                       ├── NewsExplorerApp.java
│                       ├── NewsExplorerApi.java
│                       ├── ArticleCorpus.java
│                       ├── NewsArticle.java
│                       ├── NaiveSearch.java
│                       ├── KmpSearch.java
│                       ├── RabinKarpSearch.java
│                       ├── FuzzyEditDistance.java
│                       ├── TrieSearch.java
│                       ├── SuffixArrayLCP.java
│                       ├── DocumentSimilarityGraph.java
│                       └── AlgorithmBenchmark.java
└── bin/  # compiled .class files; regenerated by compile scripts
```

### Main Java classes

  -----------------------------------------------------------------------
  Class                               Responsibility
  ----------------------------------- -----------------------------------
  `NewsExplorerApp`                   Application entry point,
                                      command-line menu, demo/server
                                      launch

  `NewsExplorerApi`                   Local HTTP server and API route
                                      handlers

  `ArticleCorpus`                     Java-side in-memory article
                                      collection and corpus operations

  `NewsArticle`                       Data model for a news article

  `NaiveSearch`                       Brute-force baseline string
                                      matching

  `KmpSearch`                         KMP algorithm, LPS computation, and
                                      trace data

  `RabinKarpSearch`                   Rolling-hash search and trace data

  `FuzzyEditDistance`                 Levenshtein distance, fuzzy search,
                                      and DP trace

  `TrieSearch`                        Trie-based word/prefix indexing

  `SuffixArrayLCP`                    Suffix-array, LCP, and
                                      repeated-substring analysis

  `DocumentSimilarityGraph`           TF-IDF, cosine similarity, and
                                      similarity graph data

  `AlgorithmBenchmark`                Runs algorithm comparisons and
                                      collects metrics
  -----------------------------------------------------------------------

------------------------------------------------------------------------

##  Requirements

-   A JDK with both `java` and `javac` available on `PATH` (Java 17 or
    later is the intended baseline in the project instructions; use the
    JDK version with which the project compiles successfully).
-   Windows: use the supplied `.bat` scripts.
-   Linux/macOS: use the supplied `.sh` scripts and ensure they have
    execute permission if required.
-   A modern web browser.

No database server is required for the supplied in-memory
implementation. No external Java dependency manager is required by the
compile scripts.

------------------------------------------------------------------------

##  Quick Start

### Windows

1.  Extract the project ZIP.

2.  Open the extracted `dsa 3 project` folder.

3.  Compile the Java sources:

    ``` bat
    compile.bat
    ```

4.  Start the website and Java API server:

    ``` bat
    run-website.bat
    ```

5.  Open the browser at:

    **http://localhost:8080/**

The website launcher attempts to open the local URL and starts the Java
server. Keep the server terminal open while using the site.

### Linux / macOS

From the project directory:

``` bash
chmod +x compile.sh run.sh
./compile.sh
./run.sh --server
```

Then open **http://localhost:8080/** in your browser.

If the browser page does not load, first confirm that compilation
succeeded and that the server process is still running.

------------------------------------------------------------------------

##  Console / CLI Mode

Compile the project first, then run:

### Windows

``` bat
run.bat
```

Run the demo mode:

``` bat
run.bat --demo
```

### Linux / macOS

``` bash
./run.sh
```

Run the demo mode:

``` bash
./run.sh --demo
```

Use the interactive menu to explore the available algorithm
demonstrations and analysis options. For the browser application, launch
the server with `--server`.

------------------------------------------------------------------------

## 🔌 Local API

The Java server listens on port `8080` and registers these routes:

  Endpoint                  Purpose
  ------------------------- ----------------------------------------
  `GET /api/status`         Reports server status
  `/api/search`             Search the article corpus
  `/api/kmp-trace`          Returns KMP trace data
  `/api/rabin-karp-trace`   Returns Rabin--Karp trace data
  `/api/fuzzy-trace`        Returns fuzzy/edit-distance trace data
  `/api/suffix-trace`       Returns suffix-array analysis data
  `/api/graph`              Returns article-similarity graph data
  `/api/benchmark`          Returns benchmark information

The exact HTTP methods, query parameters, and response fields are
defined in `NewsExplorerApi.java`. Refer to that file when integrating
another client or documenting the API contract.

------------------------------------------------------------------------

##  How It Works

1.  The user enters a query or chooses a visualization in the browser.
2.  JavaScript handles the UI interaction and sends an HTTP request to
    the local Java API when backend processing is required.
3.  `NewsExplorerApi` dispatches the request to the relevant Java class.
4.  The selected algorithm processes the query, text, or article data.
5.  The API returns structured response data, generally as JSON.
6.  The frontend renders the search results, trace, graph, or benchmark
    information.

The Java corpus and the frontend's `articles.json` are separate data
sources in the supplied project. Check the relevant source files before
assuming they contain the same articles or counts.

------------------------------------------------------------------------

##  Testing & Benchmarking

Before including performance numbers in a project report:

1.  Compile and run the project successfully.
2.  Use the same text corpus and query for comparable exact-search
    algorithms.
3.  Test several input sizes and query types.
4.  Run each benchmark more than once.
5.  Record the JDK version, machine specifications, input size, and test
    conditions.
6.  Distinguish theoretical complexity from measured execution time.
7.  Do not compare algorithms for different tasks as if they were
    interchangeable---for example, exact pattern matching and
    edit-distance fuzzy search solve different problems.

Suggested test cases:

-   A query that occurs in an article.
-   A query that does not occur.
-   An empty query.
-   A query longer than the text.
-   Repeated or overlapping matches.
-   Mixed-case text.
-   A small spelling difference for fuzzy search.
-   Empty or very short article content.
-   Similar and dissimilar article pairs.
-   A larger corpus to observe scaling behavior.

Store actual measurements separately and label any hypothetical data as
illustrative. Do not claim a speedup or accuracy rate without recorded
evidence.

------------------------------------------------------------------------

##  Notes & Limitations

-   The application uses local article data and an in-memory Java
    corpus; it is not a live news aggregator unless an external
    ingestion source is added.
-   The frontend article dataset and Java-side corpus may differ.
-   TF-IDF and cosine similarity measure term-based similarity; they do
    not guarantee semantic equivalence.
-   Fuzzy search depends on the configured edit-distance threshold and
    can return false positives or miss more distant spelling variants.
-   Rolling hashes can collide; hash matches should be verified when
    correctness requires it.
-   Performance measurements vary with hardware, JDK, input data,
    warm-up, and measurement method.
-   The repository includes compiled output in `bin/`, but source files
    and compile scripts should be treated as the primary reproducible
    artifacts.
-   If your course requires manually implemented data structures, review
    use of Java Collections in each relevant class and follow your
    instructor's exact restrictions.

------------------------------------------------------------------------

## 🛠️ Troubleshooting

### `java` or `javac` is not recognized

Install a JDK and add its `bin` directory to your system `PATH`. Open a
new terminal and verify:

``` bash
java -version
javac -version
```

### The website cannot connect to the backend

-   Confirm the Java server is running.
-   Visit `http://localhost:8080/api/status`.
-   Check whether another process is using port `8080`.
-   Look for errors in the terminal where the server was launched.

### Compilation fails

Run the compile script from the project root. Read the first compiler
error carefully and confirm that the JDK version supports the source
code.

### The page opens but article results differ from Java search

The frontend and Java backend can load different article collections.
Check `articles.json`, `ArticleCorpus.java`, and the relevant code in
`app.js`.

------------------------------------------------------------------------

##  Academic Context

This project was developed for **Data Structures and Algorithms -- 3
(25CS2103E)** and demonstrates the application of string matching,
dynamic programming, tries, suffix structures, graph representations,
document similarity, and algorithm benchmarking.

**Project title:** News Explorer --- An Efficient News Search and
Text-Analysis Engine Using Advanced Data Structures and Algorithms

**Team** - G V MADHAV --- `2520030568` - K RICHIE KIRAN ---
`2520030498` - B VISHNU VARDHAN --- `2520030392`

**Guide:** Dr. Sireesha, Professor

**Academic year:** 2026--27

------------------------------------------------------------------------

##  License

The supplied `package.json` declares the project license as **MIT**. If
you distribute the project publicly, include an appropriate `LICENSE`
file and confirm that the project's source code and article data can
legally be distributed under that license.

------------------------------------------------------------------------

##  Acknowledgements

We thank our project guide, **Dr. Sireesha, Professor**, for her
guidance and support, and acknowledge the faculty and Department of
Computer Science and Engineering for the academic context in which this
project was developed.

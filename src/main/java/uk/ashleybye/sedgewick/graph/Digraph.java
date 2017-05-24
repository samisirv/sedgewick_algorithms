package uk.ashleybye.sedgewick.graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import uk.ashleybye.sedgewick.collections.Bag;

/**
 * The Digraph type stores a representation of a directed graph in an adjacency-list. Vertices are
 * represented by their array index, with their adjacent vertices stored in a list referenced by the
 * array index. i.e. the head vertex is the array index with the index of each tail vertex stored in
 * the adjacency-list. This provides: space usage proportional to V + E; constant time to add an
 * edge; and, time proportional to the degree of a vertex to iterate through all vertices adjacent
 * to it (i.e. constant time per adjacent vertex processed).
 *
 * Unlike for an undirected graph, the edges in a digraph are one-directional, thus there are no
 * corresponding entries in the adjacency-list for a tail vertex, unless a directed edge also exists
 * in the opposite direction.
 *
 * Using a symbol-table in lieu of a vertex-indexed array could be useful to allow adding and
 * deleting of vertices. It would also remove the requirement that vertex names are integer indices.
 * See Sedgewick, Algorithms, p527 for more discussion on this and other possible alternative data
 * structures.
 */
public class Digraph {

  /**
   * The number of vertices.
   */
  private final int numVertices;

  /**
   * The number of edges.
   */
  private int numEdges;

  /**
   * The adjacency lists.
   */
  private Bag<Integer>[] adjacencyLists;

  /**
   * Create a V-vertex directed graph with zero edges.
   *
   * @param numVertices The number of vertices.
   */
  public Digraph(int numVertices) {
    this.numVertices = numVertices;
    this.numEdges = 0;
    adjacencyLists = (Bag<Integer>[]) new Bag[numVertices];
    for (int vertex = 0; vertex < numVertices; vertex++) {
      adjacencyLists[vertex] = new Bag<>();
    }
  }

  /**
   * Create an directed graph by reading input from a file.
   *
   * It expects an input format consisting of 2E + 2 integer values each separated by a newline.
   * First, the number of vertices, next the number of edges, followed by pairs of vertex numbers
   * of edges. For example:
   *
   * 250
   * 1273
   * 244 246
   * 239 240
   * 238 245
   * ...
   *
   * @param fileName The path to the file containing the source data.
   *
   * @throws IOException If the specified file cannot be loaded.
   */
  public Digraph(String fileName) throws IOException {
    Path filePath = Paths.get(fileName);
    try (Scanner scanner = new Scanner(Files.newInputStream(filePath))) {
      this.numVertices = scanner.nextInt();
      this.numEdges = scanner.nextInt();
      adjacencyLists = (Bag<Integer>[]) new Bag[numVertices];
      for (int vertex = 0; vertex < numVertices; vertex++) {
        adjacencyLists[vertex] = new Bag<>();
      }

      for (int e = 0; e < numEdges; e++) {
        int u = scanner.nextInt();
        int v = scanner.nextInt();
        this.addEdge(u, v);

        // numEdges is incremented in addEdge(), so decrement to keep E constant.
        numEdges--;
      }
    } catch (IOException exception) {
      throw new IOException("Could not open file: " + fileName);
    }
  }

  /**
   * Gets the number of vertices in this graph.
   *
   * @return The number of vertices.
   */
  public int getNumVertices() {
    return numVertices;
  }

  /**
   * Gets the number of edges in this graph.
   *
   * @return The number of edges.
   */
  public int getNumEdges() {
    return numEdges;
  }

  /**
   * Add a directed edge from vertex u to vertex v in this graph.
   *
   * @param u Vertex incident to edge.
   * @param v Vertex incident to edge.
   */
  public void addEdge(int u, int v) {
    adjacencyLists[u].add(v);
    numEdges++;
  }

  /**
   * Creates a digraph that is the reverse of this digraph. That is, all head->tail edges are
   * reversed to become tail->head edges.
   *
   * @return The reverse of this digraph.
   */
  public Digraph reverse() {
    Digraph reversed = new Digraph(numVertices);
    for (int head = 0; head < numVertices; head++) {
      for (int tail : this.adjacentTo(head)) {
        reversed.addEdge(tail, head);
      }
    }

    return reversed;
  }

  /**
   * Gets the vertices adjacent to the specified vertex.
   *
   * @param vertex The vertex to get adjacent vertices for.
   *
   * @return The vertices adjacent to the specified vertex.
   */
  public Iterable<Integer> adjacentTo(int vertex) {
    return adjacencyLists[vertex];
  }

  /**
   * Gets a {@code String} representation of this graph, in the following format:
   *
   * V vertices, E edges
   * v: e e e
   * v: e e e e e
   * v: e
   * ...
   *
   * @return A {@code String} representation of this graph.
   */
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(numVertices).append(" vertices, ").append(numEdges).append(" edges")
        .append("\n");
    for (int v = 0; v < numVertices; v++) {
      stringBuilder.append(v).append(": ");
      for (int w : this.adjacentTo(v)) {
        stringBuilder.append(w).append(" ");
      }
      stringBuilder.append("\n");
    }

    return stringBuilder.toString();
  }
}

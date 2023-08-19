package islands.model;

import java.util.*;

/**
 * A graph.
 *
 * @param <T> the type of data held in nodes of the graph
 */
public class Graph<T> {
    private final List<Node<T>> nodes = new ArrayList<>();

    /**
     * Constructs an empty graph.
     */
    public Graph() {
    }

    /**
     * Makes a deep copy of this graph.
     *
     * @return the new graph
     */
    public Graph<T> deepCopy() {
        Graph<T> newGraph = new Graph<>();
        Map<Node<T>, Node<T>> translation = new HashMap<>();

        // Translate nodes.
        for (Node<T> oldNode : getNodes()) {
            Node<T> newNode = newGraph.addNode(oldNode.data);
            translation.put(oldNode, newNode);
        }

        // Fix parents and neighbors.
        for (Node<T> oldNode : getNodes()) {
            Node<T> newNode = translation.get(oldNode);
            newNode.parent = translation.get(oldNode.find());
            // Translate edges.
            newNode.neighbors.clear();
            for (Node<T> neighbor : oldNode.getNeighbors()) {
                newNode.addEdge(translation.get(neighbor));
            }
        }

        return newGraph;
    }

    /**
     * Adds a node with the specified data value to this graph.
     *
     * @param data the data value
     * @return the new node
     */
    public Node<T> addNode(T data) {
        Node<T> node = new Node<>(data);
        nodes.add(node);
        return node;
    }

    /**
     * Adds a bidirectional edge between two nodes.
     *
     * @param node1 the first node
     * @param node2 the second node
     */
    public void addEdge(Node<T> node1, Node<T> node2) {
        node1.addEdge(node2);
        node2.addEdge(node1);
    }

    /**
     * Gets the number of sets with the specified data value.
     *
     * @param data the value
     * @return the number of sets with nodes having this value
     */
    public int getSetCount(T data) {
        Set<Node<T>> reps = new HashSet<>();
        for (Node<T> node : nodes) {
            if (node.getData() == data) {
                reps.add(node.find());
            }
        }
        return reps.size();
    }

    /**
     * Gets the nodes of this graph in the order they were added.
     *
     * @return the nodes of this graph
     */
    public List<Node<T>> getNodes() {
        return nodes;
    }

    /**
     * A node within a graph.
     *
     * @param <T> the type of data stored in each node
     */
    public static class Node<T> {
        private static final int MAX_EDGE_COUNT = 6;
        private T data;
        private Node<T> parent;
        private int rank = 0;
        private List<Node<T>> neighbors;

        /**
         * Constructs a new node with the provided data value and no neighbors.
         *
         * @param data the data value
         */
        public Node(T data) {
            this.data = data;
            this.parent = this;
            this.neighbors = new ArrayList<>(MAX_EDGE_COUNT);
        }

        /**
         * Gets the neighbors of this node.
         *
         * @return the neighbors of this node
         */
        public List<Node<T>> getNeighbors() {
            return neighbors;
        }

        /**
         * Adds a neighbor to this node
         *
         * @param neighbor the neighbor
         */
        public void addEdge(Node<T> neighbor) {
            neighbors.add(neighbor);
        }

        /**
         * Gets the data associated with this node.
         *
         * @return the data associated with this node
         */
        public T getData() {
            return data;
        }

        /**
         * Sets the data associated with this node.
         *
         * @param data the data value
         */
        public void setData(T data) {
            this.data = data;
        }

        /**
         * Finds the representative of the set to which this node belongs.
         *
         * @return the representative
         */
        public Node<T> find() {
            if (parent == this) {
                return this;
            }
            // For now, don't use path compression.
            return parent.find();
        }

        /**
         * Merges the two sets to which this node and the other node belong.
         *
         * @param other the other node
         */
        public void union(Node<T> other) {
            Node<T> thisRep = this.find();
            Node<T> otherRep = other.find();

            // If they're already in the same set, stop.
            if (thisRep == otherRep) {
                return;
            }

            // Choose the node with the larger rank as the parent of the other.
            if (thisRep.rank > otherRep.rank) {
                otherRep.parent = thisRep;
            } else if (thisRep.rank < otherRep.rank) {
                thisRep.parent = otherRep.parent;
            } else {
                // Arbitrarily choose a node to be the parent of the other.
                otherRep.parent = thisRep;
                thisRep.rank++;
            }
        }
    }
}

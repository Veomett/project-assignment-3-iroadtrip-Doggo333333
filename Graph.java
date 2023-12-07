import java.util.*;

public class Graph {
    Map<String, Map<String, Integer>> neighbors;

    public Graph() {

        this.neighbors = new HashMap<>();

    }

    public void addCountry(String country) {

        if (!this.hasNeighbor(country))
            neighbors.put(country, new HashMap<>());

    }

    public boolean hasNeighbor(String country) {

        return neighbors.containsKey(country);

    }

    public boolean areNeighbors(String startCountry, String endCountry) {

        return (hasNeighbor(startCountry) && neighbors.get(startCountry).containsKey(endCountry));

    }
    public void addEdge(String startCountry, String endCountry, int distance) {

        neighbors.get(startCountry).put(endCountry, distance);
        neighbors.get(endCountry).put(startCountry, distance);

    }

    public void replaceEdge(String startCountry, String endCountry, int distance) {

        neighbors.get(startCountry).replace(endCountry, distance);
        neighbors.get(endCountry).replace(startCountry, distance);

    }



    public Map<String, String> dijkstra(String startCountry, String endCountry) {

        PriorityQueue<Node> costPQ = new PriorityQueue<>();
        Map<String, Integer> cost = new HashMap<>();
        Map<String, String> dijkstraPath = new HashMap<>();
        Set<String> finalList = new HashSet<>();

        for (String country : neighbors.keySet()) {
            if (country.equals(startCountry)) {
                cost.put(startCountry, 0);
                if (neighbors.get(startCountry) != null) {
                    for (String neighbor : neighbors.get(startCountry).keySet()) {
                        cost.put(neighbor, getDistance());
                        costPQ.add(new Node(neighbor, getDistance()));
                        dijkstraPath.put(neighbor, startCountry);
                    }
                }
            }
        }

        while (!finalList.contains(endCountry) && !costPQ.isEmpty()) {
            Node currNode = costPQ.poll();
            if (currNode != null && !finalList.contains(currNode.country) && neighbors.get(currNode.country) != null) {
                finalList.add(currNode.country);
                for (String neighbor : neighbors.get(currNode.country).keySet()) {
                    int neighborDist = getDistance();
                    if (neighborDist != -1 && !cost.containsKey(neighbor)) {
                        cost.put(neighbor, cost.get(currNode.country) + neighborDist);
                        dijkstraPath.put(neighbor, currNode.country);
                        Node insertPQ = new Node(neighbor, cost.get(neighbor));
                        costPQ.add(insertPQ);
                    } else if (neighborDist != -1 && cost.get(currNode.country) + neighborDist < cost.get(neighbor)) {
                        cost.replace(neighbor, cost.get(currNode.country) + neighborDist);
                        dijkstraPath.put(neighbor, currNode.country);
                        Node insertPQ = new Node(neighbor, cost.get(neighbor));
                        costPQ.add(insertPQ);
                    }
                }
            }
        }

        if (finalList.contains(endCountry) && (cost.get(endCountry) != null)) {
            return dijkstraPath;
        } else {
            Map<String, String> emptyPath = new HashMap<>();
            return emptyPath;
        }
    }

    private int getDistance() {
        return 0;
    }

    public static class Node implements Comparable<Node> {
        String country;
        int distance;

        Node(String c, int d) {
            country = c;
            distance = d;
        }

        @Override
        public int compareTo(Node n) {
            return this.distance - n.distance;
        }
    }
}

# CS 245 (Fall 2023) - Assignment 3 - IRoadTrip

Can you plan the path for a road trip from one country to another?

Change the java source code, but do not change the data files. See Canvas for assignment details.

This project was extremely difficult and it only started to make sense near the end. The main premise of this code is to find the shortest path between two countries. I originally had thought the distances in the borders file were the distances we had to use. However, that was not the case. Then it was easy to tell what the project was expecting. The primary class, IRoadTrip, initializes a Graph object and two maps for country codes (cIDMap) and a mapping to fix inconsistent country names (brokenCountriesMap). The data files, namely borders.txt, state_name.tsv, and capdist.csv, are processed using methods like getData, brokenCountries, Graph, cID, and dists.

The Graph class serves as a representation of the network of countries and their connections. It uses a HashMap to store the adjacency list of neighbors for each country. The class includes methods for adding countries, adding edges, checking if countries are neighbors, and implementing Dijkstra's algorithm to find the shortest path between two countries. Data processing methods, such as Graph, cID, and dists, read and preprocess information from the input files. The dijkstra method in the Graph class specifically implements Dijkstra's algorithm, utilizing a priority queue to prioritize countries based on their distances from the source country. User interaction methods, including acceptUserInput, getDistance, findPath, and printPath, enable users to input countiry names, find the shortest route, and print detailed information about the path. A nested class within Graph, named Node, represents a node in the priority queue with a country name and distance value, implementing the Comparable interface to compare nodes based on distance. The main method creates an IRoadTrip object and initiates user interaction through the acceptUserInput method. 




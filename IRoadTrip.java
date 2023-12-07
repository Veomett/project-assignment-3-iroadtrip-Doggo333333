import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import static java.lang.Integer.MAX_VALUE;

public class IRoadTrip {
    private Graph graph;
    private Map<String, String> cIDMap;
    private Map<String, String> brokenCountriesMap;

    public IRoadTrip (String [] args) {
        graph= new Graph();
        cIDMap = new HashMap<String, String>();
        brokenCountriesMap = brokenCountries();

        if (args.length != 3) {
            System.err.println(" enter files <statenameTSV> <bordersTXT> <capdistCSV> IN THIS ORDER");
            System.exit(1);
        }
        
        String statenameTSV = args[0];
        String bordersTXT = args[1];
        String capdistCSV = args[2];
        getData(bordersTXT, statenameTSV, capdistCSV);

    }
    private void getData(String bordersTXT, String statenameTSV, String capdistCSV) {
        Graph(bordersTXT);
        cID(statenameTSV);
        dists(capdistCSV);
    }
    private Map<String, String> brokenCountries() { //borrowed from prof code
        Map<String, String> brokenCountries = new HashMap<String, String>();
        brokenCountries.put("German Federal Republic", "Germany");
        brokenCountries.put("Macedonia (Former Yugoslav Republic of)", "Macedonia");
        brokenCountries.put("Bosnia-Herzegovina", "Bosnia and Herzegovina");
        brokenCountries.put("Bahamas", "Bahamas, The");
        brokenCountries.put("Zambia.", "Zambia");
        brokenCountries.put("US", "United States of America");
        brokenCountries.put("United States", "United States of America");
        brokenCountries.put("Greenland).", "Greenland");
        brokenCountries.put("Congo, Democratic Republic of (Zaire)", "Democratic Republic of the Congo");
        brokenCountries.put("Congo, Democratic Republic of the", "Democratic Republic of the Congo");
        brokenCountries.put("Congo, Republic of the", "Republic of the Congo");
        brokenCountries.put("Gambia, The", "The Gambia");
        brokenCountries.put("Gambia", "The Gambia");
        brokenCountries.put("Macedonia", "North Macedonia");
        brokenCountries.put("Macedonia (Former Yugoslav Republic of)", "North Macedonia");
        brokenCountries.put("Italy.", "Italy");
        brokenCountries.put("East Timor", "Timor-Leste");
        brokenCountries.put("UK", "United Kingdom");
        brokenCountries.put("Korea, North", "North Korea");
        brokenCountries.put("Korea, People's Republic of", "North Korea");
        brokenCountries.put("Korea, South", "South Korea");
        brokenCountries.put("Korea, Republic of", "South Korea");
        brokenCountries.put("UAE", "United Arab Emirates");
        brokenCountries.put("Turkey (Turkiye)", "Turkey");
        brokenCountries.put("Botswana.", "Botswana");
        brokenCountries.put("Myanmar (Burma)", "Burma");
        brokenCountries.put("Vietnam, Democratic Republic of", "Vietnam");
        brokenCountries.put("Cambodia (Kampuchea)", "Cambodia");
        brokenCountries.put("Sri Lanka (Ceylon)", "Sri Lanka");
        brokenCountries.put("Kyrgyz Republic", "Kyrgyzstan");
        brokenCountries.put("Yemen (Arab Republic of Yemen)", "Yemen");
        brokenCountries.put("Turkey (Ottoman Empire)", "Turkey");
        brokenCountries.put("Iran (Persia)", "Iran");
        brokenCountries.put("Zimbabwe (Rhodesia)", "Zimbabwe");
        brokenCountries.put("Tanzania/Tanganyika", "Tanzania");
        brokenCountries.put("Congo", "Republic of the Congo");
        brokenCountries.put("Burkina Faso (Upper Volta)", "Burkina Faso");
        brokenCountries.put("Belarus (Byelorussia)", "Belarus");
        brokenCountries.put("Russia (Soviet Union)", "Russia");
        brokenCountries.put("Italy/Sardinia", "Italy");
        return brokenCountries;
    }

    private void Graph(String filename) {
        String row;
        String[] nCountires;
        String cName;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((row = br.readLine()) != null) {
                String[] rCountries = row.split(" = ", 2);
                cName = rCountries[0];
                if (brokenCountriesMap.containsKey(cName)) {
                    cName = brokenCountriesMap.get(cName);
                }
                graph.addCountry(cName);
                if (rCountries.length > 1) {
                    nCountires = rCountries[1].split(" km; | km, | km|km |; | [(]| [)]");
                } else {
                    nCountires = new String[0];
                }
                for (String nCountire : nCountires) {
                    String neighborName = nCountire.replaceAll("\\d| \\d|,", "");
                    if (brokenCountriesMap.containsKey(neighborName)) {
                        neighborName = brokenCountriesMap.get(neighborName);
                    }
                    graph.addCountry(neighborName);
                    graph.addEdge(cName, neighborName, MAX_VALUE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void cID(String filename) {
        String[] countryID;
        String row;
        String cName;
        String countryCode;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((row = br.readLine()) != null) {
                countryID = row.trim().split("\\t");
                if (countryID[countryID.length - 1].startsWith("2020")) {
                    cName = countryID[2];
                    if (brokenCountriesMap.containsKey(cName)) {
                        cName = brokenCountriesMap.get(cName);
                    }
                    countryCode = countryID[1];
                    cIDMap.put(countryCode, cName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dists(String filename) {
        String[] distancesArray;
        String row;
        String startCountry;
        String endCountry;
        int distance;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine(); // Skip the first line
            while ((row = br.readLine()) != null) {
                distancesArray = row.trim().split(",");
                if (distancesArray.length < 5) {
                    System.err.println("Invalid line: " + row);
                    continue;
                }
                startCountry = cIDMap.get(distancesArray[1]);
                endCountry = cIDMap.get(distancesArray[3]);
                distance = Integer.parseInt(distancesArray[4]);
                if (startCountry != null && endCountry != null && graph.hasNeighbor(startCountry) && graph.areNeighbors(startCountry, endCountry)) {
                    graph.replaceEdge(startCountry, endCountry, distance);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public int getDistance (String startCountry, String endCountry) {
        if (graph.hasNeighbor(startCountry) && graph.areNeighbors(startCountry, endCountry))
        {
            if (graph.neighbors.get(startCountry).get(endCountry) < MAX_VALUE){
                return graph.neighbors.get(startCountry).get(endCountry);
            }
        }
        return -1;
    }


    public List<String> findPath (String startCountry, String endCountry) {

        List<String> path = new ArrayList<>();
        Map<String, String> dijkstraResults = graph.dijkstra(startCountry, endCountry);
        if (dijkstraResults == null){
            System.out.println("Returning empty path");
            return path;
        }
        String curC = dijkstraResults.get(endCountry);
        String nxtC = endCountry;

        while (curC != null){
            path.add(0, curC + " --> " + nxtC + " (" + getDistance(curC, nxtC) + " km.)");
            nxtC = dijkstraResults.get(nxtC);
            curC = dijkstraResults.get(nxtC);
        }

        return path;

    }


    public void acceptUserInput() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the name of the first country (type EXIT to quit): ");
            String startCountry = scanner.nextLine().trim();

            if (startCountry.equalsIgnoreCase("EXIT")) {
                break;
            }

            if (!graph.hasNeighbor(startCountry)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue;
            }

            System.out.print("Enter the name of the second country (type EXIT to quit): ");
            String endCountry = scanner.nextLine().trim();

            if (endCountry.equalsIgnoreCase("EXIT")) {
                break;
            }

            if (!graph.hasNeighbor(endCountry)) {
                System.out.println("Invalid country name. Please enter a valid country name.");
                continue;
            }


            System.out.println("The shortest route from " + startCountry + " to " + endCountry + ":");
            List<String> path = findPath(startCountry, endCountry);
            if (!path.isEmpty()) {
                printPath(path);
            }


        }
    }

    private void printPath(List<String> path) {
        for (String step : path)
            System.out.println(step);
    }


    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
        a3.acceptUserInput();
    }
}


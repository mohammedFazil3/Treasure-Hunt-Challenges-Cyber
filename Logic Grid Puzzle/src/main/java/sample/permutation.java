package main.java.sample;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class permutation {

    public static Map<String, List<String>> generateMap() {
        // List of employees
        List<String> employees = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie"));
        // List of operating systems
        List<String> operatingSystems = new ArrayList<>(Arrays.asList("Windows", "Linux", "MacOS"));
        // List of rooms
        List<String> rooms = new ArrayList<>(Arrays.asList("101", "102", "103"));

        List<List<String>> employeePermutations = generatePermutations(employees);
        List<List<String>> osPermutations = generatePermutations(operatingSystems);
        List<List<String>> roomPermutations = generatePermutations(rooms);

        // Map to store each permutation with its corresponding clues
        Map<String, List<String>> permutationClueMap = new HashMap<>();

        for (List<String> employeePerm : employeePermutations) {
            for (List<String> osPerm : osPermutations) {
                for (List<String> roomPerm : roomPermutations) {
                    StringBuilder permutationKey = new StringBuilder();
                    for (int i = 0; i < 3; i++) {
                        String info = employeePerm.get(i) + " uses " + osPerm.get(i) + " and sits in Room " + roomPerm.get(i);
                        permutationKey.append(info).append(" | ");
                    }
                    // Generate clues and store them in the map
                    List<String> clues = generateAllClues(employeePerm, osPerm, roomPerm);
                    permutationClueMap.put(permutationKey.toString(), clues);

                }
            }
        }


        return permutationClueMap;
    }

    // Method to generate all clues for each permutation and return them as a list of strings
    private static List<String> generateAllClues(List<String> employees, List<String> operatingSystems, List<String> rooms) {
        List<String> clues = new ArrayList<>();

        String aliceOS = operatingSystems.get(employees.indexOf("Alice"));
        String aliceRoom = rooms.get(employees.indexOf("Alice"));

        String bobOS = operatingSystems.get(employees.indexOf("Bob"));
        String bobRoom = rooms.get(employees.indexOf("Bob"));

        String charlieOS = operatingSystems.get(employees.indexOf("Charlie"));
        String charlieRoom = rooms.get(employees.indexOf("Charlie"));

        // Clue 1
        if (!(aliceOS.equals("Windows")) && aliceOS.equals("MacOS")) {
            clues.add("Alice does not use Windows, and she does not sit in Room " + ((bobOS.equals("Linux")) ? bobRoom : charlieRoom) + ".");
        } else if (!(bobOS.equals("Windows")) && bobOS.equals("MacOS")) {
            clues.add("Bob does not use Windows, and he does not sit in Room " + ((aliceOS.equals("Linux")) ? aliceRoom : charlieRoom) + ".");
        } else if (!(charlieOS.equals("Windows")) && charlieOS.equals("MacOS")) {
            clues.add("Charlie does not use Windows, and he does not sit in Room " + ((bobOS.equals("Linux")) ? bobRoom : aliceRoom) + ".");
        }

        // Clue 2
        if (bobOS.equals("Linux")) {
            clues.add("Bob uses Linux but does not sit in Room " + (charlieOS.equals("Windows") ? charlieRoom : aliceRoom) + ".");
        } else if (charlieOS.equals("Linux")) {
            clues.add("Charlie uses Linux but does not sit in Room " + (bobOS.equals("Windows") ? bobRoom : aliceRoom) + ".");
        } else if (aliceOS.equals("Linux")) {
            clues.add("Alice uses Linux but does not sit in Room " + (bobOS.equals("Windows") ? bobRoom : charlieRoom) + ".");
        }

        // Clue 3
        if (aliceOS.equals("MacOS")) {
            clues.add("The person who uses MacOS does not sit in Room " + (charlieOS.equals("Linux") ? charlieRoom : bobRoom) + ".");
        } else if (charlieOS.equals("MacOS")) {
            clues.add("The person who uses MacOS does not sit in Room " + (aliceOS.equals("Linux") ? aliceRoom : bobRoom) + ".");
        } else if (bobOS.equals("MacOS")) {
            clues.add("The person who uses MacOS does not sit in Room " + (aliceOS.equals("Linux") ? aliceRoom : charlieRoom) + ".");
        }

        // Clue 4
        if (aliceOS.equals("Windows")) {
            clues.add("The person who sits in " + aliceRoom + " uses Windows.");
        } else if (bobOS.equals("Windows")) {
            clues.add("The person who sits in " + bobRoom + " uses Windows.");
        } else if (charlieOS.equals("Windows")) {
            clues.add("The person who sits in " + charlieRoom + " uses Windows.");
        }

        // Clue 5
        if (!charlieOS.equals("MacOS") && charlieOS.equals("Windows")) {
            clues.add("Charlie does not use MacOS.");
        } else if (!bobOS.equals("MacOS") && bobOS.equals("Windows")) {
            clues.add("Bob does not use MacOS.");
        } else if (!aliceOS.equals("MacOS") && aliceOS.equals("Windows")) {
            clues.add("Alice does not use MacOS.");
        }

        return clues;
    }

    // Method to generate all permutations of a list
    public static <T> List<List<T>> generatePermutations(List<T> originalList) {
        if (originalList.isEmpty()) {
            List<List<T>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }

        T firstElement = originalList.remove(0);
        List<List<T>> returnValue = new ArrayList<>();
        List<List<T>> permutations = generatePermutations(originalList);
        for (List<T> smallerPermutated : permutations) {
            for (int index = 0; index <= smallerPermutated.size(); index++) {
                List<T> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        originalList.add(0, firstElement);
        return returnValue;
    }
}

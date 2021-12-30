import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day12 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("inputs\\day12.txt"));
        System.out.println("Part 1: " + day12(input, true));
        System.out.println("Part 2: " + day12(input, false));
    }

    public static long day12(String input, boolean singlev) {
        HashMap<String, List<String>> passages = new HashMap<>();
        for (String line : input.lines().toList()) {
            String[] p = line.split("-");
            passages.computeIfAbsent(p[0], k -> new ArrayList<>()).add(p[1]);
            passages.computeIfAbsent(p[1], k -> new ArrayList<>()).add(p[0]);
        }
        return day12rec(passages, new Stack<>(), singlev, "start");
    }

    private static long day12rec(HashMap<String, List<String>> passages,
                                 Stack<String> path, boolean singlev, String toAdd) {
        if (toAdd.equals("end")) return 1;
        boolean usedSmallDouble = singlev || path.contains(toAdd.toLowerCase());
        return passages.getOrDefault(toAdd, Collections.emptyList())
                .stream()
                .filter(next -> !next.equals("start") &&
                        Collections.frequency(path, next.toLowerCase()) < (usedSmallDouble ? 1 : 2))
                .mapToLong(next -> {
                    path.push(toAdd);
                    long v = day12rec(passages, path, usedSmallDouble, next);
                    path.pop();
                    return v;
                }).sum();
    }
}

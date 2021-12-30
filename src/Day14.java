import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day14 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("inputs\\day14.txt"));
        System.out.println("Part 1: " + day14(input, 10));
        System.out.println("Part 2: " + day14(input, 40));
    }

    public static long day14(String input, int step) {
        String poly = input.lines().findFirst().orElse("");
        Map<String, Character> combi = input.lines().skip(2).map(s -> s.split(" -> "))
                .collect(Collectors.toMap(s -> s[0], s -> s[1].charAt(0)));
        //count array
        long[] el = new long[26];
        poly.chars().boxed().toList().forEach(c -> el[c - 65]++);
        Map<String, Long> map = new HashMap<>();
        for (int i = 0; i < poly.length() - 1; i++) map.merge(poly.substring(i, i + 2), 1L, Long::sum);
        while (step-- > 0) {
            var temp = new HashMap<String, Long>();
            for (var e : map.entrySet()) {
                String pair = e.getKey();
                Long c = e.getValue();
                char ins = combi.get(pair);
                temp.merge("" + pair.charAt(0) + ins, c, Long::sum);
                temp.merge("" + ins + pair.charAt(1), c, Long::sum);
                el[ins - 65] += c;
            }
            map = temp;
        }
        List<Long> freq = Arrays.stream(el).boxed().filter(x -> x != 0).sorted().toList();
        return freq.get(freq.size() - 1) - freq.get(0);
    }
}

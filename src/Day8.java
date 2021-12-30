import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day8 {
    public static void main(String[] args) throws IOException {
        int[] res = day8(Files.readString(Path.of("inputs\\day8.txt")));
        System.out.println("Part 1: " + res[0]);
        System.out.println("Part 2: " + res[1]);
    }

    public static int[] day8(String in) {
        AtomicInteger c = new AtomicInteger();
        AtomicInteger sum = new AtomicInteger();
        in.lines().forEach(l -> {
            List<List<String>> str = Arrays.stream(l.split("\\|"))
                    .map(s -> Arrays.stream(s.strip().split(" "))
                            .map(ss -> {
                                char[] temp = ss.toCharArray();
                                Arrays.sort(temp);
                                return String.valueOf(temp);
                            }).collect(Collectors.toList())).toList();

            List<String> inputs = str.get(0);
            inputs.sort(Comparator.comparingInt(String::length));
            List<String> outputs = str.get(1);
            HashMap<String, Integer> hmap = new HashMap<>();
            hmap.put(inputs.get(0), 1);
            hmap.put(inputs.get(1), 7);
            hmap.put(inputs.get(2), 4);
            hmap.put(inputs.get(9), 8);
            int six_index = 0;
            for (int i = 6; i < 9; i++) {
                String curr = inputs.get(i);
                int d = (day8Match(inputs.get(1), curr)) ? day8Match(inputs.get(2), curr) ? 9 : 0 : 6;
                if (d == 6) six_index = i;
                hmap.put(curr, d);
            }
            for (int i = 3; i < 6; i++) {
                String curr = inputs.get(i);
                hmap.put(curr, (day8Match(inputs.get(0), curr) ? 3 : day8Match(curr, inputs.get(six_index)) ? 5 : 2));
            }
            for (int i = outputs.size(); i > 0; i--) {
                String curr = outputs.get(i - 1);
                if (curr.length() <= 4 || curr.length() == 7) c.getAndIncrement();
                sum.getAndAdd((int) Math.pow(10, outputs.size() - i) * hmap.get(curr));
            }
        });
        return new int[]{c.get(), sum.get()};
    }

    public static boolean day8Match(String s, String t) {
        for (char c : s.toCharArray()) if (!t.contains(c + "")) return false;
        return true;
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day20 {
    public static void main(String[] args) throws IOException {
        long[] res = day20(Files.readString(Path.of("inputs\\day20.txt")), 50);
        System.out.println("Part 1: " + res[0]);
        System.out.println("Part 2: " + res[1]);
    }

    public static long[] day20(String input, int steps) {
        List<String> lines = input.lines().map(l -> l.replaceAll("\\.", "0"))
                .map(l -> l.replaceAll("#", "1")).toList();
        String algo = lines.get(0);
        List<List<Integer>> image = lines.subList(2, lines.size()).stream().map(l -> l.codePoints().map(
                x -> x - 48).boxed().collect(Collectors.toList())).collect(Collectors.toList());
        long res = 0, res2 = 0;
        int def = 0;
        for (int s = 1; s <= steps; s++) {
            var newimg = new ArrayList<List<Integer>>();
            for (int y = -1; y <= image.size(); y++) {
                var r = new ArrayList<Integer>();
                for (int x = -1; x <= image.get(0).size(); x++) {
                    int idx = 0;
                    for (int ydif = -1; ydif <= 1; ydif++) {
                        for (int xdif = -1; xdif <= 1; xdif++) {
                            int c = (ydif + y < 0 || ydif + y >= image.size() ||
                                    xdif + x < 0 || xdif + x >= image.get(0).size())
                                    ? def : image.get(ydif + y).get(xdif + x);
                            idx |= (c == 1) ? (int) Math.pow(2, 8 - ((ydif + 1) * 3 + (xdif + 1))) : 0;
                        }
                    }
                    r.add(algo.charAt(idx) - 48);
                }
                newimg.add(r);
            }
            def = algo.charAt((def == 0) ? 0 : 511) - 48;
            image = newimg;
            res2 = image.stream().map(l -> l.stream().filter(c -> c == 1).count()).reduce(0L, Long::sum);
            if (s == 2) res = res2;
        }
        return new long[]{res, res2};
    }
}

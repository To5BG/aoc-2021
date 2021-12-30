import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day2 {
    public static void main(String[] args) throws IOException {
        long[] res1 = day2("inputs\\day2.txt", false);
        long[] res2 = day2("inputs\\day2.txt", true);
        System.out.println("Part 1: " + String.format("{%s,%s,%s}", res1[0], res1[1], res1[2]) +
                " Result: " + res1[0] * res1[1]);
        System.out.println("Part 2: " + String.format("{%s,%s,%s}", res2[0], res2[1], res2[2]) +
                " Result: " + res2[0] * res2[1]);
    }

    public static long[] day2(String filepath, boolean hasAim) throws IOException {
        List<String> inlist = Files.readAllLines(Path.of(filepath));
        long x = 0, y = 0, aim = 0;
        for (String l : inlist) {
            String[] line = l.split(" ");
            long temp = Long.parseLong(line[1]);
            switch (line[0]) {
                case "forward" -> {
                    x += temp;
                    y += (hasAim) ? temp * aim : 0;
                }
                case "down" -> {
                    if (hasAim) aim += temp;
                    else y += temp;
                }
                case "up" -> {
                    if (hasAim) aim -= temp;
                    else y -= temp;
                }
            }
        }
        return new long[]{x, y, aim};
    }
}

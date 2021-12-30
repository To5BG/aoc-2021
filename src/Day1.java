import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day1 {

    public static void main(String[] args) throws IOException {
        System.out.println("Part 1: " + day1(Files.readString(Path.of("inputs\\day1.txt")), false));
        System.out.println("Part 2: " + day1(Files.readString(Path.of("inputs\\day1.txt")), true));
    }

    public static int day1(String input, boolean tri) {
        int res = 0, prev = 0, prev2 = 0, prev3 = 0;
        for (Integer n : input.lines().map(Integer::parseInt).toList()) {
            res += (tri) ? (prev3 < n) ? 1 : 0 : (prev < n) ? 1 : 0;
            prev3 = prev2;
            prev2 = prev;
            prev = n;
        }
        return res - ((tri) ? 3 : 1);
    }
}

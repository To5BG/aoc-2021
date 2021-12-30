import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day7 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("inputs\\day7.txt"));
        System.out.println("Part 1: " + day7(input, false));
        System.out.println("Part 2: " + day7(input, true));
    }

    public static int day7(String in, boolean fuelg) {
        List<Integer> crabs = Arrays.stream(in.split(",")).map(Integer::parseInt).toList();
        int minD = Integer.MAX_VALUE;
        for (int i = Collections.min(crabs); i < Collections.max(crabs); i++) {
            int temp = 0;
            for (Integer num : crabs) {
                int d = Math.abs(i - num);
                temp += (fuelg) ? (Math.pow(d, 2) + d) / 2 : d;
            }
            if (minD > temp) minD = temp;
        }
        return minD;
    }
}

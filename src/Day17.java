import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.sqrt;

public class Day17 {
    public static void main(String[] args) throws IOException {
        int[] res = day17(Files.readString(Path.of("inputs\\day17.txt")));
        System.out.println("Part 1: " + res[0]);
        System.out.println("Part 2: " + res[1]);
    }

    public static int[] day17(String input) {
        List<Integer> nums = Arrays.stream(input.split("[^\\d-]+")).skip(1).map(Integer::parseInt).toList();
        int x1 = Math.min(nums.get(0), nums.get(1)), x2 = Math.max(nums.get(0), nums.get(1));
        int y1 = Math.min(nums.get(2), nums.get(3)), y2 = Math.max(nums.get(2), nums.get(3));
        int count = 0, maxy = Integer.MIN_VALUE;
        for (int i = (int) Math.ceil((-1 + sqrt(1 + 8 * Math.abs(x1))) / 2); i <= x2; i++) {
            for (int j = y1; j <= Math.pow(y2 - y1, 2); j++) {
                int xspeed = i, yspeed = j, xpos = 0, ypos = 0;
                while (true) {
                    xpos += Math.max(0, xspeed--);
                    ypos += yspeed--;
                    if (xpos > x2 || ypos < y1) break;
                    if (x1 <= xpos && xpos <= x2 && y1 <= ypos && ypos <= y2) {
                        if (j > maxy) maxy = j;
                        count++;
                        break;
                    }
                }
            }
        }
        return new int[]{maxy * (maxy + 1) / 2, count};
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Day9 {
    public static void main(String[] args) throws IOException {
        int[] res = day9(Files.readString(Path.of("inputs\\day9.txt")));
        System.out.println("Part 1: " + res[0]);
        System.out.println("Part 2: " + res[1]);
    }

    public static int[] day9(String input) {
        String header = "9";
        header = header.repeat(input.substring(0, input.indexOf("\n")).length() + 2);
        String in = String.format("%s\n", header);
        Scanner scr = new Scanner(input);
        while (scr.hasNextLine()) in = in.concat(String.format("9%s9\n", scr.nextLine()));
        int[][] map = String.format("%s%s", in, header).lines()
                .map(line -> line.chars().map(Character::getNumericValue).toArray())
                .toArray(int[][]::new);
        int risk = 0;
        ArrayList<Point> lows = new ArrayList<>();
        for (int x = 1; x < map.length - 1; x++) {
            for (int y = 1; y < map[0].length - 1; y++) {
                int curr = map[x][y];
                if (curr < map[x - 1][y] && curr < map[x + 1][y] && curr < map[x][y - 1] && curr < map[x][y + 1]) {
                    risk += map[x][y] + 1;
                    lows.add(new Point(x, y, 0));
                }
            }
        }
        boolean[][] seen = new boolean[map.length][map[0].length];
        return new int[]{risk, lows.stream().mapToInt(point -> day9Basin(map, point, seen)).
                sorted().skip(lows.size() - 3).reduce(1, (a, b) -> a * b)};
    }

    static int day9Basin(int[][] map, Point low, boolean[][] seen) {
        Queue<Point> pending = new LinkedList<>();
        pending.add(low);
        int res = 0;
        while (!pending.isEmpty()) {
            Point point = pending.remove();
            int x = point.x(), y = point.y();
            if (seen[x][y]) continue;
            seen[x][y] = true;
            res++;
            if (map[x - 1][y] != 9) pending.add(new Point(x - 1, y, 0));
            if (map[x + 1][y] != 9) pending.add(new Point(x + 1, y, 0));
            if (map[x][y - 1] != 9) pending.add(new Point(x, y - 1, 0));
            if (map[x][y + 1] != 9) pending.add(new Point(x, y + 1, 0));
        }
        return res;
    }

    public record Point(int x, int y, int z) {
    }
}

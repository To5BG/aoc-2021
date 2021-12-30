import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Day15 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("inputs\\day15.txt"));
        System.out.println("Part 1: " + day15(input, false));
        System.out.println("Part 2: " + day15(input, true));
    }

    public static int day15(String input, boolean incr) {
        int[][] map = input.lines().map(line -> line.codePoints().map(Character::getNumericValue).toArray())
                .toArray(int[][]::new);
        int mapl = map.length, mapw = map[0].length;
        int endx = (incr) ? mapl * 5 - 1 : mapl - 1, endy = (incr) ? mapw * 5 - 1 : mapw - 1;

        var open = new PriorityQueue<>(Comparator.comparingInt(CoordRisk::r));
        var score = new HashMap<Point, Integer>();

        Point start = new Point(0, 0, 0);
        score.put(start, 0);
        open.add(new CoordRisk(start, 0));
        while (!open.isEmpty()) {
            Point cur = open.poll().p();
            int curx = cur.x(), cury = cur.y();
            if (curx == endx && cury == endy) return score.get(cur);
            var neighbors = new ArrayList<Point>(4);
            if (curx > 0) neighbors.add(new Point(curx - 1, cury, 0));
            if (curx < endx) neighbors.add(new Point(curx + 1, cury, 0));
            if (cury > 0) neighbors.add(new Point(curx, cury - 1, 0));
            if (cury < endy) neighbors.add(new Point(curx, cury + 1, 0));

            for (Point near : neighbors) {
                int nx = near.x(), ny = near.y();
                int trisk = (map[ny % mapl][nx % mapw] - 1 + ny / mapl + nx / mapw) % 9 + 1 + score.get(cur);
                if (trisk < score.getOrDefault(near, Integer.MAX_VALUE)) {
                    score.put(near, trisk);
                    int md = Math.abs(nx - endx) + Math.abs(ny - endy);
                    open.add(new CoordRisk(near, trisk + md));
                }
            }
        }
        return -1;
    }

    private record CoordRisk(Point p, int r) {
    }

    public record Point(int x, int y, int z) {
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day13 {
    public static void main(String[] args) throws IOException {
        int[] res = day13(Files.readString(Path.of("inputs\\day13.txt")));
        System.out.println("Part 1: " + res[0]);
        System.out.println("Part 2: " + res[1]);
    }

    public static int[] day13(String input) {
        Set<Point> points = new HashSet<>();
        List<String> folds = new ArrayList<>();
        Scanner scr = new Scanner(input);
        boolean onFolds = false;
        while (scr.hasNextLine()) {
            String line = scr.nextLine();
            if (line.equals("")) onFolds = true;
            else if (onFolds) folds.add(line);
            else {
                List<Integer> linearr = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
                points.add(new Point(linearr.get(0), linearr.get(1), 0));
            }
        }
        int res = 0, res2 = 0;
        for (String f : folds) {
            String[] fold = f.split(" ")[2].split("=");
            char dir = fold[0].charAt(0);
            int num = Integer.parseInt(fold[1]);
            points = points.stream().map(p -> {
                if (dir == 'x' && p.x() > num) return new Point(2 * num - p.x(), p.y(), 0);
                else if (dir == 'y' && p.y() > num) return new Point(p.x(), 2 * num - p.y(), 0);
                else return p;
            }).collect(Collectors.toSet());
            res2 = points.size();
            //assign after first fold for part 1
            if (res == 0) res = res2;
        }
        //print final grid
        int maxX = points.stream().map(Point::x).max(Comparator.naturalOrder()).orElseThrow();
        int maxY = points.stream().map(Point::y).max(Comparator.naturalOrder()).orElseThrow();
        int[][] graph = new int[maxX + 1][maxY + 1];
        for (Point p : points) graph[p.x()][p.y()]++;
        for (int j = 0; j < graph[0].length; j++) {
            for (int[] r : graph) {
                if (r[j] == 1) System.out.print("#");
                else System.out.print(".");
            }
            System.out.println();
        }
        return new int[]{res, res2};
    }

    public record Point(int x, int y, int z) {
    }
}

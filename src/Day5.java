import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day5 {

    public record Point(int x, int y, int z) {
        public static Point read(String s) {
            var split = s.split(",");
            return new Point(Integer.parseInt(split[0].strip()), Integer.parseInt(split[1].strip()), 0);
        }
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("inputs\\day5.txt"));
        System.out.println("Part 1: " + day5(input, false));
        System.out.println("Part 2: " + day5(input, true));
    }

    public static long day5(String input, boolean includeDiagonals) {
        List<Point[]> pointpairs = input.lines().map(l -> {
            var split = l.split(" -> ");
            return new Point[]{Point.read(split[0]), Point.read(split[1])};
        }).toList();
        return pointpairs.stream().flatMap(l -> {
                    Point p1 = l[0];
                    Point p2 = l[1];
                    int dx = Math.abs(p1.x() - p2.x());
                    int dy = Math.abs(p1.y() - p2.y());
                    boolean isvert = p1.x() == p2.x();
                    var delta = isvert ? dy : dx;
                    if (dx != dy) {
                        BiFunction<Integer, Integer, Integer> op_x = (isvert)
                                ? (a, b) -> a : ((p1.x() < p2.x()) ? (Integer::sum) : (a, b) -> a - b);
                        BiFunction<Integer, Integer, Integer> op_y = (isvert)
                                ? ((p1.y() < p2.y()) ? (Integer::sum) : (a, b) -> a - b) : (a, b) -> a;
                        return IntStream.rangeClosed(0, delta).mapToObj(i -> new Point(
                                op_y.apply(p1.y(), i), op_x.apply(p1.x(), i), 0));
                    } else if (includeDiagonals) {
                        BiFunction<Integer, Integer, Integer> op_x = (p1.x() < p2.x()) ? (Integer::sum) : (a, b) -> a - b;
                        BiFunction<Integer, Integer, Integer> op_y = (p1.y() < p2.y()) ? (Integer::sum) : (a, b) -> a - b;
                        return IntStream.rangeClosed(0, delta).mapToObj(i -> new Point(
                                op_y.apply(p1.y(), i), op_x.apply(p1.x(), i), 0));
                    }
                    return Stream.empty();
                }).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values().stream().filter(n -> n > 1).count();
    }
}

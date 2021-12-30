import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day18 {
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Path.of("inputs\\day18.txt"));
        System.out.println("Part 1: " + day18(input, false));
        System.out.println("Part 2: " + day18(input, true));
    }

    public static int day18(String input, boolean isLargest) {
        //flatten snailfish num
        List<ArrayList<Point>> nums = input.lines().map(l -> {
            var list = new ArrayList<Point>();
            int y = -1;
            for (char c : l.toCharArray()) {
                switch (c) {
                    case '[' -> y++;
                    case ']' -> y--;
                    case ',' -> y += 0;
                    default -> list.add(new Point(c - 48, y, 0));
                }
            }
            return list;
        }).toList();
        List<Point> sum = new ArrayList<>(nums.get(0));
        int mag = Integer.MIN_VALUE;
        for (int i = 0; i < nums.size(); i++) {
            for (int j = 0; j < nums.size(); j++) {
                if (i == j) continue;
                //if first traversal finished -> part 1 done
                if (i != 0 && !isLargest) return mag(sum);

                if (!isLargest) sum.addAll(nums.get(j));
                else sum.addAll(Stream.concat(nums.get(i).stream(), nums.get(j).stream()).toList());

                sum = sum.stream().map(e -> new Point(e.x(), e.y() + 1, e.z())).collect(Collectors.toList());
                boolean notReduced = true;
                while (notReduced) {
                    notReduced = false;
                    for (int k = 0; k < sum.size(); k++) { //explosion
                        if (sum.get(k).y() != 4) continue;
                        if (k != 0) { //left
                            var templ = sum.get(k - 1);
                            sum.set(k - 1, new Point(templ.x() + sum.get(k).x(), templ.y(), templ.z()));
                        }
                        if (k < sum.size() - 2) { //right
                            var tempr = sum.get(k + 2);
                            sum.set(k + 2, new Point(tempr.x() + sum.get(k + 1).x(), tempr.y(), tempr.z()));
                        }
                        sum.remove(k + 1);
                        sum.set(k, new Point(0, sum.get(k).y() - 1, 0));
                        k = -1;
                    } // splitting
                    for (int k = 0; k < sum.size(); k++) {
                        if (sum.get(k).x() <= 9) continue;
                        var cur = sum.get(k);
                        int newdepth = cur.y() + 1;
                        float newval = (float) cur.x();
                        sum.set(k, new Point((int) Math.floor(newval / 2), newdepth, 0));
                        sum.add(k + 1, new Point((int) Math.ceil(newval / 2), newdepth, 0));
                        notReduced = true;
                        break;
                    }
                }
                if (isLargest) {
                    mag = Math.max(mag, mag(sum));
                    sum = new ArrayList<>();
                }
            }
        }
        return mag;
    }

    public static int mag(List<Point> n) { //find num magnitude iteratively
        for (int i = 3; i >= 0; i--) {
            for (var j = 0; j < n.size() - 1; j++) {
                Point l = n.get(j), r = n.get(j + 1);
                if (l.y() != i) continue;
                n.remove(j + 1);
                n.set(j, new Point(3 * l.x() + 2 * r.x(), i - 1, 0));
                j = -1;
            }
        }
        return n.get(0).x();
    }

    public record Point(int x, int y, int z) {
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day19 {
    public static void main(String[] args) throws IOException {
        int[] res = day19("inputs\\day19.txt");
        System.out.println("Part 1: " + res[0]);
        System.out.println("Part 2: " + res[1]);
    }

    public static int[] day19(String filepath) throws IOException {
        //instantiate list of scanners
        var scanners = Arrays.stream(Files.readString(Path.of(filepath))
                .split("\\r\\n\\r\\n")).map(s -> {
            var temp = Arrays.asList(s.split("\\r\\n"));
            return new Day19Scanner(temp.subList(1, temp.size()).stream().map(t -> Arrays.stream(t.split(","))
                            .mapToInt(Integer::parseInt).toArray())
                    .map(a -> new Point(a[0], a[1], a[2])).collect(Collectors.toList()));
        }).collect(Collectors.toList());
        Day19Scanner base = scanners.remove(0);
        var beacons = new ArrayList<Point>();
        beacons.add(new Point(0, 0, 0));
        while (scanners.size() > 0) {
            outer:
            for (Day19Scanner scr : scanners) {
                for (Day19Scanner rotscr : scr.getAllRotations()) {
                    Point trans = base.findTranslation(rotscr);
                    //if a no rotation of rotscr matches the base scanner, continue
                    if (trans == null) continue;
                    beacons.add(trans);
                    for (Point c : rotscr.points) { //update points
                        c = new Point(c.x() + trans.x(), c.y() + trans.y(), c.z() + trans.z());
                        if (!base.points.contains(c)) base.points.add(c);
                    }
                    scanners.remove(scr);
                    break outer;
                }
            }
        }
        int max = 0;
        for (int i = 0; i < beacons.size(); i++) {
            for (int j = 0; j < beacons.size(); j++) {
                if (i == j) continue;
                Point p1 = beacons.get(i), p2 = beacons.get(j);
                max = Math.max(max, Math.abs(p1.x() - p2.x()) + Math.abs(p1.y() - p2.y()) + Math.abs(p1.z() - p2.z()));
            }
        }
        return new int[]{base.points.size(), max};
    }


    record Day19Scanner(List<Point> points) {
        public Point findTranslation(Day19Scanner peer) {
            Map<Point, Integer> map = new HashMap<>();
            for (Point p : points) {
                for (Point p2 : peer.points) {
                    Point diff = new Point(p.x() - p2.x(), p.y() - p2.y(), p.z() - p2.z());
                    map.merge(diff, 1, Integer::sum);
                }
            }
            List<Map.Entry<Point, Integer>> r = map.entrySet().stream().filter(s -> s.getValue() >= 12).toList();
            if (r.size() == 0) return null;
            return r.get(0).getKey();
        }

        public List<Day19Scanner> getAllRotations() {
            List<List<Point>> res = new ArrayList<>();
            for (int i = 0; i < 24; i++) res.add(new ArrayList<>(points.size()));
            for (Point p : points) {
                List<Point> r = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    for (int s = 0; s < 3; s++) {
                        p = new Point(p.x(), p.z(), -p.y());
                        r.add(p);
                        for (int j = 0; j < 3; j++) {
                            p = new Point(-p.y(), p.x(), p.z());
                            r.add(p);
                        }
                    }
                    p = new Point(-p.z(), -p.y(), -p.x());
                }
                for (int i = 0; i < 24; i++) res.get(i).add(r.get(i));
            }
            return res.stream().map(Day19Scanner::new).toList();
        }
    }

    public record Point(int x, int y, int z) {
    }
}

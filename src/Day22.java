import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day22 {
    public static void main(String[] args) throws IOException {
        System.out.println("Part 1: " + day22("inputs\\day22.txt", 20));
        System.out.println("Part 2: " + day22("inputs\\day22.txt", 420));
    }

    public static long day22(String filepath, int instr) throws IOException {
        if (instr <= 0) return 0;
        List<String> lines = Files.readAllLines(Path.of(filepath));
        lines = lines.subList(0, Math.min(instr, lines.size()));
        List<List<Integer>> cuboids = lines.stream().map(l -> {
            String[] temp = l.replaceAll("[^-onf\\d]+", ",").split(",");
            List<Integer> res = new ArrayList<>();
            res.add(temp[0].equals("on") ? 1 : 0);
            for (int i = 1; i < temp.length; i++) res.add(Integer.parseInt(temp[i]));
            return res;
        }).toList();
        List<List<Integer>> cur = new ArrayList<>();
        cur.add(cuboids.get(0));
        for (int i = 1; i < cuboids.size(); i++) {
            int size = cur.size();
            cur.add(cuboids.get(i));
            for (int j = 0; j < size; j++) {
                if (((cuboids.get(i).get(1) <= cur.get(j).get(1) &&
                        cur.get(j).get(1) <= cuboids.get(i).get(2)) ||
                        (cur.get(j).get(1) <= cuboids.get(i).get(1) &&
                                cuboids.get(i).get(1) <= cur.get(j).get(2))) &&
                        ((cuboids.get(i).get(3) <= cur.get(j).get(3) &&
                                cur.get(j).get(3) <= cuboids.get(i).get(4)) ||
                                (cur.get(j).get(3) <= cuboids.get(i).get(3) &&
                                        cuboids.get(i).get(3) <= cur.get(j).get(4))) &&
                        ((cuboids.get(i).get(5) <= cur.get(j).get(5) &&
                                cur.get(j).get(5) <= cuboids.get(i).get(6)) ||
                                (cur.get(j).get(5) <= cuboids.get(i).get(5) &&
                                        cuboids.get(i).get(5) <= cur.get(j).get(6)))) {
                    // 1. on x-axis: left
                    if (cur.get(j).get(2) >= cuboids.get(i).get(1) &&
                            cuboids.get(i).get(1) >= cur.get(j).get(1)) {
                        List<Integer> temp = new ArrayList<>(List.of(cur.get(j).get(0), cur.get(j).get(1),
                                cuboids.get(i).get(1) - 1, cur.get(j).get(3),
                                cur.get(j).get(4), cur.get(j).get(5), cur.get(j).get(6)));
                        if (temp.get(1) <= temp.get(2) && temp.get(3) <= temp.get(4) &&
                                temp.get(5) <= temp.get(6)) {
                            cur.add(temp);
                            cur.get(j).set(1, cuboids.get(i).get(1));
                        }
                    }
                    // 2. on x-axis: right
                    if (cur.get(j).get(1) <= cuboids.get(i).get(2) &&
                            cuboids.get(i).get(2) <= cur.get(j).get(2)) {
                        List<Integer> temp = new ArrayList<>(List.of(cur.get(j).get(0), cuboids.get(i).get(2) + 1,
                                cur.get(j).get(2), cur.get(j).get(3), cur.get(j).get(4),
                                cur.get(j).get(5), cur.get(j).get(6)));
                        if (temp.get(1) <= temp.get(2) && temp.get(3) <= temp.get(4) &&
                                temp.get(5) <= temp.get(6)) {
                            cur.add(temp);
                            cur.get(j).set(2, cuboids.get(i).get(2));
                        }
                    }
                    // 3. on y-axis: back
                    if (cur.get(j).get(3) <= cuboids.get(i).get(4) &&
                            cuboids.get(i).get(4) <= cur.get(j).get(4)) {
                        List<Integer> temp = new ArrayList<>(List.of(cur.get(j).get(0), cur.get(j).get(1),
                                cur.get(j).get(2), cuboids.get(i).get(4) + 1,
                                cur.get(j).get(4), cur.get(j).get(5), cur.get(j).get(6)));
                        if (temp.get(1) <= temp.get(2) && temp.get(3) <= temp.get(4) &&
                                temp.get(5) <= temp.get(6)) {
                            cur.add(temp);
                            cur.get(j).set(4, cuboids.get(i).get(4));
                        }
                    }
                    // 4. on y-axis: front
                    if (cur.get(j).get(4) >= cuboids.get(i).get(3) &&
                            cuboids.get(i).get(3) >= cur.get(j).get(3)) {
                        List<Integer> temp = new ArrayList<>(List.of(cur.get(j).get(0), cur.get(j).get(1),
                                cur.get(j).get(2), cur.get(j).get(3), cuboids.get(i).get(3) - 1,
                                cur.get(j).get(5), cur.get(j).get(6)));
                        if (temp.get(1) <= temp.get(2) && temp.get(3) <= temp.get(4) &&
                                temp.get(5) <= temp.get(6)) {
                            cur.add(temp);
                            cur.get(j).set(3, cuboids.get(i).get(3));
                        }
                    }
                    // 5. on z axis: top
                    if (cur.get(j).get(5) <= cuboids.get(i).get(6) &&
                            cuboids.get(i).get(6) <= cur.get(j).get(6)) {
                        List<Integer> temp = new ArrayList<>(List.of(cur.get(j).get(0), cur.get(j).get(1),
                                cur.get(j).get(2), cur.get(j).get(3), cur.get(j).get(4),
                                cuboids.get(i).get(6) + 1, cur.get(j).get(6)));
                        if (temp.get(1) <= temp.get(2) && temp.get(3) <= temp.get(4) &&
                                temp.get(5) <= temp.get(6)) {
                            cur.add(temp);
                            cur.get(j).set(6, cuboids.get(i).get(6));
                        }
                    }
                    // 6. on z axis: bottom
                    if (cur.get(j).get(6) >= cuboids.get(i).get(5) &&
                            cuboids.get(i).get(5) >= cur.get(j).get(5)) {
                        List<Integer> temp = new ArrayList<>(List.of(cur.get(j).get(0),
                                cur.get(j).get(1), cur.get(j).get(2), cur.get(j).get(3),
                                cur.get(j).get(4), cur.get(j).get(5), cuboids.get(i).get(5) - 1));
                        if (temp.get(1) <= temp.get(2) && temp.get(3) <= temp.get(4) &&
                                temp.get(5) <= temp.get(6)) {
                            cur.add(temp);
                            cur.get(j).set(5, cuboids.get(i).get(5));
                        }
                    }
                } else {
                    cur.add(cur.get(j));
                }
            }
            cur = cur.subList(size, cur.size());
        }
        long res = 0;
        for (List<Integer> cuboid : cur) {
            if (cuboid.get(0) == 1) {
                res += (long) (cuboid.get(2) - cuboid.get(1) + 1) *
                        (cuboid.get(4) - cuboid.get(3) + 1) * (cuboid.get(6) - cuboid.get(5) + 1);
            }
        }
        return res;
    }
}

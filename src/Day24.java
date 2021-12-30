import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Stream;

public class Day24 {
    public static void main(String[] args) throws IOException {
        System.out.println(day24("inputs\\day24.txt", 1));
        System.out.println(day24("inputs\\day24.txt", 2));
    }

    public static String day24(String filepath, int part) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filepath));
        List<List<Integer>> terms = new ArrayList<>();
        for (int i = 0; i < lines.size(); i += 18) {
            int finalI = i;
            List<Integer> temp = new ArrayList<>();
            temp.add(lines.get(finalI + 4).split(" ")[2].equals("26") ? 1 : 0);
            temp.addAll(Stream.of(5, 15).map(j -> Integer.parseInt(lines.get(finalI + j)
                    .split(" ")[2])).toList());
            terms.add(temp);
        }
        Deque<Integer[]> prevs = new ArrayDeque<>(); //used as Stack ADT
        Integer[] digits = new Integer[14];
        for (List<Integer> instr : terms) {
            if (instr.get(0) == 0) prevs.addFirst(new Integer[]{terms.indexOf(instr), instr.get(2)});
            else {
                Integer[] prev = prevs.pop();
                int comp = prev[1] + instr.get(1);
                digits[prev[0]] = (part == 1) ? Math.min(9, 9 - comp) : Math.max(1, 1 - comp);
                digits[terms.indexOf(instr)] = digits[prev[0]] + comp;
            }
        }
        String res = "";
        for (Integer digit : digits) res = String.format("%s%s", res, digit);
        return res;
    }
}

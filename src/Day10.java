import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Day10 {
    public static void main(String[] args) throws IOException {
        long[] res = day10(Files.readString(Path.of("inputs\\day10.txt")));
        System.out.println("Part 1: " + res[0]);
        System.out.println("Part 2: " + res[1]);
    }

    public static long[] day10(String input) {
        AtomicLong corruptsum = new AtomicLong();
        List<Long> incompletesums = new ArrayList<>(List.of(0L));
        input.lines().forEach(l -> {
            String[] res = day10LineCheck(l);
            l = res[1];
            if (res[0].equals("-1")) {
                l = l.replaceAll("[<\\[({]", "");
                switch (l.substring(0, 1)) {
                    case "]" -> corruptsum.addAndGet(57);
                    case ")" -> corruptsum.addAndGet(3);
                    case "}" -> corruptsum.addAndGet(1197);
                    case ">" -> corruptsum.addAndGet(25137);
                }
            } else if (res[0].equals("0")) {
                int le = l.length();
                long tempsum = 0;
                for (int i = le - 1; i >= 0; i--) {
                    tempsum *= 5;
                    switch (l.substring(i, i + 1)) {
                        case "(" -> tempsum += 1;
                        case "[" -> tempsum += 2;
                        case "{" -> tempsum += 3;
                        case "<" -> tempsum += 4;
                    }
                }
                incompletesums.add(tempsum);
            }
        });
        incompletesums.sort(Comparator.naturalOrder());
        return new long[]{corruptsum.get(), incompletesums.get(incompletesums.size() / 2)};
    }

    public static String[] day10LineCheck(String line) {
        int check = 0;
        while (line.length() != 0) {
            String curr = line;
            line = line.replaceFirst("(\\(\\))|(\\[])|(\\{})|(<>)", "");
            if (line.equals(curr)) break;
        }
        if (line.length() == 0) check = 1;
        else if (line.contains("]") || line.contains(")") || line.contains("}") || line.contains(">")) check = -1;
        return new String[]{String.valueOf(check), line};
    }
}

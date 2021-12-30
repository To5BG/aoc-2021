import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day21 {
    public static void main(String[] args) throws IOException {
        long[] res = day21(Files.readString(Path.of("inputs\\day21.txt")));
        System.out.println("Part 1: " + res[0]);
        System.out.println("Part 2: " + res[1]);
    }

    public static long[] day21(String input) {
        Integer[] pos = input.lines().map(s -> Integer.parseInt(s.substring(s.indexOf(":") + 2)))
                .toArray(Integer[]::new);
        int[] scr = new int[2];
        long res;
        int i = 0;
        while (true) {
            pos[(i % 6) / 3] = (pos[(i % 6) / 3] + i % 100) % 10 + 1;
            if ((i + 1) % 3 == 0) scr[(i % 6) / 3] += pos[(i % 6) / 3];
            if (scr[0] >= 1000 || scr[1] >= 1000) {
                res = (long) Math.min(scr[0], scr[1]) * (i + 1);
                break;
            }
            i++;
        }
        long[] startwo = day21Star2(true, 0, 0, scr[0], scr[1],
                1, new int[]{0, 0, 0, 1, 3, 6, 7, 6, 3, 1}, 0, 0);
        return new long[]{res, startwo[0], startwo[1]};
    }

    public static long[] day21Star2(boolean p1Turn, int scr1, int scr2, int p1, int p2, long paths, int[] npaths,
                                    long p1w, long p2w) {
        if (scr1 >= 21) return new long[]{p1w + paths, p2w};
        else if (scr2 >= 21) return new long[]{p1w, p2w + paths};
        else {
            for (int i = 3; i <= 9; i++) {
                long[] recres;
                if (p1Turn) recres = day21Star2(false, scr1 + (p1 + i - 1) % 10 + 1, scr2,
                        (p1 + i - 1) % 10 + 1, p2, paths * npaths[i], npaths, p1w, p2w);
                else recres = day21Star2(true, scr1, scr2 + (p2 + i - 1) % 10 + 1,
                        p1, (p2 + i - 1) % 10 + 1, paths * npaths[i], npaths, p1w, p2w);
                p1w = recres[0];
                p2w = recres[1];
            }
        }
        return new long[]{p1w, p2w};
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public class Day3 {
    public static void main(String[] args) throws IOException {
        String in = Files.readString(Path.of("inputs\\day3.txt"));
        int[] res1 = day3Star1(in, 12);
        System.out.println("Part 1: " + res1[0] + " " + res1[1] + " Result: " + res1[0] * res1[1]);
        int res21 = day3Star2(in, 0, 12, 0);
        int res22 = day3Star2(in, 0, 12, 1);
        System.out.println("Part 2: " + res21 + " " + res22 + " Result: " + res21 * res22);
    }

    public static int[] day3Star1(String input, int n) {
        int[] pos = new int[n];
        long c = input.lines().peek(l -> {
            for (int i = 0; i < n; i++)
                if (l.charAt(i) == '1') pos[i]++;
        }).count();
        int gamma = 0;
        for (int i = 0; i < n; i++)
            if (pos[i] >= c / 2 + c % 2) gamma |= (1 << n - i - 1);
        return new int[]{gamma, ~gamma & (1 << n) - 1};
    }

    public static int day3Star2(String input, int pos, int n, int keyval) {
        if (pos >= n || input.lines().count() == 1) return Integer.parseInt(input.substring(0, n), 2);
        int key = day3Star1(input, n)[keyval];
        int d = ((key & (1 << (n - pos - 1))) == 0) ? 48 : 49;
        AtomicReference<String> res = new AtomicReference<>("");
        int finalPos = pos;
        input.lines().forEach(l -> {
            if (l.charAt(finalPos) == d) res.set(String.format("%s%s\n", res, l));
        });
        return day3Star2(res.get(), ++pos, n, keyval);
    }
}

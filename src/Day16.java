import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class Day16 {
    public static void main(String[] args) throws IOException {
        long[] res = day16(Files.readString(Path.of("inputs\\day16.txt")), 0, 0);
        System.out.println("Part 1: " + res[0]);
        System.out.println("Part 2: " + res[1]);
    }

    public static long[] day16(String input, long versum, int idx) {
        if (idx == 0) { //start, transform input
            StringBuilder in = new StringBuilder(new BigInteger(input, 16).toString(2));
            if (input.startsWith("0")) in.insert(0, "0000");
            if (in.length() % 4 != 0) in.insert(0, "0".repeat(4 - in.length() % 4));
            input = in.toString();
        }
        versum += Long.parseLong(input.substring(idx, idx + 3), 2);
        int typeid = Integer.parseInt(input.substring(idx + 3, idx + 6), 2);
        idx += 6;
        if (typeid == 4) { //if literal, return version and value
            StringBuilder d = new StringBuilder();
            while (true) {
                String ch = input.substring(idx, idx + 5);
                d.append(ch.substring(1));
                idx += 5;
                if (ch.startsWith("0")) break;
            }
            return new long[]{versum, Long.parseLong(d.toString(), 2), (long) idx};
        } else { //else determine type, recursive call until literal is found
            int lentype = input.charAt(idx) - 48;
            int l = (lentype == 1) ? 11 : 15;
            int len = Integer.parseInt(input.substring(idx + 1, idx + 1 + l), 2);
            idx += l + 1;
            List<Long> sub = new ArrayList<>();
            if (lentype == 0) { //total length case
                while (len > 0) {
                    int start = idx;
                    long[] res = day16(input, versum, idx);
                    versum = res[0];
                    sub.add(res[1]);
                    idx = (int) res[2];
                    len -= (idx - start);
                }
            } else for (int i = 0; i < len; i++) { //total iteration case
                long[] res = day16(input, versum, idx);
                versum = res[0];
                sub.add(res[1]);
                idx = (int) res[2];
            }
            // perform proper function
            BiFunction<Long, Long, Long> op;
            switch (typeid) {
                case 1 -> op = (a, b) -> a * b;
                case 2 -> op = Long::min;
                case 3 -> op = Long::max;
                case 5 -> op = (a, b) -> a > b ? 1L : 0L;
                case 6 -> op = (a, b) -> a < b ? 1L : 0L;
                case 7 -> op = (a, b) -> a.equals(b) ? 1L : 0L;
                default -> op = Long::sum;
            }
            final var opF = op;
            return new long[]{versum, sub.stream().reduce(opF::apply).orElseThrow(), (long) idx};
        }
    }
}

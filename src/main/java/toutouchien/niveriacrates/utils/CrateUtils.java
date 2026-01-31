package toutouchien.niveriacrates.utils;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class CrateUtils {
    private CrateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean allowed(String s) {
        for (char c : s.toCharArray()) {
            if (!allowed(c))
                return false;
        }

        return true;
    }

    public static boolean allowed(char c) {
        return c >= '0' && c <= '9'
                || c >= 'A' && c <= 'Z'
                || c >= 'a' && c <= 'z'
                || c == '_' || c == '-';
    }
}

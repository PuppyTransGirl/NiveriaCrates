package toutouchien.niveriacrates.utils;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class CrateUtils {
    private CrateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean idAllowed(String s) {
        if (s.length() > 20)
            return false;

        for (char c : s.toCharArray()) {
            if (!idAllowed(c))
                return false;
        }

        return true;
    }

    public static boolean idAllowed(char c) {
        return c >= '0' && c <= '9'
                || c >= 'A' && c <= 'Z'
                || c >= 'a' && c <= 'z'
                || c == '_' || c == '-';
    }
}

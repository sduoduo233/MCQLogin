package com.duoduo.mcqlogin.utils;

import java.util.ArrayList;
import java.util.List;

public class Base64 {
    private static final char[] ALPHA = "LVoJPiCN2R8G90yg+hmFHuacZ1OWMnrsSTXkYpUq/3dlbfKwv6xztjI7DeBE45QA".toCharArray();

    private static char getByte(String s, int i) {
        char x = s.charAt(i);
        if (x > 255) {
            throw new IllegalArgumentException("invalid char: " + x);
        }
        return x;
    }

    public static String encode(String s) {
        int i;
        int b10;
        ArrayList<Character> x = new ArrayList<>();
        int imax = s.length() - s.length() % 3;
        if (s.length() == 0) {
            return s;
        }

        for (i = 0; i < imax; i += 3) {
            b10 = (getByte(s, i) << 16) | (getByte(s, i + 1) << 8) | (getByte(s, i + 2));
            x.add(ALPHA[b10 >> 18]);
            x.add(ALPHA[(b10 >> 12) & 63]);
            x.add(ALPHA[(b10 >> 6) & 63]);
            x.add(ALPHA[b10 & 63]);
        }

        switch (s.length() - imax) {
            case 1:
                b10 = getByte(s, i) << 16;
                x.add(ALPHA[b10 >> 18]);
                x.add(ALPHA[(b10 >> 12) & 63]);
                x.add('=');
                x.add('=');
                break;
            case 2:
                b10 = (getByte(s, i) << 16) | (getByte(s, i + 1) << 8);
                x.add(ALPHA[b10 >> 18]);
                x.add(ALPHA[(b10 >> 12) & 63]);
                x.add(ALPHA[(b10 >> 6) & 63]);
                x.add('=');
                break;
        }

        return join(x);
    }

    private static String join(List<Character> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for (Character ch : list) {
            builder.append(ch);
        }
        return builder.toString();
    }
}
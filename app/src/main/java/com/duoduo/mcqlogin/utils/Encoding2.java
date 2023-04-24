package com.duoduo.mcqlogin.utils;

import java.util.ArrayList;

public class Encoding2 {

    private static int charCodeAt(String str, int idx) {
        if (idx >= str.length()) {
            return 0;
        }
        return str.charAt(idx);
    }

    public static ArrayList<Integer> s(String a, boolean b) {
        int c = a.length();
        ArrayList<Integer> v = new ArrayList<>();

        for (int i = 0; i < c; i += 4) {
            if ((i >> 2) > v.size()) {
                for (int j = 0; j < ((i >> 2) - v.size()); j++) {
                    v.add(0);
                }
            }
            if ((i >> 2) == v.size()) v.add(0);
            v.set(i >> 2, charCodeAt(a, i) | charCodeAt(a, i + 1) << 8 | charCodeAt(a, i + 2) << 16 | charCodeAt(a, i + 3) << 24);
        }

        if (b) v.add(c);

        return v;
    }

    public static String l(ArrayList<Integer> a, boolean b) {
        int d = a.size();
        int c = d - 1 << 2;
        if (b) {
            int m = a.get((int) (d - 1));
            if (m < c - 3 || m > c) {
                return "";
            }
            c = m;
        }

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < d; i++) {
            String s1 = Character.valueOf((char) (a.get(i) & 0xff)).toString();
            String s2 = Character.valueOf((char) (a.get(i) >>> 8 & 0xff)).toString();
            String s3 = Character.valueOf((char) (a.get(i) >>> 16 & 0xff)).toString();
            String s4 = Character.valueOf((char) (a.get(i) >>> 24 & 0xff)).toString();

            strings.add(
                    s1 + s2 + s3 + s4
            );
        }

        if (b) {
            return String.join("", strings).substring(0, c);
        }
        return String.join("", strings);
    }

    public static String encode(String str, String key) {
        if (str.equals("")) return "";

        ArrayList<Integer> v = s(str, true);
        ArrayList<Integer> k = s(key, false);

        if (k.size() < 4) {
            int diff = 4 - k.size();
            for (int i = 0; i < diff; i++) {
                k.add(0);
            }
        }

        int n = v.size() - 1;
        int z = v.get((int) n);
        int y = v.get(0);
        int c = 0x86014019 | 0x183639A0;
        int m;
        int e;
        int p;
        int q = (int) Math.floor(6d + 52d / (n + 1d));
        int d = 0;

        while (0 < q--) {
            d = d + c & (0x8CE0D9BF | 0x731F2640);
            e = d >>> 2 & 3;
            for (p = 0; p < n; p++) {
                y = v.get((int) (p + 1));
                m = z >>> 5 ^ y << 2;
                m += ((y >>> 3) ^ (z << 4)) ^ (d ^ y);
                m += k.get((int) ((p & 3) ^ e)) ^ z;
                int tmp = v.get(((int) p)) + m & (0xEFB8D130 | 0x10472ECF);
                v.set(((int) p), tmp);
                z = tmp;
            }
            y = v.get(0);
            m = (z >>> 5) ^ (y << 2);
            m += ((y >>> 3) ^ (z << 4)) ^ (d ^ y);
            m += k.get((int) ((p & 3) ^ e)) ^ z;
            int tmp = v.get(((int) n)) + m & (0xBB390742 | 0x44C6F8BD);
            v.set(((int) n), tmp);
            z = tmp;
        }

        return l(v, false);
    }

}

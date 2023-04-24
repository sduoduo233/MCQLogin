package com.duoduo.mcqlogin

import com.duoduo.mcqlogin.utils.Encoding2
import com.duoduo.mcqlogin.utils.Base64
import org.junit.Assert.*
import org.junit.Test


class EncodingTest {
    @Test
    fun md5() {
        assertEquals(com.duoduo.mcqlogin.utils.md5("ab58484d01cda346", "95bbea2ba9d45880"), "8df06419947e18a834c3f8b02432c7fb")
        assertEquals(com.duoduo.mcqlogin.utils.md5("1b941b2e97c920bc", "c95bd0a44b8d4a05"), "474da0d73f269f062bfe90a92bc8188f")
        assertEquals(com.duoduo.mcqlogin.utils.md5("c7775022617dd187", "2c6d4497e9f67e73"), "cb1168c2ca115b28327a8ca7c37029ef")
    }

    @Test
    fun encode() {
        // s
        assertArrayEquals(Encoding2.s("test123", false).toArray(), arrayOf(1953719668, 3355185))
        assertArrayEquals(Encoding2.s("test123", true).toArray(), arrayOf(1953719668, 3355185, 7))

        assertArrayEquals(Encoding2.s("318e6429a0eeff24", false).toArray(), arrayOf(1698181427, 959591478, 1701130337, 875718246))
        assertArrayEquals(Encoding2.s("318e6429a0eeff24", true).toArray(), arrayOf(1698181427, 959591478, 1701130337, 875718246, 16))


        assertArrayEquals(Encoding2.s("26fedda82c9066144854eea8dce0b7c65f7820c657939ce0", false).toArray(), arrayOf(1701197362, 945906788, 809067314, 875640374, 875903028, 945907045, 811950948, 912471906, 943154741, 912470066, 859387701, 811950905))
        assertArrayEquals(Encoding2.s("26fedda82c9066144854eea8dce0b7c65f7820c657939ce0", true).toArray(), arrayOf(1701197362, 945906788, 809067314, 875640374, 875903028, 945907045, 811950948, 912471906, 943154741, 912470066, 859387701, 811950905, 48))

        // l
        assertEquals(Encoding2.l(arrayListOf(1, 2, 3, 4, 5, 6, 7), false), "\u0001\u0000\u0000\u0000\u0002\u0000\u0000\u0000\u0003\u0000\u0000\u0000\u0004\u0000\u0000\u0000\u0005\u0000\u0000\u0000\u0006\u0000\u0000\u0000\u0007\u0000\u0000\u0000")
        assertEquals(Encoding2.l(arrayListOf(32, 456, 1436, 68, 2346, 547, 22), true), " \u0000\u0000\u0000È\u0001\u0000\u0000\u009C\u0005\u0000\u0000D\u0000\u0000\u0000*\t\u0000\u0000#\u0002")
        assertEquals(Encoding2.l(arrayListOf(32, 456, 1436, 68, 2346, 547, 212), true), "")

        // encode
        assertEquals(Encoding2.encode("test123123", "key312123"), "·r«¥w_K%H\t`[\u0019Ro½")
        assertEquals(Encoding2.encode("4333288f6a", "b200177889089d37"), "\u009B½E(ä«2ðK5åKº\u0091é¬")
        assertEquals(Encoding2.encode("7321b397a5af21629171b06bf32a11eefbc518273d58366c", "626883cb40adfaa4efdcc6e41208851880eb5ae66e91c757"), "\u008F_¯\u001Bó\u000FnÓ\u008DáO¹µÍ\u0091;#Â\u0018'nü\u001A¡«ÛÍ9=\u0011\u000Eæ\u0082\b\u0090d5·Ê¨ãV\u0012\u0081£0ïÃI ÍZ")
    }

    @Test
    fun base64Test() {
        assertEquals("Z+==", Base64.encode("a"))
        assertEquals("bETr3t3Yey0piQYf2Z8HxRRrqYPP6AbaOGzxhawxwtuZ5JCkmSiUHisMjcNAtet7Yid6uhwH/OHeLO0/gaJ5LEc9V60e4NfbOA3BOiMC/5ptWuBfkZRLdeIoFTHOl1xC", Base64.encode("³¸^§Jdäãe\u0017é-!\u0082\u0094È\u0092^\u009EA\u0004Çû\u0016h¼òEkò¿EXô1£J\u0001fPWÜÕqÿÓ\u009D7\u0090Z±U\u001BÔ¡¥9\u0001£h=`ý\u0003µÌ\u0007\u0013yð{lkúzhW\u0006£Ùtm^\u00AD\u008D\u0082@«\u009D\u0082N\u0015\u001A\u00AD\u009C\u0086"))
        assertEquals("OCubWC4b2NnwMU6Y", Base64.encode("hello, world"))
        assertEquals("9C1k1UPxZFPj9knUZIup0IZ70zhXZzRY9znTyJ0T9FuU1FPDyF9eZ+==",  Base64.encode("0fcfa2a1527fcee7f774bc2d37a83a15fe18939a"))
        assertEquals("9FZx0FMxZU+z9v==", Base64.encode("162572bd33"))
        assertEquals("9zipyahpyaZ60kL6ZzSjZzZ7yFRY0F+eZkPD9F+e", Base64.encode("31e9de9f1601c85c6792d549b18149"))
    }

}
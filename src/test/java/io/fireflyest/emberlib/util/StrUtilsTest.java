package io.fireflyest.emberlib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * 文本工具类测试
 */
public class StrUtilsTest {

    @Test
    public void testAppend() {
        final String str = StrUtils.append("str", '+', 10, 1.0, true);
        assertEquals("str+101.0true", str);
    }

    @Test
    public void testFormat() {
        final String str = StrUtils.format("test{}test{}", 1, true);
        assertEquals("test1testtrue", str);
    }

    @Test
    public void testToSentence() {
        assertNull(StrUtils.toSentence(null));
        assertEquals("", StrUtils.toSentence(""));
        assertEquals("a", StrUtils.toSentence("a"));
        assertEquals("null", StrUtils.toSentence("null"));
        assertEquals("camel camel", StrUtils.toSentence("camelCamel"));
        assertEquals("constant constant", StrUtils.toSentence("CONSTANT_CONSTANT"));
        assertEquals("dot dot", StrUtils.toSentence("dot.dot"));
        assertEquals("Pascal pascal", StrUtils.toSentence("PascalPascal"));
        assertEquals("path path", StrUtils.toSentence("path\\path"));
        assertEquals("snake snake", StrUtils.toSentence("snake_snake"));
        assertEquals("title title", StrUtils.toSentence("Title Title"));
    }

    @Test
    public void testUpperFirst() {
        assertNull(StrUtils.upperFirst(null));
        assertEquals("", StrUtils.upperFirst(""));
        assertEquals("A", StrUtils.upperFirst("a"));
        assertEquals("Null", StrUtils.upperFirst("null"));
    }

}

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
        final String str = TextUtils.append("str", '+', 10, 1.0, true);
        assertEquals("str+101.0true", str);
    }

    @Test
    public void testFormat() {
        final String str = TextUtils.format("test{}test{}", 1, true);
        assertEquals("test1testtrue", str);
    }

    @Test
    public void testToSentence() {
        assertNull(TextUtils.toSentence(null));
        assertEquals("", TextUtils.toSentence(""));
        assertEquals("a", TextUtils.toSentence("a"));
        assertEquals("null", TextUtils.toSentence("null"));
        assertEquals("camel camel", TextUtils.toSentence("camelCamel"));
        assertEquals("constant constant", TextUtils.toSentence("CONSTANT_CONSTANT"));
        assertEquals("dot dot", TextUtils.toSentence("dot.dot"));
        assertEquals("Pascal pascal", TextUtils.toSentence("PascalPascal"));
        assertEquals("path path", TextUtils.toSentence("path\\path"));
        assertEquals("snake snake", TextUtils.toSentence("snake_snake"));
        assertEquals("title title", TextUtils.toSentence("Title Title"));
    }

    @Test
    public void testUpperFirst() {
        assertNull(TextUtils.upperFirst(null));
        assertEquals("", TextUtils.upperFirst(""));
        assertEquals("A", TextUtils.upperFirst("a"));
        assertEquals("Null", TextUtils.upperFirst("null"));
    }

}

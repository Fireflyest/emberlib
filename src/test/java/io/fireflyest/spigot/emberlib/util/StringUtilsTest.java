package io.fireflyest.spigot.emberlib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * 文本工具类测试
 */
public class StringUtilsTest {

    @Test
    public void testAppend() {
        final String str = StringUtils.append("str", '+', 10, 1.0, true);
        assertEquals("str+101.0true", str);
    }

    @Test
    public void testFormat() {
        final String str = StringUtils.format("test{}test{}", 1, true);
        assertEquals("test1testtrue", str);
    }

    @Test
    public void testJsonToList() {

    }

    @Test
    public void testJsonToObj() {

    }

    @Test
    public void testToJson() {

    }

    @Test
    public void testToSentence() {
        assertNull(StringUtils.toSentence(null));
        assertEquals("", StringUtils.toSentence(""));
        assertEquals("a", StringUtils.toSentence("a"));
        assertEquals("null", StringUtils.toSentence("null"));
        assertEquals("camel camel", StringUtils.toSentence("camelCamel"));
        assertEquals("constant constant", StringUtils.toSentence("CONSTANT_CONSTANT"));
        assertEquals("dot dot", StringUtils.toSentence("dot.dot"));
        assertEquals("Pascal pascal", StringUtils.toSentence("PascalPascal"));
        assertEquals("path path", StringUtils.toSentence("path\\path"));
        assertEquals("snake snake", StringUtils.toSentence("snake_snake"));
        assertEquals("title title", StringUtils.toSentence("Title Title"));
    }

    @Test
    public void testUpperFirst() {
        assertNull(StringUtils.upperFirst(null));
        assertEquals("", StringUtils.upperFirst(""));
        assertEquals("A", StringUtils.upperFirst("a"));
        assertEquals("Null", StringUtils.upperFirst("null"));
    }

}

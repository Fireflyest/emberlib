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
        assertEquals(str, "str+101.0true");
    }

    @Test
    public void testFormat() {
        final String str = StringUtils.format("test{}test{}", 1, true);
        assertEquals(str, "test1testtrue");
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
        assertEquals(StringUtils.toSentence(""), "");
        assertEquals(StringUtils.toSentence("a"), "a");
        assertEquals(StringUtils.toSentence("null"), "null");
        assertEquals(StringUtils.toSentence("camelCamel"), "camel camel");
        assertEquals(StringUtils.toSentence("CONSTANT_CONSTANT"), "constant constant");
        assertEquals(StringUtils.toSentence("dot.dot"), "dot dot");
        assertEquals(StringUtils.toSentence("PascalPascal"), "Pascal pascal");
        assertEquals(StringUtils.toSentence("path\\path"), "path path");
        assertEquals(StringUtils.toSentence("snake_snake"), "snake snake");
        assertEquals(StringUtils.toSentence("Title Title"), "title title");
    }

    @Test
    public void testUpperFirst() {
        assertNull(StringUtils.upperFirst(null));
        assertEquals(StringUtils.upperFirst(""), "");
        assertEquals(StringUtils.upperFirst("a"), "A");
        assertEquals(StringUtils.upperFirst("null"), "Null");
    }

}

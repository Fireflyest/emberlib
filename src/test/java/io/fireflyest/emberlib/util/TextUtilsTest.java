package io.fireflyest.emberlib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * 文本工具类测试
 */
public class TextUtilsTest {

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
    public void testUpperFirst() {
        assertNull(TextUtils.upperFirst(null));
        assertEquals("", TextUtils.upperFirst(""));
        assertEquals("A", TextUtils.upperFirst("a"));
        assertEquals("Null", TextUtils.upperFirst("null"));
    }

    @Test
    public void testCamelSplit() {
        
    }

    @Test
    public void testFind() {
        
    }

    @Test
    public void testMatch() {
        
    }

    @Test
    public void testSymbolSplit() {
        
    }

    @Test
    public void testToCamel() {
        
    }

}

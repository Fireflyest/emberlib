package io.fireflyest.emberlib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 时间工具类测试
 * 
 * @author Fireflyest
 * @since 
 */
public class TimeUtilsTest {

    @Test
    public void testHowLong() {
        assertEquals("3h 11m 54s", TimeUtils.howLong(11514180L));
    }

    @Test
    public void testGetLocalDate() {
        assertEquals(TimeUtils.getLocalDate(), TimeUtils.getLocalDate(TimeUtils.getTime()));

    }

    @Test
    public void testGetLocalTime() {
        assertEquals(TimeUtils.getLocalTime(), TimeUtils.getLocalTime(TimeUtils.getTime()));
    }

    @Test
    public void testGetTime() {
        assertTrue(TimeUtils.getTime() > 0);
    }
}

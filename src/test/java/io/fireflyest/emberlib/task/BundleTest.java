package io.fireflyest.emberlib.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * 任务数据测试
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class BundleTest {

    @Test
    public void testGet() {
        final Bundle bundle = new Bundle(10, .5, 10000L, true, "test");
        assertEquals(true, bundle.getBoolean());
        assertEquals("test", bundle.getString());
        assertEquals(10, bundle.getNumber());
        assertEquals(.5, bundle.getNumber());
        assertEquals(10000L, bundle.getNumber());

        assertNull(bundle.getString());
    }

}

package io.fireflyest.emberlib.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;
import org.junit.Test;

/**
 * 缓存测试
 */
public class CacheOrganismTest {

    public static final String KEY_1 = "key1";
    public static final String KEY_2 = "key2";
    public static final String KEY_3 = "key3";
    public static final String KEY_4 = "key4";
    public static final String VALUE_1 = "value1";
    public static final String VALUE_2 = "value2";
    public static final String VALUE_3 = "value3";
    public static final String VALUE_4 = "value4";

    @Test
    public void testAge() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        try {
            synchronized (organism) {
                organism.wait(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(organism.age(KEY_1) >= 10);
    }

    @Test
    public void testDel() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        organism.del(KEY_1);
        assertNull(organism.get(KEY_1));
    }

    @Test
    public void testExist() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        organism.setex(KEY_2, 9, VALUE_1);
        try {
            synchronized (organism) {
                organism.wait(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(organism.exist(KEY_1));
        assertFalse(organism.exist(KEY_2));
    }

    @Test
    public void testExpire() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        // 未过期
        organism.expire(KEY_1, 200);
        try {
            synchronized (organism) {
                organism.wait(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(organism.get(KEY_1));
        // 重新设置时间
        organism.expire(KEY_1, 9);
        try {
            synchronized (organism) {
                organism.wait(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNull(organism.get(KEY_1));
        // 过期再重新设置时间无效
        organism.expire(KEY_1, 10);
        assertNull(organism.get(KEY_1));
    }

    @Test
    public void testGet() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        assertEquals(VALUE_1, organism.get(KEY_1));
    }

    @Test
    public void testKeySet() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        assertEquals(1, organism.keySet().size());
    }

    @Test
    public void testSaveAndLoad() {
        final File file = new File("./test.orga");
        final CacheOrganism organism = new CacheOrganism(null);
        // 集合
        organism.set(KEY_1, VALUE_1);
        organism.sadd(KEY_1, VALUE_2);
        organism.sadd(KEY_1, VALUE_3);
        // 键值对
        organism.set(KEY_2, VALUE_2);
        // 限时键值对
        organism.setex(KEY_3, 9, VALUE_3);
        organism.setex(KEY_4, 900, VALUE_4);

        try {
            synchronized (organism) {
                organism.wait(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        organism.save(file, "latest", false);

        organism.set(KEY_1, VALUE_2);
        organism.save(file, "backup", false);

        final CacheOrganism organismLatest = new CacheOrganism(null);
        organismLatest.load(file, "latest", false);
        assertEquals(3, organismLatest.scard(KEY_1));
        assertEquals(VALUE_2, organismLatest.get(KEY_2));
        assertNull(organismLatest.get(KEY_3));
        assertTrue(organismLatest.exist(KEY_4));

        final CacheOrganism organismBackup = new CacheOrganism(null);
        organismBackup.load(file, "backup", false);
        assertEquals(VALUE_2, organismLatest.get(KEY_1));
        assertEquals(VALUE_2, organismLatest.get(KEY_2));
        assertNull(organismLatest.get(KEY_3));
        assertTrue(organismLatest.exist(KEY_4));

        file.delete();
    }

    @Test
    public void testPersist() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.setex(KEY_1, 9, VALUE_1);
        organism.persist(KEY_1);
        try {
            synchronized (organism) {
                organism.wait(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotNull(organism.get(KEY_1));
    }

    @Test
    public void testSadd() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        organism.sadd(KEY_1, VALUE_2);
        assertNotNull(organism.smembers(KEY_1));
    }

    @Test
    public void testScard() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        organism.sadd(KEY_1, VALUE_2);
        assertEquals(2, organism.scard(KEY_1));
    }

    @Test
    public void testSet() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        assertEquals(VALUE_1, organism.get(KEY_1));
        organism.set(KEY_1, VALUE_2);
        assertEquals(VALUE_2, organism.get(KEY_1));
    }

    @Test
    public void testSetex() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.setex(KEY_1, 9, VALUE_1);
        try {
            synchronized (organism) {
                organism.wait(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNull(organism.get(KEY_1));
    }

    @Test
    public void testSmembers() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        organism.sadd(KEY_1, VALUE_2);
        organism.sadd(KEY_1, VALUE_3);
        final Set<String> smembers = organism.smembers(KEY_1);
        assertTrue(smembers != null && smembers.contains(VALUE_1));
        assertTrue(smembers != null && smembers.contains(VALUE_2));
        assertTrue(smembers != null && smembers.contains(VALUE_3));
    }

    @Test
    public void testSexist() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        organism.sadd(KEY_1, VALUE_2);
        assertTrue(organism.sexist(KEY_1, VALUE_1));
        assertTrue(organism.sexist(KEY_1, VALUE_2));
        assertFalse(organism.sexist(KEY_1, VALUE_3));
        assertFalse(organism.sexist(KEY_2, VALUE_3));
    }

    @Test
    public void testSpop() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        organism.sadd(KEY_1, VALUE_2);
        organism.sadd(KEY_1, VALUE_3);
        assertEquals(3, organism.scard(KEY_1));
        organism.spop(KEY_1);
        assertEquals(2, organism.scard(KEY_1));
        organism.spop(KEY_1);
        assertEquals(1, organism.scard(KEY_1));
        organism.spop(KEY_1);
        assertEquals(0, organism.scard(KEY_1));
    }

    @Test
    public void testSrem() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.set(KEY_1, VALUE_1);
        organism.sadd(KEY_1, VALUE_2);
        organism.sadd(KEY_1, VALUE_3);
        organism.srem(KEY_1, VALUE_2);
        assertNotNull(organism.get(KEY_1));
        assertNull(organism.get(KEY_2));
        assertNotNull(organism.get(KEY_1));
    }

    @Test
    public void testTtl() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.setex(KEY_1, 10, VALUE_1);
        organism.set(KEY_2, VALUE_2);
        organism.setex(KEY_3, 4, VALUE_3);
        try {
            synchronized (organism) {
                organism.wait(5);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(organism.ttl(KEY_1) <= 5);
        assertEquals(-1, organism.ttl(KEY_2));
        assertEquals(0, organism.ttl(KEY_3));
    }

    @Test
    public void testRelease() {
        final CacheOrganism organism = new CacheOrganism(null);
        organism.setex(KEY_1, 9, VALUE_1);
        organism.set(KEY_2, VALUE_2);
        organism.setex(KEY_3, 4, VALUE_3);
        assertEquals(3, organism.keySet().size());
        try {
            synchronized (organism) {
                organism.wait(5);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(3, organism.keySet().size());
        try {
            synchronized (organism) {
                organism.wait(5);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(3, organism.keySet().size());
        organism.release();
        assertEquals(1, organism.keySet().size());
    }

}

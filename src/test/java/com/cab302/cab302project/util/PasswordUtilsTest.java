package com.cab302.cab302project.util;

import org.junit.jupiter.api.*;

/**
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public class PasswordUtilsTest {

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testHashKnownValue() {
        String expectedHash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
        String actualHash = PasswordUtils.hashSHA256("password");
        Assertions.assertEquals(expectedHash, actualHash);
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testHashConsistency() {
        String testString = "Coffee";
        String hashOne = PasswordUtils.hashSHA256(testString);
        String hashTwo = PasswordUtils.hashSHA256(testString);
        Assertions.assertEquals(hashOne, hashTwo);
    }

    /**
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    @Test
    void testExceptionThrow() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            PasswordUtils.hashSHA256(null);
        });
    }
}

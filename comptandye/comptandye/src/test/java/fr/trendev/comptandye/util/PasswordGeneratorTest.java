/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class PasswordGeneratorTest {

    public PasswordGeneratorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of autoGenerate method, of class PasswordGenerator.
     */
    @Test
    public void testAutoGenerate() {
        System.out.println("autoGenerate");

        int n = 10000;
        int size = 10;
        Set<String> spwd = new HashSet<>(n);

        assert spwd.isEmpty();

        IntStream.range(0, n).forEach(i -> spwd.add(PasswordGenerator.
                encrypt_SHA256(PasswordGenerator.
                        autoGenerate(size))));

        assert !spwd.isEmpty();

        System.out.println(n + " secured passwords generated");
        if (spwd.size() != n) {
            System.out.println(spwd.size() * 100 / n + "% are different");
        } else {
            System.out.println("They are all different");
        }

    }

    /**
     * Test of encrypt_SHA256 method, of class PasswordGenerator.
     */
    @Test
    public void testEncrypt_SHA256() {
        System.out.println("encrypt_SHA256");
        String pwd = "error";
        String pwd_sha256 = "ca00fccfb408989eddc401062c4d1219a6aceb6b9b55412357f1790862e8f178";

        String pwd2 = "password";
        String pwd2_sha256 = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";

        String pwd3 = "comptandye_password";
        String pwd3_sha256 = "f2838ae81b17b2ffacd7d5cc68a314621eb2d08bd57f82a36fb1a64964bf3346";

        assertPasswordEncryption(pwd, pwd_sha256);
        assertPasswordEncryption(pwd2, pwd2_sha256);
        assertPasswordEncryption(pwd3, pwd3_sha256);

    }

    private void assertPasswordEncryption(String pwd, String pwd_sha256) {
        assert PasswordGenerator.encrypt_SHA256(pwd).equals(pwd_sha256);

        System.out.println("\"" + pwd + "\" ==> " + PasswordGenerator.
                encrypt_SHA256(pwd));
    }

}

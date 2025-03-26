import src.MathOperation;
import src.Prime;

public class MainTest {
    private static int totalTests = 0;
    private static int passedTests = 0;

    private static void assertTrue(String message, boolean condition) {
        totalTests++;
        if (condition) {
            System.out.println("✓ PASS: " + message);
            passedTests++;
        } else {
            System.out.println("✗ FAIL: " + message);
        }
    }

    private static void assertFalse(String message, boolean condition) {
        assertTrue(message, !condition);
    }

    private static void assertEquals(String message, long expected, long actual) {
        assertTrue(message, expected == actual);
    }

    private static void assertNotNull(String message, Object obj) {
        assertTrue(message, obj != null);
    }

    public static void main(String[] args) {
        System.out.println("เริ่มการทดสอบ...\n");

        // ทดสอบ GCD
        System.out.println("Testing GCD:");
        assertEquals("GCD(9,15) should be 3", 3L, MathOperation.gcd(9L, 15L));
        assertEquals("GCD(0,5) should be 5", 5L, MathOperation.gcd(0L, 5L));
        assertEquals("GCD(5,0) should be 5", 5L, MathOperation.gcd(5L, 0L));
        assertEquals("GCD(7,7) should be 7", 7L, MathOperation.gcd(7L, 7L));
        System.out.println();

        // ทดสอบ FindInverse
        System.out.println("Testing FindInverse:");
        assertEquals("Inverse of 3 mod 11 should be 4", 4L, MathOperation.findInverse(3L, 11L));
        assertEquals("Inverse of 2 mod 4 should not exist", -1L, MathOperation.findInverse(2L, 4L));
        Long a = 3L, n = 11L;
        Long inverse = MathOperation.findInverse(a, n);
        assertEquals("(3 * inverse) mod 11 should be 1", 1L, (a * inverse) % n);
        System.out.println();

        // ทดสอบ FastExpo
        System.out.println("Testing FastExpo:");
        assertEquals("3^5 mod 100 should be 43", 43L, MathOperation.fastExpo(3L, 5L, 100L));
        assertEquals("5^0 mod 100 should be 1", 1L, MathOperation.fastExpo(5L, 0L, 100L));
        assertEquals("2^10 mod 3 should be 1", 1L, MathOperation.fastExpo(2L, 10L, 3L));
        System.out.println();

        // ทดสอบ IsPrime
        System.out.println("Testing IsPrime:");
        assertTrue("97 should be prime", Prime.isPrime(97L));
        assertFalse("99 should not be prime", Prime.isPrime(99L));
        assertTrue("2 should be prime", Prime.isPrime(2L));
        assertFalse("1 should not be prime", Prime.isPrime(1L));
        System.out.println();

        // ทดสอบ GenPrime
        System.out.println("Testing GenPrime:");
        Long prime10 = Prime.GenPrime(10L, "Main.java");
        assertNotNull("Generated prime should not be null", prime10);
        assertTrue("Generated number should be prime", Prime.isPrime(prime10));
        assertTrue("Generated prime should be in correct range",
                prime10 >= (1L << 9) && prime10 < (1L << 10));
        System.out.println("Generated 10-bit prime: " + prime10);
        System.out.println();

        // ทดสอบ GenRandomNowithInverse
        System.out.println("Testing GenRandomNowithInverse:");
        // ทดสอบกับ modulus เล็ก
        Prime.Triple result1 = Prime.GenRandomNowithInverse(11L);
        assertNotNull("Result should not be null", result1);
        assertEquals("GCD(e,n) should be 1", 1L, MathOperation.gcd(result1.e, result1.n));
        assertEquals("(e * e^-1) mod n should be 1",
                1L, (result1.e * result1.eInverse) % result1.n);
        System.out.println("Result with n=11: " + result1);

        // ทดสอบกับ modulus ใหญ่
        Prime.Triple result2 = Prime.GenRandomNowithInverse(100L);
        assertNotNull("Result should not be null", result2);
        assertEquals("GCD(e,n) should be 1", 1L, MathOperation.gcd(result2.e, result2.n));
        assertEquals("(e * e^-1) mod n should be 1",
                1L, (result2.e * result2.eInverse) % result2.n);
        System.out.println("Result with n=100: " + result2);

        // ทดสอบช่วงค่าที่ถูกต้อง
        assertTrue("e should be >= 2", result2.e >= 2L);
        assertTrue("e should be < n", result2.e < result2.n);
        assertTrue("e^-1 should be >= 1", result2.eInverse >= 1L);
        assertTrue("e^-1 should be < n", result2.eInverse < result2.n);

        // แสดงสรุปผลการทดสอบ
        System.out.println("\nสรุปผลการทดสอบ:");
        System.out.println("ผ่าน: " + passedTests + "/" + totalTests + " การทดสอบ");
        if (passedTests == totalTests) {
            System.out.println("✓ การทดสอบทั้งหมดผ่าน!");
        } else {
            System.out.println("✗ มีการทดสอบที่ไม่ผ่าน " + (totalTests - passedTests) + " รายการ");
        }
    }
}
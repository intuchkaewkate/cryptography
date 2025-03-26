import src.MathOperation;
import src.Prime;

public class Main {
    public static void testGenRandomNowithInverse() {
        System.out.println("\nTesting GenRandomNowithInverse:");
        Long[] testValues = { 11L, 17L, 26L, 100L };
        for (Long n : testValues) {
            System.out.println("\nWith n = " + n);
            Prime.Triple result = Prime.GenRandomNowithInverse(n);
            System.out.println("Result: " + result);
            // ตรวจสอบความถูกต้อง
            System.out.println("Verification:");
            System.out.println("- GCD(e,n) = " + MathOperation.gcd(result.e, result.n));
            System.out.println("- (e * e^-1) mod n = " + ((result.e * result.eInverse) % result.n));
        }

    }

    public static void main(String[] args) {
        System.out.println(MathOperation.gcd((long) 9, (long) 15));
        System.out.println(MathOperation.findInverse((long) 3, (long) 11));
        System.out.println(MathOperation.fastExpo((long) 3, (long) 5, (long) 100));
        System.out.println(Prime.isPrime((long) 97));

        String filename = "Main.java";
        // Long prime = Prime.GenPrime(10L, "Main.java");
        // Long prime = Prime.GenPrime(20L, filename); // too long
        // System.out.println(prime);

        var x = Prime.GenRandomNowithInverse(1000L);
        System.out.println(x);

    }
}

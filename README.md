import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Scanner;

public class DigiSign {

    public static boolean isPrime(BigInteger n) {
        return n.isProbablePrime(1);
    }

    public static boolean isDivisor(BigInteger p, BigInteger q) {
        return p.subtract(BigInteger.ONE).mod(q).equals(BigInteger.ZERO);
    }

    public static String hashMessage(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(msg.getBytes());
            StringBuilder hexStr = new StringBuilder();
            for (byte b : hash) {
                hexStr.append(String.format("%02x", b));
            }
            return hexStr.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BigInteger multiplicativeInverse(BigInteger a, BigInteger m) {
        return a.modInverse(m);
    }

    public static String readMessage(String filePath) throws IOException {
        return Files.readString(Paths.get(filePath));
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter p (prime): ");
        BigInteger p = new BigInteger(scanner.nextLine());
        if (!isPrime(p)) {
            System.out.println("p is not prime");
            return;
        }

        System.out.print("Enter q (divisor of p-1): ");
        BigInteger q = new BigInteger(scanner.nextLine());
        if (!isDivisor(p, q)) {
            System.out.println("q is not a divisor of p-1");
            return;
        }

        System.out.print("Enter h: ");
        BigInteger h = new BigInteger(scanner.nextLine());
        if (h.compareTo(BigInteger.ONE) <= 0 || h.compareTo(p) >= 0) {
            System.out.println("Invalid h");
            return;
        }

        BigInteger g = h.modPow(p.subtract(BigInteger.ONE).divide(q), p);

        System.out.print("Enter x (private key < q): ");
        BigInteger x = new BigInteger(scanner.nextLine());
        if (x.compareTo(q) >= 0) {
            System.out.println("Invalid x");
            return;
        }

        BigInteger y = g.modPow(x, p);
        System.out.println("Public Key: y = " + y);

        System.out.print("Enter file path: ");
        String msg = readMessage(scanner.nextLine());

        System.out.print("Enter k (< q): ");
        BigInteger k = new BigInteger(scanner.nextLine());
        if (k.compareTo(q) >= 0) {
            System.out.println("Invalid k");
            return;
        }

        BigInteger r = g.modPow(k, p).mod(q);
        BigInteger hash = new BigInteger(hashMessage(msg), 16);
        BigInteger s = k.modInverse(q).multiply(hash.add(x.multiply(r))).mod(q);

        System.out.println("Signature: r = " + r + ", s = " + s);

        // Verification
        BigInteger sinv = s.modInverse(q);
        BigInteger u1 = hash.multiply(sinv).mod(q);
        BigInteger u2 = r.multiply(sinv).mod(q);
        BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);

        System.out.println(r.equals(v) ? "Verified" : "Not verified");
    }
}

package src;

import java.util.Random;

public class Prime {
    public static boolean isPrime(Long num) {
        if (num <= 1) {
            return false;
        }
        if (num == 2) {
            return true;
        } else if (num % 2 == 0) {
            return false;
        }
        for (Long i = (long) 2; i < num; i++) {
            Long temp = MathOperation.fastExpo(i, (num - 1) / 2, num);
            if (temp != (long) 1 && temp != num - 1) {
                return false;
            }
        }
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            Long witness = rand.nextLong(1, num);
            if (MathOperation.gcd(num, witness) > 1) {
                return false;
            }
            Long temp = MathOperation.fastExpo(witness, (num - 1) / 2, num);
            if (temp != (long) 1 && temp != num - 1) {
                return false;
            }
        }
        return true;
    }

    public static Long GenPrime(Long n, String filename) {
        try {
            // อ่านข้อมูล n bits จากไฟล์
            java.io.FileInputStream fis = new java.io.FileInputStream(filename);
            byte[] bytes = new byte[Math.max(1024, (int) (n + 7) / 8)]; // ให้แน่ใจว่ามี bytes เพียงพอสำหรับการอ่าน
            int bytesRead = fis.read(bytes);
            fis.close();

            if (bytesRead <= 0) {
                throw new java.io.IOException("ไฟล์ว่างเปล่า");
            }

            // แสดงผลในรูปแบบเลขฐาน 16
            // System.out.println("Bytes in hexadecimal:");
            // for (int i = 0; i < bytesRead; i++) {
            // System.out.printf("%02X ", bytes[i]);
            // }
            // System.out.println("\n");

            // แปลง bytes เป็น bits และหาเลข 1 ตัวแรก
            StringBuilder bits = new StringBuilder();
            for (int i = 0; i < bytesRead; i++) {
                for (int j = 7; j >= 0; j--) { // วนลูปจากบิตซ้ายสุด (MSB) ไปขวาสุด (LSB)
                    // เลื่อน (shift) bits ไปทางขวา j ตำแหน่ง
                    // & 1: AND กับ 1 เพื่อเอาเฉพาะ bit สุดท้าย
                    bits.append((bytes[i] >> j) & 1); // แปลงแต่ละบิตเป็น 0 หรือ 1 แล้วเก็บใน StringBuilder
                }
            }

            // หาตำแหน่งของเลข 1 ตัวแรกและนับ n bits จากตำแหน่งนั้น
            int startIndex = bits.indexOf("1");
            if (startIndex == -1 || startIndex + n > bits.length()) {
                throw new java.io.IOException("ไม่สามารถหาเลข n bits ที่ขึ้นต้นด้วย 1 ได้");
            }

            // แสดงผล binary pattern พร้อมไฮไลท์ส่วนที่จะใช้
            /*
             * System.out.println("Binary pattern (highlighted bits will be used):");
             * String fullBits = bits.toString();
             * String beforeTarget = fullBits.substring(0, startIndex);
             * String target = fullBits.substring(startIndex, startIndex + n.intValue());
             * String afterTarget = fullBits.substring(startIndex + n.intValue());
             * 
             * // ใช้ ANSI escape codes สำหรับสี
             * System.out.print(beforeTarget); // ส่วนก่อนหน้า
             * System.out.print("\u001B[31m" + target + "\u001B[0m"); // ส่วนที่จะใช้
             * (สีแดง)
             * System.out.println(afterTarget); // ส่วนที่เหลือ
             * System.out.println();
             */

            // แปลง n bits เป็นตัวเลข
            String nBits = bits.substring(startIndex, startIndex + n.intValue());
            // System.out.println("nBits = " + nBits);
            Long number = Long.parseLong(nBits, 2);
            // System.out.println("number = " + number);

            // ตรวจสอบว่าตัวเลขอยู่ในช่วง 2^(n-1) ถึง (2^n - 1)
            Long lowerBound = 1L << (n - 1); // ขอบล่าง = 2^(n-1)
            Long upperBound = (1L << n) - 1; // ขอบบน = 2^n - 1

            if (number < lowerBound || number > upperBound) {
                number = lowerBound; // ถ้าเลขที่ได้อยู่นอกช่วง ให้เริ่มจากขอบล่าง
            }

            // หาจำนวนเฉพาะถัดไปในช่วงที่กำหนด
            if (number % 2 == 0) {
                number++; // ถ้าเริ่มต้นจากเลขคู่ ให้เพิ่ม 1 เพื่อให้เป็นเลขคี่
            }
            while (number <= upperBound) {
                if (isPrime(number) && isPrime((number - 1) / 2)) { // Safe Prime
                    return number;
                }
                number += 2; // เพิ่มทีละ 2 เพื่อข้ามเลขคู่
            }

            return -1L; // ไม่พบจำนวนเฉพาะในช่วงที่กำหนด
        } catch (Exception e) {
            System.err.println("เกิดข้อผิดพลาด: " + e.getMessage());
            return -1L;
        }
    }

    // Class สำหรับเก็บผลลัพธ์ (e, e^-1, n)
    public static class Triple {
        public Long e; // ค่า e ที่สุ่มมา
        public Long eInverse; // ค่า e^-1 mod n
        public Long n; // ค่า n ที่รับเข้ามา

        public Triple(Long e, Long eInverse, Long n) {
            this.e = e;
            this.eInverse = eInverse;
            this.n = n;
        }

        @Override
        public String toString() {
            return String.format("(e=%d, e^-1=%d, n=%d)", e, eInverse, n);
        }
    }

    public static Triple GenRandomNowithInverse(Long n) {
        Random rand = new Random();

        // วนลูปจนกว่าจะได้ค่าที่ถูกต้อง
        while (true) {
            // สุ่มค่า e ในช่วง [2, n-1]
            Long e = rand.nextLong(2, n);

            // ตรวจสอบว่า gcd(e,n) = 1
            if (MathOperation.gcd(e, n) == 1) {
                // หา inverse ของ e mod n
                Long eInverse = MathOperation.findInverse(e, n);

                // ถ้ามี inverse (ค่าไม่เท่ากับ -1)
                if (eInverse != -1) {
                    // ตรวจสอบความถูกต้อง: (e * e^-1) mod n ต้องเท่ากับ 1
                    if ((e * eInverse) % n == 1) {
                        return new Triple(e, eInverse, n);
                    }
                }
            }
        }
    }
}
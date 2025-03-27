package src;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MathOperation {

    public static Long gcd(Long num1, Long num2) {
        if (num1 == 0)
            return num2;
        if (num2 == 0)
            return num1;

        if (num1 < num2) {
            // สลับค่า num1 และ num2 เพื่อให้ num1 ใหญ่กว่าเสมอ
            Long temp = num2;
            num2 = num1;
            num1 = temp;
        }
        Long remainder = num1 % num2; // คำนวณเศษจากการหาร
        if (remainder == 0) {
            return num2; // หากหารลงตัว ค่า num2 คือ GCD
        } else {
            return gcd(num2, remainder); // ทำซ้ำโดยเรียก gcd ด้วยตัวหารเก่าและเศษ
        }
    }

    public static Long findInverse(Long num, Long mod) {
        // ถ้า gcd ไม่เท่ากับ 1 แสดงว่าไม่มี inverse
        if (gcd(num, mod) != 1) {
            return -1L;
        }

        Long masterMod = mod;
        List<Long> quotientList = new ArrayList<>();
        List<Long> auxillaryList = new ArrayList<>();
        List<Long> remainderList = new ArrayList<>();
        int step = 0;

        while (true) {
            quotientList.add(step, mod / num); // หาร mod ด้วย num เก็บค่าผลหาร
            Long remainder = mod % num; // คำนวณเศษจากการหาร
            remainderList.add(step, remainder);
            mod = num; // อัพเดท mod ด้วย num
            num = remainder; // อัพเดท num ด้วยเศษที่ได้

            if (step == 0) {
                auxillaryList.add(0, (long) 0); // ค่าเริ่มต้น
            } else if (step == 1) {
                auxillaryList.add(1, (long) 1); // ค่าเริ่มต้น
            } else {
                // คำนวณค่า auxillary (สัมประสิทธิ์ที่ใช้หาค่า inverse)
                Long x = auxillaryList.get(step - 2) - (auxillaryList.get(step - 1) * quotientList.get(step - 2));
                auxillaryList.add(step, x % masterMod);
            }

            // เช็คถ้าเศษเป็น 0 แล้ว
            if (remainder == 0) {
                if (remainderList.get(step - 1) == 1) {
                    step++;
                    break; // มี inverse modulo
                } else {
                    return (long) -1; // ไม่มี inverse modulo เพราะ gcd ไม่ใช่ 1
                }
            }
            step++;
        }

        // คืนค่า inverse modulo ที่ได้
        return (auxillaryList.get(step - 2)
                - (auxillaryList.get(step - 1) * quotientList.get(step - 2))) % masterMod;
    }

    public static Long fastExpo(Long num, Long exp, Long mod) {
        // กรณีฐานยกกำลัง 0 ต้องได้ 1
        if (exp == 0) {
            return 1L;
        }
        // กรณีเลขยกกำลังติดลบ
        if (exp < 0) {
            return -1L;
        }

        Dictionary<Long, Long> expList = new Hashtable<>();
        if (exp == 0) {
            return (long) 0;
        } else if (exp < 0) {
            return (long) -1;
        }
        Long maxI = (long) 0;
        expList.put((long) 1, num % mod);
        for (Long i = (long) 2; i <= exp; i *= 2) {
            maxI = i;
            expList.put(i, (expList.get(i / 2) * expList.get(i / 2)) % mod);
        }
        Long ans = (long) 1;
        for (Long i = maxI; exp > 0; i /= 2) {
            if (i <= exp) {
                ans = (ans * expList.get(i)) % mod;
                exp -= i;
            }
        }
        return ans;
    }

    public Set<Long> generator(Long num) {
        Set<Long> generatorList = new HashSet<>();
        for (Long i = 2L; i < num - 1; i++) {
            if (generatorList.contains(i))
                continue;
            if (fastExpo(i, (num - 1) / 2, num) != 1) {
                generatorList.add(i);
            } else {
                generatorList.add(num - i);
            }
        }
        return generatorList;
    }
}
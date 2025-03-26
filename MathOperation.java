import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class MathOperation {

    public Long gcd(Long num1, Long num2) {
        if (num1 < num2) {
            Long temp = num2;
            num2 = num1;
            num1 = temp;
        }
        Long remainder = num1 % num2;
        if (remainder == 0) {
            return num2;
        } else {
            return gcd(num2, remainder);
        }
    }

    public Long findInverse(Long num, Long mod) {
        Long masterMod = mod;
        List<Long> quotientList = new ArrayList<>();
        List<Long> auxillaryList = new ArrayList<>();
        List<Long> remainderList = new ArrayList<>();
        int step = 0;
        while (true) {
            quotientList.add(step, mod / num);
            Long remainder = mod % num;
            remainderList.add(step, remainder);
            mod = num;
            num = remainder;
            if (step == 0) {
                auxillaryList.add(0, (long) 0);
            } else if (step == 1) {
                auxillaryList.add(1, (long) 1);
            } else {
                auxillaryList.add(step, (auxillaryList.get(step - 2)
                        - (auxillaryList.get(step - 1) * quotientList.get(step - 2))) % masterMod);
            }
            if (remainder == 0) {
                if (remainderList.get(step - 1) == 1) {
                    step++;
                    break;
                } else {
                    return (long) -1;
                }
            }
            step++;
        }
        return (auxillaryList.get(step - 2)
                - (auxillaryList.get(step - 1) * quotientList.get(step - 2))) % masterMod;
    }

    public Long fastExpo(Long num, Long exp, Long mod) {
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

    public boolean isPrime(Long num) {
        if (num == 2) {
            return true;
        } else if (num % 2 == 0) {
            return false;
        }
        for (Long i = (long) 2; i < num; i++) {
            Long temp = fastExpo(i, (num - 1) / 2, num);
            if (temp != (long) 1 && temp != num - 1) {
                return false;
            }
        }
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            Long witness = rand.nextLong(1, num);
            if (gcd(num, witness) > 1) {
                return false;
            }
            Long temp = fastExpo(witness, (num - 1) / 2, num);
            if (temp != (long) 1 && temp != num - 1) {
                return false;
            }
        }
        return true;
    }
}
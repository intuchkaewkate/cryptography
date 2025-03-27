import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import src.MathOperation;

public class EmergencyMain {
    public static void main(String[] args) {
        MathOperation myMath = new MathOperation();
        Set<Long> genList = myMath.generator(23L);
        ArrayList<Long> sortedList = new ArrayList<>();
        for (Long i : genList) {
            sortedList.add(i);
        }
        Collections.sort(sortedList);
        System.out.println(sortedList);
    }
}

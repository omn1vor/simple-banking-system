package banking;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Util {
    public static void numberIntoArray(long num, int[] arr, int start, int end) {
        int index = end;
        while (index >= start && num != 0) {
            arr[index--] = (int) num % 10;
            num /= 10;
        }
    }

    public static String arrayToString(int[] arr) {
        return Arrays.stream(arr)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    public static int getCheckSum(int[] digits) {
        int controlNumber = 0;
        for (int i = 0; i < digits.length - 1; i++) {
            int num = digits[i] * (i % 2 == 0 ? 2 : 1);
            if (num > 9) {
                num -= 9;
            }
            controlNumber +=num;
        }
        return controlNumber % 10 == 0 ? 0 : 10 - controlNumber % 10;
    }
}

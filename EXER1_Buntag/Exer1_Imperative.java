public class Exer1_Imperative {
    public static void main(String[] args) {
        int sum = 0;

        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                int square = i * i;
                sum = sum + square;
            }
        }

        System.out.println("Sum of squares of even numbers: " + sum);
    }
}
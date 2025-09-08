public class Exer1_Declarative {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        int sum = 0;

        for (int i = 0; i < 10; i++) {
            int num = numbers[i];
            if (num % 2 == 0) {           
                int square = num * num;   
                sum = sum + square;       
            }
        }

        System.out.println("Sum of squares of even numbers: " + sum);
    }
}
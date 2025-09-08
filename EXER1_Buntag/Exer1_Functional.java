public class Exer1_Functional {
    public static void main(String[] args) {
        int sum = java.util.stream.IntStream.rangeClosed(1, 10)  
                    .filter(n -> n % 2 == 0)                 
                    .map(n -> n * n)                            
                    .sum();                                   

        System.out.println("Sum of squares of even numbers: " + sum);
    }
}
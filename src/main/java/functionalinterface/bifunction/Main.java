package functionalinterface.bifunction;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class Main {

    public static void main(String[] args){
        BiFunction<Integer, Integer, Integer> plus = (i, j) -> i + j;
        System.out.println(plus.apply(10, 20)); // 10 + 20 = 30
        System.out.println("######################");
        BiFunction<Integer, Integer, Integer> multiply = (i, j) -> i * j;
        System.out.println(multiply.apply(10, 20)); // 10 * 20 = 200
        System.out.println("######################");
        //함수를 조합해서 사용.
        UnaryOperator<Integer> minus = (i) -> i - 50; //Function<Integer, Integer> minus = (i) -> i - 50;
        System.out.println(plus.andThen(minus).apply(20, 30)); //(20 + 30) - 50 = 0
        System.out.println("######################");
        System.out.println(multiply.andThen(minus).apply(20, 30)); //(20 * 30) - 50 = 550
    }
}

package functionalinterface.binaryoperator;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class Main {
    public static void main(String[] args) {
        BiFunction<Integer, Integer, Integer> plus = (i, j) -> i + j;
        BiFunction<Integer, Integer, Integer> multiply = (i, j) -> i * j;

        // BiFunction<T, U, R> 인터페이스의 특수한 형태.
        // 3개(입력하는 값 2개, 반환하는 값 1개)의 타입이 모두 같은 경우에 사용 가능
        // BiFunction 인터페이스를 상속받았다.
        BinaryOperator<Integer> minus = (i, j) -> i - j; //==BiFunction<Integer, Integer, Integer> minus = (i, j) -> i - j;
        System.out.println(minus.apply(20, 10));
    }
}

package functionalinterface.unaryoperator;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Main {
    public static void main(String[] args){
        Function<Integer, Integer> plus20 = (i) -> i + 20;
        Function<Integer, Integer> multiply2 = (i) -> i * 2;

        // Function 인터페이스의 특수한 형태.
        // 위와 같이 입력하는 값과 반환하는 값의 타입이 같다면 사용 가능.
        // Function 인터페이스를 상속받았다.
        UnaryOperator<Integer> plus30 = (i) -> i + 30; // ==Function<Integer, Integer> plus30 = (i) -> i + 30;
        UnaryOperator<Integer> multiply4 = (i) -> i * 4; // ==Function<Integer, Integer> multiply4 = (i) -> i * 4;
        System.out.println(plus30.apply(30));
        System.out.println(multiply4.apply(30));
        System.out.println(plus30.andThen(multiply4).apply(40));
        System.out.println(plus30.compose(multiply4).apply(40));
        System.out.println(plus30.andThen(plus20).andThen(multiply2).andThen(multiply4).apply(30));
    }
}

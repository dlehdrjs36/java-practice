package functionalinterface.function;

import java.util.function.Function;

public class Main {
    public static void main(String[] args){
        /*
         * 자바에서 제공하는 함수형 인터페이스
         * 고차 함수(High-Order Function의 특성이 나타난다.
         */

        //1. Function<T, R> 인터페이스를 구현한 클래스를 사용.
        Plus10 plus10 = new Plus10();
        System.out.println(plus10.apply(1));
        System.out.println("#########################");

        //2. 람다 표현식으로 사용.
        Function<Integer, Integer> plus20 = (i) -> i + 20;
        System.out.println(plus20.apply(1));
        System.out.println("#########################");

        Function<Integer, Integer> multiply2 = (i) -> i * 2;
        System.out.println(multiply2.apply(1));
        System.out.println("#########################");

        //3. 함수를 조합하여 사용.
        //함수 조합용 메소드 : compose, andThen
        //compose 메소드는 입력 값을 인자로 전달한 함수를 먼저 적용하고 결과 값을 plus10의 입력 값으로 전달하여 동작.
        //T를 넣으면 multiply2를 수행하고 그 결과를 plus20 수행.
        //2 * 2 + 20 = 24
        Function<Integer, Integer> multiply2AndPlus20 = plus20.compose(multiply2);
        System.out.println(multiply2AndPlus20.apply(2));
        System.out.println("#########################");
        //andThen 메소드는 입력 값을 plus20을 먼저 적용하고 그 결과를 인자로 전달한 함수의 입력 값으로 사용하여 동작.
        //20+2 * 2 = 44
        Function<Integer, Integer> plus20AndMultiply2 = plus20.andThen(multiply2);
        System.out.println(plus20AndMultiply2.apply(2));
        System.out.println(plus20.andThen(multiply2).apply(2));
    }
}

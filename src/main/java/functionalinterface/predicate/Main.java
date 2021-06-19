package functionalinterface.predicate;

import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        //인자를 하나 받아서 true, false를 반환해주는 함수형 인터페이스
        Predicate<String> startWithDevHistory = (s) -> s.startsWith("DevHistory");
        //짝수 검사
        Predicate<Integer> isEven = (i) -> i%2 == 0;

        System.out.println(startWithDevHistory.test("DevHistory"));
        System.out.println(startWithDevHistory.test("DevHistory2"));
        System.out.println(startWithDevHistory.test("NoDevHistory2"));
        System.out.println("#################################");
        //결과에 not을 취함
        Predicate<String> notStartWithDevHistory = startWithDevHistory.negate();
        System.out.println(notStartWithDevHistory.test("DevHistory"));
        System.out.println(notStartWithDevHistory.test("DevHistory2"));
        System.out.println(notStartWithDevHistory.test("NoDevHistory"));
        System.out.println("#################################");
        //OR 연산
        System.out.println(startWithDevHistory.or(notStartWithDevHistory).test("DevHistory"));
        //AND 연산
        System.out.println(startWithDevHistory.and(notStartWithDevHistory).test("DevHistory"));

    }
}

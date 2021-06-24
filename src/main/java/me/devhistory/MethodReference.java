package me.devhistory;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/*
    메소드 레퍼런스 클래스
 */
class Greeting {
    private String name;

    //1) 생성자 참조
    public Greeting(){
    }

    //2) 생성자 참조
    public Greeting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    //인스턴스 메소드 참조
    public String hello(String name) {
        return "hello " + name;
    }

    //스태틱 메소드 참조
    public static String hi(String name) {
        return "hi " + name;
    }

}

public class MethodReference {
    public static void main(String[] args){
        UnaryOperator<String> hi = (s) -> "hi " + s;

        //위와 같은 기능을 하는 것이 이미 구현되어 있다면 직접 구현하지않고 메소드 레퍼런스를 활용해서 사용가능
        /* 1. 스태틱 메소드 참조 */
        UnaryOperator<String> hi2 = Greeting::hi;
        System.out.println(hi2.apply("DevHistory"));
        /* 2. 특정 객체의 인스턴스 메소드 참조 */
        Greeting greeting = new Greeting();
        UnaryOperator<String> hello = greeting::hello;
        System.out.println(hello.apply("DevHistory"));
        /* 3. 생성자 참조 */
        //1), 2)의 메소드 레퍼런스는 서로 같아보이지만 실제로는 다른 생성자를 참조한다.

        //1) 매개변수가 없는 생성자
        Supplier<Greeting> newGreeting = Greeting::new;
        Greeting greeting1 = newGreeting.get();
        System.out.println("1) 매개변수가 없는 생성자 : " + greeting1.getName());
        //2) 매개변수가 존재하는 생성자
        Function<String, Greeting> devHistoryGreeting = Greeting::new;
        Greeting devHistory = devHistoryGreeting.apply("DevHistory");
        System.out.println("2) 매개변수가 존재하는 생성자 : " + devHistory.getName());

        /*
            4. 특정 타입의 불특정 다수 인스턴스의 메소드 참조
            == 임의 객체의 인스턴스 메소드 참조
        */
        String[] names = {"history", "java8", "dev"};
        //Comparator: String 값을 입력받아 int 값을 반환. 비교를 위한 용도
        //String 클래스에 정의된 compareToIgnoreCase 메소드를 이용할 수 있다.(내부에서 Comparator 사용)
        //배열의 요소 각각마다 자기 자신과 다음 인자를 비교해가며 정렬 수행
        Arrays.sort(names, String::compareToIgnoreCase);
        System.out.println(Arrays.toString(names));
    }
}

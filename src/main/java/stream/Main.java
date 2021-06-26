package stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args){
        List<String> names = new ArrayList<>();
        names.add("history");
        names.add("dev");
        names.add("apple");
        names.add("goo");

        /*
            종료형 오퍼레이션이 없기 때문에 실행되지 않는다.
            스트림의 정의만 있는 상태.
        */
//        List<String> collect = names.stream()
//                .map((s) -> {
//                    System.out.println(s); //중개 오퍼레이션(map)은 종료 오퍼레이션이 없으면 출력이 되지 않는다. 중개 오퍼레이션은 근본적으로 lazy 하다는 의미가 바로 이런 특징이다.
//                    return s.toUpperCase();
//                });
        System.out.println("===중간 오퍼레이션 수행 시 출력되는 데이터===");
        List<String> collect = names.stream()
                .map((s) -> {
                    System.out.println(s); //출력이 된다. 종료 오퍼레이션(collect)이 존재한다.
                    return s.toUpperCase();
                }).collect(Collectors.toList()); //종료 오퍼레이션이 오면 정의된 스트림이 실행된다.
        System.out.println("===중간 오퍼레이션 수행 후 적용 된 데이터===");
        collect.forEach(System.out::println);

        System.out.println("#######################");
        /*
            스트림이 처리하는 원본 소스는 변경되지 않는다.
        */
        names.forEach(System.out::println);
        System.out.println("#######################");
        /*
            스트림을 사용하면 손쉽게 병렬 처리할 수 있다.
        */
        //기존 forEach를 사용해서 병렬적으로 처리하기에는 쉽지 않다.
        for (String name : names) {
            if(name.startsWith("a")) {
                System.out.println(name.toUpperCase() + " " + Thread.currentThread().getName());
            }
            System.out.println(name + " " + Thread.currentThread().getName());
        }
        System.out.println("#######################");

        List<String> collect1 = names.stream().map((s) -> {
            System.out.println(s + " " + Thread.currentThread().getName());
            return s.toUpperCase();
        }).collect(Collectors.toList());
        collect1.forEach(System.out::println);
        System.out.println("#######################");

        //스트림을 사용하면 JVM이 알아서 병렬적으로 처리해준다.
        //다만 병렬처리를 한다고해서 무조건 빨라지는 것은 아니다. 병럴처리가 무조건 빠르다면 리액티브 스트림이 등장할 이유가 없다.
        //병렬처리도 비용이 든다. 쓰레드를 만들고, 병렬적으로 처리한 것을 수집해야하고, 쓰레드 간에 왔다갔다하는 컨텍스트 스위치의 비용이 한 쓰레드에서 처리하는 비용보다 더 클 수도있다.
        //다만 데이터가 정말 방대하게 큰 경우, 병렬처리는 도움이 된다. 그러나 대부분의 경우에는 병렬처리가 필요없다. 병렬처리는 데이터 소스와 중간 오퍼레이션에서 처리하는 로직에 따라 많이 달라지기 때문에 특정상황에서 좋다고 말할 수 없다.
        //케이스별로 성능을 측정해서 병렬처리를 사용여부를 결정하는 것이 바람직하다.
        List<String> collect2 = names.parallelStream().map((s) -> {
            System.out.println(s + " " + Thread.currentThread().getName());
            return s.toUpperCase();
        }).collect(Collectors.toList());
        collect2.forEach(System.out::println);

    }
}

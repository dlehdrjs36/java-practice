package stream.uses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        List<OnlineClass> springClasses = new ArrayList<>();
        springClasses.add(new OnlineClass(1, "spring boot", true));
        springClasses.add(new OnlineClass(2, "spring data jpa", true));
        springClasses.add(new OnlineClass(3, "spring mvc", false));
        springClasses.add(new OnlineClass(4, "spring core", false));
        springClasses.add(new OnlineClass(5, "rest api development", false));

        System.out.println("spring 으로 시작하는 수업");
        springClasses.stream()
                .filter((c) -> c.getTitle().startsWith("spring"))
                .forEach((c) -> System.out.println("\t" + c.getId()));
        System.out.println("#######################");

        System.out.println("close 되지 않은 수업");
        springClasses.stream()
                .filter(Predicate.not(OnlineClass::isClosed))
                .forEach((c) -> System.out.println("\t" + c.getId()));
        System.out.println("#######################");

        System.out.println("수업 이름만 모아서 스트림 만들기");
        springClasses.stream()
                .map(OnlineClass::getTitle)
                .forEach(System.out::println);
        System.out.println("#######################");

        List<OnlineClass> javaClasses = new ArrayList<>();
        javaClasses.add(new OnlineClass(1, "The Java, Test", true));
        javaClasses.add(new OnlineClass(2, "The Java, Code manipulation", true));
        javaClasses.add(new OnlineClass(3, "The Java, 8 to 11", false));

        List<List<OnlineClass>> devHistoryEvent = new ArrayList<>();
        devHistoryEvent.add(springClasses);
        devHistoryEvent.add(javaClasses);

        System.out.println("두 수업 목록에 들어있는 모든 수업 아이디 출력");
        //리스트를 flatting 시킴. ( 리스트에 들어있는 값을 모두 뽑아냄. 납작하게 만듬 )
        devHistoryEvent.stream()
                .flatMap(Collection::stream) //1. 리스트를 납작하게 만들어 온라인 클래스로만 스트림을 구성
                .forEach(c -> System.out.println(c.getId())); //2. 온라인 클래스에서 수업 아이디 출력
        System.out.println("#######################");

        System.out.println("10부터 1씩 증가하는 무제한 스트림 중에서 앞에 10개 빼고 최대 10개 까지만");
        Stream.iterate(10, i -> i + 1)
                .skip(10) //1. 처음의 10개 (제외)
                .limit(10) //2. 이후 10개 까지 (제한)
                .forEach(System.out::println); // 20~ 29
        System.out.println("#######################");

        System.out.println("자바 수업 중에 Test가 들어있는 수업이 있는지 확인");
        boolean test = javaClasses.stream().anyMatch(c -> c.getTitle().contains("Test"));
        System.out.println(test);
        System.out.println("#######################");

        System.out.println("스프링 수업 중에 제목에 spring이 들어간 제목만 모아서 List로 만들기");
        List<String> spring = springClasses.stream()
                .filter(c -> c.getTitle().contains("spring"))
                .map(OnlineClass::getTitle)
                .collect(Collectors.toList());
        spring.forEach(System.out::println);
        System.out.println("=================");
        List<String> spring1 = springClasses.stream()
                .map(OnlineClass::getTitle)
                .filter(c -> c.contains("spring"))
                .collect(Collectors.toList());
        spring1.forEach(System.out::println);

    }
}

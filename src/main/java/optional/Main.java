package optional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class Main {
    public static void main(String[] args) {
        List<OnlineClass> springClasses = new ArrayList<>();
        springClasses.add(new OnlineClass(1, "spring boot", true));
        springClasses.add(new OnlineClass(2, "spring data jpa", true));
        springClasses.add(new OnlineClass(3, "spring mvc", false));
        springClasses.add(new OnlineClass(4, "spring core", false));
        springClasses.add(new OnlineClass(5, "rest api development", false));

        OnlineClass spring_boot = new OnlineClass(1, "optional boot", true);
        Optional<Progress> progress = spring_boot.getProgress();
        progress.ifPresent((p) -> System.out.println(p.getStudyDuration()));

        /*
            Map의 Key 타입에 Optional을 사용하는 것은 매우 좋지 않은 방법이다.
            Map의 특징 중 하나가 Key 타입은 null일 수 없다는 점인데
            Optional을 사용한다면 Key 타입은 null이 아니라는 Map의 특징을 깨트리는 행위이다.
        */

        /*
            프리미티브 타입을 아래와 같이 일반 Optional로 사용하는 것은 권장하지 않는다.
            내부적으로 프리미티브 타입이 박싱, 언박싱되는 불필요한 단계를 거치게 된다.(성능이 좋지 않다.)
            따라서 프리미티브 타입용 Optional을 사용해서 불필요한 박싱, 언박싱을 방지해야 한다.(권장)
        */
        Optional.of(10); // 프리미티브 타입 Optional 생성 올바른 방법 X
        OptionalInt.of(10); // 프리미티브 타입 Optional 생성 올바른 방법 O
/* =============================================================================================== */
        //Optional 활용
        Optional<OnlineClass> optional = springClasses.stream()
                .filter(oc -> oc.getTitle().startsWith("spring"))
                .findFirst();

        boolean present = optional.isPresent();
        boolean empty = optional.isEmpty(); // 자바 11부터 제공. present가 비어있으면 true. isPresent()에 not 연산 한것과 같다.
        System.out.println(!present);
        System.out.println(empty);
        System.out.println("#######################");
        /*
            아래의 코드는 optional이 비어있는 경우 런타임 에러 발생
            OnlineClass onlineClass = optional.get();
            System.out.println(onlineClass.getTitle());
            위와 같은 런타임 에러를 방지하기 위하여
            optional이 제공하는 다양한 메소드를 이용해서 가능하다면 get으로 꺼내지말고 다른방식으로 처리하자
         */
        //1. Optional 값을 가지고 무언가를 해야하는 경우. ifPresent 사용.
        optional.ifPresent(oc -> System.out.println(oc.getTitle()));
        System.out.println("#######################");
        //2. Optional 값을 가져와서 나중에도 참조를 하는 등의 지속적인 작업을 해야하는 경우.
        //적합한 상황 예
        //상수로 이미 만들어진 인스턴스를 사용하는 경우(optional이 존재하든 말든 무조건 수행) : orElse
        //동적으로 실제로 인스턴스를 사용하는 경우(optional이 없는 경우에만 수행) : orElseGet
        //만들어 줄 수 없는 상황(기본적으로는 NoSuchElementException 발생, 원하는 에러를 Supplier로 제공해주면 된다.) : orElseThrow

        //있으면 꺼내오고 없을 때는 새로운 클래스를 만들어라.
        OnlineClass onlineClass = optional.orElseGet(Main::createNewClasses); //함수형 인터페이스의 구현체이기 떄문에 lazy하게 다룰 수 있다.
        System.out.println(onlineClass.getTitle());
        System.out.println("#######################");

        //값이 반드시 있어야 하는 경우
        Optional<OnlineClass> optional2 = springClasses.stream()
                .filter(oc -> oc.getTitle().startsWith("jpa"))
                .findFirst();
        OnlineClass onlineClass2 = optional2.orElseThrow(IllegalStateException::new);
        System.out.println(onlineClass2.getTitle());
        System.out.println("#######################");

        //optional의 filter 값을 걸러내는 옵션. 값이 있다는 가정하에 동작. 값이 없다면 비어있는 Optional 객체를 반환한다.
        Optional<OnlineClass> onlineClass1 = optional.filter(OnlineClass::isClosed);
        System.out.println(onlineClass1.isEmpty());
        System.out.println(onlineClass1.isPresent());
        System.out.println("#######################");

        //optional의 map 해당 값을 담고있는 Optional 객체 반환. 값이 없다면 비어있는 Optional 객체가 반환된다.
        Optional<Integer> integer = optional.map(OnlineClass::getId);
        System.out.println(integer.isPresent());

        //optional의 flatMap 맵으로 반환하는 타입이 이미 Optional 객체일때 두 번 감싸지게 되는데 이를 한겹까서 반환해준다.
        //optional의 flatMap은 스트림의 flatMap과는 다르다. 스트림의 flatMap은 인풋 1개 아웃풋 여러개가 발생한다.
        Optional<Optional<Progress>> progress1 = optional.map(OnlineClass::getProgress);
        Optional<Progress> progress2 = progress1.orElseThrow(); //여기서는 값이 비어있으면 에러 발생하지않고 결과가 빈 Optional 객체를 반환한다. 아래의 형태와 유사하다고 볼 수 있다.
        //Optional<Progress> progress3 = progress1.orElse(Optional.empty());

        /* flatMap을 사용한 아래의 결과와 위의 결과는 같다. */
        Optional<Progress> progress4 = optional.flatMap(OnlineClass::getProgress);
    }

    private static OnlineClass createNewClasses() {
        System.out.println("creating new online class");
        return new OnlineClass(10, "New class", false);
    }
}

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

        OnlineClass spring_boot = new OnlineClass(1, "spring boot", true);
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

    }
}

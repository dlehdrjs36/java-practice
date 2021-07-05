package java8annotation;

import java.util.Arrays;
import java.util.List;

@Chicken
@Chicken("양념")
public class Main {
    public static void main(@Chicken String[] args) throws @Chicken RuntimeException, NoSuchMethodException, NoSuchFieldException {
        List<@Chicken String> names = Arrays.asList("dev");

        /* 애노테이션 읽기 */
        //1. 클래스에서 해당 애노테이션 타입으로 바로 읽어오는 방법
        Chicken[] chickens = Main.class.getDeclaredAnnotationsByType(Chicken.class);
        Arrays.stream(chickens).forEach(c -> System.out.println(c.value()));
        System.out.println("###############");
        //2. 컨테이너 애노테이션 타입으로 읽는 방법
        ChickenContainer chickenContainer = Main.class.getAnnotation(ChickenContainer.class);
        Arrays.stream(chickenContainer.value()).forEach(c -> System.out.println(c.value()));
    }

    static class FeelsLikeChicken<@Chicken T> {
        /*
            <C> : 타입 파라미터
            C   : 타입
         */
        public static <@Chicken C> void print(@Chicken C c) {
            System.out.println(c);
        }
    }
}

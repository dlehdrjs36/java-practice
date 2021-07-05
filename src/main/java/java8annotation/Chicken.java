package java8annotation;

import java.lang.annotation.*;

/*
    @Target(ElementType.TYPE_PARAMETER)
     - 제네릭을 사용할 때 타입 파라미터, 타입 변수앞에 애노테이션을 사용할 수 있다.
    @Target(ElementType.TYPE_USE)
     - 타입 파라미터를 포함해서 타입 선언부에서 애노테이션을 사용할 수 있다.
    @Repeatable(컨테이너 성격의 애노테이션 설정)
     - 여러 개의 애노테이션을 가지고 있을 컨테이너 애노테이션을 설정해주면 반복해서 애노테이션 사용이 가능하다.
     - 컨테이너의 애노테이션 설정(Retention, Target)은 반드시 자기자신이 감싸는 애노테이션의 범위보다 같거나 넓어야 한다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Repeatable(ChickenContainer.class)
public @interface Chicken {
    String value() default "후라이드";
}

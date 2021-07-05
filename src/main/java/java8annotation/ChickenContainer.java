package java8annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
    컨테이너 애노테이션은 자기자신이 감쌀 애노테이션 배열을 가지고 있어야 한다.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
public @interface ChickenContainer {
    Chicken[] value();
}

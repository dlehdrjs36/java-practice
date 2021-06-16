package me.devhistory;

/**
 * 인터페이스에 추상 메소드가 1개면 함수형 인터페이스.
 * 추상 메소드가 1개라면 다른 메소드가 있어도 상관없다.
 * @FunctionalInterface :함수형 인터페이스인지 확인해주는 애노테이션
 */
@FunctionalInterface
public interface RunSomething {
    void doIt();

    static void printName() {
        System.out.println("DevHistory");
    }

    default void printAge() {
        System.out.println("299");
    }
}

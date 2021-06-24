package java8interface;

public interface Bar extends Goo{
    /*
        기존 Goo가 기본 메소드로 제공하던 void printNameUpperCase()를
        Bar 인터페이스에서는 더 이상 기본 메소드로 제공하고 싶지 않을 때는 추상 메소드로 다시 선언 해준다.
    */
    void printNameUpperCase();
}

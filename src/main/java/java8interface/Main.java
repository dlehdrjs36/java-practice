package java8interface;

public class Main {
    public static void main(String[] args){
        Goo goo = new DefaultGoo("Goo");

        /*
            default 키워드 메소드 & 추상 메소드 : 인스턴스 사용
        */
        goo.printName(); //인터페이스의 추상 메소드 구현
        goo.printNameUpperCase(); //기본 메소드의 충돌이 발생하여 인터페이스의 기본 메소드 재정의
        goo.printNameLowerCase(); //인터페이스의 기본 메소드

        /*
            static 키워드 메소드 : 해당 인터페이스 타입에서 사용
        */
        Goo.printAnything(); //인터페이스의 헬퍼, 유틸리티 메소드
    }
}

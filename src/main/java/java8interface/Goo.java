package java8interface;

public interface Goo {

    void printName();
    String getName();

    /*
        나중에 해당 인터페이스로 공통적인 기능을 제공해주었으면 좋겠다고하여 추가한 추상 메소드 : printNameUpperCase()
    */
    //void printNameUpperCase(); 그러나 해당 형태로 추가하면 해당 인터페이스를 구현한 모든 클래스에서 컴파일 에러 발생

    /*
        java8에서 등장한 default 키워드를 이용하여 메소드를 추가하면 해당 인터페이스를 구현하는 클래스에서 메소드를 구현하지 않아도 사용 가능하다.
        하지만 이 기능이 항상 모든 상황에 동작한다는 보장이 없다. 현재는 getName()에서 null을 리턴하면 런타임 에러가 발생할 수 있다.
        문서화(@implSpec 자바독 태그)를 통해 이 기본 구현체가 어떤 일을하는지 명시해야한다.

        인터페이스의 기본 메소드(default 메소드)의 제약사항
        - Object가 제공하는 메소드(equals, hasCode 등)들은 인터페이스의 기본 메소드로 제공할 수 없다.
          단, 추상 메소드로 선언하는 것은 상관이 없다.
    */
    /**
     * @implSpec 이 구현체는 getName()으로 가져온 문자열을 소문자로 바꿔 출력한다.
     */
    default void printNameLowerCase(){
        System.out.println(getName().toLowerCase());
    }
    
    /**
     * @implSpec 이 구현체는 getName()으로 가져온 문자열을 대문자로 바꿔 출력한다.
     */
    default void printNameUpperCase(){
        System.out.println(getName().toUpperCase());
    }

    /*
        Object가 제공하는 기본 메소드를 인터페이스의 기본 메소드로 제공하려했기에 컴파일 에러 발생
    */
    //default String toString(){ }

    /*
        Object가 제공하는 기본 메소드이지만 추상 메소드로 선언만 하는 것은 가능하다.
        다만 이는 모든 인스턴스가 오브젝트에서 구현된 것을 상속받고 있다.
        이것을 사용하는 경우는 인터페이스에서 가지는 특별한 제약사항이 있을때 사용한다.
        예를 들어, 이 인터페이스를 구현하는 인스턴스의 toString은 일반적인 Object의 toString과는 다르다.
    */
    String toString();

    /*
        static 키워드를 이용하여 해당 타입과 관련하여 헬퍼 또는 유틸리티 메소드를 제공할 수 있다.
    */
    static void printAnything() {
        System.out.println("Goo");
    }
}

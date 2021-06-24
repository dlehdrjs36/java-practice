package java8interface;

/*
    만약, 동일한 기본 메소드를 제공하는 인터페이스를 여러 개 구현한 경우 컴파일 에러 발생.
    어떤 인터페이스의 기본 메소드를 사용해야 될지 모르기 때문이다.
    이렇게 기본 메소드가 충돌하는경우는 직접 재정의(Override)해서 사용해야한다.
*/
public class DefaultGoo implements Goo, Hoo {

    String name;

    public DefaultGoo(String name) {
        this.name = name;
    }

    @Override
    public void printName() {
        System.out.println(this.name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    /*
        인터페이스에서 기본으로 제공하는 메소드가 문제가 있을 경우
        재정의(Override)하여 사용할 수 있다.
    */
    @Override
    public void printNameUpperCase() {
        System.out.println(this.name.toUpperCase());
    }
}

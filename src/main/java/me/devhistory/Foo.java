package me.devhistory;

public class Foo {

    public static void main(String[] args) {
        //자바에서 람다는 특수한 형태의 오브젝트로 볼 수 있다.
        //함수형 인터페이스를 인라인으로 구현한 형태

        // 익명 내부 클래스 (anonymous inner class)
        RunSomething runSomething = new RunSomething() {
            @Override
            public void doIt() {
                System.out.println("Hello");
            }
        };
        runSomething.doIt();

        // 함수형 인터페이스는 구현할 때, 람다 형태로 표현 가능
        RunSomething runSomething2 = () -> System.out.println("Hello");
        runSomething2.doIt();

        // 람다식 사용 시, 여러 줄일 경우 중괄호 사용
        RunSomething runSomething3 = () -> {
            System.out.println("Hello1");
            System.out.println("Hello2");
        };
        runSomething3.doIt();

        //매개변수를 가지는 함수형 인터페이스
        RunSomething2 runSomething4 = number -> number + 10;
        System.out.println(runSomething4.doIt(1));
        System.out.println(runSomething4.doIt(1));
        System.out.println(runSomething4.doIt(1));
        System.out.println(runSomething4.doIt(2));
        System.out.println(runSomething4.doIt(2));
        System.out.println(runSomething4.doIt(2));

        //같은 값이 입력되었는데 반환 값이 바뀌는 경우(함수의 밖에있는 값을 이용해서 내부에서 연산하는 경우)
        //아래와 같은 형태는 순수 함수(Pure function)이라고 볼 수 없고 상태값을 가지고 있다(의존한다)라고 볼 수 있다.
        RunSomething2 runSomething5 = new RunSomething2() {
            int baseNumber = 10;
            @Override
            public int doIt(int number) {
                baseNumber++; //외부 값 변경
                return number + baseNumber;
            }
        };

        //람다에서 외부 변수를 참조해서 내부에서 사용할 수 있다.
        //하지만 이 경우는 외부 변수가 상수이거나, 사실상 상수(effectively final)로 보기때문에 해당 값 변경시 내부에서 사용 불가능하다.
        int baseNumber2 = 100;
        RunSomething2 runSomething6 = number -> number + baseNumber2;
        //baseNumber2++; 해당 명령문이 수행되면 사실상 상수가 아니게 된다. 컴파일 에러 발생

    }
}

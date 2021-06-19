package functionalinterface.consumer;

import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        //Consumer<T> : 받아서 아무것도 리턴하지 않음, 내부에서 받은 인자를 소비만 한다.
        Consumer<Integer> printT = (i) -> System.out.println(i);
        printT.accept(10);
    }
}

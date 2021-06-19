package functionalinterface.supplier;

import java.util.function.Supplier;

public class Main {
    public static void main(String[] args){
        //인자를 받지 않고 해당하는 타입의 인자를 내부에서 생성하여 반환하기만 한다.
        Supplier<Integer> get10 = () -> 10;
        System.out.println(get10);
        System.out.println(get10.get());
    }
}

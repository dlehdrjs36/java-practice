package java8date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        /*
            기존의 클래스(Date, Calendar ...)는 객체의 상태를 바꿀수 있기(mutable) 때문에 thread safe하지 않다.
        */
        Date date = new Date(); // 기계용 시간
        long time = date.getTime();
        System.out.println(date);
        System.out.println(time);

        Thread.sleep(1000 * 3); //3초
        Date after3Seconds = new Date();
        System.out.println(after3Seconds);
        after3Seconds.setTime(time);
        System.out.println(after3Seconds);
        System.out.println("#######################");
        /*
            버그 발생 여지가 있다. 1월은 0부터 시작하기 때문에 실수할 여지가 있다.
            또한 int형의 값은 아무 값이나 들어올 수 있기 때문에 type safe하지 않다. (달은 1월부터 12월인데 이것을 초과하거나 음수 값이 들어올 수 있다)
            type safe해질려면 int로 받지말고 타입으로 값을 받아야한다. (달은 Month라는 타입으로 받으면 type safe 해진다)
        */
        //Calendar birthDay = new GregorianCalendar(1993, 6, 24);
        Calendar birthDay = new GregorianCalendar(1993, Calendar.JUNE, 24);
        System.out.println(birthDay.getTime());
        birthDay.add(Calendar.DAY_OF_YEAR, 1);
        System.out.println(birthDay.getTime());

    }
}

package java8date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        /*
            기존의 클래스(Date, Calendar ...)는 객체의 상태를 바꿀수 있기(mutable) 때문에 thread safe하지 않다.
        */
        Date date = new Date(); // 기계용 시간
        long time = date.getTime();
        System.out.println(date);
        System.out.println(time);

        Thread.sleep(1000 * 2); //3초
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
        System.out.println("#######################");

/* ======================================================================================================= */
        /*
            Date-Time는 서로 변환 가능하다.

            - Instant -> ZonedDateTime
                instant.atZone(ZoneId.of("Asia/Seoul"))

            - ZonedDateTime -> LocalDateTime
                zonedDateTime1.toLocalDateTime();

            - ZonedDateTime -> Instant
                zonedDateTime1.toInstant();
                        .
                        .
                        .
                        .
                        
            파라미터에서 TemporalUnit 타입은 ChronoUnit 으로 사용한다는 것 기억하기
        */

        /* 기계용 시간을 표현 */
        Instant instant = Instant.now(); //현재 UTC(GMT)를 리턴한다.
        System.out.println(instant); // 기준시 UTC, GMT
        System.out.println(instant.atZone(ZoneId.of("UTC")));
        System.out.println(instant.atZone(ZoneId.of("GMT")));
        System.out.println("#######################");

        //기계용 시간을 인류용 시간으로 변환 가능
        ZoneId zone = ZoneId.systemDefault(); //현재 지역을 리턴한다.
        System.out.println(zone);
        ZonedDateTime zonedDateTime = instant.atZone(zone); //기준시 UTC(GMT)를 현재 지역의 일시로 변환한다.
        System.out.println(zonedDateTime);
        System.out.println("#######################");

        /* 기계용 시간을 비교하는 방법 */
        Instant InstantNow = Instant.now();
        Instant plus = InstantNow.plus(10, ChronoUnit.SECONDS); //새로운 인스턴스 생성, 기존 인스턴스 변경하지 않음
        Duration between = Duration.between(InstantNow, plus);
        System.out.println(between.getSeconds()); //InstantNow와 plus의 기간
        System.out.println("#######################");

        /* 인류용 일시 표현 */
        LocalDateTime LocalDateTimeNow = LocalDateTime.now(); //현재 시스템 ZoneId를 참고해서 일시를 반환한다.
        DateTimeFormatter MMddyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // 포맷 형태 직접 작성
        DateTimeFormatter isoLocalDate = DateTimeFormatter.ISO_LOCAL_DATE; // 미리 정의해둔 포맷 사용

        System.out.println(LocalDateTimeNow);
        System.out.println(LocalDateTimeNow.format(MMddyyyy)); //LocalDateTimeNow 포매팅
        System.out.println(LocalDateTimeNow.format(isoLocalDate));//LocalDateTimeNow 포매팅
        System.out.println("#######################");
        LocalDate parse = LocalDate.parse("07/15/1982", MMddyyyy); //파싱
        System.out.println(parse);
        System.out.println("#######################");
        LocalDateTime birthDay2 = LocalDateTime.of(1993, Month.JUNE, 24, 0, 0, 0); //현재 시스템 일시 생성
        System.out.println(birthDay2);
        System.out.println("#######################");

        ZonedDateTime nowInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul")); //특정 Zone의 일시를 반환한다.
        System.out.println(nowInKorea);
        System.out.println("#######################");

        ZonedDateTime zonedDateTime1 = instant.atZone(ZoneId.of("Asia/Seoul")); //기계 시간을 인류용 일시로 변환
        System.out.println(zonedDateTime1);
        System.out.println("#######################");

        /* 인류용 기간을 비교하는 방법 */
        LocalDate today = LocalDate.now();
        LocalDate thisYearBirthday = LocalDate.of(2021, Month.JUNE, 24);

        Period period = Period.between(today, thisYearBirthday); //today와 thisYearBirthday 기간
        System.out.println(period.getDays());

        Period until = today.until(thisYearBirthday); //today부터 thisYearBirthday 까지
        System.out.println(period.get(ChronoUnit.DAYS));

        /*
            레거시 API 지원
            서로 변환이 가능하다.
        */
        Date date2 = new Date();
        Instant instant2 = date2.toInstant();
        Date newDate = Date.from(instant2);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        //LocalDateTime localDateTime = gregorianCalendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        ZonedDateTime dateTime = gregorianCalendar.toInstant().atZone(ZoneId.systemDefault());
        GregorianCalendar from = GregorianCalendar.from(dateTime);

        ZoneId zoneId = TimeZone.getTimeZone("PST").toZoneId();
        TimeZone timeZone = TimeZone.getTimeZone(zoneId);
    }}
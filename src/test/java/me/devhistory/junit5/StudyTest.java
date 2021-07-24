package me.devhistory.junit5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assumptions.*;
import static org.junit.jupiter.api.Assertions.*;

class StudyTest {
    @Test
    @Tag("fast") //특정 기준으로 테스트를 분류할 수 있다.
    @DisplayName("스터디 만들기 fast")
    @DisabledOnOs({OS.MAC, OS.WINDOWS}) // MAC, WINDOWS 운영체제에서는 테스트 수행안하도록 설정
    @EnabledOnJre(JRE.OTHER) //JAVA_8, JAVA_9, JAVA_10, JAVA_11, JAVA_12, JAVA_13, JAVA_14 가 아닌경우에만 실행
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "history")
    void create_new_study() {
        /*
         * assertThrows
         * 어떠한 코드 실행 시 어떠한 타입의 예외가 발생하는지 테스트할 수 있다.
         * 발생한 예외를 받아서 메시지가 내가 기대하는 메시지와 같은지도 테스트할 수 있다.
         */
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        String message = exception.getMessage();
        assertEquals("limit은 0보다 커야 한다.", message);

        Study study = new Study(10);
        /*
         * assertAll
         * 모든 테스트를 한번에 실행할 수 있다.
         * - 깨진 테스트를 한번에 확인 가능
         */
        assertAll(
            /*
             * assertTimeout
             * 실행하는 코드가 특정 시간이내에 반드시 완료가 되어야하는 경우를 테스트할 수 있다.
             * 단점은 코드 블록이 실행이 끝날 때까지 기다리고 시간을 비교한다.
             * - 1초안에 실행되는것을 테스트했는데 테스트하는 시간이 3초가 넘었다.
             */
            () -> assertTimeout(Duration.ofMillis(100), () -> {
                new Study(10);
                Thread.sleep(70);
            }),

            /*
             * assertTimeoutPreemptively
             * 위와 마찬가지로 실행하는 코드가 특정 시간이내에 반드시 완료가 되어야하는 경우를 테스트할 수 있다.
             * 차이점은 1초안에 실행되는것을 테스트하는 경우, 코드 블록이 1초가 넘으면 즉각적으로 테스트 종료(실패).
             *
             * 주의점
             * - 코드 블록을 별도의 쓰레드에서 실행하기 때문에 코드 블록 내부에 ThreadLocal을 사용하는 코드가 있다면 예상치 못한 결과가 발생할 수 있다.
             * 예) 스프링 트랜잭션 처리는 ThreadLocal을 기본 전략으로 사용한다. ThreadLocal은 다른 쓰레드간에 공유가 안된다.
             *     스프링 트랜잭션 설정이 테스트에서 제대로 동작하지 않을 수도 있다.(롤백되지 않고 DB에 반영) 트랜잭션 설정을 가지고 있는 쓰레드가 아닌 별개의 쓰레드로 실행하기 때문이다.
             * - 쓰레드와 전혀 관련없는 코드를 실행한다면 사용해도 괜찮다.
             *
             * TODO ThreadLocal
             */
            () -> assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
                new Study(10);
                Thread.sleep(70);
            }),

            /*
             * assertNotNull
             * 값이 null이 아닌지 확인
             */
            () -> assertNotNull(study),

            /*
             * assertEquals(기대하는값, 실제 값, 실패시 표시될 메시지)
             * 일반 String이 아닌 람다 식으로 메시지를 전달하는 이유
             * - 복잡한 메시지를 전달해야 할 때, 많은 문자열 연산이 존재할 수 있다. 예) "스터디를 처음 만들면 상태값이 "+ StudyStatus.DRAFT +" 이다."
             * - 람다식으로 만들어 주면 문자열 연산을 최대한 필요한 시점에 하게 된다. (테스트를 실패했을 경우에만 문자열 연산을 수행한다.)
             * - 일반 String으로 작성하게 된다면 테스트가 성공하든 실패하든 상관없이 문자열 연산을 수행한다. (필요하지 않은 시점에도 문자열 연산을 수행한다.)
             * - 따라서 문자열 연산의 비용이 많이 크다고 생각하면 람다 식으로 작성하는것이 좋다.
             */
            () ->  assertEquals(StudyStatus.DRAFT, study.getStatus(),
                    () -> "스터디를 처음 만들면 상태값이 "+ StudyStatus.DRAFT +" 이다."), //테스트 실패 시 메시지도 전달할 수 있다.

            /*
             * assertTrue
             * 실제 값이 기대한 값과 같은지 확인
             */
            () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다.")
        );
    }

    //@Test
    //@Tag("slow") //특정 기준으로 테스트를 분류할 수 있다. 예) 로컬에서 수행하기에는 오래걸리는 테스트들은 CI 환경에서 동작하도록 설정 가능하다.
    @SlowTest //태그가 slow로 설정된 커스텀 애노테이션, 문자열은 type-safe 하지않다. 태그 이름이 오타가 나는 경우 원하는대로 동작하지 않는다. 오타를 줄이고 테스트를 원하는대로 동작하도록하기 위해서 커스텀 애노테이션을 사용하는 것이 좋다.
    @DisplayName("스터디 만들기 slow")
    @EnabledOnOs({OS.MAC, OS.LINUX, OS.WINDOWS}) //MAC, LINUX, WINDOWS 운영체제에서만 테스트 수행되도록 설정
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9,JRE.JAVA_10, JRE.JAVA_11})
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL") //환경변수 값이 LOCAL 경우에만 테스트 수행되도록 설정
    void create_new_study_again() {
        /*
         * assumeTrue, assumingThat
         * 특정 조건을 만족하면 테스트가 수행되도록 할 수 있다.
         * 예) 시스템 환경변수에 따라서 동작하는 테스트 조작 가능
         *
         * assumeTrue : 조건이 다르면 테스트가 해당 위치에서 종료된다.
         * assumingThat : 조건이 다르면 해당 테스트는 수행되지않고 아래의 테스트는 계속 수행된다.
         */
        String test_env = System.getenv("TEST_ENV");
        System.out.println(test_env);

        //시스템 환경변수가 LOCAL 경우에만 테스트 수행
        assumingThat("LOCAL".equalsIgnoreCase(test_env), ()->{
            System.out.println("local");
            Study actual = new Study(100);
            assertEquals(actual.getLimit(), 100);
        });

        //시스템 환경변수가 History 경우에만 테스트 수행. 환경변수가 다르면 여기서 테스트가 멈춘다.
        //assumeTrue("History".equalsIgnoreCase(test_env));
        assumeTrue("LOCAL".equalsIgnoreCase(test_env));

        //환경변수가 다르면 현재 테스트는 수행되지않고 아래의 테스트가 계속 수행된다.
        assumingThat("History".equalsIgnoreCase(test_env), ()->{
            System.out.println("history");
            Study actual = new Study(10);
            assertEquals(actual.getLimit(), 10);
        });

    }

}
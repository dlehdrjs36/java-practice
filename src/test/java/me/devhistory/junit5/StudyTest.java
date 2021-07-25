package me.devhistory.junit5;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assumptions.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudyTest {
    @Order(2)
    @Test
    @Tag("fast") //특정 기준으로 테스트를 분류할 수 있다.
    @DisplayName("스터디 만들기 fast")
    //@DisabledOnOs({OS.MAC, OS.WINDOWS}) // MAC, WINDOWS 운영체제에서는 테스트 수행안하도록 설정
    //@EnabledOnJre(JRE.OTHER) //JAVA_8, JAVA_9, JAVA_10, JAVA_11, JAVA_12, JAVA_13, JAVA_14 가 아닌경우에만 실행
    //@EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "history")
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
    @Order(1)
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

    @Order(3)
    @DisplayName("스터디 만들기(RepeatFastTest)")
    @RepeatFastTest //== @RepeatedTest(value = 10, name = "{displayName} 반복, {currentRepetition}/{totalRepetitions}") //테스트를 반복할 횟수, 반복하는 테스트의 이름
    void repeatTest(RepetitionInfo repetitionInfo){
        System.out.println("test" + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions()); //현재 몇 번째 반복하고 있는것인지, 총 몇번 반복해야 하는지 등을 알 수 있다.
    }

    @Order(4)
    @Tag("fast")
    @DisplayName("스터디 만들기(ParameterizedTest)")
    @ParameterizedTest(name = "{index}. {displayName}, message={0}")
    @ValueSource(strings = {"날씨가", "많이", "더워지고", "있네요."})
    @NullAndEmptySource //@EmptySource + @NullSource
    //@EmptySource 테스트 인자로 비어있는 문자열을 추가
    //@NullSource 테스트 인자로 null 추가
    void parameterizedTest(String message){
        System.out.println(message);

    }

    @Order(5)
    @Tag("fast")
    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index}. {displayName}, message={0}")
    @ValueSource(ints = {10, 20, 30})
    @NullSource
    void parameterizedTest2(Integer limit){
        System.out.println(limit);
    }

    /*
     * ValueSource를 Study 타입으로 받을 수도 있다.(인자 1개)
     */
    @Order(6)
    @Tag("fast")
    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index}. {displayName}, message={0}")
    @ValueSource(ints = {10, 20, 30})
    void parameterizedTest3(@ConvertWith(StudyConverter.class) Study study){
        System.out.println(study.getLimit());
    }
    //SimpleArgumentConverter 상속 받은 구현체를 이용하여 파라미터를 특정 타입으로 변환 가능
    //SimpleArgumentConverter는 하나의 인자에만 적용할 수 있다.
    static class StudyConverter extends SimpleArgumentConverter{
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertEquals(Study.class, targetType, "Can only convert to Study");
            return new Study((Integer.parseInt(source.toString())));
        }
    }

    /*
     * CsvSource를 Study 타입으로 받을 수도 있다.(인자 2개)
     */
    //1. 두 개의 인자를 받아서 Study 생성
    @Order(7)
    @Tag("fast")
    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index}. {displayName}, message={0}, {1}")
    @CsvSource({"10, '자바 스터디'", "20, 스프링"})
    void parameterizedTest4(Integer limit, String name){
        Study study = new Study(limit, name);
        System.out.println(study);
    }

    //2. ArgumentsAccessor를 이용해서 Study 생성
    @Order(8)
    @Tag("fast")
    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index}. {displayName}, message={0}, {1}")
    @CsvSource({"30, 'HTTP'", "40, 스프링 부트"})
    void parameterizedTest5(ArgumentsAccessor argumentsAccessor){
        Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        System.out.println(study);
    }

    //3. 커스텀 Accessor를 이용해서 Study 생성
    @Order(9)
    @Tag("fast")
    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index}. {displayName}, message={0}, {1}")
    @CsvSource({"40, 'JPA'", "50, JUnit"})
    void parameterizedTest6(@AggregateWith(StudyAggregator.class) Study study){
        System.out.println(study);
    }
    //커스텀 Accessor
    static class StudyAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            return new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        }
    }

    int value = 0;

    /*
     * JUnit의 기본전략은 테스트 메소드마다 인스턴스를 새로 만들기 때문에 value 값은 어떤 테스트에서도 0으로 표시된다.
     * - 테스트 메소드마다 실행하는 인스턴스 해시 값이 모두 다르다.
     * 테스트 메소드마다 인스턴스를 새로 만드는 이유는 테스트 메소드를 독립적으로 실행하여 예싱치 못한 부작용을 방지하기 위함
     */
    @Order(10)
    @Tag("fast")
    @Test
    void testInstance() {
        System.out.println(this + ":" + value++); // 0
    }

    @Order(11)
    @Tag("fast")
    @Test
    void testInstance2() {
        System.out.println(this + ":" + value++); // 0
    }

    @Order(12)
    @Tag("fast")
    @Test
    void testInstance3() {
        System.out.println(this + ":" + value++); // 0
    }

    /*
     * 테스트 인스턴스 전략을 클래스 마다 하나만 생성(@TestInstance(TestInstance.Lifecycle.PER_CLASS))하도록 변경하면 BeforeAll, AfterAll이 static일 필요는 없다.
     *  - 테스트 인스턴스를 하나만 만들기 때문에 해당하는 인스턴스도 하나만 있으면 된다.
     *  - 그러나 테스트 인스턴스를 테스트 메소드별로 만든다면 여러 테스트 메소드에서 static하게 사용하기 위해서 static 선언이 필요하다.
     * 테스트 인스턴스가 하나이기 때문에 테스트별 인스턴스 해시 값이 일치하고 위에서 선언한 value 값도 테스트 메소드별로 변경이 된다.
     */
    @BeforeAll
    void beforeAll() {
        System.out.println("before all");
    }

    @AfterAll
    void afterAll() {
        System.out.println("after all");
    }
}
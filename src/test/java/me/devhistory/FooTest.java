package me.devhistory;


import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//junit 5 부터는 테스트 클래스, 테스트 메소드, 라이프사이클 메소드가 public일 필요가 없다. private 사용 불가능.
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) //Underscore를 빈 공백으로 치환
class FooTest {
    @Test
    void create_new_foo() {
        Foo foo = new Foo();
        System.out.println("create");
        assertNotNull(foo);
    }

    @Test
    @Disabled //해당 테스트를 실행하지 않겠다고 설정하는 애노테이션
    void create_new_foo_again() {
        System.out.println("create1");
    }

    @Test
    void create2() {
        System.out.println("create2");
    }

    @Test
    @DisplayName("\uD83D\uDC7E foo 만들기 \uD83D\uDC7E")
    void create3() {
        System.out.println("create3");
    }

    /**
     * 모든 테스트 실행전 단 한번 호출된다.
     * static 메소드로 구현해야한다.
     * return type void
     */
    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll");
    }

    /**
     * 모든 테스트 실행된 이후 단 한번 호출된다.
     * static 메소드로 구현해야한다.
     * return type void
     */
    @AfterAll
    static void afterAll() {
        System.out.println("afterAll");
    }

    /**
     * 모든 테스트를 실행할 때,
     * 각각의 테스트를 실행하기 이전에 한 번씩 호출
     */
    @BeforeEach
    void beforeEach() {
        System.out.println("beforeEach");
    }

    /**
     * 모든 테스트를 실행할 때,
     * 각각의 테스트를 실행한 이후에 한 번씩 호출
     */
    @AfterEach
    void afterEach() {
        System.out.println("afterEach");
        System.out.println();
    }

}
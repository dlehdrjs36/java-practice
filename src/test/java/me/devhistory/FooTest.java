package me.devhistory;

import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;
//junit 5 부터는 클래스가 public일 필요가 없다.
class FooTest {

    @Test
    void create() {
        Foo foo = new Foo();
        assertNotNull(foo);
    }

    @Test
    void create1() {
        System.out.println("create1");
    }

    /**
     * 모든 테스트 실행전 단 한번 호출된다.
     * static 메소드를 사용해서 구현해야한다.
     * private X
     * return type X
     */
    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll");
    }
    /**
     * 모든 테스트 실행된 이후 단 한번 호출된다.
     * static 메소드를 사용해서 구현해야한다.
     * private X
     * return type X
     */
    @AfterAll
    static void afterAll() {
        System.out.println("afterAll");
    }

    /**
     * 모든 테스트를 실행할 떄,
     * 각각의 테스트를 실행하기 이전에 한 번씩 호출
     */
    @BeforeEach
    void beforeEach() {
        System.out.println("beforeEach");
    }
    /**
     * 모든 테스트를 실행할 떄,
     * 각각의 테스트를 실행하기 이후에 한 번씩 호출
     */
    @AfterEach
    void afterEach() {
        System.out.println("afterEach");
    }

}
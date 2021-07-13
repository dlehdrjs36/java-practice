package me.devhistory;

import org.junit.Test;

import static org.junit.Assert.*;

public class FooTest {

    @Test
    public void create() {
        Foo foo = new Foo();
        assertNotNull(foo);
    }
}
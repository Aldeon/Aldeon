package org.aldeon.jetty.json;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConcreteJsonParserTest {

    @Test
    public void shouldConvertObjectToString() {

        ConcreteJsonParser parser = new ConcreteJsonParser();

        TestClass obj = new TestClass();
        obj.foo = 7;
        obj.bar = "asd";

        String json = parser.toJson(obj);

        assertEquals("{\"foo\":7,\"bar\":\"asd\"}", json);
    }

    @Test
    public void shouldConvertStringIntoObjectOfKnownClass() {

        ConcreteJsonParser parser = new ConcreteJsonParser();

        String json = "{\"foo\":5,\"bar\":\"qwerty\"}";

        TestClass obj = parser.fromJson(json, TestClass.class);

        assertEquals(5, obj.foo);
        assertEquals("qwerty", obj.bar);
    }

    public class TestClass{
        public int foo;
        public String bar;

    }
}

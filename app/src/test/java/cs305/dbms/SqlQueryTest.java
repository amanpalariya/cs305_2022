package cs305.dbms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class SqlQueryTest {
    @Test
    void integerParam() throws IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        String rawQueryString = "REPLACE ${value} WITH ACTUAL VALUE";
        HashMap<Integer, String> map = new HashMap<>();
        map.put(12, "REPLACE 12 WITH ACTUAL VALUE");
        map.put(0, "REPLACE 0 WITH ACTUAL VALUE");
        map.put(-5, "REPLACE -5 WITH ACTUAL VALUE");

        SqlQuery<Integer> query = new SqlQuery<>(rawQueryString);
        for (Integer param : map.keySet()) {
            assertEquals(query.getQuery(param), map.get(param));
        }
    }

    @Test
    void floatParam() throws IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        String rawQueryString = "REPLACE ${value} WITH ACTUAL VALUE";
        HashMap<Float, String> map = new HashMap<>();
        map.put(1.2f, "REPLACE 1.2 WITH ACTUAL VALUE");
        map.put(0.0f, "REPLACE 0.0 WITH ACTUAL VALUE");
        map.put(-5.9f, "REPLACE -5.9 WITH ACTUAL VALUE");

        SqlQuery<Float> query = new SqlQuery<>(rawQueryString);
        for (Float param : map.keySet()) {
            assertEquals(query.getQuery(param), map.get(param));
        }
    }

    @Test
    void doubleParam() throws IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        String rawQueryString = "REPLACE ${value} WITH ACTUAL VALUE";
        HashMap<Double, String> map = new HashMap<>();
        map.put(1.2, "REPLACE 1.2 WITH ACTUAL VALUE");
        map.put(0.0, "REPLACE 0.0 WITH ACTUAL VALUE");
        map.put(-5.9, "REPLACE -5.9 WITH ACTUAL VALUE");

        SqlQuery<Double> query = new SqlQuery<>(rawQueryString);
        for (Double param : map.keySet()) {
            assertEquals(query.getQuery(param), map.get(param));
        }
    }

    @Test
    void booleanParam() throws IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        String rawQueryString = "REPLACE ${value} WITH ACTUAL VALUE";
        HashMap<Boolean, String> map = new HashMap<>();
        map.put(true, "REPLACE TRUE WITH ACTUAL VALUE");
        map.put(false, "REPLACE FALSE WITH ACTUAL VALUE");

        SqlQuery<Boolean> query = new SqlQuery<>(rawQueryString);
        for (Boolean param : map.keySet()) {
            assertEquals(query.getQuery(param), map.get(param));
        }
    }

    @Test
    void stringParam() throws IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        String rawQueryString = "REPLACE ${value} WITH ACTUAL VALUE";
        HashMap<String, String> map = new HashMap<>();
        map.put("a string", "REPLACE \"a string\" WITH ACTUAL VALUE");
        map.put("Complex", "REPLACE \"Complex\" WITH ACTUAL VALUE");
        map.put("", "REPLACE \"\" WITH ACTUAL VALUE");

        SqlQuery<String> query = new SqlQuery<>(rawQueryString);
        for (String param : map.keySet()) {
            assertEquals(query.getQuery(param), map.get(param));
        }
    }

    @Test
    void arrayListParam() throws IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        String rawQueryString = "REPLACE ${value} WITH ACTUAL VALUE";

        SqlQuery<ArrayList<?>> query = new SqlQuery<>(rawQueryString);

        {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(2);
            list.add(3);
            list.add(-5);
            assertEquals(query.getQuery(list), "REPLACE (2,3,-5) WITH ACTUAL VALUE");
        }

        {
            ArrayList<String> list = new ArrayList<>();
            list.add("a");
            list.add("testing ok");
            assertEquals(query.getQuery(list), "REPLACE (\"a\",\"testing ok\") WITH ACTUAL VALUE");
        }

        {
            ArrayList<ArrayList<?>> list = new ArrayList<>();
            ArrayList<Boolean> listA = new ArrayList<>();
            ArrayList<String> listB = new ArrayList<>();
            ArrayList<Double> listC = new ArrayList<>();
            ArrayList<Integer> listD = new ArrayList<>();
            listA.add(true);
            listA.add(true);
            listB.add("a string");
            listC.add(0.0);
            listC.add(1.2);

            list.add(listA);
            list.add(listB);
            list.add(listC);
            list.add(listD);
            assertEquals(query.getQuery(list), "REPLACE ((TRUE,TRUE),(\"a string\"),(0.0,1.2),()) WITH ACTUAL VALUE");
        }
    }

    public class Car {
        String manufacturer;
        Integer year;

        public Car(String manufacturer, Integer year) {
            this.manufacturer = manufacturer;
            this.year = year;
        }
    }

    @Test
    void unimplementedPrimitiveParam() {
        String rawQueryString = "Today's date is ${value}";

        SqlQuery<Date> query = new SqlQuery<>(rawQueryString);

        assertThrows(PrimitiveNotImplementedException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                query.getQuery(new Date());
            }
        });

    }

    @Test
    void illegalObjectParam() {
        String rawQueryString = "The car was released by ${manufacturer} in the month ${month}";

        SqlQuery<Car> query = new SqlQuery<>(rawQueryString);

        assertThrows(IllFormedParamException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                query.getQuery(new Car("BMW", 2022));
            }
        });

    }

    @Test
    void objectParam() throws IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        String rawQueryString = "The car was released by ${manufacturer} in the year ${year}";

        SqlQuery<Car> query = new SqlQuery<>(rawQueryString);

        assertEquals(query.getQuery(new Car("BMW", 2022)), "The car was released by \"BMW\" in the year 2022");
        assertEquals(query.getQuery(new Car("TATA", 2050)), "The car was released by \"TATA\" in the year 2050");
    }
}

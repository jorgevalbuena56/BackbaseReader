package com.backbase.backbasereader;

import com.backbase.backbasereader.model.City;
import com.backbase.backbasereader.parser.CitiesReader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPageWithFiveCities() throws Exception {
        CitiesReader citiesReader = new CitiesReader();
        int count = 0;
        int pageSize = 5;
        Map<String, TreeSet<City>> result = citiesReader.read(1, pageSize);
        for (TreeSet<City> cities : result.values()) {
            count += cities.size();
        }
        assertEquals("Size of the Result list match",pageSize, count);
    }

    @Test
    public void getPageWithFiftyCities() throws Exception {
        CitiesReader citiesReader = new CitiesReader();
        int count = 0;
        int pageSize = 50;
        Map<String, TreeSet<City>> result = citiesReader.read(1, pageSize);
        for (TreeSet<City> cities : result.values()) {
            count += cities.size();
        }
        assertEquals("Size of the Result list match",pageSize, count);
    }

    @Test
    public void getCitiesWithInvalidPageNumber() throws Exception {
        CitiesReader citiesReader = new CitiesReader();
        int count = 0;
        int pageSize = 50;
        Map<String, TreeSet<City>> result = citiesReader.read(-1, pageSize);
        for (TreeSet<City> cities : result.values()) {
            count += cities.size();
        }
        assertEquals("Size of the Result list match",0, count);
    }

    @Test
    public void getPageWithOutOfBoundsPageSize() throws Exception {
        CitiesReader citiesReader = new CitiesReader();
        int count = 0;
        int pageSize = 500000;
        Map<String, TreeSet<City>> result = citiesReader.read(1, pageSize);
        for (TreeSet<City> cities : result.values()) {
            count += cities.size();
        }
        assertNotEquals("Size of the Result list match",pageSize, count);
    }

    @Test
    public void getPageWithOutOfBoundsPageNumber() throws Exception {
        CitiesReader citiesReader = new CitiesReader();
        int count = 0;
        int pageSize = 500000;
        Map<String, TreeSet<City>> result = citiesReader.read(99999999, pageSize);
        for (TreeSet<City> cities : result.values()) {
            count += cities.size();
        }
        assertEquals("Size of the Result list match",0, count);
    }
}
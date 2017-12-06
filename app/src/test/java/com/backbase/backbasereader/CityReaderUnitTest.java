package com.backbase.backbasereader;

import android.content.Context;
import android.content.res.AssetManager;

import com.backbase.backbasereader.model.City;
import com.backbase.backbasereader.parser.CitiesReader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class CityReaderUnitTest {

    private static final String FILE_NAME = "cities.json";

    @Mock
    Context mMockContext;

    @Mock
    AssetManager assetManager;

    @Before
    public void setup() throws Exception{
        MockitoAnnotations.initMocks(this);
        doReturn(assetManager).when(mMockContext).getAssets();
        InputStream inputStream =
                getClass().getClassLoader().getResourceAsStream(FILE_NAME);
        doReturn(inputStream).when(assetManager).open(FILE_NAME);
    }

    @Test
    public void getPageWithFiveCities() throws Exception {
        CitiesReader citiesReader = new CitiesReader();
        int count = 0;
        int pageSize = 5;
        int pageNumber = 1;
        Map<String, TreeSet<City>> result =
                citiesReader.read(mMockContext, pageNumber, pageSize);
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
        int pageNumber = 1;
        Map<String, TreeSet<City>> result =
                citiesReader.read(mMockContext, pageNumber, pageSize);
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
        int pageNumber = -1;
        Map<String, TreeSet<City>> result =
                citiesReader.read(mMockContext, pageNumber, pageSize);
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
        int pageNumber = 1;
        Map<String, TreeSet<City>> result =
                citiesReader.read(mMockContext, pageNumber, pageSize);
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
        int pageNumber = 99999999;
        Map<String, TreeSet<City>> result =
                citiesReader.read(mMockContext, pageNumber, pageSize);
        for (TreeSet<City> cities : result.values()) {
            count += cities.size();
        }
        assertEquals("Size of the Result list match",0, count);
    }

    @Test
    public void getSearchResultByValidKeyword() throws Exception {
        List<City> toFilter = getPrepopulatedList();
        String prefix = "Al";
        //filter the result list by a valid key
        List<City> filtered = CitiesReader.filter(toFilter, prefix);
        for (City city : filtered) {
            assertTrue(city.getName().startsWith(prefix));
        }
    }

    @Test
    public void getSearchResultByInValidKeyword() throws Exception {
        List<City> toFilter = getPrepopulatedList();
        String prefix = "#$%$^%^234234";
        //filter the result list by a valid key
        List<City> filtered = CitiesReader.filter(toFilter, prefix);
        assertEquals("Size of the Result list match",0, filtered.size());
    }

    @Test
    public void getSearchResultByEmptyKeyword() throws Exception {
        List<City> toFilter = getPrepopulatedList();
        String prefix = "";
        //filter the result list by a valid key
        List<City> filtered = CitiesReader.filter(toFilter, prefix);
        assertEquals("Size of the Result list match",0, filtered.size());
    }

    @Test
    public void getSearchResultByNullKeyword() throws Exception {
        List<City> toFilter = getPrepopulatedList();
        String prefix = null;
        //filter the result list by a valid key
        List<City> filtered = CitiesReader.filter(toFilter, prefix);
        assertEquals("Size of the Result list match",0, filtered.size());
    }

    @Test
    public void getSearchResultByNull() throws Exception {
        List<City> toFilter = null;
        String prefix = null;
        //filter the result list by a valid key
        List<City> filtered = CitiesReader.filter(toFilter, prefix);
        assertEquals("Size of the Result list match",0, filtered.size());
    }

    private List<City> getPrepopulatedList() throws Exception {
        CitiesReader citiesReader = new CitiesReader();
        int pageSize = 500;
        int pageNumber = 1;
        Map<String, TreeSet<City>> result =
                citiesReader.read(mMockContext, pageNumber, pageSize);

        List<City> toFilter = new ArrayList<>();
        for (TreeSet<City> cities : result.values()) {
            toFilter.addAll(new ArrayList<>(cities));
        }

        return toFilter;
    }
}
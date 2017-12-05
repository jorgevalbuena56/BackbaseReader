package com.backbase.backbasereader.parser;

import android.content.Context;

import com.backbase.backbasereader.model.City;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Class to read the cities using the stream - tree model approach with Jackson
 */

public class CitiesReader {

    private static final String FILE_NAME = "cities.json";

    public Map<String, TreeSet<City>> read(Context context, int pageNumber, int pageSize) throws Exception {
        JsonFactory jsonFactory = new MappingJsonFactory();
        //InputStream is = getClass().getClassLoader().getResourceAsStream(FILE_NAME);
        InputStream is = context.getAssets().open(FILE_NAME);
        JsonParser jsonParser = jsonFactory.createParser(is);

        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        Map<String, TreeSet<City>> citiesByCountry = new TreeMap<>();

        // paging calculation
        int skippedElements = 0;
        int to = pageNumber * pageSize;
        int from = to - pageSize;

        JsonToken current;
        current = jsonParser.nextToken();

        if (current != JsonToken.START_ARRAY || pageNumber < 0
            || pageSize < 0 || from < 0 || to < 0) {
            return citiesByCountry;
        }

        //Skip elements based on page number and page size
        while(skippedElements < from && jsonParser.nextToken() != JsonToken.END_ARRAY) {
            jsonParser.skipChildren();
            skippedElements++;
        }

        int index = from;
        while (jsonParser.nextToken() != JsonToken.END_ARRAY && index < to) {
            // read the record into a tree model,
            // this moves the parsing position to the end of it
            JsonNode node = jsonParser.readValueAsTree();
            //automatically bind with the POJO class
            City city = objectMapper.convertValue(node, City.class);
            //append city information in the corresponding map position
            if (citiesByCountry.containsKey(city.getCountry())) {
                citiesByCountry.get(city.getCountry()).add(city);
            } else {
                TreeSet<City> cities = new TreeSet<>();
                cities.add(city);
                citiesByCountry.put(city.getCountry(), cities);
            }

            index++;
        }
        return citiesByCountry;
    }
}

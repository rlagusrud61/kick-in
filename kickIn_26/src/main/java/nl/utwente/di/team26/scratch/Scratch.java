package nl.utwente.di.team26.scratch;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

class Scratch {
    public static void main(String[] args) {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src\\main\\resources\\materials\\active materials.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject resources = (JSONObject) obj;
            JSONArray listOfResources = (JSONArray) resources.get("fileImages");

            for (Object object : listOfResources) {
                System.out.println(object);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
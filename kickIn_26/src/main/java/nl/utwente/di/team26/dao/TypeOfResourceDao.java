package nl.utwente.di.team26.dao;

import nl.utwente.di.team26.model.Static;
import nl.utwente.di.team26.model.TypeOfResource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public enum TypeOfResourceDao {
    instance;

    private Map<String, TypeOfResource> typeOfResources;

    private TypeOfResourceDao() {
        this.typeOfResources = new HashMap<>();

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("D:\\Documents\\TCS\\Module4\\Project\\Kick-In Team26\\kickIn_26\\src\\main\\resources\\materials\\active materials.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject resources = (JSONObject) obj;
            JSONArray listOfResources = (JSONArray) resources.get("fileImages");

            System.out.println(listOfResources);

            int i = 1;
            for (Object object : listOfResources) {
                JSONObject json = (JSONObject) object;
                this.typeOfResources.put(String.valueOf(i), new TypeOfResource(i, (String) json.get("name"), (String) json.get("description")));
                StaticResourceDao.instance.addStaticResource(new Static(i, (String) json.get("url")));
                i++;
            }

            System.out.println(this.typeOfResources);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    public Map<String, TypeOfResource> getAllTypeOfResources() {
        return this.typeOfResources;
    }

    public TypeOfResource getTypeOfResource(String id) {
        return this.typeOfResources.get(id);
    }

    public void removeType(int resource_id) {
        this.typeOfResources.remove(String.valueOf(resource_id));
        StaticResourceDao.instance.removeStaticResource(resource_id);
    }

    public void addType(TypeOfResource type) {
        this.typeOfResources.put(String.valueOf(type.getResource_id()), type);
    }
}

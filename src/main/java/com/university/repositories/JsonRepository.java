package com.university.repositories;

import org.json.JSONArray;
import org.json.JSONObject;
import com.university.models.Course;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class JsonRepository<T> implements Repository<T> {
    protected String filePath;

    public JsonRepository(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }
    
    private void ensureFileExists() {
        try {
            if (!Files.exists(Paths.get(filePath))) {
                Files.write(Paths.get(filePath), new JSONArray().toString().getBytes());
            }
        } catch (IOException e) {
            throw new RepositoryException("Failed to create JSON file", e);
        }
    }

    protected synchronized JSONArray readJsonArray() throws RepositoryException {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return new JSONArray(content);
        } catch (IOException e) {
            throw new RepositoryException("Failed to read JSON file", e);
        }
    }

    protected synchronized void writeJsonArray(JSONArray jsonArray) throws RepositoryException {
        try {
            Files.write(Paths.get(filePath), jsonArray.toString(2).getBytes());
        } catch (IOException e) {
            throw new RepositoryException("Failed to write JSON file", e);
        }
    }

    protected abstract T fromJson(JSONObject jsonObject);
    protected abstract JSONObject toJson(T entity);

    @Override
    public synchronized List<T> findAll() {
        JSONArray jsonArray = readJsonArray();
        List<T> entities = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            entities.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return entities;
    }

    @Override
    public synchronized Optional<T> findById(String id) {
        JSONArray jsonArray = readJsonArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.getString("id").equals(id)) {
                return Optional.of(fromJson(jsonObject));
            }
        }
        return Optional.empty();
    }

    
    @Override
    public synchronized void save(T entity) {
        JSONArray jsonArray = readJsonArray();
        JSONObject jsonObject = toJson(entity);
        
        boolean found = false;

        // Check if the entity is an instance of Course
        if (entity instanceof Course) {
            String courseCode = jsonObject.getString("code"); // Use "code" for Course
            // Check if the course already exists in the JSON array
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getString("code").equals(courseCode)) { // Compare with course code
                    jsonArray.put(i, jsonObject); // Update the existing course
                    found = true;
                    break;
                }
            }
            // If the course does not exist, add it to the array
            if (!found) {
                jsonArray.put(jsonObject);
            }
        } else {
            // Assuming other entities have an "id" field
            String id = jsonObject.getString("id"); // Use "id" for other entities
            // Check if the entity already exists in the JSON array
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getString("id").equals(id)) { // Compare with id
                    jsonArray.put(i, jsonObject); // Update the existing entity
                    found = true;
                    break;
                }
            }
            // If the entity does not exist, add it to the array
            if (!found) {
                jsonArray.put(jsonObject);
            }
        }

        // Write the updated JSON array back to the file
        writeJsonArray(jsonArray);
    }

    @Override
    public synchronized void delete(String id) {
        JSONArray jsonArray = readJsonArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("id").equals(id)) {
                jsonArray.remove(i);
                break;
            }
        }
        writeJsonArray(jsonArray);
    }

    public synchronized void saveAll(List<T> entities) {
        JSONArray jsonArray = new JSONArray();
        for (T entity : entities) {
            jsonArray.put(toJson(entity));
        }
        writeJsonArray(jsonArray);
    }
}

class RepositoryException extends RuntimeException {
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
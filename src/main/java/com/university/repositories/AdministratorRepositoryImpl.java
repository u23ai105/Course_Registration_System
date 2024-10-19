package com.university.repositories;

import com.university.models.Administrator;
import org.json.JSONObject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdministratorRepositoryImpl extends JsonRepository<Administrator> implements AdministratorRepository {

    public AdministratorRepositoryImpl() {
        super("src/main/resources/administrators.json");
    }

    @Override
    protected Administrator fromJson(JSONObject jsonObject) {
        Administrator administrator = new Administrator(
            jsonObject.getString("id"),
            jsonObject.getString("name"),
            jsonObject.getString("email"),
            jsonObject.getString("password")
        );
        administrator.setRole(jsonObject.getString("role"));
        return administrator;
    }

    @Override
    protected JSONObject toJson(Administrator administrator) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", administrator.getId());
        jsonObject.put("name", administrator.getName());
        jsonObject.put("email", administrator.getEmail());
        jsonObject.put("password", administrator.getPassword());
        jsonObject.put("role", administrator.getRole());
        return jsonObject;
    }

    @Override
    public Optional<Administrator> findByEmail(String email) {
        return findAll().stream()
                .filter(admin -> admin.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Administrator> findByRole(String role) {
        return findAll().stream()
                .filter(admin -> admin.getRole().equals(role))
                .collect(Collectors.toList());
    }
}
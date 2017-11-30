package ro.contezi.shopping;

import java.util.Map;

public class ConfigurableUser {
    private final String id;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String password;

    public ConfigurableUser(Map<String, String> properties) {
        id = properties.get("id");
        email = properties.get("email");
        firstName = properties.get("firstName");
        lastName = properties.get("lastName");
        password = properties.get("password");
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

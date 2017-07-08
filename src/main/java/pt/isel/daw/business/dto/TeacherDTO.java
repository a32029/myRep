package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeacherDTO {
    @JsonProperty("teacher_id")
    private String teacherId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("role")
    private String role;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("enabled")
    private boolean enabled;

    public TeacherDTO(String teacherId, String name, String email, String role, String username, String password, boolean enabled) {
        this.teacherId = teacherId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public TeacherDTO() {}

    public String getTeacherId() {
        return teacherId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
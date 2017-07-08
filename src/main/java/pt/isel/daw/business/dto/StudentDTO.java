package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentDTO {
    @JsonProperty("student_id")
    private String studentId;
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

    public StudentDTO(String studentId, String name, String email, String role, String username, String password, boolean enabled) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public StudentDTO() {}

    public String getStudentId() {
        return studentId;
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
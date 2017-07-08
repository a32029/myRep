package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticatedUserDTO {
    @JsonProperty("id")
    private String id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("is_teacher")
    private boolean isTeacher;
    @JsonProperty("is_admin")
    private boolean isAdmin;
    @JsonProperty("coordinator_in_course")
    private String coordinatorInCourse;
    @JsonProperty("classes")
    private List<String> classes;

    public AuthenticatedUserDTO(String id, String username, boolean isTeacher, boolean isAdmin, String coordinatorInCourse, List<String> classes) {
        this.id = id;
        this.username = username;
        this.isTeacher = isTeacher;
        this.isAdmin = isAdmin;
        this.coordinatorInCourse = coordinatorInCourse;
        this.classes = classes;
    }

    public AuthenticatedUserDTO() {
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getCoordinatorInCourse() {
        return coordinatorInCourse;
    }

    public List<String> getClasses() {
        return classes;
    }
}
package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeacherListDTO {
    @JsonProperty("teacher_id")
    private String teacherId = "";
    @JsonProperty("password")
    private String password = "";

    public TeacherListDTO() {}

    public String getTeacherId() {
        return teacherId;
    }

    public String getPassword() {
        return password;
    }
}
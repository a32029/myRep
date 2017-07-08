package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentListDTO {
    @JsonProperty("student_id")
    private String studentId = "";
    @JsonProperty("password")
    private String password = "";

    public StudentListDTO() {}

    public String getStudentId() {
        return studentId;
    }

    public String getPassword() {
        return password;
    }
}
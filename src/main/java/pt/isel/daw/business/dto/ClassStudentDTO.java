package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassStudentDTO {
    @JsonProperty("class_id")
    private String classId;
    @JsonProperty("student_id")
    private String studentId;

    public ClassStudentDTO(String classId, String studentId) {
        this.classId = classId;
        this.studentId = studentId;
    }

    public ClassStudentDTO() {
    }

    public String getClassId() {
        return classId;
    }

    public String getStudentId() {
        return studentId;
    }
}
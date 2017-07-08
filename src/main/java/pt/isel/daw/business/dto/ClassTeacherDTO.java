package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassTeacherDTO {
    @JsonProperty("class_id")
    private String classId;
    @JsonProperty("teacher_id")
    private String teacherId;

    public ClassTeacherDTO(String classId, String teacherId) {
        this.classId = classId;
        this.teacherId = teacherId;
    }

    public ClassTeacherDTO() {
    }

    public String getClassId() {
        return classId;
    }

    public String getTeacherId() {
        return teacherId;
    }
}
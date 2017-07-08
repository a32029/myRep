package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassListDTO {
    @JsonProperty("course_acronym")
    private String courseAcronym = "";
    @JsonProperty("class_id")
    private String classId = "";
    @JsonProperty("active")
    private boolean active = true;

    public ClassListDTO(String courseAcronym) {
        this.courseAcronym = courseAcronym;
    }

    public ClassListDTO() {
    }

    public String getClassId() {
        return classId;
    }

    public boolean isActive() {
        return active;
    }

    public String getCourseAcronym() {
        return courseAcronym;
    }
}

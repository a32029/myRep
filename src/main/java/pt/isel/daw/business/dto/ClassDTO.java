package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassDTO {
    @JsonProperty("class_id")
    private String classId;
    @JsonProperty("identifier")
    private String identifier;
    @JsonProperty("course_acronym")
    private String courseAcronym;
    @JsonProperty("semester_id")
    private String semesterId;
    @JsonProperty("auto_enrollment")
    private boolean autoEnrollment;
    @JsonProperty("max_students_group")
    private String maxStudentsPerGroup;
    @JsonProperty("active")
    private boolean active;

    public ClassDTO(String classId, String identifier, String courseAcronym, String semesterId, boolean autoEnrollment, String maxStudentsPerGroup, boolean active) {
        this.classId = classId;
        this.identifier = identifier;
        this.courseAcronym = courseAcronym;
        this.semesterId = semesterId;
        this.autoEnrollment = autoEnrollment;
        this.maxStudentsPerGroup = maxStudentsPerGroup;
        this.active = active;
    }

    public ClassDTO() {}

    public String getClassId() {
        return classId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getCourseAcronym() {
        return courseAcronym;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public boolean isAutoEnrollment() {
        return autoEnrollment;
    }

    public String getMaxStudentsPerGroup() {
        return maxStudentsPerGroup;
    }

    public boolean isActive() {
        return active;
    }
}

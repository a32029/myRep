package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupDTO {
    @JsonProperty("class_id")
    private String classId;
    @JsonProperty("group_id")
    private String groupId;
    @JsonProperty("student_id")
    private String studentId;

    public GroupDTO(String classId, String groupId, String studentId) {
        this.classId = classId;
        this.groupId = groupId;
        this.studentId = studentId;
    }

    public GroupDTO() {}

    public String getClassId() {
        return classId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getStudentId() {
        return studentId;
    }
}
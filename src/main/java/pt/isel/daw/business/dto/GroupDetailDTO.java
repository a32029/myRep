package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupDetailDTO {
    @JsonProperty("class_id")
    private String classId;
    @JsonProperty("group_id")
    private String groupId;
    @JsonProperty("student_id")
    private String studentId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("role")
    private String role;

    public GroupDetailDTO(String classId, String groupId, String studentId, String name, String email, String role) {
        this.classId = classId;
        this.groupId = groupId;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public GroupDetailDTO() {}

    public String getClassId() {
        return classId;
    }

    public String getGroupId() {
        return groupId;
    }

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
}
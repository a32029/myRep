package pt.isel.daw.business.entities;

public class GroupDetailEntity {
    private String classId;
    private String groupId;
    private String studentId;
    private String name;
    private String email;
    private String role;

    public GroupDetailEntity(String classId, String groupId, String studentId, String name, String email, String role) {
        this.classId = classId;
        this.groupId = groupId;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

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
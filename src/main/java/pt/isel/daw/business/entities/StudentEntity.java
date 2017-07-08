package pt.isel.daw.business.entities;

public class StudentEntity {
    private String studentId;
    private String name;
    private String email;
    private String role;
    private String username;
    private String password;
    private boolean enabled;

    public StudentEntity(String studentId, String name, String email, String role, String username, String password, boolean enabled) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
package pt.isel.daw.business.entities;

public class TeacherEntity {
    private String teacherId;
    private String name;
    private String email;
    private String role;
    private String username;
    private String password;
    private boolean enabled;

    public TeacherEntity(String teacherId, String name, String email, String role, String username, String password, boolean enabled) {
        this.teacherId = teacherId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public String getTeacherId() {
        return teacherId;
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
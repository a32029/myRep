package pt.isel.daw.business.entities;

import java.util.List;

public class AuthenticatedUserEntity {
    private String id;
    private String username;
    private boolean isTeacher;
    private boolean isAdmin;
    private String coordinatorInCourse;
    private List<String> classes;

    public AuthenticatedUserEntity(String id, String username, boolean isTeacher, boolean isAdmin, String coordinatorInCourse, List<String> classes) {
        this.id = id;
        this.username = username;
        this.isTeacher = isTeacher;
        this.isAdmin = isAdmin;
        this.coordinatorInCourse = coordinatorInCourse;
        this.classes = classes;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getCoordinatorInCourse() {
        return coordinatorInCourse;
    }

    public List<String> getClasses() {
        return classes;
    }
}
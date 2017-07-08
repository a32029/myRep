package pt.isel.daw.business.entities;

public class ClassEntity {
    private String classId;
    private String identifier;
    private String courseAcronym;
    private String semesterId;
    private boolean autoEnrollment;
    private String maxStudentsPerGroup;
    private boolean active;

    public ClassEntity(String classId, String identifier, String courseAcronym, String semesterId, boolean autoEnrollment, String maxStudentsPerGroup, boolean active) {
        this.classId = classId;
        this.identifier = identifier;
        this.courseAcronym = courseAcronym;
        this.semesterId = semesterId;
        this.autoEnrollment = autoEnrollment;
        this.maxStudentsPerGroup = maxStudentsPerGroup;
        this.active = active;
    }

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
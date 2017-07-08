package pt.isel.daw.business.entities;

public class CourseEntity {
    private String name;
    private String acronym;
    private String coordinatorId;
    private boolean active;

    public CourseEntity(String name, String acronym, String coordinatorId, boolean active) {
        this.name = name;
        this.acronym = acronym;
        this.coordinatorId = coordinatorId;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getCoordinatorId() {
        return coordinatorId;
    }

    public boolean isActive() {
        return active;
    }
}
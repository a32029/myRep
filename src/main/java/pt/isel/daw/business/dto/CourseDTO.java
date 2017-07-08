package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("acronym")
    private String acronym;
    @JsonProperty("coordinator_id")
    private String coordinatorId;
    @JsonProperty("active")
    private boolean active;

    public CourseDTO(String name, String acronym, String coordinatorId, boolean active) {
        this.name = name;
        this.acronym = acronym;
        this.coordinatorId = coordinatorId;
        this.active = active;
    }

    public CourseDTO() {}

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
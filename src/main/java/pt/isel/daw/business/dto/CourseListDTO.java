package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseListDTO {
    @JsonProperty("acronym")
    private String acronym = "";
    @JsonProperty("active")
    private boolean active = true;

    public CourseListDTO() {}

    public String getAcronym() {
        return acronym;
    }

    public boolean isActive() {
        return active;
    }
}

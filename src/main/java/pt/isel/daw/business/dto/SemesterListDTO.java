package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SemesterListDTO {
    @JsonProperty("semester_id")
    private String semesterId = "";

    public SemesterListDTO(String semesterId) {
        this.semesterId = semesterId;
    }

    public SemesterListDTO() {
    }

    public String getSemesterId() {
        return semesterId;
    }
}

package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SemesterDTO {
    private String semesterId;
    @JsonProperty("start_year")
    private String startYear;
    @JsonProperty("end_year")
    private String endYear;
    @JsonProperty("season")
    private String season;

    public SemesterDTO(String semesterId, String startYear, String endYear, String season) {
        this.semesterId = semesterId;
        this.startYear = startYear;
        this.endYear = endYear;
        this.season = season;
    }

    public SemesterDTO() {}

    public String getSemester() {
        return semesterId;
    }

    public String getStartYear() {
        return startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public String getSeason() {
        return season;
    }
}

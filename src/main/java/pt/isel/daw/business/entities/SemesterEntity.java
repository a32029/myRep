package pt.isel.daw.business.entities;

import java.util.Calendar;

public class SemesterEntity {
    private String startYear;
    private String endYear;
    private String season;

    public SemesterEntity(String semesterId) {
        this.startYear = "20" + semesterId.substring(0,2);
        this.endYear = "20" + semesterId.substring(2,4);
        this.season = semesterId.substring(4, 5).toUpperCase().equals("I")?"winter":"summer";
    }

    public SemesterEntity(String startYear, String endYear, String season) {
        this.startYear = startYear;
        this.endYear = endYear;
        this.season = season;
    }

    public SemesterEntity() {
        this("" + Calendar.getInstance().get(Calendar.YEAR),
                "" + (Calendar.getInstance().get(Calendar.YEAR) + 1),
                Calendar.getInstance().get(Calendar.MONTH) > 1?"winter":"summer");
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

    public String getSemester() {
        return startYear.substring(2,4) + endYear.substring(2,4) + (season.toUpperCase().equals("WINTER")?"i":"v");
    }
}
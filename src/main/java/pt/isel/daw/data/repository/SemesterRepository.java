package pt.isel.daw.data.repository;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pt.isel.daw.business.entities.*;
import pt.isel.daw.data.factory.PageListFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SemesterRepository {
    public static String createSemester(JdbcTemplate jt, SemesterEntity semester) {
        try {
            String semesterId = semester.getSemester();

            if (semester.getSemester().length() == 0)
                throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "create_semester");

            jt.update("insert into semester (semester_id) values (?)", semesterId);

            return semesterId;
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "create_semester");
        }
    }

    public static void removeSemester(JdbcTemplate jt, String semesterId) {
        if (semesterId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "remove_semester");

        try {
            jt.update("delete from semester where semester_id = (?)", semesterId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "remove_semester");
        }
    }

    public static SemesterEntity getSemesterBy(JdbcTemplate jt, String searchParam, String searchValue) {
        if (searchParam.length() == 0 || searchValue.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "semester_detail");

        try {
            List<SemesterEntity> semesters = jt.query("SELECT (*) FROM semester WHERE " + searchParam + " LIKE (?)", new Object[] { searchValue }, semestersRowMapper());

            return semesters.get(0);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "semester_detail");
        }
    }

    public static List<SemesterEntity> getAllSemesters(JdbcTemplate jt) {
        try {
            return jt.query("SELECT (*) FROM semester ORDER BY semester_id", semestersRowMapper());
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "semester_detail");
        }
    }

    public static PageListEntity<SemesterEntity> getListSemesters(JdbcTemplate jt, int pageNo, int pageSize, String searchParam, String searchValue) {
        if (searchParam.length() == 0 || searchValue.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "list_semesters");

        try {
            return new PageListFactory<SemesterEntity>().fetchPage(
                    jt,
                    "SELECT count(*) FROM semester WHERE " + searchParam + " LIKE (?)",
                    "SELECT (*) FROM semester WHERE " + searchParam + " LIKE (?) ORDER BY semester_id",
                    new Object[]{ searchValue.replace('*', '%') },
                    pageNo,
                    pageSize,
                    semestersRowMapper()
            );
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "list_semesters");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    public static RowMapper<SemesterEntity> semestersRowMapper() {
        return new RowMapper<SemesterEntity>() {
            public SemesterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                SemesterEntity actor = new SemesterEntity(rs.getString("semester_id"));
                return actor;
            }
        };
    }
}
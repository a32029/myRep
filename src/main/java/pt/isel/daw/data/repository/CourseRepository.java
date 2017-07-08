package pt.isel.daw.data.repository;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pt.isel.daw.business.entities.ControllerExceptionEntity;
import pt.isel.daw.business.entities.CourseEntity;
import pt.isel.daw.business.entities.PageListEntity;
import pt.isel.daw.data.factory.PageListFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CourseRepository {
    public static String createCourse(JdbcTemplate jt, CourseEntity course) {
        String name = course.getName().trim();
        String acronym = course.getAcronym().trim().toUpperCase();
        String coordinatorId = course.getCoordinatorId();
        boolean active = course.isActive();

        if (name.length() == 0 || acronym.length() == 0 || coordinatorId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "create_course");
        try {
            jt.update("INSERT INTO course (name, acronym, coordinator_id, active) VALUES (?, ?, ?, ?)", name, acronym, coordinatorId, active);
            
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "create_course");
        }
        
        return acronym;
    }

    public static String updateCourse(JdbcTemplate jt, CourseEntity course) {
        String acronym = course.getAcronym().trim().toUpperCase();
        String coordinatorId = course.getCoordinatorId();
        boolean active = course.isActive();

        if (acronym.length() == 0 || coordinatorId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "update_course");
        try {
            jt.update("update course set coordinator_id = (?), active = (?) where acronym = (?)", coordinatorId, active, acronym);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "update_course");
        }

        return acronym;
    }

    public static void setCourseActive(JdbcTemplate jt, String acronym, boolean active) {
        try {
            jt.update("update course set active = (?) where acronym = (?)",active, acronym.trim());
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "activate_course");
        }
    }

    public static CourseEntity getCourseDetail(JdbcTemplate jt, String searchParam, String searchValue) {
        try {
            List<CourseEntity> courses = jt.query("SELECT (*) FROM course WHERE " + searchParam + " LIKE ('" + searchValue + "')", new Object[] {}, coursesRowMapper());
    
            return courses.get(0);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "course_detail");
        }
    }

    public static List<CourseEntity> getAllDeactivatedCourses(JdbcTemplate jt) {
        try {
            return jt.query("SELECT (*) FROM course WHERE active=FALSE ORDER BY acronym", coursesRowMapper());
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "course_detail");
        }
    }

    public static PageListEntity<CourseEntity> getListActiveCourses(JdbcTemplate jt, int pageNo, int pageSize, String searchParam, String searchValue) {
        try {
            return new PageListFactory<CourseEntity>().fetchPage(
                    jt,
                    "SELECT count(*) FROM course WHERE active=true AND " + searchParam + " LIKE (?)",
                    "SELECT (*) FROM course WHERE active=true AND " + searchParam + " LIKE (?) ORDER BY course_id",
                    new Object[]{searchValue.replace('*', '%')},
                    pageNo,
                    pageSize,
                    coursesRowMapper()
            );
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "activate_list_courses");
        }
    }

    public static PageListEntity<CourseEntity> getListAllCourses(JdbcTemplate jt, int pageNo, int pageSize, String searchParam, String searchValue) {
        try {
            return new PageListFactory<CourseEntity>().fetchPage(
                    jt,
                    "SELECT count(*) FROM course WHERE " + searchParam + " LIKE (?)",
                    "SELECT (*) FROM course WHERE " + searchParam + " LIKE (?) ORDER BY course_id",
                    new Object[]{searchValue.replace('*', '%')},
                    pageNo,
                    pageSize,
                    coursesRowMapper()
            );
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "list_courses");
        }
    }

    //-------------------------------------------------------------

    public static RowMapper<CourseEntity> coursesRowMapper() {
        return new RowMapper<CourseEntity>() {
            public CourseEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                CourseEntity actor = new CourseEntity(rs.getString("name").trim(),
                        rs.getString("acronym").trim(), rs.getString("coordinator_id"),
                        Boolean.parseBoolean(rs.getString("active")));
                return actor;
            }
        };
    }
}
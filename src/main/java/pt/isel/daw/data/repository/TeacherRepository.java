package pt.isel.daw.data.repository;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pt.isel.daw.business.entities.*;
import pt.isel.daw.data.factory.PageListFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TeacherRepository {
    public static String teacherView = "(select teacher_id, name, email, role, username, password, enabled from " +
            "user U, teacher T, credentials CR where U.user_id = T.user_id and U.user_id = CR.user_id)";

    public static String createTeacher(JdbcTemplate jt, TeacherEntity teacher) {
        String teacherId = teacher.getTeacherId();
        String name = teacher.getName();
        String email = teacher.getEmail();
        String role = teacher.getRole();
        String username = teacher.getUsername();
        String password = teacher.getPassword();
        boolean enabled = teacher.isEnabled();

        if (teacherId.length() == 0 || name.length() == 0 || email.length() == 0 || role.length() == 0 || username.length() == 0 || password.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "create_teacher");

        try {
            jt.update("INSERT INTO user (name, email, role) VALUES (?, ?, ?)", name, email, role);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "create_teacher");
        }
        int userId = jt.queryForObject("SELECT user_id FROM user WHERE email = (?)", new Object[]{email}, Integer.class);

        try {
            jt.update("INSERT INTO teacher (user_id, teacher_id) VALUES (?, ?)", userId, teacherId);
            jt.update("INSERT INTO credentials (user_id, username, password, enabled) VALUES (?, ?, ?, ?)",
                    userId, username, password, enabled ? "1" : "0");
        } catch (Exception e) {
            jt.update("delete from credentials where user_id=(?)", userId);
            jt.update("delete from teacher where user_id=(?)", userId);
            jt.update("delete from user where user_id=(?)", userId);
        }

        try {
            return jt.queryForObject("select teacher_id from teacher where user_id = ?", new Object[] { userId } , String.class);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "create_teacher");
        }
    }

    public static String updateTeacher(JdbcTemplate jt, TeacherEntity teacher) {
        String teacherId = teacher.getTeacherId();
        String name = teacher.getName();
        String email = teacher.getEmail();
        String role = teacher.getRole();
        String username = teacher.getUsername();
        String password = teacher.getPassword();
        boolean enabled = teacher.isEnabled();

        if (teacherId.length() == 0 || name.length() == 0 || email.length() == 0 || role.length() == 0 || username.length() == 0 || password.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "update_teacher");

        try {
            int userId = jt.queryForObject("select user_id from teacher where teacher_id = (?)", new Object[] { teacherId }, Integer.class);
            jt.update("update user set name = (?), email = (?), role = (?) where user_id = ?", name, email, role, userId);
            jt.update("update credentials set  username = (?), password = (?), enabled = (?) where user_id = (?)", username, password, enabled?"1":"0", userId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "update_teacher");
        }

        return teacherId;
    }

    public static void setTeacherActive(JdbcTemplate jt, TeacherEntity teacher) {
        try {
            int userId = jt.queryForObject("select user_id from teacher where teacher_id = (?)", new Object[] { teacher.getTeacherId() }, Integer.class);

            jt.update("update credentials set enabled = (1), password = (?) where user_id = (?)", teacher.getPassword(), userId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "activate_teacher");
        }
    }

    public static TeacherEntity getTeacherBy(JdbcTemplate jt, String searchParam, String searchValue) {
        if (searchParam.length() == 0 || searchValue.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "teacher_detail");

        try {
            List<TeacherEntity> teachers = jt.query("SELECT * FROM " + teacherView + " WHERE " + searchParam + " LIKE (?)", new Object[] { searchValue }, teachersRowMapper());

            return teachers.get(0);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "teacher_detail");
        }
    }

    public static List<TeacherEntity> getAllDeactivatedTeachers(JdbcTemplate jt) {
        try {
            return jt.query("SELECT (*) FROM " + teacherView + " WHERE enabled=(FALSE) ORDER BY teacher_id", teachersRowMapper());
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "teacher_detail");
        }
    }

    public static List<TeacherEntity> getAllActiveTeachers(JdbcTemplate jt) {
        try {
            return jt.query("SELECT (*) FROM " + teacherView + " WHERE enabled=(TRUE) ORDER BY teacher_id", teachersRowMapper());
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "teacher_detail");
        }
    }

    public static PageListEntity<TeacherEntity> getListTeachers(JdbcTemplate jt, int pageNo, int pageSize, String searchParam, String searchValue) {
        if (searchParam.length() == 0 || searchValue.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "teacher_detail");

        try {
            return new PageListFactory<TeacherEntity>().fetchPage(
                    jt,
                    "SELECT count(*) FROM " + teacherView +" WHERE enabled=(TRUE) AND " + searchParam + " LIKE (?)",
                    "SELECT * FROM " + teacherView + " WHERE enabled=(TRUE) AND " + searchParam + " LIKE (?) ORDER BY teacher_id",
                    new Object[]{ searchValue.replace('*', '%') },
                    pageNo,
                    pageSize,
                    teachersRowMapper()
            );
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "teacher_detail");
        }
    }

    //-------------------------------------------------------------

    public static RowMapper<TeacherEntity> teachersRowMapper() {
        return new RowMapper<TeacherEntity>() {
            public TeacherEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                TeacherEntity actor = new TeacherEntity(rs.getString("teacher_id"),
                        rs.getString("name"), rs.getString("email"),
                        rs.getString("role"), rs.getString("username"),
                        "**********", rs.getString("enabled").equals("1"));
                return actor;
            }
        };
    }
}
package pt.isel.daw.data.repository;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pt.isel.daw.business.entities.*;
import pt.isel.daw.data.factory.PageListFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StudentRepository {
    public static String studentView = "(select student_id, name, email, role, username, password, enabled from " +
            "user U, student T, credentials CR where U.user_id = T.user_id and U.user_id = CR.user_id)";

    public static String createStudent(JdbcTemplate jt, StudentEntity student) {
        String studentId = student.getStudentId();
        String name = student.getName();
        String email = student.getEmail();
        String role = student.getRole();
        String username = student.getUsername();
        String password = student.getPassword();
        boolean enabled = student.isEnabled();

        if (studentId.length() == 0 || name.length() == 0 || email.length() == 0 || role.length() == 0 || username.length() == 0 || password.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "create_student");

        try {
            jt.update("INSERT INTO user (name, email, role) VALUES (?, ?, ?)", name, email, role);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "create_student");
        }
        int userId = jt.queryForObject("SELECT user_id FROM user WHERE email = (?)", new Object[]{email}, Integer.class);

        try {
            jt.update("INSERT INTO student (user_id, student_id) VALUES (?, ?)", userId, studentId);
            jt.update("INSERT INTO credentials (user_id, username, password, enabled) VALUES (?, ?, ?, ?)",
                    userId, username, password, enabled ? "1" : "0");
        } catch (Exception e) {
            jt.update("delete from credentials where user_id=(?)", userId);
            jt.update("delete from student where user_id=(?)", userId);
            jt.update("delete from user where user_id=(?)", userId);
        }

        try {
            return jt.queryForObject("select student_id from student where user_id = ?", new Object[] { userId } , String.class);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "create_student");
        }
    }

    public static String updateStudent(JdbcTemplate jt, StudentEntity student) {
        String studentId = student.getStudentId();
        String name = student.getName();
        String email = student.getEmail();
        String role = student.getRole();
        String username = student.getUsername();
        String password = student.getPassword();
        boolean enabled = student.isEnabled();

        if (studentId.length() == 0 || name.length() == 0 || email.length() == 0 || role.length() == 0 || username.length() == 0 || password.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "update_student");

        try {
            int userId = jt.queryForObject("select user_id from student where student_id = (?)", new Object[] { studentId }, Integer.class);
            jt.update("update user set name = (?), email = (?), role = (?) where user_id = ?", name, email, role, userId);
            jt.update("update credentials set  username = (?), password = (?), enabled = (?) where user_id = (?)", username, password, enabled?"1":"0", userId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "update_student");
        }

        return studentId;
    }

    public static void setStudentActive(JdbcTemplate jt, StudentEntity student) {
        try {
            int userId = jt.queryForObject("select user_id from student where student_id = (?)", new Object[] { student.getStudentId() }, Integer.class);

            jt.update("update credentials set enabled = (1), password = (?) where user_id = (?)", student.getPassword(), userId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "activate_student");
        }
    }

    public static StudentEntity getStudentBy(JdbcTemplate jt, String searchParam, String searchValue) {
        if (searchParam.length() == 0 || searchValue.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "student_detail");

        try {
            List<StudentEntity> students = jt.query("SELECT * FROM " + studentView + " WHERE " + searchParam + " LIKE (?)", new Object[] { searchValue }, studentsRowMapper());

            return students.get(0);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "student_detail");
        }
    }

    public static List<StudentEntity> getAllDeactivatedStudents(JdbcTemplate jt) {
        try {
            return jt.query("SELECT (*) FROM " + studentView + " WHERE enabled=(FALSE) ORDER BY student_id", studentsRowMapper());
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "student_detail");
        }
    }

    public static List<StudentEntity> getAllStudents(JdbcTemplate jt) {
        try {
            return jt.query("SELECT (*) FROM " + studentView + " WHERE enabled=(TRUE) ORDER BY student_id", studentsRowMapper());
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "student_detail");
        }
    }

    public static PageListEntity<StudentEntity> getListStudents(JdbcTemplate jt, int pageNo, int pageSize, String searchParam, String searchValue) {
        if (searchParam.length() == 0 || searchValue.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "student_detail");

        try {
            return new PageListFactory<StudentEntity>().fetchPage(
                    jt,
                    "SELECT count(*) FROM " + studentView +" WHERE enabled=(TRUE) AND " + searchParam + " LIKE (?)",
                    "SELECT * FROM " + studentView + " WHERE enabled=(TRUE) AND " + searchParam + " LIKE (?) ORDER BY student_id",
                    new Object[]{ searchValue.replace('*', '%') },
                    pageNo,
                    pageSize,
                    studentsRowMapper()
            );
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "student_detail");
        }
    }

    //-------------------------------------------------------------

    public static RowMapper<StudentEntity> studentsRowMapper() {
        return new RowMapper<StudentEntity>() {
            public StudentEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                StudentEntity actor = new StudentEntity(rs.getString("student_id"),
                        rs.getString("name"), rs.getString("email"),
                        rs.getString("role"), rs.getString("username"),
                        "**********", rs.getString("enabled").equals("1"));
                return actor;
            }
        };
    }
}
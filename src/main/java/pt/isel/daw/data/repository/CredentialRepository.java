package pt.isel.daw.data.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pt.isel.daw.business.entities.AuthenticatedUserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CredentialRepository {
    public static String getOpenIdUsername(JdbcTemplate jt, String openId) {
        String username = null;
        try {
            username = jt.queryForObject("SELECT (USERNAME) FROM CREDENTIALS WHERE OPENID=(?)",
                    new Object[] { openId }, String.class);
        } catch (EmptyResultDataAccessException e) {}
        return username;
    }

    public static AuthenticatedUserEntity getAuthenticatedUserInfo(JdbcTemplate jt, String username) {
        String teacherId = null;
        try {
            teacherId = jt.queryForObject("SELECT (TEACHER_ID) FROM TEACHER T, " +
                            "  (SELECT USER_ID FROM CREDENTIALS WHERE USERNAME=(?) AND ENABLED=(TRUE)) R1 " +
                            "WHERE T.USER_ID=R1.USER_ID",
                    new Object[] { username }, String.class);
        } catch (EmptyResultDataAccessException e) {}

        if (teacherId != null) {
            boolean isAdmin = SecurityRepository.isAdmin(jt, username);

            List<String> listTeacherClasses = jt.query(
                    "SELECT CO.ACRONYM, SEMESTER_ID, IDENTIFIER FROM CLASSES_TEACHERS CT, CLASS C ,COURSE CO " +
                            "WHERE CO.ACRONYM=C.COURSE_ACRONYM AND CT.CLASS_ID=C.CLASS_ID AND TEACHER_ID=(?)",
                    new Object[] { teacherId }, new RowMapper<String>() {
                        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                            String classId = rs.getString("acronym") + "/" +
                                    rs.getString("semester_id") + "/" + rs.getString("identifier");
                            return classId;
                        }
                    });

            String course = "";
            try {
                course = (String) jt.queryForObject(
                        "SELECT (ACRONYM) FROM COURSE WHERE COORDINATOR_ID=(?) AND ACTIVE=(TRUE)",
                        new Object[]{teacherId}, String.class);
            } catch (EmptyResultDataAccessException e) {}

            return new AuthenticatedUserEntity(teacherId, username, true, isAdmin, course, listTeacherClasses);
        }

        try {
            String studentId = jt.queryForObject("SELECT (STUDENT_ID) FROM STUDENT S, " +
                            "  (SELECT USER_ID FROM CREDENTIALS WHERE USERNAME=(?) AND ENABLED=(TRUE)) R1 " +
                            "WHERE S.USER_ID=R1.USER_ID",
                    new Object[] { username }, String.class);

            List<String> listStudenteClasses = jt.query(
                    "SELECT CO.ACRONYM, SEMESTER_ID, IDENTIFIER FROM CLASSES_STUDENTS CS, CLASS C ,COURSE CO\n" +
                            "WHERE CO.ACRONYM=C.COURSE_ACRONYM AND CS.CLASS_ID=C.CLASS_ID AND STUDENT_ID=(?)",
                    new Object[] { studentId }, new RowMapper<String>() {
                        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                            String classId = rs.getString("acronym") + "/" +
                                    rs.getString("semester_id") + "/" + rs.getString("identifier");
                            return classId;
                        }
                    });

            return new AuthenticatedUserEntity(studentId, username, false, false,"", listStudenteClasses);
        } catch (Exception e) {}

        return new AuthenticatedUserEntity("","login", false, false, "", null);
    }
}
package pt.isel.daw.data.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityRepository {

    /* Only administrators and teachers can view users info */
//    public static boolean getUserInfoRestriction(JdbcTemplate jt) { //
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        return !(SecurityRepository.isAdmin(jt, username) ||
//                SecurityRepository.isTeacher(jt, username) );
//    }

    /*  A class can be created or edited by the course's coordinator or by an administrator */
    public static boolean getCreateClassRestriction(JdbcTemplate jt, String courseAcronym) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return !( SecurityRepository.isAdmin(jt, username) ||
                SecurityRepository.isCoordInCourse(jt, username, courseAcronym) );
    }

    /* The set of students can be edited by the course coordinator, by any class teacher or by an administrator. */
    /* If the class has auto-enrollment enabled, then the students can also join or leave a class. */
    public static boolean getJoinEditStudentsClassRestriction(JdbcTemplate jt, String classId, String studentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return !( SecurityRepository.isAdmin(jt, username) ||
                SecurityRepository.isCoordInCourseByClass(jt, username, classId) ||
                SecurityRepository.isTeacherInClass(jt, username, classId) ||
                (SecurityRepository.isClassAutoEnroll(jt, classId) && SecurityRepository.isStudent(jt, username, studentId)) );
    }

    /* Any student can join a group if it is enrolled in the class and the group's student limit wasn't reached yet */
    public static boolean getCreateGroupRestriction(JdbcTemplate jt, String classId, String groupId, String studentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return !( SecurityRepository.isAdmin(jt, username) ||
                SecurityRepository.isCoordInCourseByClass(jt, username, classId) ||
                SecurityRepository.isTeacherInClass(jt, username, classId) ||
                (SecurityRepository.isStudent(jt, username, studentId) &&
                        SecurityRepository.isStudentEnrolledInClass(jt, username, classId)) );
    }

    /* Any student can join a group if it is enrolled in the class and the group's student limit wasn't reached yet */
    public static boolean getJoinGroupRestriction(JdbcTemplate jt, String classId, String groupId, String studentId) {
        return !( getCreateGroupRestriction(jt, classId, groupId, studentId) ||
                        SecurityRepository.isGroupLimitNotReached(jt, classId, groupId) );
    }

    /* Any student can leave a group. */
    public static boolean getleaveGroupRestriction(JdbcTemplate jt, String classId, String studentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return !( SecurityRepository.isAdmin(jt, username) ||
                SecurityRepository.isCoordInCourseByClass(jt, username, classId) ||
                SecurityRepository.isTeacherInClass(jt, username, classId) ||
                SecurityRepository.isStudent(jt, username, studentId) );
    }
    
    //-------------------------------------------------------------------------------
    
    public static boolean isAdmin(JdbcTemplate jt, String username) {
        int count = (int) jt.queryForObject(
                "SELECT count(*) from CREDENTIALS C, USER U WHERE USERNAME=(?) AND C.USER_ID=U.USER_ID and ROLE='ROLE_ADMIN'",
                new Object[] { username }, Integer.class);
        return count > 0;
    }

//    public static boolean isTeacher(JdbcTemplate jt, String username) {
//        int count = (int) jt.queryForObject(
//                "SELECT count(*) FROM TEACHER T, (SELECT USER_ID from CREDENTIALS WHERE USERNAME=(?)) C WHERE C.USER_ID=T.USER_ID",
//                new Object[] { username }, Integer.class);
//        return count > 0;
//    }

    public static boolean isStudent(JdbcTemplate jt, String username, String studentId) {
        int count = (int) jt.queryForObject(
                "SELECT count(*) FROM STUDENT S, (SELECT USER_ID from CREDENTIALS WHERE USERNAME=(?)) C WHERE C.USER_ID=S.USER_ID AND S.STUDENT_ID=(?)",
                new Object[] { username, studentId }, Integer.class);
        return count > 0;
    }

    public static boolean isCoordInCourse(JdbcTemplate jt, String username, String courseAcronym) {
        int count = (int) jt.queryForObject("SELECT count(*) FROM TEACHER T, COURSE CO, "+
            "(SELECT USER_ID from CREDENTIALS C, USER U WHERE USERNAME=(?) AND C.USER_ID=U.USER_ID) X1 "+
            "WHERE T.USER_ID=X1.USER_ID AND CO.COORDINATOR_ID=T.TEACHER_ID AND CO.ACRONYM=(?)",
                new Object[] { username, courseAcronym }, Integer.class);
        return count > 0;
    }

    public static boolean isCoordInCourseByClass(JdbcTemplate jt, String username, String classId) {
        int count = (int) jt.queryForObject("SELECT count(*) FROM TEACHER T, COURSE CO, CLASS C, " +
                "(SELECT USER_ID from CREDENTIALS C, USER U WHERE USERNAME=(?) AND C.USER_ID=U.USER_ID) X1 " +
                "WHERE T.USER_ID=X1.USER_ID AND CO.COORDINATOR_ID=T.TEACHER_ID " +
                "AND CO.ACRONYM=C.COURSE_ACRONYM AND CLASS_ID=(?)",
                new Object[] { username, classId }, Integer.class);
        return count > 0;
    }

    public static boolean isTeacherInClass(JdbcTemplate jt, String username, String classId) {
        int count = (int) jt.queryForObject(
                "SELECT count(*) FROM TEACHER T, " +
                        "(SELECT USER_ID FROM CREDENTIALS WHERE USERNAME=(?)) C, " +
                        "(SELECT TEACHER_ID, CLASS_ID FROM CLASSES_TEACHERS) CT " +
                        "WHERE C.USER_ID=T.USER_ID AND T.TEACHER_ID=CT.TEACHER_ID AND CLASS_ID=(?)",
                new Object[] { username, classId }, Integer.class);
        return count > 0;
    }

    public static boolean isClassAutoEnroll(JdbcTemplate jt, String classId) {
        int count = (int) jt.queryForObject("SELECT count(*) FROM CLASS " +
                        "WHERE AUTO_ENROLLMENT=TRUE AND CLASS_ID=(?)",
                new Object[] { classId }, Integer.class);
        return count > 0;
    }

    public static boolean isStudentEnrolledInClass(JdbcTemplate jt, String username, String classId) {
        int count = (int) jt.queryForObject("SELECT count(*) FROM STUDENT S, " +
            "(SELECT USER_ID from CREDENTIALS C, USER U WHERE USERNAME=(?) AND C.USER_ID=U.USER_ID) X1, " +
            "(SELECT STUDENT_ID, CLASS_ID FROM CLASSES_STUDENTS) CS " +
            "WHERE S.USER_ID=X1.USER_ID AND S.STUDENT_ID=CS.STUDENT_ID AND CLASS_ID=(?)",
                new Object[] { username, classId }, Integer.class);
        return count > 0;
    }

    public static boolean isGroupLimitNotReached(JdbcTemplate jt, String classId, String groupId) {
        int count = (int) jt.queryForObject("SELECT count(*) FROM " +
                        "(SELECT C.CLASS_ID, GROUP_ID ,count(GROUP_ID) AS students_in_group, MAX_STUDENTS_GROUP " +
                        "FROM GROUPS G, CLASS C WHERE G.CLASS_ID=C.CLASS_ID " +
                        "GROUP BY C.CLASS_ID, GROUP_ID, C.MAX_STUDENTS_GROUP) " +
                        "WHERE (students_in_group<MAX_STUDENTS_GROUP) AND CLASS_ID=(?) AND GROUP_ID=(?)",
                new Object[] { classId, groupId }, Integer.class);
        return count > 0;
    }
}

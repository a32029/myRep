package pt.isel.daw.data.repository;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pt.isel.daw.business.entities.*;
import pt.isel.daw.data.factory.PageListFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ClassRepository {
    public static String createClass(JdbcTemplate jt, ClassEntity classEntity) {
        String identifier = classEntity.getIdentifier().trim();
        String courseAcronym = classEntity.getCourseAcronym().trim();
        String semesterId = classEntity.getSemesterId().trim();
        String classId = courseAcronym + "/" + semesterId + "/" + identifier;
        boolean autoEnrollment = classEntity.isAutoEnrollment();
        String maxStudentsPerGroup = classEntity.getMaxStudentsPerGroup();
        boolean active = classEntity.isActive();

        if (identifier.length() == 0 || semesterId.length() == 0 || courseAcronym.length() == 0 || maxStudentsPerGroup.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "create_class");

        if (SecurityRepository.getCreateClassRestriction(jt, courseAcronym))
            throw new ControllerExceptionEntity("type", "Restriction!", HttpStatus.FORBIDDEN, "A class can be created or edited by the course's coordinator or by an administrator.", "create_class");

        try {
            jt.update("INSERT INTO class (class_id, identifier, course_acronym, semester_id, auto_enrollment, max_students_group, active) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    classId, identifier, courseAcronym, semesterId, autoEnrollment, maxStudentsPerGroup, active);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "create_class");
        }
        return classId;
    }

    public static String updateClass(JdbcTemplate jt, ClassEntity classEntity) {
        String classId = classEntity.getClassId();
        String courseAcronym = classEntity.getCourseAcronym();
        boolean autoEnrollment = classEntity.isAutoEnrollment();
        String maxStudentsPerGroup = classEntity.getMaxStudentsPerGroup();
        boolean active = classEntity.isActive();

        if (classId.length() == 0 || courseAcronym.length() == 0 || maxStudentsPerGroup.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "update_class");

        if (SecurityRepository.getCreateClassRestriction(jt, courseAcronym))
            throw new ControllerExceptionEntity("type", "Restriction!", HttpStatus.FORBIDDEN, "A class can be created or edited by the course's coordinator or by an administrator.", "update_class");

        try {
            jt.update("update class set auto_enrollment = (?), max_students_group = (?), active = (?) where class_id = (?)", autoEnrollment, maxStudentsPerGroup, active, classId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "update_class");
        }

        return classId;
    }

    public static void setClassActive(JdbcTemplate jt, String classId, boolean active) {
        try {
            jt.update("update class set active = (?) where class_id = (?)", active, classId.trim());
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "activate_course");
        }
    }

    public static ClassEntity getClassBy(JdbcTemplate jt, String searchParam, String searchValue) {
        if (searchParam.length() == 0 || searchValue.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "list_groups_class");

        try {
            List<ClassEntity> classes = jt.query("SELECT (*) FROM class WHERE " + searchParam + "=(?)", new Object[] { searchValue }, classRowMapper());

            return classes.get(0);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "class_detail");
        }
    }

    public static List<ClassEntity> getAllDeactivatedClasses(JdbcTemplate jt, String courseAcronym) {
        try {
            return jt.query("SELECT (*) FROM class WHERE active=FALSE AND course_acronym=('"+ courseAcronym + "') ORDER BY class_id", classRowMapper());
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "class_detail");
        }
    }

    public static PageListEntity<ClassEntity> getListClasses(JdbcTemplate jt, String courseAcronym, int pageNo, int pageSize, String searchParam, String searchValue) {
        if (courseAcronym.length() == 0 || searchParam.length() == 0 || searchValue.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "list_groups_class");

        try {
            return new PageListFactory<ClassEntity>().fetchPage(
                    jt,
                    "SELECT count(*) FROM class WHERE active=true AND course_acronym=('" + courseAcronym + "') AND " + searchParam + " LIKE (?)",
                    "SELECT (*) FROM class WHERE active=true AND course_acronym=('" + courseAcronym + "') AND " + searchParam + " LIKE (?) ORDER BY class_id",
                    new Object[]{ searchValue.replace('*', '%') },
                    pageNo,
                    pageSize,
                    classRowMapper()
            );
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "list_classes");
        }
    }

    //-------------------------------------------------------------

    public static void addTeacherToClass(JdbcTemplate jt, String classId, String teacherId) {
        if (classId.length() == 0 || teacherId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "add_teacher_class");

        if (SecurityRepository.getCreateClassRestriction(jt, classId))
            throw new ControllerExceptionEntity("type", "Restriction!", HttpStatus.FORBIDDEN, "A class can be created or edited by the course's coordinator or by an administrator.", "add_teacher_class");

        try {
            jt.update("insert into classes_teachers (class_id, teacher_id) values (?, ?)", classId, teacherId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "add_teacher_class");
        }
    }

    public static void removeTeacherFromClass(JdbcTemplate jt, String classId, String teacherId) {
        if (classId.length() == 0 || teacherId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "remove_teacher_class");

        if (SecurityRepository.getCreateClassRestriction(jt, classId))
            throw new ControllerExceptionEntity("type", "Restriction!", HttpStatus.FORBIDDEN, "A class can be created or edited by the course's coordinator or by an administrator.", "remove_teacher_class");

        try {
            jt.update("delete from classes_teachers where class_id=(?) and teacher_id=(?)", classId, teacherId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "remove_teacher_class");
        }
    }

    public static TeacherEntity getTeacherInClass(JdbcTemplate jt, String classId, String teacherId) {
        if (classId.length() == 0 || teacherId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "teacher_class_detail");

        try {
            List<TeacherEntity> teachers = jt.query("select (T1.*) from classes_teachers, " +
                    TeacherRepository.teacherView + " T1 " +
                    "where class_id=(?) and teacher_id=(?) and T1.teacher_id=teacher_id", new Object[] { classId, teacherId }, TeacherRepository.teachersRowMapper());

            return teachers.get(0);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "teacher_class_detail");
        }
    }

    public static List<TeacherEntity> getAllTeachersInClass(JdbcTemplate jt, String classId) {
        if (classId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "teacher_class_detail");

        try {
            return jt.query("select (T1.*) from classes_teachers, " + TeacherRepository.teacherView + " T1 " +
                    "where class_id=(?) and T1.teacher_id=teacher_id", new Object[] { classId }, TeacherRepository.teachersRowMapper());
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "teacher_class_detail");
        }
    }

    public static PageListEntity<TeacherEntity> getPageListTeachersInClass(JdbcTemplate jt, String classId, int pageNo, int pageSize) {
        if (classId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "list_teachers_class");

        try {
            return new PageListFactory<TeacherEntity>().fetchPage(
                    jt,
                    "SELECT count(*) FROM classes_teachers WHERE class_id=(?)",
                    "SELECT (T1.*) FROM classes_teachers, " +
                            TeacherRepository.teacherView + " T1 " +
                            "WHERE class_id=(?) AND T1.teacher_id=teacher_id ORDER BY teacher_id",
                    new Object[]{ classId },
                    pageNo,
                    pageSize,
                    TeacherRepository.teachersRowMapper()
            );
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "list_teachers_class");
        }
    }

    //-------------------------------------------------------------

    public static void addStudentToClass(JdbcTemplate jt, String classId, String studentId) {
        if (classId.length() == 0 || studentId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "add_student_class");

        if (SecurityRepository.getJoinEditStudentsClassRestriction(jt, classId, studentId))
            throw new ControllerExceptionEntity("type", "Restriction!", HttpStatus.FORBIDDEN, "The set of students can be " +
                    "edited by the course coordinator, by any class teacher or by an administrator. " +
                    "If the class has auto-enrollment enabled, then the students can also join or leave a class", "add_student_class");

        try {
            jt.update("insert into classes_students (class_id, student_id) values (?, ?)", classId, studentId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "add_student_class");
        }
    }

    public static void removeStudentFromClass(JdbcTemplate jt, String classId, String studentId) {
        if (classId.length() == 0 || studentId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "remove_student_class");

        if (SecurityRepository.getJoinEditStudentsClassRestriction(jt, classId, studentId))
            throw new ControllerExceptionEntity("type", "Restriction!", HttpStatus.FORBIDDEN, "The set of students can be " +
                    "edited by the course coordinator, by any class teacher or by an administrator. " +
                    "If the class has auto-enrollment enabled, then the students can also join or leave a class", "remove_student_class");

        try {
            jt.update("delete from classes_students where class_id=(?) and student_id=(?)", classId, studentId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "remove_student_class");
        }
    }

    public static StudentEntity getStudentInClass(JdbcTemplate jt, String classId, String studentId) {
        if (classId.length() == 0 || studentId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "student_class_detail");

        try {
            List<StudentEntity> students = jt.query("select (T1.*) from classes_students, " +
                    StudentRepository.studentView + " T1 " +
                    "where class_id=(?) and student_id=(?) and T1.student_id=student_id", new Object[] { classId, studentId }, StudentRepository.studentsRowMapper());

            return students.get(0);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "student_class_detail");
        }
    }

    public static List<StudentEntity> getAllStudentsInClass(JdbcTemplate jt, String classId) {
        if (classId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "student_class_detail");

        try {
            return jt.query("select (T1.*) from classes_students, " + StudentRepository.studentView + " T1 " +
                    "where class_id=(?) and T1.student_id=student_id", new Object[] { classId }, StudentRepository.studentsRowMapper());
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "student_class_detail");
        }
    }

    public static PageListEntity<StudentEntity> getPageListStudentsInClass(JdbcTemplate jt, String classId, int pageNo, int pageSize) {
        if (classId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "list_students_class");

        try {
            return new PageListFactory<StudentEntity>().fetchPage(
                    jt,
                    "SELECT count(*) FROM classes_students WHERE class_id=(?)",
                    "SELECT (T1.*) FROM classes_students, " +
                            StudentRepository.studentView + " T1 " +
                            "WHERE class_id=(?) AND T1.student_id=student_id ORDER BY student_id",
                    new Object[]{ classId },
                    pageNo,
                    pageSize,
                    StudentRepository.studentsRowMapper()
            );
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "list_students_class");
        }
    }

    //-------------------------------------------------------------

    public static RowMapper<ClassEntity> classRowMapper() {
        return new RowMapper<ClassEntity>() {
            public ClassEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                ClassEntity actor = new ClassEntity(rs.getString("class_id"), rs.getString("identifier"),
                        rs.getString("course_acronym"), rs.getString("semester_id"),
                        Boolean.parseBoolean(rs.getString("auto_enrollment")),
                        rs.getString("max_students_group"), rs.getBoolean("active"));
                return actor;
            }
        };
    }
}
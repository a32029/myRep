package pt.isel.daw.data.repository;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import pt.isel.daw.business.entities.ControllerExceptionEntity;
import pt.isel.daw.business.entities.*;
import pt.isel.daw.data.factory.PageListFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GroupRepository {
    public static String groupView = "(select class_id, group_id, student_id, name, email, role from user u, student s, groups g where u.user_id=s.user_id and student_id=g.student_id)";

    public static void createGroupClass(JdbcTemplate jt, String classId, String groupId, String studentId) {
        if (classId.length() == 0 || groupId.length() == 0 || studentId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "join_group_class");


        if (SecurityRepository.getCreateGroupRestriction(jt, classId, groupId, studentId))
            throw new ControllerExceptionEntity("type", "Restriction!", HttpStatus.LENGTH_REQUIRED,
                    "Any student can join a group if it is enrolled in the class and the group's student limit wasn't reached yet.", "join_group_class");

        try {
            jt.update("insert into groups (class_id, group_id, student_id) values (?, ?, ?)", classId, groupId, studentId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "join_group_class");
        }
    }

    public static void joinGroupClass(JdbcTemplate jt, String classId, String groupId, String studentId) {
        if (classId.length() == 0 || groupId.length() == 0 || studentId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "join_group_class");


        if (SecurityRepository.getJoinGroupRestriction(jt, classId, groupId, studentId))
            throw new ControllerExceptionEntity("type", "Restriction!", HttpStatus.LENGTH_REQUIRED,
                    "Any student can join a group if it is enrolled in the class and the group's student limit wasn't reached yet.", "join_group_class");

        try {
            jt.update("insert into groups (class_id, group_id, student_id) values (?, ?, ?)", classId, groupId, studentId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "join_group_class");
        }
    }

    public static void leaveGroupClass(JdbcTemplate jt, String classId, String groupId, String studentId) {
        if (classId.length() == 0 || groupId.length() == 0 || studentId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "leave_group_class");

        if (SecurityRepository.getleaveGroupRestriction(jt, classId, studentId))
            throw new ControllerExceptionEntity("type", "Restriction!", HttpStatus.LENGTH_REQUIRED, "Any student can leave a group.", "leave_group_class");

        try {
            jt.update("delete from groups where class_id=(?) and group_id=(?) and student_id=(?)", classId, groupId, studentId);
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "leave_group_class");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    public static List<GroupDetailEntity> getGroupDetailInClass(JdbcTemplate jt, String classId, String groupId) {
        if (classId.length() == 0 || groupId.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "group_class_detail");

        try {
            return jt.query("select (*) from " + groupView + " WHERE class_id=(?) and group_id=(?)", new Object[] { classId, groupId }, groupDetailRowMapper());
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "group_class_detail");
        }
    }

    public static PageListEntity<GroupDetailEntity> getPageListGroupsDetailInClass(JdbcTemplate jt, String classId, int pageNo, int pageSize, String searchParam, String searchValue) {
        if (classId.length() == 0 || searchParam.length() == 0 || searchValue.length() == 0)
            throw new ControllerExceptionEntity("type", "Missing information!", HttpStatus.LENGTH_REQUIRED, "Some fields have zero lenght!", "list_groups_class");

        try {
            return new PageListFactory<GroupDetailEntity>().fetchPage(
                    jt,
                    "SELECT count(*) FROM " + groupView +" WHERE class_id=('" + classId + "') AND " + searchParam + " LIKE (?)",
                    "SELECT (*) FROM " + groupView + " WHERE class_id=('" + classId + "') AND " + searchParam + " LIKE (?) ORDER BY group_id",
                    new Object[]{ searchValue.replace('*', '%') },
                    pageNo,
                    pageSize,
                    groupDetailRowMapper()
            );
        } catch (Exception e) {
            throw new ControllerExceptionEntity("type", "SQL Exception!", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "list_groups_class");
        }
    }

    //------------------------------------------------------------------------------------------------------------------


    public static RowMapper<GroupDetailEntity> groupDetailRowMapper() {
        return new RowMapper<GroupDetailEntity>() {
            public GroupDetailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                GroupDetailEntity actor = new GroupDetailEntity(rs.getString("class_id"), rs.getString("group_id"),
                        rs.getString("student_id"), rs.getString("name"),
                        rs.getString("email"), rs.getString("role"));
                return actor;
            }
        };
    }
}
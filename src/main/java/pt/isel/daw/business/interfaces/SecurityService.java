package pt.isel.daw.business.interfaces;

import org.springframework.jdbc.core.JdbcTemplate;

public interface SecurityService {
    /* Only administrators and teachers can view users info */
    boolean getUserInfoRestriction();

    /*  A class can be created or edited by the course's coordinator or by an administrator */
    boolean getCreateClassRestriction(int classId);

    /* The set of students can be edited by the course coordinator, by any class teacher or by an administrator */
    boolean getEditStudentsRestriction(int classId);

    /* If the class has auto-enrollment enabled, then the students can also join or leave a class */
    boolean getJoinClassRestriction(int classId);

    /* Any student can join a group if it is enrolled in the class and the group's student limit wasn't reached yet */
    boolean getJoinGroupRestriction(int classId, int groupId);
    
    /* Any student can leave a group */
    boolean getleaveGroupRestriction(int classId);
}
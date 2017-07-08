package pt.isel.daw.business.interfaces;

import org.springframework.http.ResponseEntity;

public interface GroupClassService {
    ResponseEntity getPageListGroupsInClass(String classId, int pageNo, int pageSize, String searchParam, String searchValue);
    ResponseEntity getGroupDetail(String classId, int pageNo, int pageSize, String searchParam, String searchValue);
    ResponseEntity createGroupClass(String classId);
    ResponseEntity joinGroupClass(String classId, String groupId);
    ResponseEntity leaveGroupClass(String classId, String groupId);

    ResponseEntity createGroupClass(String classId, String groupId, String studentId);
    ResponseEntity joinGroupClass(String classId, String groupId, String studentId);
    ResponseEntity leaveGroupClass(String classId, String groupId, String studentId);
}
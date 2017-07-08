package pt.isel.daw.business.interfaces;

import org.springframework.http.ResponseEntity;

public interface TeacherClassService {
    ResponseEntity getPageListTeachersInClass(String classId, int pageNo, int pageSize);
    ResponseEntity getTeacherClassDetail(String classId, String teacherId);
    ResponseEntity addTeacherToClass(String classId);
    ResponseEntity removeTeacherFromClass(String classId);

    ResponseEntity addTeacherToClass(String classId, String teacherId);
    ResponseEntity removeTeacherFromClass(String classId, String teacherId);
}
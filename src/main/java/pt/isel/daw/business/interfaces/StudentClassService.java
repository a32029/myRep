package pt.isel.daw.business.interfaces;

import org.springframework.http.ResponseEntity;

public interface StudentClassService {
    ResponseEntity getPageListStudentsInClass(String classId, int pageNo, int pageSize);
    ResponseEntity getStudentClassDetail(String classId, String studentId);
    ResponseEntity addStudentToClass(String classId);
    ResponseEntity removeStudentFromClass(String classId);

    ResponseEntity addStudentToClass(String classId, String studentId);
    ResponseEntity removeStudentFromClass(String classId, String studentId);
}
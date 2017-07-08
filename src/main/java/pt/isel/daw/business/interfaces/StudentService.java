package pt.isel.daw.business.interfaces;

import org.springframework.http.ResponseEntity;
import pt.isel.daw.business.dto.*;

public interface StudentService {
    ResponseEntity getListStudents(int pageNo, int pageSize, String searchParam, String searchValue);
    ResponseEntity createStudent();
    ResponseEntity activateStudent();
    ResponseEntity updateStudent(String studentId);
    ResponseEntity getStudentDetail(String searchParam, String searchValue);

    ResponseEntity createStudent(StudentDTO student);
    ResponseEntity updateStudent(StudentDTO student);
    ResponseEntity setStudentActive(StudentListDTO student);
}
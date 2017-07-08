package pt.isel.daw.business.interfaces;

import org.springframework.http.ResponseEntity;
import pt.isel.daw.business.dto.TeacherDTO;
import pt.isel.daw.business.dto.TeacherListDTO;

public interface TeacherService {
    ResponseEntity getListTeachers(int pageNo, int pageSize, String searchParam, String searchValue);
    ResponseEntity createTeacher();
    ResponseEntity activateTeacher();
    ResponseEntity updateTeacher(String teacherId);
    ResponseEntity getTeacherDetail(String searchParam, String searchValue);

    ResponseEntity createTeacher(TeacherDTO teacher);
    ResponseEntity updateTeacher(TeacherDTO teacher);
    ResponseEntity setTeacherActive(TeacherListDTO teacher);
}
package pt.isel.daw.business.interfaces;

import org.springframework.http.ResponseEntity;
import pt.isel.daw.business.dto.ClassDTO;

import java.util.List;

public interface ClassService {
    ResponseEntity getListClasses(String courseAcronym, int pageNo, int pageSize, String searchParam, String searchValue);
    ResponseEntity createClass(String courseAcronym);
    ResponseEntity activateClass(String courseAcronym);
    ResponseEntity getClassDetail(String searchParam, String searchValue);
    ResponseEntity updateClass(String classId);

    ResponseEntity createClass(ClassDTO classDTO);
    ResponseEntity updateClass(ClassDTO classDTO);
    ResponseEntity setClassActive(String classId, boolean active);
}
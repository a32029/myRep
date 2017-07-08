package pt.isel.daw.business.interfaces;

import org.springframework.http.ResponseEntity;
import pt.isel.daw.business.dto.CourseDTO;

public interface CourseService {
    ResponseEntity getListCourses(int pageNo, int pageSize, String searchParam, String searchValue);
    ResponseEntity createCourse();
    ResponseEntity activateCourse();
    ResponseEntity getCourseDetail(String searchParam, String searchValue);
    ResponseEntity updateCourse(String acronym);

    ResponseEntity createCourse(CourseDTO course);
    ResponseEntity updateCourse(CourseDTO course);
    ResponseEntity setCourseActive(String acronym, boolean active);
}
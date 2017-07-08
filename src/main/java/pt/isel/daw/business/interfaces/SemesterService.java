package pt.isel.daw.business.interfaces;

import org.springframework.http.ResponseEntity;
import pt.isel.daw.business.dto.*;

import java.util.List;

public interface SemesterService {
    ResponseEntity getListSemesters(int pageNo, int pageSize, String searchParam, String searchValue);
    ResponseEntity createSemester();
    ResponseEntity removeSemester();
    ResponseEntity getSemesterDetail(String searchParam, String searchValue);

    ResponseEntity createSemester(SemesterDTO semester);
    ResponseEntity removeSemester(String semesterId);
}
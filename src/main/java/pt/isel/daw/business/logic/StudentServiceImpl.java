package pt.isel.daw.business.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import pt.isel.daw.business.dto.*;
import pt.isel.daw.business.entities.*;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.business.interfaces.StudentService;
import pt.isel.daw.data.factory.ContainerFactory;
import pt.isel.daw.data.factory.DTOFactory;
import pt.isel.daw.data.factory.ResponseEntityFactory;
import pt.isel.daw.data.repository.StudentRepository;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private DataSourceTransactionManager transactionManager;

    public StudentServiceImpl(DataSourceTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    @Autowired
    JdbcTemplate jt;

    @Autowired
    private ConstantService constantService;

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getListStudents(int pageNo, int pageSize, String searchParam, String searchValue) {
        PageListEntity<StudentEntity> pl = StudentRepository.getListStudents(jt, pageNo, pageSize, searchParam, searchValue);
        PageListDTO<StudentDTO> plDTO = DTOFactory.convertToDto(pl);

        Resource resource = new Resource(ContainerFactory.createListContainer(jt, constantService, plDTO),
                new Link(constantService.getConstant("api.list_students_templated", new Object[]{pageNo, pageSize, searchParam, searchValue})).withSelfRel());

        resource.add(new Link(constantService.getConstant("api.create_student")).withRel("create_student"));
        resource.add(new Link(constantService.getConstant("api.activate_student")).withRel("activate_student"));

        List<StudentDTO> studentsDTO = plDTO.getPageItems();
        for (StudentDTO studentDTO:studentsDTO) {
            String studentId = studentDTO.getStudentId();
            resource.add(new Link(constantService.getConstant("api.student_detail_templated", new Object[] {studentDTO.getStudentId()})).withRel("get_student_" + studentId));
        }

        if (plDTO.hasNextPage())
            resource.add(new Link(constantService.getConstant("api.list_students_templated", new Object[]{plDTO.getPageNumber()+1, pageSize, searchParam, searchValue})).withRel("next"));
        if (plDTO.hasPrevPage())
            resource.add(new Link(constantService.getConstant("api.list_students_templated", new Object[]{plDTO.getPageNumber()-1, pageSize, searchParam, searchValue})).withRel("prev"));

        resource.add(new Link(constantService.getConstant("docs.list_students")).withRel("docs_list_students"));

        return new ResponseEntity(resource, HttpStatus.OK);
    }

    public ResponseEntity createStudent() {
        StudentDTO student = new StudentDTO("", "", "", "", "", "", true);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.studentFormFactory(jt, constantService, student), constantService.getConstant("api.create_student"));
        responseEntity.add(constantService.getConstant("resource.create_student"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_students"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.create_student"), "docs_create_student", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity activateStudent() {
        StudentListDTO studentsDTO = new StudentListDTO();
        List<StudentEntity> deactivatedStudents = StudentRepository.getAllDeactivatedStudents(jt);
        List<StudentDTO> deactivatedStudentsDTO = DTOFactory.convertToDto(deactivatedStudents);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.activateStudentFormFactory(jt, constantService, studentsDTO, deactivatedStudentsDTO), constantService.getConstant("api.activate_student"));
        responseEntity.add(constantService.getConstant("resource.activate_student"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_students"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.activate_student"), "docs_activate_student", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getStudentDetail(String searchParam, String searchValue) {
        StudentEntity studentEntity = StudentRepository.getStudentBy(jt, searchParam, searchValue);
        StudentDTO studentDTO = DTOFactory.convertToDto(studentEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.createDetailContainer(jt, constantService, studentDTO),
                constantService.getConstant("api.student_detail_templated", new Object[] {studentDTO.getStudentId()}));
        responseEntity.add(new Link(constantService.getConstant("api.list_courses")).withRel("list_courses"), false);
        responseEntity.add(new Link(constantService.getConstant("api.update_student_templated", new Object[] {studentDTO.getStudentId()})).withRel("update_student"), false);
        responseEntity.add(constantService.getConstant("docs.student_detail"), "docs_student_detail", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity updateStudent(String classId) {
        StudentEntity studentEntity = StudentRepository.getStudentBy(jt, "student_id", classId);
        StudentDTO studentDTO = DTOFactory.convertToDto(studentEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.studentFormFactory(jt, constantService, studentDTO), constantService.getConstant("api.update_student"));
        responseEntity.add(constantService.getConstant("resource.update_student"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_students"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.update_student"), "docs_update_student", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity createStudent(StudentDTO studentDTO) {
        StudentEntity studentEntity = DTOFactory.convertToEntity(studentDTO);
        String studentId = StudentRepository.createStudent(jt, studentEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("create_student", constantService.getConstant("resource.create_student"));
        responseEntity.add(new Link(constantService.getConstant("api.student_detail_templated", new Object[] {studentId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity updateStudent(StudentDTO studentDTO) {
        StudentEntity studentEntity = DTOFactory.convertToEntity(studentDTO);
        String studentId = StudentRepository.updateStudent(jt, studentEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("update_student", constantService.getConstant("resource.update_student"));
        responseEntity.add(new Link(constantService.getConstant("api.student_detail_templated", new Object[] {studentId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity setStudentActive(StudentListDTO studentDTO) {
        StudentEntity studentEntity = DTOFactory.convertToEntity(studentDTO);
        StudentRepository.setStudentActive(jt, studentEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("activate_student", constantService.getConstant("resource.activate_student"));
        responseEntity.add(constantService.getConstant("api.list_students"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.activate_student"), "docs_activate_student", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }
}
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
import pt.isel.daw.business.interfaces.StudentClassService;
import pt.isel.daw.data.factory.ContainerFactory;
import pt.isel.daw.data.factory.DTOFactory;
import pt.isel.daw.data.factory.ResponseEntityFactory;
import pt.isel.daw.data.repository.ClassRepository;
import pt.isel.daw.data.repository.StudentRepository;

import java.util.List;

public class StudentClassServiceImpl implements StudentClassService {
    private DataSourceTransactionManager transactionManager;

    public StudentClassServiceImpl(DataSourceTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    @Autowired
    JdbcTemplate jt;

    @Autowired
    private ConstantService constantService;

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getPageListStudentsInClass(String classId, int pageNo, int pageSize) {
        PageListEntity<StudentEntity> pl = ClassRepository.getPageListStudentsInClass(jt, classId, pageNo, pageSize);
        PageListDTO<StudentDTO> plDTO = DTOFactory.convertToDto(pl);

        Resource studentsClass = new Resource(ContainerFactory.createListContainer(jt, constantService, plDTO),
                new Link(constantService.getConstant("api.list_students_class_templated", new Object[]{classId, pageNo, pageSize})).withSelfRel());

        studentsClass.add(new Link(constantService.getConstant("api.add_student_class_templated", new Object[]{ classId })).withRel("add_student_class"));
        studentsClass.add(new Link(constantService.getConstant("api.remove_student_class_templated", new Object[]{ classId })).withRel("remove_student_class"));

        List<StudentDTO> studentsDTO = plDTO.getPageItems();
        for (StudentDTO studentDTO:studentsDTO) {
            String student_id = studentDTO.getStudentId();
            studentsClass.add(new Link(constantService.getConstant("api.student_detail_templated", new Object[] {studentDTO.getStudentId()})).withRel("get_student_" + student_id));
        }

        if (plDTO.hasNextPage())
            studentsClass.add(new Link(constantService.getConstant("api.list_students_class_templated", new Object[]{classId, plDTO.getPageNumber()+1, pageSize})).withRel("next"));
        if (plDTO.hasPrevPage())
            studentsClass.add(new Link(constantService.getConstant("api.list_students_class_templated", new Object[]{classId, plDTO.getPageNumber()-1, pageSize})).withRel("prev"));
        else
            studentsClass.add(new Link(constantService.getConstant("api.class_detail_templated", new Object[] {classId})).withRel("prev"));

        return new ResponseEntity(studentsClass, HttpStatus.OK);
    }

    public ResponseEntity addStudentToClass(String classId) {
        ClassStudentDTO classStudentDTO = new ClassStudentDTO(classId,"");
        List<StudentEntity> studentsEntity = StudentRepository.getAllStudents(jt);
        List<StudentDTO> studentsDTO = DTOFactory.convertToDto(studentsEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.addStudentClassFormFactory(jt, constantService, classStudentDTO, studentsDTO),
                new Link(constantService.getConstant("api.add_student_class_templated", new Object[] {classId})).withSelfRel());
        responseEntity.add(new Link(constantService.getConstant("resource.add_student_class")).withRel("form_url"), false);
        responseEntity.add(new Link(constantService.getConstant("api.class_detail_templated", new Object[] {classId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity removeStudentFromClass(String classId) {
        ClassStudentDTO classStudentDTO = new ClassStudentDTO(classId,"");
        List<StudentEntity> studentsInClassEntity = ClassRepository.getAllStudentsInClass(jt, classId);
        List<StudentDTO> studentsInClassDTO = DTOFactory.convertToDto(studentsInClassEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.removeStudentClassFormFactory(jt, constantService, classStudentDTO, studentsInClassDTO),
                new Link(constantService.getConstant("api.remove_student_class_templated", new Object[] {classId})).withSelfRel());
        responseEntity.add(new Link(constantService.getConstant("resource.remove_student_class")).withRel("form_url"), false);
        responseEntity.add(new Link(constantService.getConstant("api.class_detail_templated", new Object[] {classId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getStudentClassDetail(String classId, String studentId) {
        StudentEntity studentEntity = ClassRepository.getStudentInClass(jt, classId, studentId);
        StudentDTO studentDTO = DTOFactory.convertToDto(studentEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.createDetailContainer(jt, constantService, studentDTO),
                new Link(constantService.getConstant("api.student_class_detail_templated", new Object[] {classId, studentId})).withSelfRel());
        responseEntity.add(new Link(constantService.getConstant("resource.students_class_templated", new Object[] {classId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity addStudentToClass(String classId, String studentId) {
        ClassRepository.addStudentToClass(jt, classId, studentId);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("add_student_class",
                new Link(constantService.getConstant("resource.add_student_class_templated", new Object[] {classId, studentId})).withSelfRel());
        responseEntity.add(new Link(constantService.getConstant("api.student_detail_templated", new Object[] {studentId})).withRel("get_student"), false);
        responseEntity.add(new Link(constantService.getConstant("api.student_class_detail", new Object[] {classId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity removeStudentFromClass(String classId, String studentId) {
        ClassRepository.removeStudentFromClass(jt, classId, studentId);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("remove_student_class",
                new Link(constantService.getConstant("resource.remove_student_class_templated", new Object[] {classId, studentId})).withSelfRel());
        responseEntity.add(new Link(constantService.getConstant("api.student_class_detail", new Object[] {classId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }
}
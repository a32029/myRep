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
import pt.isel.daw.business.interfaces.TeacherClassService;
import pt.isel.daw.data.factory.ContainerFactory;
import pt.isel.daw.data.factory.DTOFactory;
import pt.isel.daw.data.factory.ResponseEntityFactory;
import pt.isel.daw.data.repository.ClassRepository;
import pt.isel.daw.data.repository.TeacherRepository;

import java.util.List;

public class TeacherClassServiceImpl implements TeacherClassService {
    private DataSourceTransactionManager transactionManager;

    public TeacherClassServiceImpl(DataSourceTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    @Autowired
    JdbcTemplate jt;

    @Autowired
    private ConstantService constantService;

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getPageListTeachersInClass(String classId, int pageNo, int pageSize) {
        PageListEntity<TeacherEntity> pl = ClassRepository.getPageListTeachersInClass(jt, classId, pageNo, pageSize);
        PageListDTO<TeacherDTO> plDTO = DTOFactory.convertToDto(pl);

        Resource teachersClass = new Resource(ContainerFactory.createListContainer(jt, constantService, plDTO),
                new Link(constantService.getConstant("api.list_teachers_class_templated", new Object[]{classId, pageNo, pageSize})).withSelfRel());

        teachersClass.add(new Link(constantService.getConstant("api.add_teacher_class_templated", new Object[]{ classId })).withRel("add_teacher_class"));
        teachersClass.add(new Link(constantService.getConstant("api.remove_teacher_class_templated", new Object[]{ classId })).withRel("remove_teacher_class"));

        List<TeacherDTO> teachersDTO = plDTO.getPageItems();
        for (TeacherDTO teacherDTO:teachersDTO) {
            String teacher_id = teacherDTO.getTeacherId();
            teachersClass.add(new Link(constantService.getConstant("api.teacher_detail_templated", new Object[] {teacherDTO.getTeacherId()})).withRel("get_teacher_" + teacher_id));
        }

        if (plDTO.hasNextPage())
            teachersClass.add(new Link(constantService.getConstant("api.list_teachers_class_templated", new Object[]{classId, plDTO.getPageNumber()+1, pageSize})).withRel("next"));
        if (plDTO.hasPrevPage())
            teachersClass.add(new Link(constantService.getConstant("api.list_teachers_class_templated", new Object[]{classId, plDTO.getPageNumber()-1, pageSize})).withRel("prev"));
        else
            teachersClass.add(new Link(constantService.getConstant("api.class_detail_templated", new Object[] {classId})).withRel("prev"));

        return new ResponseEntity(teachersClass, HttpStatus.OK);
    }

    public ResponseEntity addTeacherToClass(String classId) {
        ClassTeacherDTO classTeacherDTO = new ClassTeacherDTO(classId,"");
        List<TeacherEntity> activeTeachers = TeacherRepository.getAllActiveTeachers(jt);
        List<TeacherDTO> activeTeachersDTO = DTOFactory.convertToDto(activeTeachers);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.addTeacherClassFormFactory(jt, constantService, classTeacherDTO, activeTeachersDTO),
                new Link(constantService.getConstant("api.add_teacher_class_templated", new Object[] {classId})).withSelfRel());
        responseEntity.add(new Link(constantService.getConstant("resource.add_teacher_class")).withRel("form_url"), false);
        responseEntity.add(new Link(constantService.getConstant("api.class_detail_templated", new Object[] {classId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity removeTeacherFromClass(String classId) {
        ClassTeacherDTO classTeacherDTO = new ClassTeacherDTO(classId,"");
        List<TeacherEntity> teachersEntity = ClassRepository.getAllTeachersInClass(jt, classId);
        List<TeacherDTO> teachersDTO = DTOFactory.convertToDto(teachersEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.removeTeacherClassFormFactory(jt, constantService, classTeacherDTO, teachersDTO),
                new Link(constantService.getConstant("api.remove_teacher_class_templated", new Object[] {classId})).withSelfRel());
        responseEntity.add(new Link(constantService.getConstant("resource.remove_teacher_class")).withRel("form_url"), false);
        responseEntity.add(new Link(constantService.getConstant("api.class_detail_templated", new Object[] {classId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getTeacherClassDetail(String classId, String teacherId) {
        TeacherEntity teacherEntity = ClassRepository.getTeacherInClass(jt, classId, teacherId);
        TeacherDTO teacherDTO = DTOFactory.convertToDto(teacherEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.createDetailContainer(jt, constantService, teacherDTO),
                new Link(constantService.getConstant("api.teacher_class_detail_templated", new Object[] {classId, teacherId})).withSelfRel());
        responseEntity.add(new Link(constantService.getConstant("resource.teachers_class_templated", new Object[] {classId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity addTeacherToClass(String classId, String teacherId) {
        ClassRepository.addTeacherToClass(jt, classId, teacherId);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("add_teacher_class",
                new Link(constantService.getConstant("resource.add_teacher_class_templated", new Object[] {classId, teacherId})).withSelfRel());
        responseEntity.add(new Link(constantService.getConstant("api.teacher_detail_templated", new Object[] {teacherId})).withRel("get_teacher"), false);
        responseEntity.add(new Link(constantService.getConstant("api.teacher_class_detail", new Object[] {classId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity removeTeacherFromClass(String classId, String teacherId) {
        ClassRepository.removeTeacherFromClass(jt, classId, teacherId);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("remove_teacher_class",
                new Link(constantService.getConstant("resource.remove_teacher_class_templated", new Object[] {classId, teacherId})).withSelfRel());
        responseEntity.add(new Link(constantService.getConstant("api.teacher_class_detail", new Object[] {classId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }
}
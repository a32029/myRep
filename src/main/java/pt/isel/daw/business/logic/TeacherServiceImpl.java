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
import pt.isel.daw.business.interfaces.TeacherService;
import pt.isel.daw.data.factory.ContainerFactory;
import pt.isel.daw.data.factory.DTOFactory;
import pt.isel.daw.data.factory.ResponseEntityFactory;
import pt.isel.daw.data.repository.TeacherRepository;

import java.util.List;

public class TeacherServiceImpl implements TeacherService {
    private DataSourceTransactionManager transactionManager;

    public TeacherServiceImpl(DataSourceTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    @Autowired
    JdbcTemplate jt;

    @Autowired
    private ConstantService constantService;

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getListTeachers(int pageNo, int pageSize, String searchParam, String searchValue) {
        PageListEntity<TeacherEntity> pl = TeacherRepository.getListTeachers(jt, pageNo, pageSize, searchParam, searchValue);
        PageListDTO<TeacherDTO> plDTO = DTOFactory.convertToDto(pl);

        Resource resource = new Resource(ContainerFactory.createListContainer(jt, constantService, plDTO),
                new Link(constantService.getConstant("api.list_teachers_templated", new Object[]{pageNo, pageSize, searchParam, searchValue})).withSelfRel());

        resource.add(new Link(constantService.getConstant("api.create_teacher")).withRel("create_teacher"));
        resource.add(new Link(constantService.getConstant("api.activate_teacher")).withRel("activate_teacher"));

        List<TeacherDTO> teachers = plDTO.getPageItems();
        for (TeacherDTO teacher:teachers) {
            String teacherId = teacher.getTeacherId();
            resource.add(new Link(constantService.getConstant("api.teacher_detail_templated", new Object[] {teacher.getTeacherId()})).withRel("get_teacher_" + teacherId));
        }

        if (plDTO.hasNextPage())
            resource.add(new Link(constantService.getConstant("api.list_teachers_templated", new Object[]{plDTO.getPageNumber()+1, pageSize, searchParam, searchValue})).withRel("next"));
        if (plDTO.hasPrevPage())
            resource.add(new Link(constantService.getConstant("api.list_teachers_templated", new Object[]{plDTO.getPageNumber()-1, pageSize, searchParam, searchValue})).withRel("prev"));

        resource.add(new Link(constantService.getConstant("docs.list_teachers")).withRel("docs_list_teachers"));

        return new ResponseEntity(resource, HttpStatus.OK);
    }

    public ResponseEntity createTeacher() {
        TeacherDTO teacher = new TeacherDTO("", "", "", "", "", "", true);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.teacherFormFactory(jt, constantService, teacher), constantService.getConstant("api.create_teacher"));
        responseEntity.add(constantService.getConstant("resource.create_teacher"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_teachers"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.create_teacher"), "docs_create_teacher", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity activateTeacher() {
        TeacherListDTO teachersDTO = new TeacherListDTO();
        List<TeacherEntity> deactivatedTeachers = TeacherRepository.getAllDeactivatedTeachers(jt);
        List<TeacherDTO> deactivatedTeachersDTO = DTOFactory.convertToDto(deactivatedTeachers);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.activateTeacherFormFactory(jt, constantService, teachersDTO, deactivatedTeachersDTO), constantService.getConstant("api.activate_teacher"));
        responseEntity.add(constantService.getConstant("resource.activate_teacher"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_teachers"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.activate_teacher"), "docs_activate_teacher", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getTeacherDetail(String searchParam, String searchValue) {
        TeacherEntity teacherEntity = TeacherRepository.getTeacherBy(jt, searchParam, searchValue);
        TeacherDTO teacherDTO = DTOFactory.convertToDto(teacherEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.createDetailContainer(jt, constantService, teacherDTO),
                constantService.getConstant("api.teacher_detail_templated", new Object[] {teacherDTO.getTeacherId()}));
        responseEntity.add(new Link(constantService.getConstant("api.list_courses")).withRel("list_courses"), false);
        responseEntity.add(new Link(constantService.getConstant("api.update_teacher_templated", new Object[] {teacherDTO.getTeacherId()})).withRel("update_teacher"), false);
        responseEntity.add(constantService.getConstant("docs.teacher_detail"), "docs_teacher_detail", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity updateTeacher(String classId) {
        TeacherEntity teacherEntity = TeacherRepository.getTeacherBy(jt, "teacher_id", classId);
        TeacherDTO teacherDTO = DTOFactory.convertToDto(teacherEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.teacherFormFactory(jt, constantService, teacherDTO), constantService.getConstant("api.update_teacher"));
        responseEntity.add(constantService.getConstant("resource.update_teacher"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_teachers"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.update_teacher"), "docs_update_teacher", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity createTeacher(TeacherDTO teacherDTO) {
        TeacherEntity teacherEntity = DTOFactory.convertToEntity(teacherDTO);
        String teacherId = TeacherRepository.createTeacher(jt, teacherEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("create_teacher", constantService.getConstant("resource.create_teacher"));
        responseEntity.add(new Link(constantService.getConstant("api.teacher_detail_templated", new Object[] {teacherId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity updateTeacher(TeacherDTO teacherDTO) {
        TeacherEntity teacherEntity = DTOFactory.convertToEntity(teacherDTO);
        String teacherId = TeacherRepository.updateTeacher(jt, teacherEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("update_teacher", constantService.getConstant("resource.update_teacher"));
        responseEntity.add(new Link(constantService.getConstant("api.teacher_detail_templated", new Object[] {teacherId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity setTeacherActive(TeacherListDTO teacherDTO) {
        TeacherEntity teacherEntity = DTOFactory.convertToEntity(teacherDTO);
        TeacherRepository.setTeacherActive(jt, teacherEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("activate_teacher", constantService.getConstant("resource.activate_teacher"));
        responseEntity.add(constantService.getConstant("api.list_teachers"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.activate_teacher"), "docs_activate_teacher", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }
}
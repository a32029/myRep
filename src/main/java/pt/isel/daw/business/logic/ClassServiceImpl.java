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
import pt.isel.daw.business.interfaces.ClassService;
import pt.isel.daw.data.factory.ContainerFactory;
import pt.isel.daw.data.factory.DTOFactory;
import pt.isel.daw.data.factory.ResponseEntityFactory;
import pt.isel.daw.data.repository.ClassRepository;
import pt.isel.daw.data.repository.SemesterRepository;

import java.util.List;

public class ClassServiceImpl implements ClassService {
    private DataSourceTransactionManager transactionManager;

    public ClassServiceImpl(DataSourceTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    @Autowired
    JdbcTemplate jt;

    @Autowired
    private ConstantService constantService;

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getListClasses(String courseAcronym, int pageNo, int pageSize, String searchParam, String searchValue) {
        PageListEntity<ClassEntity> pl = ClassRepository.getListClasses(jt, courseAcronym, pageNo, pageSize, searchParam, searchValue);
        PageListDTO<ClassDTO> plDTO = DTOFactory.convertToDto(pl);

        Resource resource = new Resource(ContainerFactory.createListContainer(jt, constantService, plDTO),
                new Link(constantService.getConstant("api.list_classes_templated", new Object[]{courseAcronym, pageNo, pageSize, searchParam, searchValue})).withSelfRel());

        resource.add(new Link(constantService.getConstant("api.create_class_templated", new Object[]{ courseAcronym })).withRel("create_class"));
        resource.add(new Link(constantService.getConstant("api.activate_class_templated", new Object[]{ courseAcronym })).withRel("activate_class"));

        List<ClassDTO> classesDTO = plDTO.getPageItems();
        for (ClassDTO classDTO:classesDTO) {
            String dawClassId = classDTO.getClassId();
            resource.add(new Link(constantService.getConstant("api.class_detail_templated", new Object[] {classDTO.getClassId()})).withRel("get_class_" + dawClassId));
        }

        if (plDTO.hasNextPage())
            resource.add(new Link(constantService.getConstant("api.list_classes_templated", new Object[]{courseAcronym, plDTO.getPageNumber()+1, pageSize, searchParam, searchValue})).withRel("next"));
        if (plDTO.hasPrevPage())
            resource.add(new Link(constantService.getConstant("api.list_classes_templated", new Object[]{courseAcronym, plDTO.getPageNumber()-1, pageSize, searchParam, searchValue})).withRel("prev"));
        else
            resource.add(new Link(constantService.getConstant("api.course_detail_templated", new Object[]{ courseAcronym })).withRel("prev"));

        resource.add(new Link(constantService.getConstant("docs.list_classes")).withRel("docs_list_classes"));

        return new ResponseEntity(resource, HttpStatus.OK);
    }

    public ResponseEntity createClass(String courseAcronym) {
        ClassDTO classDTO = new ClassDTO("-----", "", courseAcronym, "", false, "", true);
        List<SemesterEntity> semestersEntity = SemesterRepository.getAllSemesters(jt);
        List<SemesterDTO> semestersDTO = DTOFactory.convertToDto(semestersEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.classFormFactory(jt,
                constantService, classDTO, semestersDTO), constantService.getConstant("api.create_class"));
        responseEntity.add(constantService.getConstant("resource.create_class"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_classes_templated", new Object[]{courseAcronym, 1, 5, "class_id", "*"}), "prev", false);

        responseEntity.add(constantService.getConstant("docs.create_class"), "docs_create_class", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity activateClass(String courseAcronym) {
        ClassListDTO classesDTO = new ClassListDTO();
        List<ClassEntity> deactivatedClassesEntity = ClassRepository.getAllDeactivatedClasses(jt, courseAcronym);
        List<ClassDTO> deactivatedClassesDTO = DTOFactory.convertToDto(deactivatedClassesEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.activateClassFormFactory(jt, constantService, classesDTO, deactivatedClassesDTO), constantService.getConstant("api.activate_class"));
        responseEntity.add(constantService.getConstant("resource.activate_class"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_classes_templated", new Object[]{courseAcronym, 1, 5, "class_id", "*"}), "prev", false);
        responseEntity.add(constantService.getConstant("docs.activate_class"), "docs_activate_class", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getClassDetail(String searchParam, String searchValue) {
        ClassEntity classEntity = ClassRepository.getClassBy(jt, searchParam, searchValue);
        ClassDTO classDTO = DTOFactory.convertToDto(classEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.createDetailContainer(jt, constantService, classDTO),
                constantService.getConstant("api.class_detail_templated", new Object[] {classDTO.getClassId()}));
        responseEntity.add(new Link(constantService.getConstant("api.update_class_templated", new Object[] {classDTO.getClassId()})).withRel("update_class"), false);
        responseEntity.add(new Link(constantService.getConstant("api.list_teachers_class_templated", new Object[] {classDTO.getClassId(), 1, 10})).withRel("list_teachers_class"), false);
        responseEntity.add(new Link(constantService.getConstant("api.list_students_class_templated", new Object[] {classDTO.getClassId(), 1, 10})).withRel("list_students_class"), false);
        responseEntity.add(new Link(constantService.getConstant("api.list_groups_class_templated", new Object[] {classDTO.getClassId(), 1, 10, "group_id", "*"})).withRel("list_groups_class"), false);
        responseEntity.add(new Link(constantService.getConstant("api.list_classes")).withRel("prev"), false);
        responseEntity.add(constantService.getConstant("docs.class_detail"), "docs_class_detail", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity updateClass(String classId) {
        ClassEntity classEntity = ClassRepository.getClassBy(jt, "class_id", classId);
        ClassDTO classDTO = DTOFactory.convertToDto(classEntity);
        List<SemesterEntity> semestersEntity = SemesterRepository.getAllSemesters(jt);
        List<SemesterDTO> semestersDTO = DTOFactory.convertToDto(semestersEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.classFormFactory(jt,
                constantService, classDTO, semestersDTO), constantService.getConstant("api.update_class"));
        responseEntity.add(constantService.getConstant("resource.update_class"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_classes"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.update_class"), "docs_update_class", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity createClass(ClassDTO classDTO) {
        ClassEntity ClassEntity = DTOFactory.convertToEntity(classDTO);
        String classId = ClassRepository.createClass(jt, ClassEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("create_class",
                constantService.getConstant("resource.create_class"));
        responseEntity.add(new Link(constantService.getConstant("api.class_detail_templated", new Object[] {classId})).withRel("class_detail"), false);
        responseEntity.add(new Link(constantService.getConstant("api.list_classes")).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity updateClass(ClassDTO classDTO) {
        ClassEntity ClassEntity = DTOFactory.convertToEntity(classDTO);
        String classId = ClassRepository.updateClass(jt, ClassEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("update_class",
                constantService.getConstant("resource.update_class"));
        responseEntity.add(new Link(constantService.getConstant("api.class_detail_templated", new Object[] {classId})).withRel("class_detail"), false);
        responseEntity.add(new Link(constantService.getConstant("api.list_classes")).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity setClassActive(String classId, boolean active) {
        ClassRepository.setClassActive(jt, classId, active);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("activate_class",
                constantService.getConstant("resource.activate_class_templated", new Object[]{classId, active}));
        responseEntity.add(constantService.getConstant("api.list_classes"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.activate_class"), "docs_activate_class", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }
}
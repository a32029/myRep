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
import pt.isel.daw.business.interfaces.CourseService;
import pt.isel.daw.data.factory.ContainerFactory;
import pt.isel.daw.data.factory.DTOFactory;
import pt.isel.daw.data.factory.ResponseEntityFactory;
import pt.isel.daw.data.repository.CourseRepository;
import pt.isel.daw.data.repository.TeacherRepository;

import java.util.List;

public class CourseServiceImpl implements CourseService {
    private DataSourceTransactionManager transactionManager;

    public CourseServiceImpl(DataSourceTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    @Autowired
    JdbcTemplate jt;

    @Autowired
    private ConstantService constantService;

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getListCourses(int pageNo, int pageSize, String searchParam, String searchValue) {
        PageListEntity<CourseEntity> pl = CourseRepository.getListActiveCourses(jt, pageNo, pageSize, searchParam, searchValue);
        PageListDTO<CourseDTO> plDTO = DTOFactory.convertToDto(pl);

        Resource resource = new Resource(ContainerFactory.createListContainer(jt, constantService, plDTO),
                new Link(constantService.getConstant("api.list_courses_templated", new Object[]{pageNo, pageSize, searchParam, searchValue})).withSelfRel());

        resource.add(new Link(constantService.getConstant("api.create_course")).withRel("create_course"));
        resource.add(new Link(constantService.getConstant("api.activate_course")).withRel("activate_course"));

        List<CourseDTO> courses = plDTO.getPageItems();
        for (CourseDTO course:courses) {
            String acronym = course.getAcronym();
            resource.add(new Link(constantService.getConstant("api.course_detail_templated", new Object[] {course.getAcronym()})).withRel("get_course_" + acronym));
        }

        if (plDTO.hasNextPage())
            resource.add(new Link(constantService.getConstant("api.list_courses_templated", new Object[]{plDTO.getPageNumber()+1, pageSize, searchParam, searchValue})).withRel("next"));
        if (plDTO.hasPrevPage())
            resource.add(new Link(constantService.getConstant("api.list_courses_templated", new Object[]{plDTO.getPageNumber()-1, pageSize, searchParam, searchValue})).withRel("prev"));

        resource.add(new Link(constantService.getConstant("docs.list_courses")).withRel("docs_list_courses"));

        return new ResponseEntity(resource, HttpStatus.OK);
    }

    public ResponseEntity createCourse() {
        CourseDTO courseDTO = new CourseDTO("", "", "", true);
        List<TeacherEntity> activeTeachers = TeacherRepository.getAllActiveTeachers(jt);
        List<TeacherDTO> activeTeachersDTO = DTOFactory.convertToDto(activeTeachers);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.courseFormFactory(jt, constantService, courseDTO, activeTeachersDTO), constantService.getConstant("api.create_course"));
        responseEntity.add(constantService.getConstant("resource.create_course"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_courses"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.create_course"), "docs_create_course", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity activateCourse() {
        CourseListDTO courseList = new CourseListDTO();
        List<CourseEntity> coursesEntity = CourseRepository.getAllDeactivatedCourses(jt);
        List<CourseDTO> coursesDTO = DTOFactory.convertToDto(coursesEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.activateCourseFormFactory(jt, constantService, courseList, coursesDTO), constantService.getConstant("api.activate_course"));
        responseEntity.add(constantService.getConstant("resource.activate_course"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_courses"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.activate_course"), "docs_activate_course", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getCourseDetail(String searchParam, String searchValue) {
        CourseEntity courseEntity = CourseRepository.getCourseDetail(jt, searchParam, searchValue);
        CourseDTO courseDTO = DTOFactory.convertToDto(courseEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.createDetailContainer(jt, constantService, courseDTO), constantService.getConstant("api.course_detail_templated", new Object[] {courseDTO.getAcronym()}));
        responseEntity.add(constantService.getConstant("api.update_course_templated", new Object[] {courseDTO.getAcronym()}), "update_course", false);
        responseEntity.add(constantService.getConstant("api.list_classes_templated", new Object[] {courseDTO.getAcronym(), 1, 5, "class_id", "*"}), "list_classes", false);
        responseEntity.add(constantService.getConstant("api.list_courses"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.course_detail"), "docs_course_detail", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity updateCourse(String acronym) {
        CourseEntity courseEntity = CourseRepository.getCourseDetail(jt, "acronym", acronym);
        CourseDTO courseDTO = DTOFactory.convertToDto(courseEntity);
        List<TeacherEntity> activeTeachers = TeacherRepository.getAllActiveTeachers(jt);
        List<TeacherDTO> activeTeachersDTO = DTOFactory.convertToDto(activeTeachers);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.courseFormFactory(jt, constantService, courseDTO, activeTeachersDTO), constantService.getConstant("api.update_course"));
        responseEntity.add(constantService.getConstant("resource.update_course"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_courses"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.update_course"), "docs_update_course", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity createCourse(CourseDTO course) {
        CourseEntity courseEntity = DTOFactory.convertToEntity(course);
        String acronym = CourseRepository.createCourse(jt, courseEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("create_course", constantService.getConstant("api.create_course"));
        responseEntity.add(constantService.getConstant("api.course_detail_templated", new Object[] {acronym}), "course_detail", false);
        responseEntity.add(constantService.getConstant("api.list_courses"), "prev", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity updateCourse(CourseDTO course) {
        CourseEntity courseEntity = DTOFactory.convertToEntity(course);
        String acronym = CourseRepository.updateCourse(jt, courseEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("update_course", constantService.getConstant("api.update_course"));
        responseEntity.add(constantService.getConstant("api.course_detail_templated", new Object[] {acronym}), "course_detail", false);
        responseEntity.add(constantService.getConstant("api.list_courses"), "prev", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity setCourseActive(String acronym, boolean active) {
        CourseRepository.setCourseActive(jt, acronym, active);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("activate_course",
                constantService.getConstant("resource.activate_course_templated", new Object[]{acronym, active}));
        responseEntity.add(constantService.getConstant("api.list_courses"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.activate_course"), "docs_activate_course", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }
}
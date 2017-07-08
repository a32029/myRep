package pt.isel.daw.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.business.dto.ControllerExceptionDTO;
import pt.isel.daw.business.dto.CourseDTO;
import pt.isel.daw.business.dto.ErrorInfoDTO;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.business.interfaces.CourseService;
import pt.isel.daw.data.factory.ContainerFactory;

@RestController
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class CourseController {
    private CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    JdbcTemplate jt;

    @Autowired
    private ConstantService constantService;

    //------------------------------------------------------------------------------------------------------------------

//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    ErrorInfo handleBadRequest(HttpServletRequest req, Exception ex) {
//        ex.printStackTrace();
//        return new ErrorInfo("", ex.getMessage(), HttpStatus.SEE_OTHER.value(), "", "");
//    }
//

    @ExceptionHandler(ControllerExceptionDTO.class)
    @ResponseBody
    ResponseEntity handleResourceException(ControllerExceptionDTO ex) {
        ErrorInfoDTO errorInfo = new ErrorInfoDTO(constantService.getConstant("docs." + ex.getInstance()), ex.getTitle(), ex.getStatus().value(), ex.getDetail(), constantService.getConstant("api." + ex.getInstance()));
        Resource resource = new Resource(ContainerFactory.createErrorContainer(jt, constantService, errorInfo));
        return new ResponseEntity(resource, ex.getStatus());
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${api.list_courses}",method=RequestMethod.GET)
    public ResponseEntity getListCourses(@RequestParam(value = "p", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "l", defaultValue = "5") int pageSize,
                                         @RequestParam(value = "sp", defaultValue = "name")String searchParam,
                                         @RequestParam(value = "sv", defaultValue = "*")String searchValue) {
        return courseService.getListCourses(pageNo, pageSize, searchParam, searchValue);
    }

    @RequestMapping(value="${api.create_course}", method= RequestMethod.GET)
    public ResponseEntity createCourse() {
        return courseService.createCourse();
    }

    @RequestMapping(value="${api.activate_course}", method= RequestMethod.GET)
    public ResponseEntity activateCourse() {
        return courseService.activateCourse();
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${api.course_detail}", method= RequestMethod.GET)
    public ResponseEntity getCourse(@RequestParam("acronym") String acronym) {
        return courseService.getCourseDetail("acronym", acronym);
    }

    @RequestMapping(value="${api.update_course}", method= RequestMethod.GET)
    public ResponseEntity updateCourse(@RequestParam("acronym") String acronym) {
        return courseService.updateCourse(acronym);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${resource.create_course}", method= RequestMethod.POST)
    public ResponseEntity createCourse(@RequestBody CourseDTO course) {
        return courseService.createCourse(course);
    }

    @RequestMapping(value="${resource.update_course}", method= RequestMethod.POST)
    public ResponseEntity updateCourse(@RequestBody CourseDTO course) {
        return courseService.updateCourse(course);
    }

    @RequestMapping(value="${resource.activate_course}", method= RequestMethod.PUT)
    public ResponseEntity setCourseActive(@RequestParam("acronym") String acronym, @RequestParam("active") boolean active) {
        return courseService.setCourseActive(acronym, active);
    }
}
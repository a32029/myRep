package pt.isel.daw.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.business.dto.*;
import pt.isel.daw.business.interfaces.ClassService;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.data.factory.ContainerFactory;

@RestController
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class ClassController {

    @Autowired
    private ClassService classService;

    @Autowired
    JdbcTemplate jt;

    @Autowired
    private ConstantService constantService;

    //-------------------------------------------------------------

//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    ErrorInfo handleBadRequest(HttpServletRequest req, Exception ex) {
//        ex.printStackTrace();
//        return new ErrorInfo("", ex.getMessage(), Status.SEE_OTHER, "", "");
//    }

    @ExceptionHandler(ControllerExceptionDTO.class)
    @ResponseBody
    ResponseEntity handleResourceException(ControllerExceptionDTO ex) {
        ErrorInfoDTO errorInfo = new ErrorInfoDTO(constantService.getConstant("docs." + ex.getInstance()), ex.getTitle(), ex.getStatus().value(), ex.getDetail(), constantService.getConstant("api." + ex.getInstance()));
        Resource resource = new Resource(ContainerFactory.createErrorContainer(jt, constantService, errorInfo));
        return new ResponseEntity(resource, ex.getStatus());
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${api.list_classes}",method=RequestMethod.GET)
    public ResponseEntity getListClasses(@RequestParam(value = "acronym") String courseAcronym, @RequestParam(value = "p", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "l", defaultValue = "5") int pageSize,
                                         @RequestParam(value = "sp", defaultValue = "class_id")String searchParam,
                                         @RequestParam(value = "sv", defaultValue = "*")String searchValue) {
        return classService.getListClasses(courseAcronym, pageNo, pageSize, searchParam, searchValue);
    }

    @RequestMapping(value="${api.create_class}", method= RequestMethod.GET)
    public ResponseEntity createCourse(@RequestParam(value = "acronym") String courseAcronym) {
        return classService.createClass(courseAcronym);
    }

    @RequestMapping(value="${api.activate_class}", method= RequestMethod.GET)
    public ResponseEntity activateClass(@RequestParam(value = "acronym") String courseAcronym) {
        return classService.activateClass(courseAcronym);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${api.class_detail}", method= RequestMethod.GET)
    public ResponseEntity getClass(@RequestParam("class_id") String classId) {
        return classService.getClassDetail("class_id", classId);
    }

    @RequestMapping(value="${api.update_class}", method= RequestMethod.GET)
    public ResponseEntity updateClass(@RequestParam("class_id") String classId) {
        return classService.updateClass(classId);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${resource.create_class}", method= RequestMethod.POST)
    public ResponseEntity createClass(@RequestBody ClassDTO classDTO) {
        return classService.createClass(classDTO);
    }

    @RequestMapping(value="${resource.update_class}", method= RequestMethod.POST)
    public ResponseEntity updateClass(@RequestBody ClassDTO classDTO) {
        return classService.updateClass(classDTO);
    }

    @RequestMapping(value="${resource.activate_class}", method= RequestMethod.PUT)
    public ResponseEntity setClassActive(@RequestParam("class_id") String classId, @RequestParam("active") boolean active) {
        return classService.setClassActive(classId, active);
    }
}
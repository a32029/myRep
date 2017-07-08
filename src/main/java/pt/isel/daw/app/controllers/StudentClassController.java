package pt.isel.daw.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.business.dto.ControllerExceptionDTO;
import pt.isel.daw.business.dto.ErrorInfoDTO;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.business.interfaces.StudentClassService;
import pt.isel.daw.data.factory.ContainerFactory;

@RestController
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class StudentClassController {
    private StudentClassService studentClassService;

    @Autowired
    public StudentClassController(StudentClassService studentClassService) {
        this.studentClassService = studentClassService;
    }

    @Autowired
    JdbcTemplate jt;

    @Autowired
    private ConstantService constantService;

    //-------------------------------------------------------------
//
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    ErrorInfo handleBadRequest(HttpServletRequest req, Exception ex) {
//        ex.printStackTrace();
//        return new ErrorInfo("", ex.getMessage(), HttpStatus.SEE_OTHER, "", "");
//    }

    @ExceptionHandler(ControllerExceptionDTO.class)
    @ResponseBody
    ResponseEntity handleResourceException(ControllerExceptionDTO ex) {
        ErrorInfoDTO errorInfo = new ErrorInfoDTO(constantService.getConstant("docs." + ex.getInstance()), ex.getTitle(), ex.getStatus().value(), ex.getDetail(), constantService.getConstant("api." + ex.getInstance()));
        Resource resource = new Resource(ContainerFactory.createErrorContainer(jt, constantService, errorInfo));
        return new ResponseEntity(resource, ex.getStatus());
    }

    //-------------------------------------------------------------

    @RequestMapping(value= "${api.list_students_class}",method=RequestMethod.GET)
    public ResponseEntity getPageListStudentsInClass(@RequestParam("class_id") String classId,
                                                     @RequestParam(value = "p", defaultValue = "1") int pageNo,
                                                     @RequestParam(value = "l", defaultValue = "5") int pageSize) {
        return studentClassService.getPageListStudentsInClass(classId, pageNo, pageSize);
    }

    @RequestMapping(value="${api.add_student_class}",method= RequestMethod.GET)
    public ResponseEntity addStudentToClassMenu(@RequestParam("class_id") String classId) {
        return studentClassService.addStudentToClass(classId);
    }

    @RequestMapping(value="${api.remove_student_class}",method=RequestMethod.GET)
    public ResponseEntity removeStudentFromClassMenu(@RequestParam("class_id") String classId) {
        return studentClassService.removeStudentFromClass(classId);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${api.student_class_detail}",method=RequestMethod.GET)
    public ResponseEntity getStudentClassDetail(@RequestParam("class_id") String classId, @RequestParam(value = "student_id") String studentId) {
        return studentClassService.getStudentClassDetail(classId, studentId);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${resource.add_student_class}",method= RequestMethod.PUT)
    public ResponseEntity addStudentToClass(@RequestParam("class_id") String classId, @RequestParam(value = "student_id") String studentId) {
        return studentClassService.addStudentToClass(classId, studentId);
    }

    @RequestMapping(value="${resource.remove_student_class}",method=RequestMethod.DELETE)
    public ResponseEntity removeStudentFromClass(@RequestParam("class_id") String classId, @RequestParam(value = "student_id") String studentId) {
        return studentClassService.removeStudentFromClass(classId, studentId);
    }
}
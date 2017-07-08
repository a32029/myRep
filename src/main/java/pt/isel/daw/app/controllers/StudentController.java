package pt.isel.daw.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.business.dto.ControllerExceptionDTO;
import pt.isel.daw.business.dto.ErrorInfoDTO;
import pt.isel.daw.business.dto.StudentDTO;
import pt.isel.daw.business.dto.StudentListDTO;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.business.interfaces.StudentService;
import pt.isel.daw.data.factory.ContainerFactory;

import javax.ws.rs.core.Response.Status;

@RestController
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class StudentController {
    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

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

    @RequestMapping(value="${api.list_students}",method=RequestMethod.GET)
    public ResponseEntity getListStudents(@RequestParam(value = "p", defaultValue = "1") int pageNo,
                                          @RequestParam(value = "l", defaultValue = "100") int pageSize,
                                          @RequestParam(value = "sp", defaultValue = "name")String searchParam,
                                          @RequestParam(value = "sv", defaultValue = "*")String searchValue) {
        return studentService.getListStudents(pageNo, pageSize, searchParam, searchValue);
    }

    @RequestMapping(value="${api.create_student}", method= RequestMethod.GET)
    public ResponseEntity createStudent() {
        return studentService.createStudent();
    }

    @RequestMapping(value="${api.activate_student}", method= RequestMethod.GET)
    public ResponseEntity activateStudent() {
        return studentService.activateStudent();
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${api.student_detail}", method= RequestMethod.GET)
    public ResponseEntity getStudentById(@RequestParam("student_id") String studentId) {
        return studentService.getStudentDetail("student_id", studentId);
    }

    @RequestMapping(value="${api.update_student}", method= RequestMethod.GET)
    public ResponseEntity updateStudent(@RequestParam("student_id") String studentId) {
        return studentService.updateStudent(studentId);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${resource.create_student}", method= RequestMethod.POST)
    public ResponseEntity createStudent(@RequestBody StudentDTO studentDTO) {
        return studentService.createStudent(studentDTO);
    }

    @RequestMapping(value="${resource.update_student}", method= RequestMethod.POST)
    public ResponseEntity updateStudent(@RequestBody StudentDTO studentDTO) {
        return studentService.updateStudent(studentDTO);
    }

    @RequestMapping(value="${resource.activate_student}", method= RequestMethod.POST)
    public ResponseEntity setStudentActive(@RequestBody StudentListDTO studentDTO) {
        return studentService.setStudentActive(studentDTO);
    }
}
package pt.isel.daw.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.business.dto.*;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.business.interfaces.TeacherService;
import pt.isel.daw.data.factory.ContainerFactory;

@RestController
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class TeacherController {
    private TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
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

    @RequestMapping(value="${api.list_teachers}",method=RequestMethod.GET)
    public ResponseEntity getListTeachers(@RequestParam(value = "p", defaultValue = "1") int pageNo,
                                          @RequestParam(value = "l", defaultValue = "100") int pageSize,
                                          @RequestParam(value = "sp", defaultValue = "name")String searchParam,
                                          @RequestParam(value = "sv", defaultValue = "*")String searchValue) {
        return teacherService.getListTeachers(pageNo, pageSize, searchParam, searchValue);
    }

    @RequestMapping(value="${api.create_teacher}", method= RequestMethod.GET)
    public ResponseEntity createTeacher() {
        return teacherService.createTeacher();
    }

    @RequestMapping(value="${api.activate_teacher}", method= RequestMethod.GET)
    public ResponseEntity activateTeacher() {
        return teacherService.activateTeacher();
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${api.teacher_detail}", method= RequestMethod.GET)
    public ResponseEntity getTeacherById(@RequestParam("teacher_id") String teacherId) {
        return teacherService.getTeacherDetail("teacher_id", teacherId);
    }

    @RequestMapping(value="${api.update_teacher}", method= RequestMethod.GET)
    public ResponseEntity updateTeacher(@RequestParam("teacher_id") String teacherId) {
        return teacherService.updateTeacher(teacherId);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${resource.create_teacher}", method= RequestMethod.POST)
    public ResponseEntity createTeacher(@RequestBody TeacherDTO teacherDTO) {
        return teacherService.createTeacher(teacherDTO);
    }

    @RequestMapping(value="${resource.update_teacher}", method= RequestMethod.POST)
    public ResponseEntity updateTeacher(@RequestBody TeacherDTO teacherDTO) {
        return teacherService.updateTeacher(teacherDTO);
    }

    @RequestMapping(value="${resource.activate_teacher}", method= RequestMethod.POST)
    public ResponseEntity setTeacherActive(@RequestBody TeacherListDTO teacherDTO) {
        return teacherService.setTeacherActive(teacherDTO);
    }
}
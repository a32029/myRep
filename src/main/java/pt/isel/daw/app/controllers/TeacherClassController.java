package pt.isel.daw.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.business.dto.ControllerExceptionDTO;
import pt.isel.daw.business.dto.ErrorInfoDTO;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.business.interfaces.TeacherClassService;
import pt.isel.daw.data.factory.ContainerFactory;

@RestController
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class TeacherClassController {
    private TeacherClassService teacherClassService;

    @Autowired
    public TeacherClassController(TeacherClassService teacherClassService) {
        this.teacherClassService = teacherClassService;
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
//        return new ErrorInfo("", ex.getMessage(), Status.SEE_OTHER, "", "");
//    }

    @ExceptionHandler(ControllerExceptionDTO.class)
    @ResponseBody
    ResponseEntity handleResourceException(ControllerExceptionDTO ex) {
        ErrorInfoDTO errorInfo = new ErrorInfoDTO(constantService.getConstant("docs." + ex.getInstance()), ex.getTitle(), ex.getStatus().value(), ex.getDetail(), constantService.getConstant("api." + ex.getInstance()));
        Resource resource = new Resource(ContainerFactory.createErrorContainer(jt, constantService, errorInfo));
        return new ResponseEntity(resource, ex.getStatus());
    }

    //-------------------------------------------------------------

    @RequestMapping(value= "${api.list_teachers_class}",method=RequestMethod.GET)
    public ResponseEntity getPageListTeachersInClass(@RequestParam("class_id") String classId,
                                                     @RequestParam(value = "p", defaultValue = "1") int pageNo,
                                                     @RequestParam(value = "l", defaultValue = "5") int pageSize) {
        return teacherClassService.getPageListTeachersInClass(classId, pageNo, pageSize);
    }

    @RequestMapping(value="${api.add_teacher_class}",method= RequestMethod.GET)
    public ResponseEntity addTeacherToClassMenu(@RequestParam("class_id") String classId) {
        return teacherClassService.addTeacherToClass(classId);
    }

    @RequestMapping(value="${api.remove_teacher_class}",method=RequestMethod.GET)
    public ResponseEntity removeTeacherFromClassMenu(@RequestParam("class_id") String classId) {
        return teacherClassService.removeTeacherFromClass(classId);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${api.teacher_class_detail}",method=RequestMethod.GET)
    public ResponseEntity getTeacherClassDetail(@RequestParam("class_id") String classId, @RequestParam(value = "teacher_id") String teacherId) {
        return teacherClassService.getTeacherClassDetail(classId, teacherId);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${resource.add_teacher_class}",method= RequestMethod.PUT)
    public ResponseEntity addTeacherToClass(@RequestParam("class_id") String classId, @RequestParam(value = "teacher_id") String teacherId) {
        return teacherClassService.addTeacherToClass(classId, teacherId);
    }

    @RequestMapping(value="${resource.remove_teacher_class}",method=RequestMethod.DELETE)
    public ResponseEntity removeTeacherFromClass(@RequestParam("class_id") String classId, @RequestParam(value = "teacher_id") String teacherId) {
        return teacherClassService.removeTeacherFromClass(classId, teacherId);
    }
}
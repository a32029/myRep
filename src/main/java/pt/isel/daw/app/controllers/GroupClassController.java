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
import pt.isel.daw.business.interfaces.GroupClassService;
import pt.isel.daw.data.factory.ContainerFactory;

import javax.ws.rs.core.Response.Status;

@RestController
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class GroupClassController {
    private GroupClassService groupClassService;

    @Autowired
    public GroupClassController(GroupClassService groupClassService) {
        this.groupClassService = groupClassService;
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

    @RequestMapping(value= "${api.list_groups_class}",method=RequestMethod.GET)
    public ResponseEntity getPageListGroupsInClass(@RequestParam("class_id") String classId,
                                                   @RequestParam(value = "p", defaultValue = "1") int pageNo,
                                                   @RequestParam(value = "l", defaultValue = "5") int pageSize,
                                                   @RequestParam(value = "sp", defaultValue = "class_id")String searchParam,
                                                   @RequestParam(value = "sv", defaultValue = "*")String searchValue) {
        return groupClassService.getPageListGroupsInClass(classId, pageNo, pageSize, searchParam, searchValue);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${api.create_group_class}",method=RequestMethod.GET)
    public ResponseEntity createGroupClass(@RequestParam("class_id") String classId) {
        return groupClassService.createGroupClass(classId);
    }

    @RequestMapping(value="${api.join_group_class}",method=RequestMethod.GET)
    public ResponseEntity joinGroupClass(@RequestParam("class_id") String classId, @RequestParam(value = "group_id")String groupId) {
        return groupClassService.joinGroupClass(classId, groupId);
    }

    @RequestMapping(value="${api.leave_group_class}",method=RequestMethod.GET)
    public ResponseEntity leaveGroupClass(@RequestParam("class_id") String classId, @RequestParam(value = "group_id")String groupId) {
        return groupClassService.leaveGroupClass(classId, groupId);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${api.group_class_detail}",method=RequestMethod.GET)
    public ResponseEntity getGroupInClass(@RequestParam("class_id") String classId,
                                          @RequestParam(value = "p", defaultValue = "1") int pageNo,
                                          @RequestParam(value = "l", defaultValue = "5") int pageSize,
                                          @RequestParam(value = "sp", defaultValue = "group_id")String searchParam,
                                          @RequestParam(value = "sv", defaultValue = "*")String searchValue) {
        return groupClassService.getGroupDetail(classId, pageNo, pageSize, searchParam, searchValue);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${resource.create_group_class}",method=RequestMethod.PUT)
    public ResponseEntity createGroupClass(@RequestParam("class_id") String classId, @RequestParam(value = "group_id") String groupId, @RequestParam(value = "student_id") String studentId) {
        return groupClassService.createGroupClass(classId, groupId, studentId);
    }

    @RequestMapping(value="${resource.join_group_class}",method=RequestMethod.PUT)
    public ResponseEntity joinGroupClass(@RequestParam("class_id") String classId, @RequestParam(value = "group_id") String groupId, @RequestParam(value = "student_id") String studentId) {
        return groupClassService.joinGroupClass(classId, groupId, studentId);
    }

    @RequestMapping(value="${resource.leave_group_class}",method=RequestMethod.DELETE)
    public ResponseEntity leaveGroupClass(@RequestParam("class_id") String classId, @RequestParam(value = "group_id") String groupId, @RequestParam(value = "student_id") String studentId) {
        return groupClassService.leaveGroupClass(classId, groupId, studentId);
    }
}
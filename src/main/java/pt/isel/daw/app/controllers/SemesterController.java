package pt.isel.daw.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.business.dto.ControllerExceptionDTO;
import pt.isel.daw.business.dto.ErrorInfoDTO;
import pt.isel.daw.business.dto.SemesterDTO;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.business.interfaces.SemesterService;
import pt.isel.daw.data.factory.ContainerFactory;

import javax.ws.rs.core.Response.Status;

@RestController
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class SemesterController {
    private SemesterService semesterService;

    @Autowired
    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
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

    @RequestMapping(value="${api.list_semesters}",method=RequestMethod.GET)
    public ResponseEntity getListSemesters(@RequestParam(value = "p", defaultValue = "1") int pageNo,
                                           @RequestParam(value = "l", defaultValue = "100") int pageSize,
                                           @RequestParam(value = "sp", defaultValue = "semester_id")String searchParam,
                                           @RequestParam(value = "sv", defaultValue = "*")String searchValue) {
        return semesterService.getListSemesters(pageNo, pageSize, searchParam, searchValue);
    }

    @RequestMapping(value="${api.create_semester}", method= RequestMethod.GET)
    public ResponseEntity createSemester() {
        return semesterService.createSemester();
    }

    @RequestMapping(value="${api.remove_semester}", method= RequestMethod.GET)
    public ResponseEntity removeSemester() {
        return semesterService.removeSemester();
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${api.semester_detail}", method= RequestMethod.GET)
    public ResponseEntity getSemesterById(@RequestParam("semester_id") String semesterId) {
        return semesterService.getSemesterDetail("semester_id", semesterId);
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${resource.create_semester}", method= RequestMethod.POST)
    public ResponseEntity createSemester(@RequestBody SemesterDTO semesterDTO) {
        return semesterService.createSemester(semesterDTO);
    }

    @RequestMapping(value="${resource.remove_semester}", method= RequestMethod.DELETE)
    public ResponseEntity removeSemester(@RequestParam("semester_id") String semesterId) {
        return semesterService.removeSemester(semesterId);
    }
}
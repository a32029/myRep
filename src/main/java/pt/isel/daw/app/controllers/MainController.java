package pt.isel.daw.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.dao.DataAccessException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.isel.daw.business.dto.AuthenticatedUserDTO;
import pt.isel.daw.business.dto.ControllerExceptionDTO;
import pt.isel.daw.business.dto.ErrorInfoDTO;
import pt.isel.daw.business.entities.AuthenticatedUserEntity;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.data.factory.ContainerFactory;
import pt.isel.daw.data.factory.DTOFactory;
import pt.isel.daw.data.repository.CredentialRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class MainController implements ErrorController {
    public static final String ERROR_PATH = "/error";
    public static final String ERROR_BAD_REQUEST_PATH = "/error/400";
    public static final String ERROR_UNAUTHORIZED_PATH = "/error/401";
    public static final String ERROR_FORBIDDEN_PATH = "/error/403";
    public static final String ERROR_NOT_FOUND_PATH = "/error/404";
    public static final String ERROR_METHOD_NOT_ALLOWED_PATH = "/error/405";
    public static final String ERROR_INTERNAL_ERROR_PATH = "/error/500";

    //-------------------------------------------------------------

    @Autowired
    JdbcTemplate jt;

    @Autowired
    private ConstantService constantService;

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(ERROR_BAD_REQUEST_PATH)
    ResponseEntity errorBadRequest() {
        return new ResponseEntity(new ErrorInfoDTO("/", "Bad Request", HttpStatus.BAD_REQUEST.value(), "", ""), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(ERROR_UNAUTHORIZED_PATH)
    ResponseEntity errorUnauthorized() {
        return new ResponseEntity(new ErrorInfoDTO("/", "Unauthorized", HttpStatus.UNAUTHORIZED.value(), "", ""), HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(ERROR_FORBIDDEN_PATH)
    ResponseEntity errorForbidden(Exception ex) {
        return new ResponseEntity(new ErrorInfoDTO("/", "Forbidden", HttpStatus.FORBIDDEN.value(), "", ""), HttpStatus.FORBIDDEN);
    }

    @RequestMapping(ERROR_NOT_FOUND_PATH)
    ResponseEntity errorNotFound(@RequestHeader(value="User-Agent", defaultValue = "none") String userAgent) {
        return new ResponseEntity(new ErrorInfoDTO("/", "Not Found", HttpStatus.NOT_FOUND.value(), "", ""), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(ERROR_METHOD_NOT_ALLOWED_PATH)
    ResponseEntity errorMethodNotAllowed(Exception ex) {
        return new ResponseEntity(new ErrorInfoDTO("/", "Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED.value(), "", ""), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @RequestMapping(ERROR_INTERNAL_ERROR_PATH)
    ResponseEntity errorInternalError() {
        return new ResponseEntity(new ErrorInfoDTO("/", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value(), "", ""), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //-------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ErrorInfoDTO handleBadRequest(HttpServletRequest req, Exception ex) {
        ex.printStackTrace();
        return new ErrorInfoDTO("", ex.getMessage(), HttpStatus.SEE_OTHER.value(), "", "");
    }

    @ExceptionHandler(ControllerExceptionDTO.class)
    @ResponseBody
    ResponseEntity handleResourceException(ControllerExceptionDTO ex) {
        ErrorInfoDTO errorInfo = new ErrorInfoDTO(constantService.getConstant("docs." + ex.getInstance()), ex.getTitle(), ex.getStatus().value(), ex.getDetail(), constantService.getConstant("api." + ex.getInstance()));
        Resource resource = new Resource(ContainerFactory.createErrorContainer(jt, constantService, errorInfo));
        return new ResponseEntity(resource, ex.getStatus());
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value="${home}",method=RequestMethod.GET)
    public void gethome(@RequestHeader(value="User-Agent", defaultValue = "none") String userAgent, HttpServletResponse response) {
        String redirectUrl = constantService.getConstant("api.home");
        try {
            if (userAgent != null)
                if (!userAgent.equals("none"))
                    redirectUrl = constantService.getConstant("html.home");
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            throw new ControllerExceptionDTO(constantService.getConstant("api.list_courses"), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "", constantService.getConstant("api.list_courses"));
        }

    }

    @RequestMapping(value="${api.home}",method=RequestMethod.GET)
    public void getHomeAPI(HttpServletResponse response) {
        String username = ContainerFactory.getAuthenticatedUser();
        AuthenticatedUserEntity authenticatedUserEntity = CredentialRepository.getAuthenticatedUserInfo(jt, username);
        AuthenticatedUserDTO authenticatedUserDTO = DTOFactory.convertToDto(authenticatedUserEntity);

        try {
            if (authenticatedUserDTO.getUsername().equals("login"))
                response.sendRedirect(constantService.getConstant("api.list_courses"));
            else if (authenticatedUserDTO.isTeacher())
                response.sendRedirect(constantService.getConstant("api.teacher_detail_templated", new Object[]{authenticatedUserDTO.getId()}));
            else
                response.sendRedirect(constantService.getConstant("api.student_detail_templated", new Object[]{authenticatedUserDTO.getId()}));
        } catch (Exception e) {
            throw new ControllerExceptionDTO(constantService.getConstant("api.list_courses"), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "", constantService.getConstant("api.list_courses"));
        }
    }
}
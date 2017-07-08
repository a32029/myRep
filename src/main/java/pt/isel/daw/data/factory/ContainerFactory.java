package pt.isel.daw.data.factory;

import org.springframework.hateoas.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pt.isel.daw.business.dto.*;
import pt.isel.daw.business.entities.AuthenticatedUserEntity;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.data.repository.*;

import java.util.HashMap;
import java.util.List;

public class ContainerFactory {

    public static ErrorContainerDTO createErrorContainer(JdbcTemplate jt, ConstantService constantService, Object object) {
        return new ErrorContainerDTO(getAuthenticatedUser(jt, constantService), object);
    }

    public static ListContainerDTO createListContainer(JdbcTemplate jt, ConstantService constantService, Object object) {
        return new ListContainerDTO(getAuthenticatedUser(jt, constantService), object);
    }

    public static DetailContainerDTO createDetailContainer(JdbcTemplate jt, ConstantService constantService, Object object) {
        return new DetailContainerDTO(getAuthenticatedUser(jt, constantService), object);
    }

    public static FormContainerDTO createFormContainer(JdbcTemplate jt, ConstantService constantService, Object object, String method, int numberOptions) {
        FormContainerDTO formContainer = new FormContainerDTO(getAuthenticatedUser(jt, constantService), object, method);
        List options = formContainer.getOptions();
        for (int i=0; i<numberOptions; ++i)
            options.add(new HashMap<String, String>());

        return formContainer;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static FormContainerDTO teacherFormFactory(JdbcTemplate jt, ConstantService constantService, TeacherDTO teacher) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, teacher, "POST", 7);
        List options = formContainer.getOptions();

        ((HashMap) options.get(3)).put("ROLE_ADMIN", "ROLE_ADMIN");
        ((HashMap) options.get(3)).put("ROLE_USER", "ROLE_USER");
        ((HashMap) options.get(6)).put("true", "true");
        ((HashMap) options.get(6)).put("false", "false");

        return formContainer;
    }

    public static FormContainerDTO activateTeacherFormFactory(JdbcTemplate jt, ConstantService constantService, TeacherListDTO teacherList, List<TeacherDTO> deactivatedTeachersDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, teacherList, "POST", 2);
        List options = formContainer.getOptions();

        for (TeacherDTO teacherDTO : deactivatedTeachersDTO)
            ((HashMap) options.get(0)).put(teacherDTO.getTeacherId(), teacherDTO.getName());

        return formContainer;
    }

    public static FormContainerDTO studentFormFactory(JdbcTemplate jt, ConstantService constantService, StudentDTO studentDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, studentDTO, "POST", 7);
        List options = formContainer.getOptions();

        ((HashMap) options.get(3)).put("ROLE_ADMIN", "ROLE_ADMIN");
        ((HashMap) options.get(3)).put("ROLE_USER", "ROLE_USER");
        ((HashMap) options.get(6)).put("true", "true");
        ((HashMap) options.get(6)).put("false", "false");

        return formContainer;
    }

    public static FormContainerDTO activateStudentFormFactory(JdbcTemplate jt, ConstantService constantService, StudentListDTO studentListDTO, List<StudentDTO> deactivatedStudentsDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, studentListDTO, "POST", 2);
        List options = formContainer.getOptions();

        for (StudentDTO studentDTO : deactivatedStudentsDTO)
            ((HashMap) options.get(0)).put(studentDTO.getStudentId(), studentDTO.getName());

        return formContainer;
    }

    public static FormContainerDTO createSemesterFormFactory(JdbcTemplate jt, ConstantService constantService, SemesterDTO semesterDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, semesterDTO, "POST", 4);
        List options = formContainer.getOptions();

        ((HashMap) options.get(3)).put("winter", "winter");
        ((HashMap) options.get(3)).put("summer", "summer");

        return formContainer;
    }

    public static FormContainerDTO removeSemesterFormFactory(JdbcTemplate jt, ConstantService constantService, SemesterListDTO semesterListDTO, List<SemesterDTO> allSemestersDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, semesterListDTO, "DELETE", 1);
        List options = formContainer.getOptions();

        for (SemesterDTO semesterDTO : allSemestersDTO)
            ((HashMap) options.get(0)).put(semesterDTO.getSemester(), semesterDTO.getSemester());

        return formContainer;
    }

    public static FormContainerDTO courseFormFactory(JdbcTemplate jt, ConstantService constantService, CourseDTO course, List<TeacherDTO> activeTeachers) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, course, "POST", 4);
        List options = formContainer.getOptions();

        for (TeacherDTO teacher:activeTeachers)
            ((HashMap) options.get(2)).put(teacher.getTeacherId(), teacher.getName());
        ((HashMap) options.get(3)).put("true", "true");
        ((HashMap) options.get(3)).put("false", "false");

        return formContainer;
    }

    public static FormContainerDTO activateCourseFormFactory(JdbcTemplate jt, ConstantService constantService, CourseListDTO courseList, List<CourseDTO> deactivatedCourses) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, courseList, "PUT", 2);
        List options = formContainer.getOptions();

        for (CourseDTO course : deactivatedCourses)
            ((HashMap) options.get(0)).put(course.getAcronym(), course.getName());
        ((HashMap) options.get(1)).put("true", "true");

        return formContainer;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static FormContainerDTO classFormFactory(JdbcTemplate jt, ConstantService constantService, ClassDTO classDTO, List<SemesterDTO> semestersDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, classDTO, "POST", 7);
        List options = formContainer.getOptions();

        ((HashMap) options.get(0)).put(classDTO.getClassId(), classDTO.getClassId());
        ((HashMap) options.get(2)).put(classDTO.getCourseAcronym(), classDTO.getCourseAcronym());
        for (SemesterDTO semesterDTO:semestersDTO)
            ((HashMap) options.get(3)).put(semesterDTO.getSemester(), semesterDTO.getSemester());
        ((HashMap) options.get(4)).put("true", "true");
        ((HashMap) options.get(4)).put("false", "false");
        ((HashMap) options.get(6)).put("true", "true");
        ((HashMap) options.get(6)).put("false", "false");

        return formContainer;
    }

    public static FormContainerDTO activateClassFormFactory(JdbcTemplate jt, ConstantService constantService, ClassListDTO classListDTO, List<ClassDTO> deactivatedClassesDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, classListDTO, "PUT", 3);
        List options = formContainer.getOptions();

        ((HashMap) options.get(0)).put(classListDTO.getCourseAcronym(), classListDTO.getCourseAcronym());
        for (ClassDTO classDTO: deactivatedClassesDTO)
            ((HashMap) options.get(1)).put(classDTO.getClassId(), classDTO.getClassId());
        ((HashMap) options.get(2)).put("true", "true");

        return formContainer;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static FormContainerDTO addTeacherClassFormFactory(JdbcTemplate jt, ConstantService constantService, ClassTeacherDTO classTeacherDTO, List<TeacherDTO> activeTeachersDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, classTeacherDTO, "PUT", 2);
        List options = formContainer.getOptions();

        ((HashMap) options.get(0)).put(classTeacherDTO.getClassId(), classTeacherDTO.getClassId());
        for (TeacherDTO teacherDTO:activeTeachersDTO)
            ((HashMap) options.get(1)).put(teacherDTO.getTeacherId(), teacherDTO.getName());

        return formContainer;
    }

    public static FormContainerDTO removeTeacherClassFormFactory(JdbcTemplate jt, ConstantService constantService, ClassTeacherDTO classTeacherDTO, List<TeacherDTO> teachersDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, classTeacherDTO, "DELETE", 2);
        List options = formContainer.getOptions();

        ((HashMap) options.get(0)).put(classTeacherDTO.getClassId(), classTeacherDTO.getClassId());
        for (TeacherDTO teacherDTO:teachersDTO)
            ((HashMap) options.get(1)).put(teacherDTO.getTeacherId(), teacherDTO.getName());

        return formContainer;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static FormContainerDTO addStudentClassFormFactory(JdbcTemplate jt, ConstantService constantService, ClassStudentDTO classStudentDTO, List<StudentDTO> studentsDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, classStudentDTO, "PUT", 2);
        List options = formContainer.getOptions();

        ((HashMap) options.get(0)).put(classStudentDTO.getClassId(), classStudentDTO.getClassId());
        for (StudentDTO studentDTO:studentsDTO)
            ((HashMap) options.get(1)).put(studentDTO.getStudentId(), studentDTO.getName());

        return formContainer;
    }

    public static FormContainerDTO removeStudentClassFormFactory(JdbcTemplate jt, ConstantService constantService, ClassStudentDTO classStudentDTO, List<StudentDTO> studentsInClassDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, classStudentDTO, "DELETE", 2);
        List options = formContainer.getOptions();

        ((HashMap) options.get(0)).put(classStudentDTO.getClassId(), classStudentDTO.getClassId());
        for (StudentDTO studentDTO:studentsInClassDTO)
            ((HashMap) options.get(1)).put(studentDTO.getStudentId(), studentDTO.getName());

        return formContainer;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static FormContainerDTO createGroupFormContainer(JdbcTemplate jt, ConstantService constantService, GroupDTO groupDTO, List<StudentDTO> studentsDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, groupDTO, "PUT", 3);
        List options = formContainer.getOptions();

        ((HashMap) options.get(0)).put(groupDTO.getClassId(), groupDTO.getClassId());
        for (StudentDTO studentDTO:studentsDTO)
            ((HashMap) options.get(2)).put(studentDTO.getStudentId(), studentDTO.getName());

        return formContainer;
    }

    public static FormContainerDTO joinGroupFormContainer(JdbcTemplate jt, ConstantService constantService, GroupDTO groupDTO, List<StudentDTO> studentsInClassDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, groupDTO, "PUT", 3);
        List options = formContainer.getOptions();

        ((HashMap) options.get(0)).put(groupDTO.getClassId(), groupDTO.getClassId());
        ((HashMap) options.get(1)).put(groupDTO.getGroupId(), groupDTO.getGroupId());
        for (StudentDTO studentDTO:studentsInClassDTO)
            ((HashMap) options.get(2)).put(studentDTO.getStudentId(), studentDTO.getName());

        return formContainer;
    }

    public static FormContainerDTO leaveGroupFormContainer(JdbcTemplate jt, ConstantService constantService, GroupDTO groupDTO, List<GroupDetailDTO> groupsDetailDTO) {
        FormContainerDTO formContainer = createFormContainer(jt, constantService, groupDTO, "DELETE", 3);
        List options = formContainer.getOptions();

        ((HashMap) options.get(0)).put(groupDTO.getClassId(), groupDTO.getClassId());
        ((HashMap) options.get(1)).put(groupDTO.getGroupId(), groupDTO.getGroupId());
        for (GroupDetailDTO groupDetailDTO:groupsDetailDTO)
            ((HashMap) options.get(2)).put(groupDetailDTO.getStudentId(), groupDetailDTO.getName());

        return formContainer;
    }

    //------------------------------------------------------------------------------------------------------------------

    public static String getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName(); //get logged in username
    }

    public static Resource getAuthenticatedUser(JdbcTemplate jt, ConstantService constantService) {
        String username = getAuthenticatedUser();
        AuthenticatedUserEntity authenticatedUserEntity = CredentialRepository.getAuthenticatedUserInfo(jt, username);
        AuthenticatedUserDTO authenticatedUserDTO = DTOFactory.convertToDto(authenticatedUserEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(authenticatedUserDTO, constantService.getConstant("api.home"));
        responseEntity.add(constantService.getConstant("api.course_detail_templated"), "coordinator_in_course", true);
        responseEntity.add(constantService.getConstant("api.class_detail_templated"), "classes", true);
        if (authenticatedUserDTO.isAdmin()) {
            responseEntity.add(constantService.getConstant("api.list_semesters"), "list_semesters", false);
            responseEntity.add(constantService.getConstant("api.list_teachers"), "list_teachers", false);
            responseEntity.add(constantService.getConstant("api.list_students"), "list_students", false);
        }

        return responseEntity.getResourceList();
    }
}
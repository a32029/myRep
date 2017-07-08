package pt.isel.daw.data.factory;

import pt.isel.daw.business.dto.*;
import pt.isel.daw.business.entities.*;

import java.util.LinkedList;
import java.util.List;

public class DTOFactory {
    public static CourseDTO convertToDto(CourseEntity courseEntity) {
        return new CourseDTO(courseEntity.getName(), courseEntity.getAcronym(), courseEntity.getCoordinatorId(), courseEntity.isActive());
    }

    public static TeacherDTO convertToDto(TeacherEntity teacherEntity) {
        return new TeacherDTO(teacherEntity.getTeacherId(), teacherEntity.getName(), teacherEntity.getEmail(),
                teacherEntity.getRole(), teacherEntity.getUsername(), teacherEntity.getPassword(), teacherEntity.isEnabled());
    }

    public static StudentDTO convertToDto(StudentEntity studentEntity) {
        return new StudentDTO(studentEntity.getStudentId(), studentEntity.getName(), studentEntity.getEmail(),
                studentEntity.getRole(), studentEntity.getUsername(), studentEntity.getPassword(), studentEntity.isEnabled());
    }

    public static SemesterDTO convertToDto(SemesterEntity semesterEntity) {
        return new SemesterDTO(semesterEntity.getSemester(), semesterEntity.getStartYear(), semesterEntity.getEndYear(), semesterEntity.getSeason());
    }

    public static ClassDTO convertToDto(ClassEntity classEntity) {
        return new ClassDTO(classEntity.getClassId(), classEntity.getIdentifier(), classEntity.getCourseAcronym(),
                classEntity.getSemesterId(),classEntity.isAutoEnrollment(), classEntity.getMaxStudentsPerGroup(), classEntity.isActive());
    }

    public static GroupDetailDTO convertToDto(GroupDetailEntity groupDetailEntity) {
        return new GroupDetailDTO(groupDetailEntity.getClassId(), groupDetailEntity.getGroupId(), groupDetailEntity.getStudentId(),
                groupDetailEntity.getName(), groupDetailEntity.getEmail(), groupDetailEntity.getRole());
    }

    public static AuthenticatedUserDTO convertToDto(AuthenticatedUserEntity authenticatedUserEntity) {
        return new AuthenticatedUserDTO(authenticatedUserEntity.getId(), authenticatedUserEntity.getUsername(),
                authenticatedUserEntity.isTeacher(), authenticatedUserEntity.isAdmin(), authenticatedUserEntity.getCoordinatorInCourse(),
                authenticatedUserEntity.getClasses());
    }

    //------------------------------------------------------------------------------------------------------------------

    public static List convertToDto(List listEntities) {
        LinkedList listDTO = new LinkedList<>();
        for (int i=0; i<listEntities.size(); ++i) {
            Object o = listEntities.get(i);
            if (o instanceof CourseEntity)
                listDTO.add(convertToDto((CourseEntity) o));
            if (o instanceof TeacherEntity)
                listDTO.add(convertToDto((TeacherEntity) o));
            if (o instanceof StudentEntity)
                listDTO.add(convertToDto((StudentEntity) o));
            if (o instanceof SemesterEntity)
                listDTO.add(convertToDto((SemesterEntity) o));
            if (o instanceof ClassEntity)
                listDTO.add(convertToDto((ClassEntity) o));
            if (o instanceof GroupDetailEntity)
                listDTO.add(convertToDto((GroupDetailEntity) o));
        }
        return listDTO;
    }

    public static PageListDTO convertToDto(PageListEntity pl) {
        return new PageListDTO(pl.getPageNumber(), pl.getPagesAvailable(), convertToDto(pl.getPageItems()));
    }

    //------------------------------------------------------------------------------------------------------------------

    public static CourseEntity convertToEntity(CourseDTO courseDTO) {
        return new CourseEntity(courseDTO.getName(), courseDTO.getAcronym(), courseDTO.getCoordinatorId(), courseDTO.isActive());
    }

    public static TeacherEntity convertToEntity(TeacherDTO teacherDTO) {
        return new TeacherEntity(teacherDTO.getTeacherId(), teacherDTO.getName(), teacherDTO.getEmail(),
                teacherDTO.getRole(), teacherDTO.getUsername(), teacherDTO.getPassword(), teacherDTO.isEnabled());
    }

    public static TeacherEntity convertToEntity(TeacherListDTO teacherDTO) {
        return new TeacherEntity(teacherDTO.getTeacherId(), "", "", "", "", teacherDTO.getPassword(), true);
    }

    public static StudentEntity convertToEntity(StudentDTO studentDTO) {
        return new StudentEntity(studentDTO.getStudentId(), studentDTO.getName(), studentDTO.getEmail(),
                studentDTO.getRole(), studentDTO.getUsername(), studentDTO.getPassword(), studentDTO.isEnabled());
    }

    public static StudentEntity convertToEntity(StudentListDTO studentDTO) {
        return new StudentEntity(studentDTO.getStudentId(), "", "", "", "", studentDTO.getPassword(), true);
    }

    public static SemesterEntity convertToEntity(SemesterDTO semesterDTO) {
        return new SemesterEntity(semesterDTO.getStartYear(), semesterDTO.getEndYear(), semesterDTO.getSeason());
    }

    public static ClassEntity convertToEntity(ClassDTO classDTO) {
        return new ClassEntity(classDTO.getClassId(), classDTO.getIdentifier(), classDTO.getCourseAcronym(),
                classDTO.getSemesterId(),classDTO.isAutoEnrollment(), classDTO.getMaxStudentsPerGroup(), classDTO.isActive());
    }
}
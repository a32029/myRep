package pt.isel.daw.business.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import pt.isel.daw.business.dto.*;
import pt.isel.daw.business.entities.*;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.business.interfaces.GroupClassService;
import pt.isel.daw.data.factory.ContainerFactory;
import pt.isel.daw.data.factory.DTOFactory;
import pt.isel.daw.data.factory.ResponseEntityFactory;
import pt.isel.daw.data.repository.ClassRepository;
import pt.isel.daw.data.repository.GroupRepository;

import java.util.List;

public class GroupClassServiceImpl implements GroupClassService {
    private DataSourceTransactionManager transactionManager;

    public GroupClassServiceImpl(DataSourceTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    @Autowired
    JdbcTemplate jt;

    @Autowired
    private ConstantService constantService;

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity getPageListGroupsInClass(String classId, int pageNo, int pageSize, String searchParam, String searchValue) {
        PageListEntity<GroupDetailEntity> pl = GroupRepository.getPageListGroupsDetailInClass(jt, classId, pageNo, pageSize, searchParam, searchValue);
        PageListDTO<GroupDetailDTO> plDTO = DTOFactory.convertToDto(pl);

        Resource groupsClass = new Resource(ContainerFactory.createListContainer(jt, constantService, plDTO),
                new Link(constantService.getConstant("api.list_groups_class_templated", new Object[] {classId, pageNo, pageSize, searchParam, searchValue})).withSelfRel());

        groupsClass.add(new Link(constantService.getConstant("api.create_group_class_templated", new Object[]{ classId })).withRel("create_group_class"));

        List<GroupDetailDTO> groupsDTO = plDTO.getPageItems();
        for (GroupDetailDTO groupDTO:groupsDTO) {
            String groupId = groupDTO.getGroupId();
            String studentId = groupDTO.getStudentId();
            groupsClass.add(new Link(constantService.getConstant("api.group_class_detail_templated", new Object[] {classId, 1, 5, "group_id", groupId})).withRel("get_group_" + groupId + "_" + studentId));
        }

        if (plDTO.hasNextPage())
            groupsClass.add(new Link(constantService.getConstant("api.list_groups_class_templated", new Object[] {classId, plDTO.getPageNumber()+1, pageSize, searchParam, searchValue})).withRel("next"));
        if (plDTO.hasPrevPage())
            groupsClass.add(new Link(constantService.getConstant("api.list_groups_class_templated", new Object[] {classId, plDTO.getPageNumber()-1, pageSize, searchParam, searchValue})).withRel("prev"));
        else
            groupsClass.add(new Link(constantService.getConstant("api.class_detail_templated", new Object[] {classId})).withRel("prev"));

        return new ResponseEntity(groupsClass, HttpStatus.OK);
    }

    @Override
    public ResponseEntity createGroupClass(String classId) {
        GroupDTO groupDTO = new GroupDTO(classId, "", "");
        List<StudentEntity> studentsEntity = ClassRepository.getAllStudentsInClass(jt, groupDTO.getClassId());
        List<StudentDTO> studentsDTO = DTOFactory.convertToDto(studentsEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.createGroupFormContainer(jt, constantService, groupDTO, studentsDTO),
                constantService.getConstant("api.create_group_class_templated", new Object[] {classId}));
        responseEntity.add(new Link(constantService.getConstant("resource.create_group_class")).withRel("form_url"), false);
        responseEntity.add(constantService.getConstant("api.group_class_detail_templated", new Object[] {classId}), "prev", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity getGroupDetail(String classId, int pageNo, int pageSize, String searchParam, String groupid) {
        PageListEntity<GroupDetailEntity> pl = GroupRepository.getPageListGroupsDetailInClass(jt, classId, pageNo, pageSize, searchParam, groupid);
        PageListDTO<GroupDetailDTO> plDTO = DTOFactory.convertToDto(pl);

        Resource groupsClass = new Resource(ContainerFactory.createListContainer(jt, constantService, plDTO),
                new Link(constantService.getConstant("api.list_groups_class_templated", new Object[] {classId, pageNo, pageSize, searchParam, groupid})).withSelfRel());

        groupsClass.add(new Link(constantService.getConstant("api.join_group_class_templated", new Object[]{ classId, groupid })).withRel("join_group_class"));
        groupsClass.add(new Link(constantService.getConstant("api.leave_group_class_templated", new Object[]{ classId, groupid })).withRel("leave_group_class"));

        List<GroupDetailDTO> groupsDTO = plDTO.getPageItems();
        for (GroupDetailDTO groupDTO:groupsDTO) {
            String studentId = groupDTO.getStudentId();
            groupsClass.add(new Link(constantService.getConstant("api.student_detail_templated", new Object[] {studentId})).withRel("get_student_" + studentId));
        }

        if (plDTO.hasNextPage())
            groupsClass.add(new Link(constantService.getConstant("api.student_detail_templated", new Object[] {classId, plDTO.getPageNumber()+1, pageSize, searchParam, groupid})).withRel("next"));
        if (plDTO.hasPrevPage())
            groupsClass.add(new Link(constantService.getConstant("api.student_detail_templated", new Object[] {classId, plDTO.getPageNumber()-1, pageSize, searchParam, groupid})).withRel("prev"));
        else
            groupsClass.add(new Link(constantService.getConstant("api.list_groups_class_templated", new Object[] {classId, 1, 4})).withRel("prev"));

        return new ResponseEntity(groupsClass, HttpStatus.OK);
    }

    @Override
    public ResponseEntity joinGroupClass(String classId, String groupId) {
        GroupDTO groupDTO = new GroupDTO(classId, groupId, "");
        List<StudentEntity> studentsInClassEntity = ClassRepository.getAllStudentsInClass(jt, groupDTO.getClassId());
        List<StudentDTO> studentsInClassDTO = DTOFactory.convertToDto(studentsInClassEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.joinGroupFormContainer(jt, constantService, groupDTO, studentsInClassDTO),
                constantService.getConstant("api.join_group_class_templated", new Object[] {classId}));
        responseEntity.add(new Link(constantService.getConstant("resource.join_group_class")).withRel("form_url"), false);
        responseEntity.add(constantService.getConstant("api.group_class_detail_templated", new Object[] {classId}), "prev", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity leaveGroupClass(String classId, String groupId) {
        GroupDTO groupDTO = new GroupDTO(classId, groupId, "");
        List<GroupDetailEntity> groupsDetailEntity = GroupRepository.getGroupDetailInClass(jt, groupDTO.getClassId(), groupDTO.getGroupId());
        List<GroupDetailDTO> groupsDetailDTO = DTOFactory.convertToDto(groupsDetailEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.leaveGroupFormContainer(jt, constantService, groupDTO, groupsDetailDTO),
                constantService.getConstant("api.leave_group_class_templated", new Object[] {classId}));
        responseEntity.add(new Link(constantService.getConstant("resource.leave_group_class")).withRel("form_url"), false);
        responseEntity.add(constantService.getConstant("api.group_class_detail_templated", new Object[] {classId}), "prev", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity createGroupClass(String classId, String groupId, String studentId) {
        GroupRepository.createGroupClass(jt, classId, groupId, studentId);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("join_group",
                constantService.getConstant("resource.join_group_class_templated", new Object[] {classId, groupId, studentId}));
        responseEntity.add(constantService.getConstant("api.group_class_detail_templated", new Object[] {classId, groupId}), "get_group", false);
        responseEntity.add(constantService.getConstant("api.student_detail_templated", new Object[] {studentId}), "get_student", false);
        responseEntity.add(constantService.getConstant("api.list_groups_class_templated", new Object[] {classId}), "prev", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity joinGroupClass(String classId, String groupId, String studentId) {
        GroupRepository.joinGroupClass(jt, classId, groupId, studentId);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("join_group",
                constantService.getConstant("resource.join_group_class_templated", new Object[] {classId, groupId, studentId}));
        responseEntity.add(constantService.getConstant("api.group_class_detail_templated", new Object[] {classId, groupId}), "get_group", false);
        responseEntity.add(constantService.getConstant("api.student_detail_templated", new Object[] {studentId}), "get_student", false);
        responseEntity.add(constantService.getConstant("api.list_groups_class_templated", new Object[] {classId}), "prev", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity leaveGroupClass(String classId, String groupId, String studentId) {
        GroupRepository.leaveGroupClass(jt, classId, groupId, studentId);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("leave_group",
                constantService.getConstant("resource.leave_group_class_templated", new Object[] {classId, groupId, studentId}));
        responseEntity.add(new Link(constantService.getConstant("api.student_detail_templated", new Object[] {studentId})).withRel("get_student"), false);
        responseEntity.add(constantService.getConstant("api.list_groups_class_templated", new Object[] {classId}), "prev", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }
}
package pt.isel.daw.business.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import pt.isel.daw.business.dto.SemesterDTO;
import pt.isel.daw.business.entities.*;
import pt.isel.daw.business.dto.*;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.business.interfaces.SemesterService;
import pt.isel.daw.data.factory.ContainerFactory;
import pt.isel.daw.data.factory.DTOFactory;
import pt.isel.daw.data.factory.ResponseEntityFactory;
import pt.isel.daw.data.repository.SemesterRepository;

import java.util.List;

public class SemesterServiceImpl implements SemesterService {
    private DataSourceTransactionManager transactionManager;

    public SemesterServiceImpl(DataSourceTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    @Autowired
    JdbcTemplate jt;

    @Autowired
    private ConstantService constantService;

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getListSemesters(int pageNo, int pageSize, String searchParam, String searchValue) {
        PageListEntity<SemesterEntity> pl = SemesterRepository.getListSemesters(jt, pageNo, pageSize, searchParam, searchValue);
        PageListDTO<SemesterDTO> plDTO = DTOFactory.convertToDto(pl);

        Resource resource = new Resource(ContainerFactory.createListContainer(jt, constantService, plDTO),
                new Link(constantService.getConstant("api.list_semesters_templated", new Object[]{pageNo, pageSize, searchParam, searchValue})).withSelfRel());

        resource.add(new Link(constantService.getConstant("api.create_semester")).withRel("create_semester"));
        resource.add(new Link(constantService.getConstant("api.remove_semester")).withRel("remove_semester"));

        List<SemesterDTO> semestersDTO = plDTO.getPageItems();
        for (SemesterDTO semesterDTO:semestersDTO) {
            String semesterId = semesterDTO.getSemester();
            resource.add(new Link(constantService.getConstant("api.semester_detail_templated", new Object[] {semesterDTO.getSemester()})).withRel("get_semester_" + semesterId));
        }

        if (plDTO.hasNextPage())
            resource.add(new Link(constantService.getConstant("api.list_semesters_templated", new Object[]{plDTO.getPageNumber()+1, pageSize, searchParam, searchValue})).withRel("next"));
        if (plDTO.hasPrevPage())
            resource.add(new Link(constantService.getConstant("api.list_semesters_templated", new Object[]{plDTO.getPageNumber()-1, pageSize, searchParam, searchValue})).withRel("prev"));

        resource.add(new Link(constantService.getConstant("docs.list_semesters")).withRel("docs_list_semesters"));

        return new ResponseEntity(resource, HttpStatus.OK);
    }

    public ResponseEntity createSemester() {
        SemesterDTO semesterDTO = new SemesterDTO();

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.createSemesterFormFactory(jt, constantService, semesterDTO), constantService.getConstant("api.create_semester"));
        responseEntity.add(constantService.getConstant("resource.create_semester"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_semesters"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.create_semester"), "docs_create_semester", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity removeSemester() {
        SemesterListDTO semestersDTO = new SemesterListDTO();
        List<SemesterEntity> allSemesters = SemesterRepository.getAllSemesters(jt);
        List<SemesterDTO> allSemestersDTO = DTOFactory.convertToDto(allSemesters);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.removeSemesterFormFactory(jt, constantService, semestersDTO, allSemestersDTO), constantService.getConstant("api.remove_semester"));
        responseEntity.add(constantService.getConstant("resource.remove_semester"), "form_url", false);
        responseEntity.add(constantService.getConstant("api.list_semesters"), "prev", false);
        responseEntity.add(constantService.getConstant("docs.remove_semester"), "docs_remove_semester", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity getSemesterDetail(String searchParam, String searchValue) {
        SemesterEntity semesterEntity = SemesterRepository.getSemesterBy(jt, searchParam, searchValue);
        SemesterDTO semesterDTO = DTOFactory.convertToDto(semesterEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory(ContainerFactory.createDetailContainer(jt, constantService, semesterDTO),
                constantService.getConstant("api.semester_detail_templated", new Object[] {semesterDTO.getSemester()}));
        responseEntity.add(constantService.getConstant("docs.semester_detail"), "docs_semester_detail", false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    //------------------------------------------------------------------------------------------------------------------

    public ResponseEntity createSemester(SemesterDTO semesterDTO) {
        SemesterEntity semesterEntity = DTOFactory.convertToEntity(semesterDTO);
        String semesterId = SemesterRepository.createSemester(jt, semesterEntity);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("create_semester", constantService.getConstant("resource.create_semester"));
        responseEntity.add(new Link(constantService.getConstant("api.semester_detail_templated", new Object[] {semesterId})).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }

    public ResponseEntity removeSemester(String semesterId) {
        SemesterRepository.removeSemester(jt, semesterId);

        ResponseEntityFactory responseEntity = new ResponseEntityFactory("remove_semester", constantService.getConstant("resource.remove_semester"));
        responseEntity.add(new Link(constantService.getConstant("api.list_semesters")).withRel("prev"), false);

        return new ResponseEntity(responseEntity.getResourceList(), HttpStatus.OK);
    }
}
package com.example.employeedata.controller;

import java.time.LocalDate;
import java.util.*;

import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.dto.*;
import com.example.employeedata.helpers.Constants;
import com.example.employeedata.service.ProjectService;
import com.example.employeedata.service.ProjectDocService;

import io.swagger.annotations.*;

@Api(tags = "Service to manage projects")
@RestController
@RequestMapping("api/projects")
public class ProjectController<E> {
    private final ProjectService projectService;
    private final ProjectDocService projectDocService;

    public ProjectController(
        ProjectService projectService,
        ProjectDocService projectDocService) {
        this.projectService = projectService;
        this.projectDocService = projectDocService;
    }
    
    @ApiOperation(value = "Creating a project")
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> saveProject(@Valid @RequestBody CreateProjectDto projectDto) {
        return new ResponseEntity<ResponseDto>(projectService.saveProject(projectDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Creating project/projects from exel file")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "file",
                required = true,
                dataType = "file",
                value = "Exel file of .xls or .xlsx format with project data")
        })
    //for now works only with postman
    @PostMapping(value = "/upload", consumes = { "multipart/form-data" })
    public ResponseEntity<ResponseDto> saveProjectsFromFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<ResponseDto>(projectService.saveProjectsFromExelFile(file), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Returns a lst of all projects")
    @GetMapping
    public ResponseEntity<List<E>> getAllProjects() {
        List<E> res = null;

        try {
            res = (List<E>) projectDocService.getAllProjects();
            
            if (res == null) {
                res = (List<E>) projectService.getAllProjects();
            }
        } catch (DataAccessResourceFailureException e) {
            res = (List<E>) projectService.getAllProjects();
        }

        return new ResponseEntity<List<E>>(res, HttpStatus.OK);
    }

    @ApiOperation(value = "Returns a lst of all projects with termination date is later than today")
    @GetMapping("/future")
    public ResponseEntity<List<ProjectDto>> getAllFutureProjects() {
        return new ResponseEntity<List<ProjectDto>>(projectService.getAllProjectsWithFutureTerminationDate(), HttpStatus.OK);
    }

    @ApiOperation(value = "Returns a lst of all projects with termination date is prior to today")
    @GetMapping("/prior")
    public ResponseEntity<List<ProjectDto>> getAllPriorProjects() {
        return new ResponseEntity<List<ProjectDto>>(projectService.getAllProjectsWithPriorTerminationDate(), HttpStatus.OK);
    }

    @ApiOperation(value = "Returns a list of all projects with paging information")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "pageNumber",
                required = false,
                dataType = "string",
                value = "Page number"),
            @ApiImplicitParam(
                name = "pageSize",
                required = false,
                dataType = "string",
                value = "Size of entities per page"),
            @ApiImplicitParam(
                name = "filter",
                required = false,
                dataType = "string",
                value = "Entity parameter by which entities will be sorted"),
            @ApiImplicitParam(
                name = "isAsc",
                required = false,
                dataType = "string",
                value = "Is sorting ascending")
        })
    @GetMapping("/pageable")
    public ResponseEntity<PaginatedResponseDto<ProjectDto>> getAllEmployeesPaging(
                @RequestParam(defaultValue = "0") Integer pageNumber,
                @RequestParam(defaultValue = "10") Integer pageSize,
                @RequestParam(defaultValue = "id") String filter,
                @RequestParam(defaultValue = "") String isAsc) {
        return new ResponseEntity<PaginatedResponseDto<ProjectDto>>(projectService.getAllProjectsPageable(pageNumber, pageSize, filter, isAsc), HttpStatus.OK);
    }

    @ApiOperation(value = "Returns a list of all projects with paging information")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "searchQuery",
                required = true,
                dataType = "string",
                value = "Text by which database will retrive similar data"),
            @ApiImplicitParam(
                name = "pageNumber",
                required = false,
                dataType = "string",
                value = "Page number"),
            @ApiImplicitParam(
                name = "pageSize",
                required = false,
                dataType = "string",
                value = "Size of entities per page"),
            @ApiImplicitParam(
                name = "filter",
                required = false,
                dataType = "string",
                value = "Entity parameter by which entities will be sorted"),
            @ApiImplicitParam(
                name = "isAsc",
                required = false,
                dataType = "string",
                value = "Is sorting ascending")
        })
    @GetMapping("/filtered/pageable")
    public ResponseEntity<PaginatedResponseDto<ProjectDto>> getAllProjectsPagingWithFilter(
                @RequestParam(defaultValue = "") String searchQuery,
                @RequestParam(defaultValue = "0") Integer pageNumber,
                @RequestParam(defaultValue = "10") Integer pageSize,
                @RequestParam(defaultValue = "id") String filter,
                @RequestParam(defaultValue = "") String isAsc) {
        return new ResponseEntity<PaginatedResponseDto<ProjectDto>>(projectService.getAllProjectsPageableAndFiltered(searchQuery, pageNumber, pageSize, filter, isAsc), HttpStatus.OK);
    }

    @ApiOperation(value = "Get projects not assigned to an employee")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "employeeId",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Employee id")
        })
    @GetMapping("/notAssignedTo/{employeeId}")
    public ResponseEntity<List<ProjectDto>> getAllProjectsNotAssignedToEmployee(@PathVariable Long employeeId) {
        return new ResponseEntity<List<ProjectDto>>(projectService.getAllProjectsNotAssignedToEmployeeFromCurrentDate(employeeId), HttpStatus.OK);
    }

    @ApiOperation(value = "Get projects not assigned to an employee")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "employeeId",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Employee id"),
            @ApiImplicitParam(
            name = "date",
            required = true,
            dataType = "string",
            paramType = "path",
            value = "Custom provided date")
        })
    @GetMapping("/notAssignedTo/{employeeId}/{date}")
    public ResponseEntity<List<ProjectDto>> getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(@PathVariable Long employeeId, @PathVariable String date) {
        return new ResponseEntity<List<ProjectDto>>(projectService.getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(employeeId, LocalDate.parse(date)), HttpStatus.OK);
    }

    @ApiOperation(value = "Get projects by development language id")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "devLanguage",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Development language id")
        })
    @GetMapping("devLang/{devLanguage}")
    public ResponseEntity<List<ProjectDto>> getAllProjectsByDevelopmentLanguage(@PathVariable Integer devLanguage) {
        return new ResponseEntity<List<ProjectDto>>(projectService.getAllProjectsByDevelopmentLanguage(devLanguage), HttpStatus.OK);
    }

    @ApiOperation(value = "Get project by id")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "id",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Project id")
        })
    @GetMapping("/{id}")
    public ResponseEntity<E> getProjectById(@PathVariable String id) {
        E res = null;

        try {
            res = (E) projectDocService.getProjectById(id);

            if (res == null) {
                res = (E) projectService.getProjectById(id.toString());
            }
        } catch (DataAccessResourceFailureException e) {
            res = (E) projectService.getProjectById(id.toString());
        }

        return new ResponseEntity<E>(res, HttpStatus.OK);
    }

    @ApiOperation(value = "Update project by id")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "id",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Project id")
        })
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateProject(@PathVariable Long id, @Valid @RequestBody EditProjectDto projectDto) {
        projectService.updateProject(id, projectDto);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Delete project by id")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "id",
                required = true,
                dataType = "string",
                paramType = "path",
                value = "Project id")
        })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }

    // still generates file to C:\\Users\\current_user\\Documents folder
    // todo: delete file after it was returned in a response
    @ApiOperation(value = "Get a list of projects in an Exel file.")
    @GetMapping("/downloadFile")
    public ResponseEntity<?> downloadProjectsInExelFile() {
        Resource resource = null;

        resource = projectService.generateExelFile();

        if(resource == null || !resource.exists()) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = Constants.DOWNLOAD_OCTET_STREAM;
        String headerValue = Constants.ATTACHMENT_FILENAME + resource.getFilename() + "\"";

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
            .body(resource);
    }
}

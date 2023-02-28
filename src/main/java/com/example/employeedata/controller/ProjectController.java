package com.example.employeedata.controller;

import java.time.LocalDate;
import java.util.*;

import javax.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.dto.*;
import com.example.employeedata.helpers.Constants;
import com.example.employeedata.service.ProjectService;
import com.example.employeedata.service.ProjectDocService;
import com.example.employeedata.helpers.DateTimeHelpers;

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
    public ResponseEntity<ResponseDto> saveProject(@Valid @RequestBody CreateProjectDto projectDto, @RequestParam String zoneId) {
        return new ResponseEntity<>(projectService.saveProject(projectDto, zoneId), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Creating project/projects from exel file")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "file",
                required = true,
                dataType = "file",
                value = "Exel file of .xls or .xlsx format with project data"),
            @ApiImplicitParam(
                name = "zoneId",
                required = true,
                dataType = "string",
                value = "Date time zone string representation")
        })
    //for now works only with postman
    @PostMapping(value = "/upload", consumes = { "multipart/form-data" })
    public ResponseEntity<ResponseDto> saveProjectsFromFile(@RequestParam("file") MultipartFile file, @RequestParam("zoneId") String zoneId) {
        return new ResponseEntity<>(projectService.saveProjectsFromExelFile(file ,zoneId), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Returns a lst of all projects")
    @GetMapping
    public ResponseEntity<List<E>> getAllProjects(@RequestParam String zoneId) {
        List<E> res;

        try {
            res = (List<E>) projectDocService.getAllProjects();
            
            if (res == null) {
                res = (List<E>) projectService.getAllProjects(zoneId);
            }
        } catch (DataAccessResourceFailureException e) {
            res = (List<E>) projectService.getAllProjects(zoneId);
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ApiOperation(value = "Returns a lst of all projects with termination date is later than today")
    @GetMapping("/future")
    public ResponseEntity<List<ProjectDto>> getAllFutureProjects(@RequestParam String zoneId) {
        return new ResponseEntity<>(projectService.getAllProjectsWithFutureTerminationDate(zoneId), HttpStatus.OK);
    }

    @ApiOperation(value = "Returns a lst of all projects with termination date is prior to today")
    @GetMapping("/prior")
    public ResponseEntity<List<ProjectDto>> getAllPriorProjects(@RequestParam String zoneId) {
        return new ResponseEntity<>(projectService.getAllProjectsWithPriorTerminationDate(zoneId), HttpStatus.OK);
    }

    @ApiOperation(value = "Returns a list of all projects with paging information")
    @ApiImplicitParams(
        value = {
            @ApiImplicitParam(
                name = "searchQuery",
                    dataType = "string",
                value = "Text by which database will retrieve similar data"),
            @ApiImplicitParam(
                name = "pageNumber",
                dataType = "string",
                value = "Page number"),
            @ApiImplicitParam(
                name = "pageSize",
                dataType = "string",
                value = "Size of entities per page"),
            @ApiImplicitParam(
                name = "sortBy",
                dataType = "integer",
                value = "Entity parameter by which entities will be sorted"),
            @ApiImplicitParam(
                name = "isAsc",
                dataType = "string",
                value = "Is sorting ascending")
        })
    @GetMapping("/page")
    public ResponseEntity<PaginatedResponseDto<ProjectDto>> getAllProjectsPage(
                @RequestParam(required = false) String searchQuery,
                @RequestParam(required = false) Integer pageNumber,
                @RequestParam(required = false) Integer pageSize,
                @RequestParam(required = false) Integer sortBy,
                @RequestParam(required = false) String isAsc,
                @RequestParam String zoneId) {
        return new ResponseEntity<>(projectService.getAllProjectsPage(searchQuery, pageNumber, pageSize, sortBy, isAsc, zoneId), HttpStatus.OK);
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
    public ResponseEntity<List<ProjectDto>> getAllProjectsNotAssignedToEmployee(@PathVariable Long employeeId, @RequestParam String zoneId) {
        return new ResponseEntity<>(projectService.getAllProjectsNotAssignedToEmployeeFromCurrentDate(employeeId, zoneId), HttpStatus.OK);
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
    public ResponseEntity<List<ProjectDto>> getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(@PathVariable Long employeeId, @PathVariable String date, @RequestParam String zoneId) {
        return new ResponseEntity<>(projectService.getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(employeeId, LocalDate.parse(date), zoneId), HttpStatus.OK);
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
    public ResponseEntity<List<ProjectDto>> getAllProjectsByDevelopmentLanguage(@PathVariable Integer devLanguage, @RequestParam String zoneId) {
        return new ResponseEntity<>(projectService.getAllProjectsByDevelopmentLanguage(devLanguage, zoneId), HttpStatus.OK);
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
    public ResponseEntity<E> getProjectById(@PathVariable String id, @RequestParam String zoneId) {
        E res;

        try {
            res = (E) projectDocService.getProjectById(id);

            if (res == null) {
                res = (E) projectService.getProjectById(id, zoneId);
            }
        } catch (DataAccessResourceFailureException e) {
            res = (E) projectService.getProjectById(id, zoneId);
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
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
    public ResponseEntity<HttpStatus> updateProject(@PathVariable Long id, @Valid @RequestBody EditProjectDto projectDto, @RequestParam String zoneId) {
        projectService.updateProject(id, projectDto, zoneId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Get a list of projects in an Exel file.")
    @GetMapping("/downloadFile")
    public ResponseEntity<?> downloadProjectsInExelFile(@RequestParam String zoneId) {
        byte[] byteArr = projectService.generateExelFile(zoneId);

        if(byteArr.length == 0) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        ByteArrayResource resource = new ByteArrayResource(byteArr);
        LocalDate date = DateTimeHelpers.getLocalDateNow();
        String contentType = Constants.DOWNLOAD_OCTET_STREAM;
        String headerValue = Constants.ATTACHMENT_FILENAME + Constants.PROJECT_FILE_NAME + date + ".xlsx";

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
            .body(resource);
    }

    @ApiOperation(value = "Get a map of projects grouped by development language.")
    @GetMapping("/byDevLanguage")
    public ResponseEntity<Map<String, List<ProjectDto>>> getProjectsGroupedByDevLanguage(@RequestParam String zoneId) {
        return new ResponseEntity<>(projectService.getProjectsGroupedByDevLanguage(zoneId), HttpStatus.OK);
    }
}

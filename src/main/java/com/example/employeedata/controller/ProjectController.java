package com.example.employeedata.controller;

import java.time.LocalDate;
import java.util.*;

import javax.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.employeedata.dto.*;
import com.example.employeedata.service.ProjectService;

import io.swagger.annotations.*;

@Api(tags = "Service to manage projects")
@RestController
@RequestMapping("api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    
    @ApiOperation(value = "Creating a project")
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> saveProject(@Valid @RequestBody CreateProjectDto projectDto) {
        return new ResponseEntity<ResponseDto>(projectService.saveProject(projectDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Returns a lst of all projects")
    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return new ResponseEntity<List<ProjectDto>>(projectService.getAllProjects(), HttpStatus.OK);
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
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        return new ResponseEntity<ProjectDto>(projectService.getProjectById(id), HttpStatus.OK);
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
}

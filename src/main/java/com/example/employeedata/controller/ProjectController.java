package com.example.employeedata.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.employeedata.dto.*;
import com.example.employeedata.service.ProjectService;

@RestController
@RequestMapping("api/projects")
public class ProjectController {
    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> saveProject(@RequestBody CreateProjectDto projectDto) {
        return new ResponseEntity<ResponseDto>(projectService.saveProject(projectDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return new ResponseEntity<List<ProjectDto>>(projectService.getAllProjects(), HttpStatus.OK);
    }

    @GetMapping("/future")
    public ResponseEntity<List<ProjectDto>> getAllFutureProjects() {
        return new ResponseEntity<List<ProjectDto>>(projectService.getAllProjectsWithFutureTerminationDate(), HttpStatus.OK);
    }

    @GetMapping("/prior")
    public ResponseEntity<List<ProjectDto>> getAllPriorProjects() {
        return new ResponseEntity<List<ProjectDto>>(projectService.getAllProjectsWithPriorTerminationDate(), HttpStatus.OK);
    }

    @GetMapping("/notAssignedTo/{employeeId}")
    public ResponseEntity<List<ProjectDto>> getAllProjectsNotAssignedToEmployee(@PathVariable Long employeeId) {
        return new ResponseEntity<List<ProjectDto>>(projectService.getAllProjectsNotAssignedToEmployeeFromCurrentDate(employeeId), HttpStatus.OK);
    }

    @GetMapping("/notAssignedTo/{employeeId}/{date}")
    public ResponseEntity<List<ProjectDto>> getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(@PathVariable Long employeeId, @PathVariable String date) {
        return new ResponseEntity<List<ProjectDto>>(projectService.getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(employeeId, LocalDate.parse(date)), HttpStatus.OK);
    }

    @GetMapping("devLang/{devLanguage}")
    public ResponseEntity<List<ProjectDto>> getAllProjectsByDevelopmentLanguage(@PathVariable Integer devLanguage) {
        return new ResponseEntity<List<ProjectDto>>(projectService.getAllProjectsByDevelopmentLanguage(devLanguage), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        return new ResponseEntity<ProjectDto>(projectService.getProjectById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateProject(@PathVariable Long id, @RequestBody EditProjectDto projectDto) {
        projectService.updateProject(id, projectDto);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }
}

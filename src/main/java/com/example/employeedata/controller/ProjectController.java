package com.example.employeedata.controller;

import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.employeedata.dto.ProjectDto;
import com.example.employeedata.entity.Project;
import com.example.employeedata.service.ProjectService;

@RestController
@RequestMapping("api/projects")
public class ProjectController {
    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    
    @PostMapping("/add")
    public ResponseEntity<Project> saveProject(@RequestBody ProjectDto projectDto) {
        return new ResponseEntity<Project>(projectService.save(projectDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Project>> allProjects() {
        return new ResponseEntity<List<Project>>(projectService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable(name= "id") long id) {
        return new ResponseEntity<Project>(projectService.getById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable(name = "id") long id, @RequestBody ProjectDto projectDto) {
        return new ResponseEntity<Project>(projectService.update(id, projectDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProject(@PathVariable(name = "id") long id) {
        projectService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

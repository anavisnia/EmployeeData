package com.example.employeedata.controller.elastic;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.employeedata.document.ProjectDoc;
import com.example.employeedata.dto.CreateProjectDto;
import com.example.employeedata.dto.EditProjectDto;
import com.example.employeedata.dto.ResponseDto;
import com.example.employeedata.service.ProjectDocService;

import io.swagger.annotations.*;

@Api(tags = "Service to manage elasticsearch project queries")
@RestController
@RequestMapping("/api/projects/elastic")
public class ProjectDocController {
    private final ProjectDocService projectDocService;

    public ProjectDocController(ProjectDocService projectDocService) {
        this.projectDocService = projectDocService;
    }

    @ApiOperation(value = "Get all projects")
    @GetMapping("/all")
    public ResponseEntity<List<ProjectDoc>> getAllProjects() {
        return new ResponseEntity<List<ProjectDoc>>(projectDocService.getAllProjects(), HttpStatus.OK);
    }

    @ApiOperation(value = "Creating a project")
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> saveProject(@Valid @RequestBody CreateProjectDto projectDto) {
        return new ResponseEntity<ResponseDto>(projectDocService.saveProject(projectDto), HttpStatus.CREATED);
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
    public ResponseEntity<ProjectDoc> getProjectById(@PathVariable String id) {
        return new ResponseEntity<ProjectDoc>(projectDocService.getProjectById(id), HttpStatus.OK);
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
    public ResponseEntity<HttpStatus> updateProject(@PathVariable String id, @Valid @RequestBody EditProjectDto editProjectDto) {
        projectDocService.updateProject(id, editProjectDto);
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
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable String id) {
        projectDocService.deleteProject(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }
}

package com.example.employeedata.service;

import java.util.*;

import com.example.employeedata.dto.ProjectDto;
import com.example.employeedata.entity.Project;

public interface ProjectService {
    Project save(ProjectDto projectDto);
    List<Project> getAll();
    Project getById(long id);
    Project update(long id, ProjectDto projectDto);
    void delete(long id);
}

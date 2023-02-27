package com.example.employeedata.mappers;

import java.util.*;

import com.example.employeedata.document.ProjectDoc;
import com.example.employeedata.dto.*;
import com.example.employeedata.helpers.*;

public final class ProjectDocMapper {
    public static final String errResource = "Project Doc";

    public static ProjectDoc mapToProjectDoc(CreateProjectDto projectDto) {
        ProjectDoc projectDoc = new ProjectDoc();
        
        projectDoc.setId(HelperFunctions.generateId());
        projectDoc.setTitle(projectDto.getTitle());
        projectDoc.setDescription(projectDto.getDescription());
        projectDoc.setCustomer(projectDto.getCustomer());
        projectDoc.setTeamSize(projectDto.getTeamSize());
        projectDoc.setTerminationDate(projectDto.getTerminationDate().toLocalDate());
        projectDoc.setCompletionDate(projectDto.getCompletionDate());
        projectDoc.setDevLanguage(CustomPropValidators.validateDevLang(projectDto.getDevLanguage(), errResource).label);
        projectDoc.setModificationDate(new Date());

        return projectDoc;
    }

    public static ProjectDoc mapToProjectDoc(ProjectDoc existingProject, EditProjectDto editProjectDto) {

        existingProject.setTitle(editProjectDto.getTitle());
        existingProject.setDescription(editProjectDto.getDescription());
        existingProject.setCustomer(editProjectDto.getCustomer());
        existingProject.setTeamSize(editProjectDto.getTeamSize());
        existingProject.setTerminationDate(editProjectDto.getTerminationDate().toLocalDate());
        existingProject.setCompletionDate(editProjectDto.getCompletionDate());
        existingProject.setDevLanguage(CustomPropValidators.validateDevLang(editProjectDto.getDevLanguage(), errResource).label);
        existingProject.setModificationDate(new Date());

        return existingProject;
    }
}

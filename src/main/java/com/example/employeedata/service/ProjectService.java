package com.example.employeedata.service;

import java.time.*;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.dto.*;

public interface ProjectService {
    ResponseDto saveProject(CreateProjectDto projectDto);
    ResponseDto saveProjectsFromExelFile(MultipartFile multipartFile, String zoneId);
    List<ProjectDto> getAllProjects(String zoneId);
    PaginatedResponseDto<ProjectDto> getAllProjectsPage(String filter, Integer pageNumber, Integer pageSize, Integer sortBy, String isAsc, String zoneId);
    List<ProjectDto> getAllProjectsWithFutureTerminationDate(String zoneId);
    List<ProjectDto> getAllProjectsWithPriorTerminationDate(String zoneId);
    List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromCurrentDate(Long employeeId, String zoneId);
    List<ProjectDto> getAllProjectsNotAssignedToEmployeeFromFutureCustomDate(Long employeeId, LocalDate date, String zoneId);
    List<ProjectDto> getAllProjectsByDevelopmentLanguage(Integer devLanguage,String zoneId);
    ProjectDto getProjectById(String projectId, String zoneId);
    void updateProject(Long projectId, EditProjectDto projectDto);
    void deleteProject(Long projectId);
    byte[] generateExelFile(String zoneId);
    Map<String, List<ProjectDto>> getProjectsGroupedByDevLanguage(String zoneId);
    Map<String, String> getZoneIdsWithOffset(String isRegionSort);
}

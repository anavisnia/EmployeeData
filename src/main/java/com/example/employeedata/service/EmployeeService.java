package com.example.employeedata.service;

import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.dto.*;

public interface EmployeeService {
    ResponseDto saveEmployee(CreateEmployeeDto employeeDto);
    ResponseDto saveEmployeesFromExelFile(MultipartFile file);
    List<EmployeeDto> getAllEmployees();
    PaginatedResponseDto<EmployeeDto> getAllEmployeesPage(String searchQuery, Integer pageNumber, Integer pageSize, Integer sortBy, String isAsc);
    List<EmployeeFileDto> getAllEmployeesIncludingProjects();
    EmployeeDto getEmployeeById(String employeeId);
    List<EmployeeDto> getEmployeesByProjectId(Long projectId);
    List<EmployeeDto> getEmployeesByDevLanguage(Integer devLanguage);
    List<EmployeeDto> getEmployeesByRole(Integer role);
    void updateEmployee(Long employeeId, EditEmployeeDto EditEmployeeDto);
    void deleteEmployee(Long employeeId);
    Map<String, List<EmployeeDto>> getEmployeesGroupedByDevLanguage();
    Map<String, List<EmployeeDto>> getEmployeesGroupedByRole();
    byte[] generateExelFile();
}

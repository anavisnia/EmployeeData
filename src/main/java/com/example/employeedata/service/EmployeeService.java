package com.example.employeedata.service;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.dto.*;

@Service
public interface EmployeeService {
    ResponseDto saveEmployee(CreateEmployeeDto employeeDto);
    ResponseDto saveEmployeesFromExelFile(MultipartFile file);
    List<EmployeeDto> getAllEmployees();
    PaginatedResponseDto<EmployeeDto> getAllEmployeesPageable(Integer pageNumber, Integer pageSize, String filter, String isAsc);
    PaginatedResponseDto<EmployeeDto> getAllEmployeesPageableAndFiltered(String query, Integer pageNumber, Integer pageSize, String filter, String isAsc);
    List<EmployeeFileDto> getAllEmployeesIncludingProjects();
    EmployeeDto getEmployeeById(String employeeId);
    List<EmployeeDto> getEmployeesByProjectId(Long projectId);
    List<EmployeeDto> getEmployeesByDevLanguage(Integer devLanguage);
    List<EmployeeDto> getEmployeesByRole(Integer role);
    void updateEmployee(Long employeeId, EditEmployeeDto EditEmployeeDto);
    void deleteEmployee(Long employeeId);
    byte[] generateExelFile();
}

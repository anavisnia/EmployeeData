package com.example.employeedata.service.impl;

import java.util.*;

import javax.validation.*;

import org.springframework.stereotype.Service;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.*;
import com.example.employeedata.exception.ResourceNotFoundException;
import com.example.employeedata.mappers.EmployeeMapper;
import com.example.employeedata.repository.*;
import com.example.employeedata.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final Validator validator;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final String resourceName = "Employee";
    
    public EmployeeServiceImpl(Validator validator, EmployeeRepository employeeRepository, ProjectRepository projectRepository) {
        this.validator = validator;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public ResponseDto saveEmployee(CreateEmployeeDto employeeDto) {
        Set<ConstraintViolation<CreateEmployeeDto>> violations = validator.validate(employeeDto);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<CreateEmployeeDto> constraintViolation : violations) {
                sb.append(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage() + ". ");
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString().trim(), violations);
        }

        Employee employee = new Employee();
        
        if (employeeDto.getProjectIds().isEmpty()) {
            employee = EmployeeMapper.mapToEmployee(employeeDto);
        } else {
            List<Project> projects = getProjects(employeeDto.getProjectIds());
            employee = EmployeeMapper.mapToEmployee(employeeDto, projects);
        }

        Employee dbResponse = employeeRepository.save(employee);

        return new ResponseDto(dbResponse.getId(), resourceName);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return EmployeeMapper.mapToListEmployeesDto(employeeRepository.findAll());
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        return EmployeeMapper.mapToEmployeeDto(
            employeeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(resourceName, "id", id)
            )
        );
    }

    @Override
    public List<EmployeeDto> getEmployeeByProjectId(Long id) {
        return EmployeeMapper.mapToListEmployeesDto(employeeRepository.findAllEmployeesByProjectId(id));
    }

    @Override
    public void updateEmployee(Long id, EditEmployeeDto editEmployeeDto) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", id)
        );

        Set<ConstraintViolation<EditEmployeeDto>> violations = validator.validate(editEmployeeDto);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<EditEmployeeDto> constraintViolation : violations) {
                sb.append(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage() + ". ");
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString().trim(), violations);
        }

        if (editEmployeeDto.getProjectIds().isEmpty()) {
            employee = EmployeeMapper.mapToEmployee(employee, editEmployeeDto);
        } else {
            List<Project> projects = getProjects(editEmployeeDto.getProjectIds());
            employee = EmployeeMapper.mapToEmployee(employee, editEmployeeDto, projects);
        }

        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", id)
        );
        
        employeeRepository.delete(employee);
    }
    
    private List<Project> getProjects(List<Long> projectIds) {
        List<Project> projects = new ArrayList<>();

        for (Long id : projectIds) {
            Project project = projectRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Project", "id", id)
            );

            projects.add(project);
        }

        return projects;
    }
    
}

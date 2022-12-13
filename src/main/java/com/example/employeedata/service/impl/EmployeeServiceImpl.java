package com.example.employeedata.service.impl;

import java.util.*;

import javax.validation.*;

import org.springframework.stereotype.Service;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.*;
import com.example.employeedata.exception.ResourceNotFoundException;
import com.example.employeedata.repository.*;
import com.example.employeedata.service.EmployeeService;
import com.example.employeedata.service.helpers.EmployeeMapper;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final Validator validator;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    
    public EmployeeServiceImpl(Validator validator, EmployeeRepository employeeRepository, ProjectRepository projectRepository) {
        this.validator = validator;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Employee save(EmployeeDto employeeDto) {
        Set<ConstraintViolation<EmployeeDto>> violations = validator.validate(employeeDto);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<EmployeeDto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }

        Employee employee = new Employee();
        
        if (employeeDto.getProjectIds().isEmpty()) {
            employee = EmployeeMapper.mapToEmployee(employeeDto);
        } else {
            List<Project> projects = getProjects(employeeDto.getProjectIds());
            employee = EmployeeMapper.mapToEmployee(employeeDto, projects);
        }
        
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getById(long id) {
        return employeeRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Employee", "id", id)
        );
    }

    @Override
    public List<Employee> getByProjectId(long id) {
        return employeeRepository.findAllEmployeesByProjectId(id);
    }

    @Override
    public Employee update(long id, EditEmployeeDto editEmployeeDto) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Employee", "id", id)
        );

        Set<ConstraintViolation<EditEmployeeDto>> violations = validator.validate(editEmployeeDto);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<EditEmployeeDto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }

        if (editEmployeeDto.getProjectIds().isEmpty()) {
            employee = EmployeeMapper.mapToEmployee(employee, editEmployeeDto);
        } else {
            List<Project> projects = getProjects(editEmployeeDto.getProjectIds());
            employee = EmployeeMapper.mapToEmployee(employee, editEmployeeDto, projects);
        }

        return employeeRepository.save(employee);
    }

    @Override
    public void delete(long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Employee", "id", id)
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

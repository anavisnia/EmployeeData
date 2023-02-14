package com.example.employeedata.service.impl;

import java.util.List;
import java.util.stream.*;

import org.springframework.stereotype.Service;

import com.example.employeedata.document.EmployeeDoc;
import com.example.employeedata.document.ProjectDoc;
import com.example.employeedata.dto.*;
import com.example.employeedata.exception.*;
import com.example.employeedata.mappers.EmployeeDocMapper;
import com.example.employeedata.repository.elastic.EmployeeESRepository;
import com.example.employeedata.repository.elastic.ProjectESRepository;
import com.example.employeedata.service.EmployeeDocService;

@Service
public class EmployeeDocServiceImpl implements EmployeeDocService {

    private final EmployeeESRepository employeeESRepository;
    private final ProjectESRepository projectESRepository;
    private final String resourceName = "EmployeeDoc";

    public EmployeeDocServiceImpl(EmployeeESRepository employeeESRepository, ProjectESRepository projectESRepository) {
        this.employeeESRepository = employeeESRepository;
        this.projectESRepository = projectESRepository;
    }

    @Override
    public ResponseDto saveEmployee(CreateEmployeeDto employeeDto) {
        EmployeeDoc employeeDoc = new EmployeeDoc();

        if (employeeDto.getProjectIds().isEmpty()) {
            employeeDoc = EmployeeDocMapper.mapToEmployeeDoc(employeeDto);
        } else {
            List<String> projectIds = employeeDto.getProjectIds().stream().map(Object::toString).collect(Collectors.toList());
            List<ProjectDoc> projects = StreamSupport.stream(projectESRepository.findAllById(projectIds).spliterator(), false)
                .collect(Collectors.toList());
            employeeDoc = EmployeeDocMapper.mapToEmployeeDoc(employeeDto, projects);
        }

        EmployeeDoc dbResponse = employeeESRepository.save(employeeDoc);

        return new ResponseDto(dbResponse.getId(), resourceName, false);
    }

    @Override
    public List<EmployeeDoc> getAllEmployees() {
        Iterable<EmployeeDoc> employees = employeeESRepository.findAll();

        List<EmployeeDoc> empl = StreamSupport.stream(employees.spliterator(), false)
            .collect(Collectors.toList());

        return empl;
    }

    @Override
    public EmployeeDoc getEmployeeById(String employeeId) {
        return employeeESRepository.findById(employeeId).orElse(null);
    }

    @Override
    public void updateEmployee(String employeeId, EditEmployeeDto editEmployeeDto) {
        EmployeeDoc employee = employeeESRepository.findById(employeeId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", employeeId)
        );
        
        List<ProjectDoc> projects = getProjectDocs(editEmployeeDto.getProjectIds().stream()
            .map(Object::toString).collect(Collectors.toList()));

        employeeESRepository.save(EmployeeDocMapper.mapToEmployeeDoc(employee, editEmployeeDto, projects));
    }

    @Override
    public void deleteEmployee(String employeeId) {
        EmployeeDoc employee = employeeESRepository.findById(employeeId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", employeeId)
        );

        employeeESRepository.delete(employee);
    }


    private List<ProjectDoc> getProjectDocs(List<String> projectIds) {
        return StreamSupport.stream(projectESRepository.findAllById(projectIds).spliterator(), false)
            .collect(Collectors.toList());
    }
}

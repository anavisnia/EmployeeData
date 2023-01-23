package com.example.employeedata.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.employeedata.document.*;
import com.example.employeedata.dto.ResultQueryDto;

@Service
public interface ESearchService {
    Iterable<EmployeeDoc> getAllEmployees();
    Iterable<ProjectDoc> getAllProjects();
    ResultQueryDto searchByQueryCustomFields(String[] fields, String query) throws IOException;
    ResultQueryDto searchByQueryCustomFields(String index, String[] fields, String query) throws IOException;
}

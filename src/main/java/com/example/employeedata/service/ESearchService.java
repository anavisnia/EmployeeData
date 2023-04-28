package com.example.employeedata.service;

import java.io.IOException;

import com.example.employeedata.document.*;
import com.example.employeedata.dto.ResultQueryDto;

public interface ESearchService {
    Iterable<EmployeeDoc> getAllEmployees();
    Iterable<ProjectDoc> getAllProjects();
    ResultQueryDto searchByQueryCustomFields(String[] fields, String query) throws IOException;
    ResultQueryDto searchByQueryCustomFields(String index, String[] fields, String query) throws IOException;
}

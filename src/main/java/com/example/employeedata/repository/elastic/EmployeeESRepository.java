package com.example.employeedata.repository.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.employeedata.document.EmployeeDoc;

public interface EmployeeESRepository extends ElasticsearchRepository<EmployeeDoc, String> {
    
}

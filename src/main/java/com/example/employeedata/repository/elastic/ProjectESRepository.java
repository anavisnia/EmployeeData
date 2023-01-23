package com.example.employeedata.repository.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.employeedata.document.ProjectDoc;

public interface ProjectESRepository extends ElasticsearchRepository<ProjectDoc, String> {
    
}

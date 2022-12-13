package com.example.employeedata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employeedata.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    
}

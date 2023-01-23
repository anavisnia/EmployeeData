package com.example.employeedata.document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.*;

import com.example.employeedata.helpers.Indecies;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = Indecies.EMPLOYEE_INDEX)
public class EmployeeDoc {
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String firstName;

    @Field(type = FieldType.Text)
    private String lastName;

    @Field(type = FieldType.Date)
    private LocalDate birthDate;

    @Field(type = FieldType.Text)
    private String role;

    @Field(type = FieldType.Text)
    private String devLanguage;

    @Field(type = FieldType.Date)
    private Date modificationDate;

    @Field(type = FieldType.Nested)
    private List<ProjectDoc> projects;
}

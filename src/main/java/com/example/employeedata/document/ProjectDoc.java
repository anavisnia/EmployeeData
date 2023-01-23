package com.example.employeedata.document;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.elasticsearch.annotations.*;

import com.example.employeedata.helpers.Indecies;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = Indecies.PROJECT_INDEX)
public class ProjectDoc {
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Integer)
    private Integer teamSize;

    @Field(type = FieldType.Keyword)
    private String customer;

    @Field(type = FieldType.Date)
    private LocalDate terminationDate;

    @Field(type = FieldType.Text)
    private String devLanguage;

    @Field(type = FieldType.Date)
    private Date modificationDate;
}

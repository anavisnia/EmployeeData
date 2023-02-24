package com.example.employeedata.dto;

import java.util.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponseDto<E> {
    private List<E> entities = new ArrayList<>();
    private Long numberOfItems = (long) 0;
    private Integer numberOfPages = 0;
    private Integer currentPage = 0;
}

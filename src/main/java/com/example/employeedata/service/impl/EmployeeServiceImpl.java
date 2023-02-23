package com.example.employeedata.service.impl;

import java.io.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.dto.*;
import com.example.employeedata.entity.*;
import com.example.employeedata.enums.FileTypes;
import com.example.employeedata.exception.*;
import com.example.employeedata.helpers.*;
import com.example.employeedata.mappers.*;
import com.example.employeedata.repository.*;
import com.example.employeedata.service.EmployeeService;

@Service
public class EmployeeServiceImpl<E> implements EmployeeService {
    private final Validator validator;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final String resourceName = "Employee";
    
    public EmployeeServiceImpl(Validator validator,
            EmployeeRepository employeeRepository,
            ProjectRepository projectRepository) {
        this.validator = validator;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public ResponseDto saveEmployee(CreateEmployeeDto employeeDto) {
        constraintViolationCheck(employeeDto);

        Employee employee = new Employee();
        
        if (employeeDto.getProjectIds().isEmpty()) {
            employee = EmployeeMapper.mapToEmployee(employeeDto);
        } else {
            List<Project> projects = getProjects(employeeDto.getProjectIds());
            employee = EmployeeMapper.mapToEmployee(employeeDto, projects);
        }
        employee.setModificationDate(new Date());
        Employee dbResponse = employeeRepository.save(employee);

        return new ResponseDto(dbResponse.getId(), resourceName, false);
    }

    @Override
    public ResponseDto saveEmployeesFromExelFile(MultipartFile multipartFile) {
        if (multipartFile == null || (multipartFile != null && multipartFile.getOriginalFilename().isBlank()) ) {
            throw new CustomValidationException("File", "File and/or file name cannot be null or empty");
        }
        CustomPropValidators.isProperFileType(multipartFile.getOriginalFilename());

        File file = FileHelperFunctions.castMultipartFileToFile(multipartFile);
        String fileType = FileHelperFunctions.getExtensionFromFileName(file.getName());
        Set<Employee> createEmployees = new HashSet<>();
        Set<String[]> failedValidationEntities = new HashSet<>();

        try(FileInputStream fileBytes = new FileInputStream(file);
             ) {
            Iterator<Row> rows = null;

            if (FileTypes.Xls.label.equalsIgnoreCase(fileType)) {
                try (HSSFWorkbook xlsWorkBook = new HSSFWorkbook(fileBytes)) {
                    HSSFSheet workBookSheet = xlsWorkBook.getSheetAt(0);
                    rows = workBookSheet.iterator();
                }
            } else if(FileTypes.Xlsx.label.equalsIgnoreCase(fileType)) {
                try (XSSFWorkbook xlsxWorkBook = new XSSFWorkbook(fileBytes)) {
                    XSSFSheet workBookSheet = xlsxWorkBook.getSheetAt(0);
                    rows = workBookSheet.iterator();
                }
            }

            int rowCount = 0;
            Row row = null;
            //to skip first row which contains additional information about cells
            if (rowCount == 0 && rows != null && rows.hasNext()) {
                rowCount++;
                row = rows.next(); // row number is 0
            }

            while (rows != null && rows.hasNext() && rowCount == 1) {
                row = rows.next();

                String[] employeeData = new String[Constants.EMPLOYEE_FILE_HEADERS.length];

                int cellCount = 0;
                while (cellCount < employeeData.length) {
                    Cell cell = row.getCell(cellCount);

                    String cellValue = "";

                    cellValue = FileHelperFunctions.getCellValue(cell);

                    employeeData[cellCount] = cellValue;

                    cellCount++;
                }

                List<Project> projects = new ArrayList<>();
                if (employeeData[5] != null) {
                    projects = getProjects(HelperFunctions.getListOfLongValuesFromString(employeeData[5]));
                }

                if (!CustomPropValidators.areAllFieldsEmpty(employeeData)) {
                    if (CustomPropValidators.isValidEmployeeFile(employeeData)) {
                        createEmployees.add(EmployeeMapper.mapToEmployee(employeeData, projects));
                    } else {
                        failedValidationEntities.add(employeeData);
                    }
                }
                
            }
        } catch (IOException e) {
            //will throw Internal Server Error

        } finally {
            //deliting temp file
            if (file.exists()) {
                file.delete();
            }
        }

        ResponseDto response = null;

        List<Long> dbResponse = employeeRepository.saveAll(createEmployees).stream()
            .filter(Objects::nonNull).map(Employee::getId).collect(Collectors.toList());

        if (createEmployees.isEmpty() && !failedValidationEntities.isEmpty()) {
            response = new ResponseDto(
                failedValidationEntities,
                "Employee/employees",
                " error. No entities to save into database",
                true);
        } else if(!failedValidationEntities.isEmpty()) {
            response = new ResponseDto(
                dbResponse, "Employee/employees created successfully",
                failedValidationEntities, "These rows conatain errors. Please check"
            );
        } else {
            response = new ResponseDto(dbResponse, "Employee/employees", false);
        }

        return response;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
            .stream()
            .map(EmployeeMapper::mapToEmployeeDto)
            .collect(Collectors.toList());
    }

    @Override
    public PaginatedResponseDto<EmployeeDto> getAllEmployeesPage(String searchQuery, Integer pageNumber, Integer pageSize, Integer sortBy, String isAsc) {
        pageNumber = pageNumber == null || (pageNumber != null && pageNumber < 0) ? 0 : pageNumber;

        pageSize = CustomPropValidators.checkPageSzie(pageSize);

        Pageable paging = null;
        Page<Employee> result = null;
        String sorting = "";

        if (searchQuery == null || searchQuery != null && searchQuery.isBlank()) {
            sorting = CustomPropValidators.checkFiledSorting(Constants.EMPLOYEE_FIELDS, sortBy);
            paging = CustomPropValidators.returnPageableWithSorting(pageNumber, pageSize, sorting, isAsc);
            result = employeeRepository.findAll(paging);
        } else {
            sorting = CustomPropValidators.checkFiledSorting(Constants.EMPLOYEE_DB_FIELDS, sortBy);
            paging = CustomPropValidators.returnPageableWithSorting(pageNumber, pageSize, sorting, isAsc);
            result = employeeRepository.findAllByQuery(searchQuery + "%", searchQuery, paging);
        }
        
        if (result.hasContent()) {
            return new PaginatedResponseDto<EmployeeDto>(
                result.getContent().stream().map(EmployeeMapper::mapToEmployeeDto).collect(Collectors.toList()),
                result.getTotalElements(),
                result.getTotalPages(),
                pageNumber
            );
        } else {
            throw new ResourceNotFoundException(resourceName + "s");
        }
    }

    @Override
    public List<EmployeeFileDto> getAllEmployeesIncludingProjects() {
        return employeeRepository.findAllEmployeesInclProjects()
            .stream()
            .map(EmployeeFileMapper::mapToEmployeeFileDto)
            .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto getEmployeeById(String employeeId) {
        return EmployeeMapper.mapToEmployeeDto(
            employeeRepository.findById(Long.parseLong(employeeId)).orElseThrow(() ->
                new ResourceNotFoundException(resourceName, "id", employeeId)
            )
        );
    }

    @Override
    public List<EmployeeDto> getEmployeesByProjectId(Long employeeId) {
        return employeeRepository.findByProjectId(employeeId)
            .stream()
            .map(EmployeeMapper::mapToEmployeeDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> getEmployeesByDevLanguage(Integer devLanguage) {
        CustomPropValidators.validateDevLang(devLanguage, resourceName);
        return employeeRepository.findByDevLanguage(devLanguage)
            .stream()
            .map(EmployeeMapper::mapToEmployeeDto)
            .collect(Collectors.toList());
    }

    
    @Override
    public List<EmployeeDto> getEmployeesByRole(Integer role) {
        CustomPropValidators.validateRole(role, resourceName);
        return employeeRepository.findByRole(role)
            .stream()
            .map(EmployeeMapper::mapToEmployeeDto)
            .collect(Collectors.toList());
    }

    @Override
    public void updateEmployee(Long employeeId, EditEmployeeDto editEmployeeDto) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", employeeId)
        );

        constraintViolationCheck(editEmployeeDto);

        if (editEmployeeDto.getProjectIds().isEmpty()) {
            employee = EmployeeMapper.mapToEmployee(employee, editEmployeeDto);
        } else {
            List<Project> projects = getProjects(editEmployeeDto.getProjectIds());
            employee = EmployeeMapper.mapToEmployee(employee, editEmployeeDto, projects);
        }

        employee.setModificationDate(new Date());
        
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
            new ResourceNotFoundException(resourceName, "id", employeeId)
        );
        
        employeeRepository.delete(employee);
    }

    @Override
    public byte[] generateExelFile() {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        try(Workbook workBook = new XSSFWorkbook()) {
            
            LocalDate date = DateTimeHelpers.getLocalDateNow();
            Sheet workBookSheet = workBook.createSheet(Constants.EMPLOYEE_FILE_NAME + date.toString());

            workBookSheet.setDefaultColumnWidth(100000);
            workBookSheet.setDefaultRowHeight((short) 500);
            
            Row header = workBookSheet.createRow(0);
            Cell headerCell = FileHelperFunctions.populateHeaderRow(header, Constants.EMPLOYEE_FILE_HEADERS);

            List<Object[]> data = employeeRepository.findAllEmployeesInclProjects();
                 
            if (data.isEmpty()) {
                throw new ResourceNotFoundException(resourceName + "s");
            }

            List<EmployeeFileDto> employeeData = data
                .stream()
                .map(EmployeeFileMapper::mapToEmployeeFileDto)
                .collect(Collectors.toList());

            Row row = null;
            Cell cell = null;

            for (int i = 1; i <= employeeData.size(); i++) {
                row = workBookSheet.createRow(i);
                EmployeeFileDto emplyee = employeeData.get(i-1);
                for (int j = 0; j < Constants.EMPLOYEE_FILE_HEADERS.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellValue(getEmployeeDataAtIndex(j, emplyee));
                }
            }

            try {
                workBook.write(bao);
                workBook.close();
            } finally {
                bao.close();
            }
        } catch (IOException e) {
            // will throw Internal Server Error
        }

        return bao.toByteArray();
    }

    @Override
    public Map<String, List<EmployeeDto>> getEmployeesGroupedByDevLanguage() {
        List<Employee> employees = employeeRepository.findAll();

        if(employees.isEmpty()) {
            throw new ResourceNotFoundException(resourceName + "s");
        }
        
        return employees
            .stream()
            .map(EmployeeMapper::mapToEmployeeDto)
            .collect(Collectors.groupingBy(EmployeeDto::getDevLanguage));
    }

    @Override
    public Map<String, List<EmployeeDto>> getEmployeesGroupedByRole() {
        List<Employee> employees = employeeRepository.findAll();

        if(employees.isEmpty()) {
            throw new ResourceNotFoundException(resourceName + "s");
        }
        
        return employees
            .stream()
            .map(EmployeeMapper::mapToEmployeeDto).
            collect(Collectors.groupingBy(EmployeeDto::getRole));
    }

    private List<Project> getProjects(Collection<Long> projectIds) {
        List<Project> projects = new ArrayList<>();

        if (!projectIds.isEmpty()) {
            projectIds.stream().filter(Objects::nonNull).forEach(
                id -> projects.add(
                        projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project", "id", id))
                    )
            );
        }

        return projects;
    }

    private <T> void constraintViolationCheck(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            violations.forEach(cv -> sb.append(cv.getPropertyPath() + " " + cv.getMessage() + ". "));
            
            throw new ConstraintViolationException("Error occurred: " + sb.toString().trim(), violations);
        }
    }

    private String getEmployeeDataAtIndex(Integer index, EmployeeFileDto employee) {
        String dataAtIndex = "";

        switch(index) {
            case 0:
                dataAtIndex = employee.getFirstName();
                break;
            case 1:
                dataAtIndex = employee.getLastName();
                break;
            case 2:
                dataAtIndex = employee.getBirthDate();
                break;
            case 3:
                dataAtIndex = employee.getRole();
                break;
            case 4:
                dataAtIndex = employee.getDevLanguage();
                break;
            case 5:
                dataAtIndex = employee.getProjectIds().trim();
                break;
            default:
                dataAtIndex = "";
        }

        return dataAtIndex;
    }
    
}

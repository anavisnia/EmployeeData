package com.example.employeedata.service.impl;

import java.io.*;
import java.util.*;

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
import com.example.employeedata.mappers.EmployeeMapper;
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

        return new ResponseDto(dbResponse.getId(), resourceName);
    }

    @Override
    public ResponseDto saveEmployeesFromExelFile(MultipartFile multipartFile) {
        if (multipartFile == null || (multipartFile != null && multipartFile.getOriginalFilename().isBlank()) ) {
            throw new CustomValidationException("File", "File cannot be null or empty");
        }
        CustomPropValidators.isProperFileType(multipartFile.getOriginalFilename());

        File file = HelperFunctions.castMultipartFileToFile(multipartFile);
        String fileType = HelperFunctions.getExtensionFromFileName(file.getName());
        Set<Employee> createEmployees = new HashSet<>();

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

            while (rows != null && rows.hasNext()) {
                Row row = rows.next();
                //to skip first row which contains additional information about cells
                if (rowCount == 0) {
                    rowCount++;
                    continue;
                }

                Iterator<Cell> cells = row.cellIterator();
                String[] employeeData = new String[6];

                int cellCount = 0;
                while (cells.hasNext() && cellCount < employeeData.length) {
                    Cell cell = cells.next();
                    String cellValue = "";

                    cellValue = HelperFunctions.getCellValue(cell);

                    employeeData[cellCount] = cellValue;

                    cellCount++;
                }

                List<Project> projects = new ArrayList<>();
                if (employeeData[5] != null) {
                    projects = getProjects(HelperFunctions.getListOfLongValuesFromString(employeeData[5]));
                }

                if (!CustomPropValidators.isMaxReachedForEmptyFields(employeeData, Constants.ALLOWED_EMPTY_FILEDS_EMPLOYEE)) {
                    createEmployees.add(EmployeeMapper.mapToEmployee(employeeData, projects));
                }
            }
        } catch (IOException e) {
            //will throw Internal Server Error

            //deliting temp file
            if (file.exists()) {
                file.delete();
            }
        }
        
        if (file.exists()) {
            file.delete();
        }

        List<Employee> dbResponse = employeeRepository.saveAll(createEmployees);
        String employeeIds = "";

        for (Employee employee : dbResponse) {
            employeeIds += employee.getId().toString() + " ";
        }
        return new ResponseDto(employeeIds.trim(), resourceName);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return EmployeeMapper.mapToListEmployeesDto(employeeRepository.findAll());
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
        return EmployeeMapper.mapToListEmployeesDto(employeeRepository.findByProjectId(employeeId));
    }

    @Override
    public List<EmployeeDto> getEmployeesByDevLanguage(Integer devLanguage) {
        CustomPropValidators.validateDevLang(devLanguage, resourceName);
        return EmployeeMapper.mapToListEmployeesDto(employeeRepository.findByDevLanguage(devLanguage));
    }

    
    @Override
    public List<EmployeeDto> getEmployeesByRole(Integer role) {
        CustomPropValidators.validateRole(role, resourceName);
        return EmployeeMapper.mapToListEmployeesDto(employeeRepository.findByRole(role));
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
    
    private List<Project> getProjects(List<Long> projectIds) {
        List<Project> projects = new ArrayList<>();

        if (!projectIds.isEmpty()) {
            for (Long id : projectIds) {
                if (id != null) {
                    Project project = projectRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Project", "id", id)
                    );
                    projects.add(project);
                } else {
                    throw new CustomValidationException("Project", "id", id);
                }
            }
        }

        return projects;
    }

    private <T> void constraintViolationCheck(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> constraintViolation : violations) {
                sb.append(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage() + ". ");
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString().trim(), violations);
        }
    }
    
}

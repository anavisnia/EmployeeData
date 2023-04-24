package com.example.employeedata.MockData;

import com.example.employeedata.dto.EmployeeDto;
import com.example.employeedata.entity.Employee;
import com.example.employeedata.entity.Project;
import com.example.employeedata.enums.DevLanguage;
import com.example.employeedata.enums.Role;
import com.example.employeedata.mappers.EmployeeMapper;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public final class EmployeeData {
    public static List<Employee> validEmployeeData() {
        List<Project> projects = ProjectData.validProjectData();
        List<Employee> list = new ArrayList<>();

        Employee empl1 = new Employee();
        empl1.setId((long) 1);
        empl1.setFirstName("Alan");
        empl1.setLastName("Eturas");
        empl1.setBirthDate(LocalDate.parse("1997-01-05"));
        empl1.setRole(Role.Designer);
        empl1.setDevLanguage(DevLanguage.Unknown);
        empl1.setModificationDate(new Date());
        empl1.setProjects(new HashSet<>(projects.subList(0, 2)));
        list.add(empl1);

        Employee empl2 = new Employee();
        empl2.setId((long) 2);
        empl2.setFirstName("Arthur");
        empl2.setLastName("Looma");
        empl2.setBirthDate(LocalDate.parse("1992-06-18"));
        empl2.setRole(Role.Developer);
        empl2.setDevLanguage(DevLanguage.Java);
        empl2.setModificationDate(new Date());
        empl2.setProjects(new HashSet<>(projects.subList(1, 1)));
        list.add(empl2);

        Employee empl3 = new Employee();
        empl3.setId((long) 3);
        empl3.setFirstName("Erika");
        empl3.setLastName("Zukiene");
        empl3.setBirthDate(LocalDate.parse("1993-02-28"));
        empl3.setRole(Role.Developer);
        empl3.setDevLanguage(DevLanguage.CSharp);
        empl3.setModificationDate(new Date());
        list.add(empl3);

        Employee empl4 = new Employee();
        empl4.setId((long) 4);
        empl4.setFirstName("Alina");
        empl4.setLastName("Cierikene");
        empl4.setBirthDate(LocalDate.parse("1998-09-09"));
        empl4.setRole(Role.DevOps);
        empl4.setDevLanguage(DevLanguage.CSharp);
        empl4.setModificationDate(new Date());
        list.add(empl4);

        Employee empl5 = new Employee();
        empl5.setId((long) 5);
        empl5.setFirstName("Alina");
        empl5.setLastName("Morina");
        empl5.setBirthDate(LocalDate.parse("1997-10-09"));
        empl5.setRole(Role.Analyst);
        empl5.setDevLanguage(DevLanguage.Unknown);
        empl5.setModificationDate(new Date());
        list.add(empl5);

        Employee empl6 = new Employee();
        empl6.setId((long) 6);
        empl6.setFirstName("Roman");
        empl6.setLastName("Kut");
        empl6.setBirthDate(LocalDate.parse("1991-10-31"));
        empl6.setRole(Role.Manager);
        empl6.setDevLanguage(DevLanguage.PHP);
        empl6.setModificationDate(new Date());
        list.add(empl6);

        return list;
    }

    public List<EmployeeDto> validEmployeeDataDto() {
        return validEmployeeData().stream().map(EmployeeMapper::mapToEmployeeDto).collect(Collectors.toList());
    }
}

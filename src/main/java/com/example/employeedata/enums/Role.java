package com.example.employeedata.enums;

public enum Role {
    Unknown("Unknown"), //0
    Director("Director"), //1
    HR("HR"), //2
    Manager("Project Manager"), //3
    DataScientist("Data Scientist"), //4
    Analyst("Data Analyst"), //5
    Architect("Architect"), //6
    Designer("Designer"), //7
    DevOps("DevOps"), //8
    Developer("Software Developer"), //9
    Tester("Software Tester"), //10
    Mentor("Mentor"), //11
    Trainee("Trainee"), //12
    UX_Designer("UX Designer"), //13
    UI_Designer("UI Designer"); //14

    public final String label;

    Role(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

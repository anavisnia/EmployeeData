package com.example.employeedata.enums;

public enum DevLanguage {
    Unknown("Unknown"), //0
    JavaScript("JavaScript"), //1
    Java("Java"), //2
    CSharp("C#"), //3
    C_Plus_Plus("C++"), //4
    Swift("Swift"), //5
    Kotlin("Kotlin"), //6
    Go("GO"), //7
    PHP("PHP"), //8
    Python("Python"), //9
    Ruby("Ruby"); //10

    public final String label;

    DevLanguage(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

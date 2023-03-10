package com.example.employeedata.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.example.employeedata.enums.FileTypes;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.exception.CustomValidationException;

public final class FileHelperFunctions {
    public static String getCellValue(Cell cell) {
        String cellValue = "";

        if (cell == null) {
            return cellValue;
        }

        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case BLANK:
                cellValue = "";
                break;
            default:
                cellValue = "";
        }

        return cellValue.trim();
    }

    public static String getExtensionFromFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static File castMultipartFileToFile(MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());

        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
        } catch (IllegalStateException | IOException e) {
            throw new CustomValidationException("File", "File cast error for file " + multipartFile.getOriginalFilename());
        }

        return file;
    }

    public static void populateHeaderRow(Row header, String[] values) {
        if (values == null || values.length == 0) {
            throw new CustomValidationException("Exel row", "Exel row representing header cannot be empty");
        }

        Cell headerCell;

        for (int i = 0; i < values.length; i++) {
            headerCell = header.createCell(i);
            headerCell.setCellValue(values[i]);
        }

    }

    public static void validateFileType(String type, String errResource) {
        List<String> types = Arrays.asList(FileTypes.labels());

        if (types.contains(type)) {
            return;
        }

        throw new CustomValidationException(errResource, type + " is not allowed");
    }

    public static void isProperFileType(String fileName) {
        String fileExtension = "";
        String errorResource = "File type";

        if (fileName.isBlank()) {
            throw new CustomValidationException(errorResource, fileExtension + " is not allowed");
        }

        fileExtension = FileHelperFunctions.getExtensionFromFileName(fileName);
        validateFileType(fileExtension, errorResource);
    }
}

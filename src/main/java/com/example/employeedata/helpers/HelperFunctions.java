package com.example.employeedata.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.elasticsearch.common.UUIDs;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.exception.CustomValidationException;

public final class HelperFunctions {
    public static String generateId() {
        return UUIDs.randomBase64UUID();
    }

    public static List<Long> getListOfLongValuesFromString(String str) {
        Set<Long> values = new HashSet<>();

        if (!str.isBlank()) {
            String[] seperatedNumbers = str.trim().split(", ");

            for (String string : seperatedNumbers) {
                values.add(Long.parseLong(string.replace(".0", "")));
            }
        }

        return new ArrayList(values);
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";

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
                break;
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
            throw new CustomValidationException("File", "File cast error");
        }

        return file;
    }
}

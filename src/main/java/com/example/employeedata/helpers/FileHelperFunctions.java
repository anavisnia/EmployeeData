package com.example.employeedata.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeedata.exception.CustomValidationException;

public final class FileHelperFunctions {
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

    public static Cell populateHeaderRow(Row header, String[] values) {
        if (values == null || (values != null && values.length == 0) ) {
            throw new CustomValidationException("Exel row", "Exel row representing header cannot be empty");
        }

        Cell headerCell = null;

        for(int i = 0; i < values.length; i++) {
            headerCell = header.createCell(i);
            headerCell.setCellValue(values[i]);
        }

        return headerCell;
    }

    public static Resource generateUrlResource(Path file) {
        if (file != null) {
            try {
                return new UrlResource(file.toUri());
            } catch (IOException e) {
                //throws Internal Server Error
            }
        }
         
        return null;
    }
}

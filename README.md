# Employee Data
#### Java Spring Boot project for educational purposes

### File Upload Guidance for Employee Controller
![Screenshot_1](https://user-images.githubusercontent.com/73888794/215988339-dc0b6155-619a-42bc-9344-6906040dddf8.png)

1. POST request "http://localhost:8080/api/employees/upload"
2. Accepts body form-data with the key: "file" and value of Exel type document with extensions .xls or .xlsx
3. If the document was processed succesfully returns ResponseDTO with the id/ids of created employees and stutus "Employee created successfylly". If the document was processed with errors, you can get either detailed error if the problem is with the data, or Internal Server Error

### Exel Documet Structure
|  | A  | B | C  | D | E  | F |
| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- | ------------- |
| 1 | First name (Text)  | Last name (Text)  | Birth date (YYYY-MM-DD), text value  | Role (Number as text value or number) | Development language (Number as text value or number)  | Project id's (Number/Numbers  as text value or number seperated by coma or leave as empty) |
| 2 | Alan  | Drew  | 1999-01-01  | 0  | 0  |  |
| 3 | Elen  | Snow  | 1981-05-09  | 9  | 3 | 1 |
| 4 | Edward  | Drow  | 1993-07-07  | 0  | 0 | 1, 4, 5 |

![Screenshot_2](https://user-images.githubusercontent.com/73888794/215995144-61f97955-5521-4d9e-b065-60b065dc9397.png)

**!Note** that "Birth Date" cell **should be formatted as text**, otherwise program won't read it as appropriate date.

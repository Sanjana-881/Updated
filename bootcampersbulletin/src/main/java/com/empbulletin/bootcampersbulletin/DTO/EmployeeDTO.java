package com.empbulletin.bootcampersbulletin.DTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO {


    private Long empId;


    private String empName;


    private String empMail;


    private Integer batchNo;


}

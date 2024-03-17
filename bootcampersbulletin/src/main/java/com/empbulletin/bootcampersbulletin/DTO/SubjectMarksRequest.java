package com.empbulletin.bootcampersbulletin.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SubjectMarksRequest {
    private Long empId;
    private String subjectName;
    private Float SubjectMarks;

}

package com.empbulletin.bootcampersbulletin.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubjectInterviewRequest {
    private Long empId;
    private String subjectName;
    private Float subjectInterviews;
}

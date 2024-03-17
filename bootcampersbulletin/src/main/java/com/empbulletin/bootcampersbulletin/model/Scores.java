package com.empbulletin.bootcampersbulletin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Scores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoresId;

    @ManyToOne
    @JoinColumn(name = "empId")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "subjectId", referencedColumnName = "subjectId")
    private Subject subject;

    @Column(name = "subjectMarks")
    private Float subjectMarks;

    @Column(name = "subjectInterviews")
    private Float subjectInterviews;



    public Scores(Employee employee, Subject subject, Float subjectMarks, Float subjectInterviews) {
        this.employee = employee;
        this.subject = subject;
        this.subjectMarks = subjectMarks;
        this.subjectInterviews = subjectInterviews;
    }
}

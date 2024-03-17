package com.empbulletin.bootcampersbulletin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subjectId")
    private Long subjectId;

    @Column(name = "subjectName")
    private String subjectName;

    @ManyToOne
    @JoinColumn(name = "batchNo", referencedColumnName = "batchNo")
    private Batches batches;


}

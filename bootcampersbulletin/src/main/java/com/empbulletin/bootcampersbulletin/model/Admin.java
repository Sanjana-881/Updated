package com.empbulletin.bootcampersbulletin.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
public class Admin {

    @Id
    private Long adminId;
    @Column
    private String adminName;
    @Column
    private String adminEmail;
    @Column
    private String adminPassword;

}


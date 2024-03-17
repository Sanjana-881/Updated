package com.empbulletin.bootcampersbulletin.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor

@Table(name = "Employee")
public class Employee {

	@Id
	@Column(name = "empId")
	private Long empId;

	@Column(name = "empName")
	private String empName;

	@Column(name = "empMail")
	private String empMail;

	@Column(name = "password_hash")
	private String password;

	@Column(name = "batchNo")
	private Integer batchNo;


	public Employee(Long empId, String empName, String empMail, String password, Integer batchNo) {
		this.empId = empId;
		this.empName = empName;
		this.empMail = empMail;
		setPassword(password);
		this.batchNo = batchNo;
	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}


}

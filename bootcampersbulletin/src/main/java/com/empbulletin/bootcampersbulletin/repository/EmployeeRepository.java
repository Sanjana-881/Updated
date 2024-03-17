package com.empbulletin.bootcampersbulletin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empbulletin.bootcampersbulletin.model.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByBatchNo(Integer batchNo);


    //all crud database operations
	
}

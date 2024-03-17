package com.empbulletin.bootcampersbulletin.service;

import com.empbulletin.bootcampersbulletin.DTO.EmployeeDTO;
import com.empbulletin.bootcampersbulletin.model.Employee;
import com.empbulletin.bootcampersbulletin.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public boolean authenticate(Long emp_id, String password) {
        Employee employee = employeeRepository.findById(emp_id).orElse(null);
        if (employee != null) {
            return BCrypt.checkpw(password, employee.getPassword());
        }
        return false;
    }



    @Transactional
    public ResponseEntity<String> updateEmployeeDetails(Long empId, EmployeeDTO updatedEmployee) {
        // Check if the employee exists
        Optional<Employee> optionalEmployee = employeeRepository.findById(empId);
        if (optionalEmployee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee with ID " + empId + " not found.");
        }

        // Update the existing employee entity with the new details
        Employee existingEmployee = optionalEmployee.get();
        if (updatedEmployee.getEmpName() != null) {
            existingEmployee.setEmpName(updatedEmployee.getEmpName());
        }
        if (updatedEmployee.getEmpMail() != null) {
            existingEmployee.setEmpMail(updatedEmployee.getEmpMail());
        }
        if (updatedEmployee.getBatchNo() != null) {
            existingEmployee.setBatchNo(updatedEmployee.getBatchNo());
        }

        // Save the updated employee entity
        employeeRepository.save(existingEmployee);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Employee details updated successfully for ID " + empId);
    }
}

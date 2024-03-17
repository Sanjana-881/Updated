package com.empbulletin.bootcampersbulletin.controller;

import java.util.LinkedHashMap;
import java.util.List;
import com.empbulletin.bootcampersbulletin.DTO.EmployeeDTO;
import com.empbulletin.bootcampersbulletin.exception.ResourceNotFoundException;
import com.empbulletin.bootcampersbulletin.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.empbulletin.bootcampersbulletin.service.ScoresService;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.empbulletin.bootcampersbulletin.model.Employee;
import com.empbulletin.bootcampersbulletin.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/v1/employees")

public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ScoresService scoresService;


    // Login authentication for Employees

    @PostMapping("/login")
    public String login(@RequestParam Long emp_id, @RequestParam String password) {
        if (employeeService.authenticate(emp_id, password)) {
            return "Login successful";
        } else {
            return "Login failed";
        }
    }


    //get all employees
    @GetMapping("/allEmployees")
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employee -> {
                    EmployeeDTO dto = new EmployeeDTO();
                    dto.setEmpId(employee.getEmpId());
                    dto.setEmpName(employee.getEmpName());
                    dto.setEmpMail(employee.getEmpMail());
                    dto.setBatchNo(employee.getBatchNo());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    // Get employee by ID
    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Employee emp = employee.get();
            EmployeeDTO dto = new EmployeeDTO();
            dto.setEmpId(emp.getEmpId());
            dto.setEmpName(emp.getEmpName());
            dto.setEmpMail(emp.getEmpMail());
            dto.setBatchNo(emp.getBatchNo());
            return dto;
        } else {
            throw new ResourceNotFoundException("Employee with id " + id + " not found");
        }
    }


    //Get all employees under the Batch_number
    @GetMapping("/batch/{batchNo}")
    public List<EmployeeDTO> getEmployeesByBatchNo(@PathVariable Integer batchNo) {
        List<Employee> employees = employeeRepository.findByBatchNo(batchNo);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("Employees in batch number " + batchNo + " not found");
        }
        return employees.stream()
                .map(employee -> {
                    EmployeeDTO dto = new EmployeeDTO();
                    dto.setEmpId(employee.getEmpId());
                    dto.setEmpName(employee.getEmpName());
                    dto.setEmpMail(employee.getEmpMail());
                    dto.setBatchNo(employee.getBatchNo());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    //get employee average marks and feedback by empId
    @GetMapping("/averageMarksFeedback/{empId}")
    public ResponseEntity<Object> getAverageMarksAndFeedback(@PathVariable Long empId) {
        // Get subject-wise marks for the employee
        Map<String, Float> subjectMarksMap = scoresService.getSubjectMarksByEmployeeId(empId);

        if (subjectMarksMap.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found for employee ID: " + empId);
        }

        // Calculate average marks
        Float averageMarks = scoresService.calculateAverageMarks(subjectMarksMap);

        // Generate feedback based on average marks
        String feedback = scoresService.generateMarksFeedback(subjectMarksMap,averageMarks);

        // Construct response JSON
        Map<String, Object> response = new LinkedHashMap<>();  // Use LinkedHashMap to maintain insertion order
        response.put("empId", empId);  // Place empId at the beginning
        response.put("subjectMarks", subjectMarksMap);
        response.put("averagemarks", averageMarks);
        response.put("feedback", feedback);

        // Return response
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //get employee average interviews and feedback by empId
    @GetMapping("/averageInterviewsFeedback/{empId}")
    public ResponseEntity<Object> getAverageInterviewsAndFeedback(@PathVariable Long empId) {
        // Get subject-wise interviews for the employee
        Map<String, Float> subjectInterviewsMap = scoresService.getSubjectInterviewsByEmployeeId(empId);

        if (subjectInterviewsMap.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found for employee ID: " + empId);
        }

        // Calculate average interviews
        Float averageInterviews = scoresService.calculateAverageInterviews(subjectInterviewsMap);

        // Generate feedback based on average interviews
        String feedback = scoresService.generateInterviewsFeedback(subjectInterviewsMap,averageInterviews);

        // Construct response JSON
        Map<String, Object> response = new LinkedHashMap<>();  // Use LinkedHashMap to maintain insertion order
        response.put("empId", empId);  // Place empId at the beginning
        response.put("subjectInterviews", subjectInterviewsMap);
        response.put("averageInterviews", averageInterviews);
        response.put("feedback", feedback);

        // Return response
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}

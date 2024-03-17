package com.empbulletin.bootcampersbulletin.controller;

import com.empbulletin.bootcampersbulletin.DTO.EmployeeDTO;
import com.empbulletin.bootcampersbulletin.exception.ResourceNotFoundException;

import com.empbulletin.bootcampersbulletin.model.*;
import com.empbulletin.bootcampersbulletin.repository.*;
import com.empbulletin.bootcampersbulletin.service.AdminService;
import com.empbulletin.bootcampersbulletin.service.EmployeeService;
import com.empbulletin.bootcampersbulletin.service.ScoresService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.empbulletin.bootcampersbulletin.DTO.SubjectMarksRequest;
import com.empbulletin.bootcampersbulletin.DTO.SubjectInterviewRequest;

import java.util.*;

import com.empbulletin.bootcampersbulletin.model.Subject;

import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private ScoresService scoresService;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ScoresRepository scoresRepository;

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private BatchesRepository batchesRepository;

    // Admin login
    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(@RequestBody Admin admin) {
        // Find admin by adminName
        Admin foundAdmin = adminRepository.findByAdminName(admin.getAdminName());
        if (foundAdmin == null) {
            return new ResponseEntity<>("Admin with username " + admin.getAdminName() + " not found", HttpStatus.NOT_FOUND);
        }

        // Check if passwords match
        if (!foundAdmin.getAdminPassword().equals(admin.getAdminPassword())) {
            return new ResponseEntity<>("Invalid password for admin " + admin.getAdminName(), HttpStatus.UNAUTHORIZED);
        }

        // If admin is found and password matches, return success
        return ResponseEntity.ok("Admin login successful");
    }

    //Adding an employee
    @PostMapping("/addEmployee")
    public ResponseEntity<String> addEmployee(@RequestBody Employee employee) {
        try {
            // Check if the batchNo exists in the Batches table
            Integer batchNo = employee.getBatchNo();
            boolean batchExists = batchesRepository.existsByBatchNo(batchNo);
            if (!batchExists) {
                return new ResponseEntity<>("Batch number " + batchNo + " does not exist. Please provide a valid batch number.", HttpStatus.BAD_REQUEST);
            }

            // Check if the empId already exists
            Optional<Employee> existingEmployeeOpt = employeeRepository.findById(employee.getEmpId());
            if (existingEmployeeOpt.isPresent()) {
                return new ResponseEntity<>("Employee with this empId already exists", HttpStatus.CONFLICT);
            }

            // If empId doesn't exist and batchNo exists, save the employee to the database
            employeeRepository.save(employee);
            return new ResponseEntity<>("Employee added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            // If there's an error, return an error response
            return new ResponseEntity<>("Failed to add employee: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Updating employee details by emp_id



    @Transactional
    @DeleteMapping("/{empId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long empId) {
        // Check if the employee exists
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + empId + " not found."));

        // Delete all scores associated with the employee
        scoresRepository.deleteAllByEmployee(employee);

        // Delete the employee
        employeeRepository.delete(employee);

        return ResponseEntity.ok("Employee with ID " + empId + " and associated scores deleted successfully.");
    }


    //updating employee details
    @PutMapping("/{empId}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long empId, @RequestBody EmployeeDTO updatedEmployee) {
        return employeeService.updateEmployeeDetails(empId, updatedEmployee);
    }



    @PostMapping("/addMarks")
    public ResponseEntity<String> addMarks(@RequestBody SubjectMarksRequest request) {
        // Retrieve empId from the request
        Long empId = request.getEmpId();

        // Check if the employee exists
        Optional<Employee> optionalEmployee = employeeRepository.findById(empId);
        if (optionalEmployee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Employee with ID " + empId + " not found.");
        }

        // Fetch the batchNo of the employee
        Employee employee = optionalEmployee.get();
        Integer batchNo = employee.getBatchNo();

        // Retrieve subjectId from the subject name and batchNo
        Optional<Subject> optionalSubject = subjectRepository.findBySubjectNameAndBatchesBatchNo(request.getSubjectName(), batchNo);
        if (optionalSubject.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Subject '" + request.getSubjectName() + "' not found for batchNo " + batchNo);
        }

        // Fetch the subjectId
        Long subjectId = optionalSubject.get().getSubjectId();

        // Check if the combination of empId and subjectId exists in the scores table
        Optional<Scores> optionalScores = scoresRepository.findByEmployeeEmpIdAndSubjectSubjectId(empId, subjectId);
        if (optionalScores.isPresent()) {
            // Update subjectMarks if the entry exists
            Scores scores = optionalScores.get();
            scores.setSubjectMarks(request.getSubjectMarks());
            scoresRepository.save(scores);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Subject marks updated successfully for Employee ID " + empId);
        } else {
            // Create a new entry if the combination does not exist
            Scores newScores = new Scores(employee, optionalSubject.get(), request.getSubjectMarks(), null);
            scoresRepository.save(newScores);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Subject marks added successfully for Employee ID " + empId);
        }
    }

    @PostMapping("/addInterviews")
    public ResponseEntity<String> addInterviews(@RequestBody SubjectInterviewRequest request) {
        // Retrieve empId from the request
        Long empId = request.getEmpId();

        // Check if the employee exists
        Optional<Employee> optionalEmployee = employeeRepository.findById(empId);
        if (optionalEmployee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Employee with ID " + empId + " not found.");
        }

        // Fetch the batchNo of the employee
        Employee employee = optionalEmployee.get();
        Integer batchNo = employee.getBatchNo();

        // Retrieve subjectId from the subject name and batchNo
        Optional<Subject> optionalSubject = subjectRepository.findBySubjectNameAndBatchesBatchNo(request.getSubjectName(), batchNo);
        if (optionalSubject.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Subject '" + request.getSubjectName() + "' not found for batchNo " + batchNo);
        }

        // Fetch the subjectId
        Long subjectId = optionalSubject.get().getSubjectId();

        // Check if the combination of empId and subjectId exists in the scores table
        Optional<Scores> optionalScores = scoresRepository.findByEmployeeEmpIdAndSubjectSubjectId(empId, subjectId);
        if (optionalScores.isPresent()) {
            // Update subjectInterviews if the entry exists
            Scores scores = optionalScores.get();
            scores.setSubjectInterviews(request.getSubjectInterviews());
            scoresRepository.save(scores);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Subject interviews updated successfully for Employee ID " + empId);
        } else {
            // Create a new entry if the combination does not exist
            Scores newScores = new Scores(employee, optionalSubject.get(), null, request.getSubjectInterviews());
            scoresRepository.save(newScores);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Subject interviews added successfully for Employee ID " + empId);
        }
    }

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




    // Get all employees
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

}

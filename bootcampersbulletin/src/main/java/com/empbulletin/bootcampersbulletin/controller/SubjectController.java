package com.empbulletin.bootcampersbulletin.controller;

import com.empbulletin.bootcampersbulletin.exception.SubjectNotFoundException;
import com.empbulletin.bootcampersbulletin.model.Subject;
import com.empbulletin.bootcampersbulletin.repository.BatchesRepository;
import com.empbulletin.bootcampersbulletin.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.empbulletin.bootcampersbulletin.repository.ScoresRepository;
import com.empbulletin.bootcampersbulletin.model.Batches;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
public class SubjectController {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    public SubjectController(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }
    @Autowired
    private ScoresRepository scoresRepository;

    @Autowired
    private BatchesRepository batchesRepository;

    //adding subject for batchNo
    @PostMapping("/addSubject")
    public ResponseEntity<String> addSubject(@RequestBody Map<String, Object> payload) {
        String subjectName = (String) payload.get("subjectName");
        Integer batchNo = (Integer) payload.get("batchNo");

        if (subjectName == null || batchNo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Subject name and batch number are required.");
        }

        // Check if the batch number exists
        boolean batchExists = batchesRepository.existsByBatchNo(batchNo);
        if (!batchExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Batch number " + batchNo + " does not exist.");
        }

        // Check if the subject with the given name exists for the provided batch number
        boolean subjectExists = subjectRepository.existsBySubjectNameAndBatchesBatchNo(subjectName, batchNo);
        if (subjectExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Subject '" + subjectName + "' already exists for batch number " + batchNo);
        }

        // Save the new subject
        Subject newSubject = new Subject();
        newSubject.setSubjectName(subjectName);

        Batches batch = new Batches();
        batch.setBatchNo(batchNo);
        newSubject.setBatches(batch);

        subjectRepository.save(newSubject);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Subject '" + subjectName + "' added successfully for batch number " + batchNo);
    }

    // deleting subject for the batchNo
    @Transactional
    @DeleteMapping("/deleteSubject")
    public ResponseEntity<String> deleteSubject(@RequestBody Map<String, Object> request) {
        // Retrieve batchNo from the request
        Integer batchNo = (Integer) request.get("batchNo");

        // Check if the batchNo exists in the batches table
        boolean batchExists = batchesRepository.existsByBatchNo(batchNo);
        if (!batchExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Batch number " + batchNo + " does not exist.");
        }

        // Retrieve subjectName from the request
        String subjectName = (String) request.get("subjectName");

        // Retrieve subjectId from the subjectName and batchNo
        Optional<Subject> optionalSubject = subjectRepository.findBySubjectNameAndBatchesBatchNo(subjectName, batchNo);
        if (optionalSubject.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Subject '" + subjectName + "' not found for batchNo " + batchNo);
        }

        // Fetch the subjectId
        Long subjectId = optionalSubject.get().getSubjectId();

        // Delete scores entries related to the subjectId
        scoresRepository.deleteBySubjectSubjectId(subjectId);

        // Delete the subject from the subject table
        subjectRepository.deleteBySubjectId(subjectId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Subject '" + subjectName + "' and its scores deleted successfully for batchNo " + batchNo);
    }


}

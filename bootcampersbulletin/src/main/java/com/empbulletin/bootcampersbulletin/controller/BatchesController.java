package com.empbulletin.bootcampersbulletin.controller;

import com.empbulletin.bootcampersbulletin.model.Batches;
import com.empbulletin.bootcampersbulletin.repository.BatchesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class BatchesController {
    @Autowired
    private BatchesRepository batchesRepository;

    //add batchNo
    @PostMapping("/addBatch")
    public ResponseEntity<String> addBatch(@RequestBody Batches batch) {
        Integer batchNo = batch.getBatchNo();
        if (batchesRepository.existsById(batchNo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Batch number " + batchNo + " already exists.");
        }

        Batches addedBatch = batchesRepository.save(batch);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Batch added successfully with batch number: " + addedBatch.getBatchNo());
    }


}

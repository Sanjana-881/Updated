package com.empbulletin.bootcampersbulletin.repository;

import com.empbulletin.bootcampersbulletin.model.Batches;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchesRepository extends JpaRepository<Batches, Integer> {



    boolean existsByBatchNo(Integer batchNo);
}

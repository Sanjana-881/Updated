package com.empbulletin.bootcampersbulletin.repository;

import com.empbulletin.bootcampersbulletin.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject,Long> {

    Optional<Subject> findBySubjectName(String subjectName);



    boolean existsBySubjectNameAndBatchesBatchNo(String subjectName, Integer batchNo);

    Optional<Subject> findBySubjectNameAndBatchesBatchNo(String subjectName, Integer batchNo);

    void deleteBySubjectId(Long subjectId);
}

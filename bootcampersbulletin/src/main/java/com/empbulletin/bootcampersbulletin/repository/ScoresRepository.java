package com.empbulletin.bootcampersbulletin.repository;

import com.empbulletin.bootcampersbulletin.model.Employee;
import com.empbulletin.bootcampersbulletin.model.Scores;
import com.empbulletin.bootcampersbulletin.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScoresRepository extends JpaRepository<Scores, Long> {

   // Optional<Scores> findByEmployeeEmpIdAndSubjectSubjectName(Long empId, String subjectName);
    @Query("SELECT sc FROM Scores sc WHERE sc.employee.empId = :empId AND sc.subject.subjectName = :subjectName")
    Scores findByEmployeeEmpIdAndSubjectSubjectName(
            @Param("empId") Long empId,
            @Param("subjectName") String subjectname
    );

    Scores findByEmployeeAndSubject(Employee employee, Subject subject);


 void deleteAllBySubject(Subject subject);

 List<Scores> findByEmployeeEmpId(Long empId);


 void deleteByEmployeeEmpId(Long empId);
 void deleteAllByEmployee(Employee employee);

 Optional<Scores> findByEmployeeEmpIdAndSubjectSubjectId(Long empId, Long subjectId);

 void deleteBySubjectSubjectId(Long subjectId);
}

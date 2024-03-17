package com.empbulletin.bootcampersbulletin.repository;


import com.empbulletin.bootcampersbulletin.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {




    Admin findByAdminName(String adminName);
}

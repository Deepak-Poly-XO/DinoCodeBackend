package com.realtimecode.repository;

import com.realtimecode.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, String> {
    List<Session> findByCreatedBy(String createdBy);
}
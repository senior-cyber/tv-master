package com.senior.cyber.tv.web.repository;

import com.senior.cyber.tv.dao.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HSessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findBySessionId(String sessionId);

}

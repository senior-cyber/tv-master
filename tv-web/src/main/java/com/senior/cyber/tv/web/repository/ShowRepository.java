package com.senior.cyber.tv.web.repository;

import com.senior.cyber.tv.dao.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByScheduleBetween(Date fromDate, Date toDate);

    List<Show> findBySchedule(Date date);

}

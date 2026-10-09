package com.example.advantumconverter.model.jpa;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface StatSnapshotRepository extends CrudRepository<StatSnapshot, Long> {

    List<StatSnapshot> findAllByOrderBySnapshotDateAsc();

    @Query("select distinct s.snapshotDate from StatSnapshot s order by s.snapshotDate asc")
    List<Timestamp> findDistinctSnapshotDates();

    @Modifying
    @Query("delete from StatSnapshot s where s.snapshotDate in :dates")
    void deleteBySnapshotDateIn(@Param("dates") Collection<Timestamp> dates);
}

package com.example.advantumconverter.model.jpa;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public interface ConversionStatSnapshotRepository extends CrudRepository<ConversionStatSnapshot, Long> {

    List<ConversionStatSnapshot> findAllByOrderBySnapshotDateAsc();

    @Query("select distinct s.snapshotDate from ConversionStatSnapshot s order by s.snapshotDate asc")
    List<Timestamp> findDistinctSnapshotDates();

    @Modifying
    @Query("delete from ConversionStatSnapshot s where s.snapshotDate in :dates")
    void deleteBySnapshotDateIn(@Param("dates") Collection<Timestamp> dates);
}

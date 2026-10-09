package com.example.advantumconverter.model.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
public class ConversionStatSnapshot {

    @Id
    @Column(name = "conversion_stat_snapshot_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversionStatSnapshotId;

    @Column(name = "snapshot_date", nullable = false)
    private Timestamp snapshotDate;

    @Column(name = "success_count")
    private Long successCount;

    @Column(name = "error_count")
    private Long errorCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionStatSnapshot that = (ConversionStatSnapshot) o;
        return Objects.equals(conversionStatSnapshotId, that.conversionStatSnapshotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversionStatSnapshotId);
    }
}

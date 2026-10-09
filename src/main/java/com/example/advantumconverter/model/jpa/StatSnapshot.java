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
public class StatSnapshot {

    @Id
    @Column(name = "stat_snapshot_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statSnapshotId;

    @Column(name = "snapshot_date", nullable = false)
    private Timestamp snapshotDate;

    @Column(name = "message_text", nullable = false)
    private String messageText;

    @Column(name = "total_count")
    private Long totalCount;

    @Column(name = "bot_count")
    private Long botCount;

    @Column(name = "web_count")
    private Long webCount;

    @Column(name = "user_count")
    private Long userCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatSnapshot that = (StatSnapshot) o;
        return Objects.equals(statSnapshotId, that.statSnapshotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statSnapshotId);
    }
}

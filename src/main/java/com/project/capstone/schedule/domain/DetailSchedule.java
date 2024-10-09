package com.project.capstone.schedule.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "detail_schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class DetailSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_schedule_id",updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id") // schedules table의 PK(schedule_id)를 참조한다는 뜻
    private Schedule schedule;

    public void addSchedule(Schedule schedule) {
        this.schedule = schedule;
        // 무한 루프에 빠지지 않도록 체크
        if (!schedule.getDetailSchedules().contains(this)) {
            schedule.addDetailSchedule(this);
        }
    }

    @Column(name = "day_sequence", nullable = false)
    private Integer daySequence;

    @Column(name = "location_sequence", nullable = false)
    private Integer locationSequence;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    @Builder
    public DetailSchedule(Integer daySequence, Integer locationSequence, String locationName) {
        this.daySequence = daySequence;
        this.locationSequence = locationSequence;
        this.locationName = locationName;
    }

    /**
     * toString() 에서 @ManyToOne의 객체를 넣을거면, 직접 구현해야됨
     */
    @Override
    public String toString() {
        return "DetailSchedule(" +
                "id=" + id + ", " +
                daySequence + "일차 " +
                locationSequence + "번 여행지 " +
                locationName + ")";
    }
}
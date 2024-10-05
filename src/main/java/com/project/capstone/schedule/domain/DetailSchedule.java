package com.project.capstone.detailSchedule.domain;

import com.project.capstone.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detail_schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class DetailSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

//    public void editDaySequence(Integer daySequence){
//        this.daySequence = daySequence;
//    }

    @Column(name = "location_sequence", nullable = false)
    private Integer locationSequence;

//    public void editLocationSequence(Integer locationSequence){
//        this.locationSequence = locationSequence;
//    }

    @Column(name = "location_name", nullable = false)
    private String locationName;

    /**
     *  생성자에 PK , FK 포함하면 안됨
     */
    /**
     *
     * @param day_sequence 여행 일차
     * @param location_sequence 그 일차에서 여행지 순서
     * @param location_name 그 여행지 이름
     */
    public DetailSchedule(Integer day_sequence, Integer location_sequence, String location_name) {
        this.daySequence = day_sequence;
        this.locationSequence = location_sequence;
        this.locationName = location_name;
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

package com.project.capstone.schedule.domain;

import com.project.capstone.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "schedule_id",updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false) // MEMBER table의 PK(MEMBER_PK)를 참조한다는 뜻
    private Member member;

    public void saveMember(Member member){
        this.member=member;
        // 무한 루프에 빠지지 않도록 체크
        if (!member.getSchedules().contains(this)) {
            member.addSchedule(this);
        }
    }

    @Column(name = "schedule_name", nullable = false)
    private String name;

    public void editName(String name4newSchedule) {
        this.name=name4newSchedule;
    }

    @Column(name = "how_many_people", nullable = false)
    private Integer howManyPeople;

    public void editHowManyPeople(Integer howManyPeople4newSchedule) {
        this.howManyPeople=howManyPeople4newSchedule;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    public void editDate(LocalDate startDate4newSchedule, LocalDate endDate4newSchedule) {
        this.startDate = startDate4newSchedule;
        this.endDate = endDate4newSchedule;
    }

    // === 일자별 스케줄 === //
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.PERSIST)
    @Setter(value = AccessLevel.NONE)
    private List<DetailSchedule> detailSchedules = new ArrayList<>();

    public Optional<DetailSchedule> getDetailSchedule(DetailSchedule detailSchedule){
        for (DetailSchedule foundDetailSchedule : detailSchedules) {
            if (foundDetailSchedule.equals(detailSchedule)) {
                return Optional.of(foundDetailSchedule);
            }
        }
        return Optional.empty();
    }

    public void addDetailSchedule(DetailSchedule detailSchedule) {
        this.detailSchedules.add(detailSchedule);
        // 무한 루프에 빠지지 않도록 체크
        if (detailSchedule.getSchedule() != this) {
            detailSchedule.addSchedule(this);
        }
    }

//    // 두 스케줄의 순서를 변경 (날짜랑 장소 순서 모두 swap)
//    public void swapDetailSchedule(DetailSchedule scheduleToSwapFirst, DetailSchedule scheduleToSwapSecond){
//        // 두 스케줄의 날짜 순서를 변경
//        Integer tmp1 = scheduleToSwapFirst.getDaySequence();
//        scheduleToSwapFirst.editDaySequence(scheduleToSwapSecond.getDaySequence());
//        scheduleToSwapSecond.editDaySequence(tmp1);
//
//        // 두 스케줄의 장소 순서를 변경
//        Integer tmp2 = scheduleToSwapFirst.getLocationSequence();
//        scheduleToSwapFirst.editLocationSequence(scheduleToSwapSecond.getLocationSequence());
//        scheduleToSwapSecond.editLocationSequence(tmp2);
//
//    }

    @Builder
    public Schedule(String name, Integer how_many_people,LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.howManyPeople = how_many_people;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * toString() 에서 @ManyToOne의 객체를 넣을거면, 직접 구현해야됨
     */
    @Override
    public String toString() {
        return "Schedule(" +
                "id=" + id + ", " +
                "스케쥴 만든 멤버=" + member.getUsername() + ", " +
                "스케줄 이름=" + name + ", " +
                "참가 인원수=" + howManyPeople + ", " +
                "시작일=" + startDate + ", " +
                "종료일=" + endDate + ", " +
                "여행지=" + detailSchedules + ")";
    }
}

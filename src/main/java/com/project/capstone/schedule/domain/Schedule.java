package com.project.capstone.schedule.domain;

import com.project.capstone.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.apache.ibatis.jdbc.Null;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id",updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false) // MEMBER table의 PK(MEMBER_PK)를 참조한다는 뜻
    private Member member;

    // this.member를 삭제하는 메소드인데, 사실상 이 스케줄을 삭제하는 메소드이다.
    public void deleteThisSchedule(){
        member.deleteSchedule(this);
        this.member=null;
    }

    public void saveMember(Member member){
        this.member=member;
        // 무한 루프에 빠지지 않도록 체크
        if (!member.getSchedules().contains(this)) {
            member.addSchedule(this);
        }
    }

    @Column(name = "schedule_name", nullable = false)
    private String name;

    @Column(name = "how_many_people", nullable = false)
    private Integer howManyPeople;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    // === 일자별 스케줄 === //
    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
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

    @Builder
    public Schedule(String name, Integer howManyPeople, LocalDate startDate, LocalDate endDate, List<DetailSchedule> detailSchedules) {
        this.name = name;
        this.howManyPeople = howManyPeople;
        this.startDate = startDate;
        this.endDate = endDate;

        this.detailSchedules = detailSchedules;
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

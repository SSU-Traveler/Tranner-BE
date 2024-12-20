package com.project.capstone.member.domain;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.candidateLocation.domain.CandidateLocation;
import com.project.capstone.schedule.domain.DetailSchedule;
import com.project.capstone.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "members")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;

    @Column(name = "user_name", nullable = false) // id, password 할 때, id임  // unique임
    private String username;

    @Column(name = "pw", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    // 닉네임 변경 메소드 (Setter를 넣으면, 무결성에 문제가 생길 수 있어, Setter 없앰...)
    public void changeNickname(String nickname) {
        this.nickname=nickname;
    }

    //비밀번호 변경 메서드(김민우 추가)
    public void changePassword(String newPassword, BCryptPasswordEncoder encoder) {
        this.password = encoder.encode(newPassword); // 비밀번호 암호화 후 설정
    }

    @Column(name = "email", nullable = false)
    private String email;

    @Temporal(TemporalType.DATE)
    @Column(name = "register_date", nullable = false)
    private LocalDate registerDate;

    @Column(name = "role", nullable = true)
    private String role;

    /******************************************************/

    // === 찜 리스트 === //
    @OneToMany(mappedBy = "member",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    public void addBookmark(Bookmark bookmark) {
        this.bookmarks.add(bookmark);
        // 무한 루프에 빠지지 않도록 체크
        if (bookmark.getMember() != this) {
            bookmark.saveMember(this);
        }
    }

    public void deleteBookmark(Bookmark bookmark) {
        this.bookmarks.remove(bookmark);
    }

    public void deleteAllBookmarks(){
        this.bookmarks.clear();
    }

    // === 장바구니 리스트 === //
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateLocation> candidateLocations = new ArrayList<>();

    public void addCandidateLocations(List<CandidateLocation> candidateLocations){
        for (CandidateLocation candidateLocation : candidateLocations) {
            addCandidateLocation(candidateLocation);
        }
    }

    public void addCandidateLocation(CandidateLocation candidateLocation) {
        this.candidateLocations.add(candidateLocation);
        // 무한 루프에 빠지지 않도록 체크
        if(candidateLocation.getMember()!=this){
            candidateLocation.saveMember(this);
        }
    }

    public void deleteCandidateLocation(CandidateLocation candidateLocation) {
        this.candidateLocations.remove(candidateLocation);
    }

    public void deleteAllCandidateLocations() {
        this.candidateLocations.clear();
    }

    // === 여행 리스트 === //
    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    public Optional<Schedule> getSchedule(Schedule schedule){
        for (Schedule findSchedule : schedules) {
            if (findSchedule.equals(schedule)) {
                return Optional.of(schedule);
            }
        }
        return Optional.empty();
    }

    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
        // 무한 루프에 빠지지 않도록 체크
        if(schedule.getMember()!=this){
            schedule.saveMember(this);
        }

        // 스케줄안에 일자별 스케줄들이 있으면
        List<DetailSchedule> detailSchedules = new ArrayList<>(schedule.getDetailSchedules());
        if(!schedule.getDetailSchedules().isEmpty()){
            for (DetailSchedule detailSchedule : detailSchedules) {
                schedule.addDetailSchedule(detailSchedule);
            }
        }
    }

    public void deleteSchedule(Schedule schedule) {
        if(this.schedules.contains(schedule)){
            this.schedules.remove(schedule);
            return;
        }
        if(schedule.getMember() == this){
            schedule.deleteThisSchedule();
        }
    }

    @Builder
    public Member(String username, String password, String nickname, String email, LocalDate registerDate, String role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.registerDate = registerDate;
        this.role = role;
    }
}
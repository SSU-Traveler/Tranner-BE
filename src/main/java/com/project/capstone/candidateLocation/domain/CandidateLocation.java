package com.project.capstone.candidateLocation.domain;

import com.project.capstone.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate_locations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class CandidateLocation { // 장바구니임 , 찜 아님

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_location_id",updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    private String username;

    public void saveMember(Member member){
        this.member=member;
        // 무한 루프에 빠지지 않도록 체크
        if (!member.getCandidateLocations().contains(this)) {
            member.addCandidateLocation(this);
        }
    }

    @Column(name = "location", nullable = false)
    private String location;

    /**
     * toString() 에서 @ManyToOne의 객체를 넣을거면, 직접 구현해야됨
     */
    @Override
    public String toString() {
        return "CandidateLocation(" +
                "id=" + id + ", " +
                "장바구니 담은 멤버=" + member.getUsername() + ", " +
                "location=" + location + ")";
    }

    @Builder
    public CandidateLocation(Member member, String location) {
        this.member = member;
        this.location = location;
    }
}
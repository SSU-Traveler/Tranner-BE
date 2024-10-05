package com.project.capstone.bookmark.domain;

import com.project.capstone.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "bookmarks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Bookmark { // 찜한 장소임 장바구니 아님

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false) // MEMBER table의 PK(MEMBER_PK)를 참조한다는 뜻
    private Member member;

    public void saveMember(Member member){
        this.member=member;
        // 무한 루프에 빠지지 않도록 체크
        if (!member.getBookmarks().contains(this)) {
            member.addBookmark(this);
        }
    }

    @Column(name="location", nullable = false)
    private String location;

    @Builder
    public Bookmark(String location) {
        this.location = location;
    }

    /**
     * toString() 에서 @ManyToOne의 객체를 넣을거면, 직접 구현해야됨
     */
    @Override
    public String toString() {
        return "Bookmark(" +
                "id=" + id + ", " +
                "찜한 멤버=" + member.getUsername() + ", " +
                "location=" + location + ")";
    }
}


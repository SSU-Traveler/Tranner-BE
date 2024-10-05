package com.project.capstone.bookmark.repository;

import com.project.capstone.bookmark.domain.Bookmark;
import com.project.capstone.bookmark.service.BookmarkService;
import com.project.capstone.member.domain.Member;
import com.project.capstone.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@DataJpaTest
/**
 * MySQL에서 test 해볼려면, 아래 @SpringBootTest, @Transcational, @Rollback(value = false) 셋 다 있어야함
 */
//@SpringBootTest
//@Transactional
//@Rollback(value = false)
class BookmarkRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Test
    @DisplayName("멤버 한명을 추가하고, 그 멤버 객체로 북마크를 C,R")
    void insertMemberAndAddBookmark() {

        // given
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.saveAndFlush(member1);

        // when
        Bookmark bookmark1 = new Bookmark("땅호리 집");
        /**
         * bookmarkRepository.save(bookmark1); 은 당연히 않된다.
         * member가 NotNull 이기에...
         */

        /**
         * MemberDAO 의 @OneToMany(mappedBy = "member",cascade = CascadeType.PERSIST) 덕분에
         * bookmark1은 자동으로 저장됨
         * 즉, bookmarkRepository.flush() 않해도 됨
         */
        member1.addBookmark(bookmark1);
        // 강제로 트랜잭션을 종료
        memberRepository.flush();

        // then
        // db에 bookmark1도 들어있다. (id = 1 , member = member1 , location = 땅호리 집)
        Bookmark foundBookmark = bookmarkRepository.findById(bookmark1.getId()).get();
        log.info("DB에 저장된 찜한 장소 = {}", foundBookmark);
        assertThat(foundBookmark).isEqualTo(bookmark1);

        // db에 저장된 member1에도 bookmark1이 들어있다.
        Member foundMember = memberRepository.findById(member1.getId()).get();
        log.info("DB에 저장된 멤버 = {}", foundMember);
        assertThat(foundMember).isEqualTo(member1);
    }

    @Test
    @DisplayName("멤버 한명의 북마크를 U") // 우리가 찜한 장소를 수정할 일은 없다. 단지 삭제할 뿐
    void updateBookmarks() {}

    @Test
    @DisplayName("멤버 한명의 북마크를 D")
    void deleteBookmark(){

        // given
        // 멤버 한명이 있다.
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.save(member1);

        // 그 멤버는 북마크가 있다.
        Bookmark bookmark1 = new Bookmark("땅호리 집");
        member1.addBookmark(bookmark1);

        Bookmark bookmark2 = new Bookmark("제로벨 집");
        member1.addBookmark(bookmark2);

        memberRepository.flush();

        // when
        // 멤버의 북마크 중 하나를 삭제
        Member updateMember = memberRepository.findByUsername("member1");
        updateMember.deleteBookmark(bookmark2);

        memberRepository.flush();

        // then
        // 멤버 객체의 북마크가 삭제 되었다.
        Member foundMember = memberRepository.findByUsername("member1");
        log.info("DB에 저장된 멤버 = {}", foundMember);

        // 북마크 테이블의 북마크가 삭제 되었다.
        assertThrows(NoSuchElementException.class,()->{
            bookmarkRepository.findById(bookmark2.getId()).get();
        });
    }

    @Test
    @DisplayName("멤버 한명을 삭제했을 때, 북마크도 D")
    void deleteMember(){

        // given
        // 멤버 한명이 있다.
        Member member1 = new Member("member1", "1111", "양", "1@gmail.com", LocalDate.now(), null);
        memberRepository.save(member1);

        // 그 멤버는 북마크가 2개 있다.
        Bookmark bookmark1 = new Bookmark("땅호리 집");
        member1.addBookmark(bookmark1);

        Bookmark bookmark2 = new Bookmark("제로벨 집");
        member1.addBookmark(bookmark2);

        memberRepository.flush();

        // when
        // 멤버를 삭제했다.
        memberRepository.delete(member1);
        memberRepository.flush();

        // then
        // 그 멤버의 북마크 2개가 사라졌다.
        assertThrows(NoSuchElementException.class,()->{
            bookmarkRepository.findById(bookmark1.getId()).get();
        });
        assertThrows(NoSuchElementException.class,()->{
            bookmarkRepository.findById(bookmark2.getId()).get();
        });
    }
}
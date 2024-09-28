package com.project.capstone.member.domain;

//import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;



@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false, unique = true, nullable = false)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String email;
    @Column(name = "register_date")
    private LocalDateTime registerDate;


    private String role;

}

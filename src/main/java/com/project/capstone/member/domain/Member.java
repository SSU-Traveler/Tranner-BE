package com.project.capstone.member.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "Members")
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberid;

    private String id;
    private String pw;
    private String nickname;
    private String email;
    private LocalDate registerdate;

}

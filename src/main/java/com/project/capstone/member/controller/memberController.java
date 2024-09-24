package com.project.capstone.member.controller;

import com.project.capstone.member.entitiy.member;
import com.project.capstone.member.service.memberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class memberController {
    private final memberService service;

}

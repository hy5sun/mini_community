package com.example.mini_community.controller;

import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.member.CreateMemberRequest;
import com.example.mini_community.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Member> join(@RequestBody CreateMemberRequest req) {
        Member savedMember = memberService.join(req);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedMember);
    }

}

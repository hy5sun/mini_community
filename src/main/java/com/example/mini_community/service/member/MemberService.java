package com.example.mini_community.service.member;

import com.example.mini_community.common.exception.BusinessException;
import com.example.mini_community.domain.member.Member;
import com.example.mini_community.dto.auth.EditMemberRequest;
import com.example.mini_community.dto.board.BoardsWithPaginationResponse;
import com.example.mini_community.dto.member.MemberDto;
import com.example.mini_community.repository.member.MemberRepository;
import com.example.mini_community.service.board.BoardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.mini_community.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardService boardService;

    @Transactional
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }

    @Transactional
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }

    @Transactional
    public BoardsWithPaginationResponse findBoardByMember(Member member, Integer page) {
        return boardService.findBoardByMember(member, page);
    }

    @Transactional
    public BoardsWithPaginationResponse findLikedBoardByMember(Member member, Integer page) {
        return boardService.findLikedBoardByMember(member, page);
    }

    @Transactional
    public BoardsWithPaginationResponse findCommentBoardByMember(Member member, Integer page) {
        return boardService.findCommentBoardByMember(member, page);
    }

    @Transactional
    public MemberDto findMyInformation(Member member) {
        Member loginMember =  memberRepository.findById(member.getId()).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
         return MemberDto.toDto(loginMember);
    }

    @Transactional
    public MemberDto editMyProfile(Member member, EditMemberRequest req) {
        member.update(req.getNickname(), req.getProfile_img());
        return MemberDto.toDto(member);
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }
}

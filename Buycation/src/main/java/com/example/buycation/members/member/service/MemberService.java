package com.example.buycation.members.member.service;

import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.dto.LoginRequestDto;
import com.example.buycation.members.member.dto.LoginResponseDto;
import com.example.buycation.members.member.dto.MemberResponseDto;
import com.example.buycation.members.member.dto.SignupRequestDto;
import com.example.buycation.members.member.dto.UpdateMemberRequestDto;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.member.mapper.MemberMapper;
import com.example.buycation.members.member.repository.MemberRepository;
import com.example.buycation.members.profile.dto.ReviewResponseDto;
import com.example.buycation.members.profile.entity.Review;
import com.example.buycation.members.profile.mapper.ReviewMapper;
import com.example.buycation.members.profile.repository.ReviewRepository;
import com.example.buycation.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_UPDATE_FAIL;
import static com.example.buycation.common.exception.ErrorCode.DUPLICATE_EMAIL;
import static com.example.buycation.common.exception.ErrorCode.DUPLICATE_NICKNAME;
import static com.example.buycation.common.exception.ErrorCode.INCORRECT_PASSWORD;
import static com.example.buycation.common.exception.ErrorCode.MEMBER_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        Member member = memberMapper.toMember(signupRequestDto);

        // 로그인 ID 중복 확인
        Optional<Member> emailDuplicateCheck = memberRepository.findByEmail(member.getEmail());
        if (emailDuplicateCheck.isPresent()) {
            throw new CustomException(DUPLICATE_EMAIL);
        }

        // 닉네임 중복 확인
        Optional<Member> nicknameDuplicateCheck = memberRepository.findByNickname(member.getNickname());
        if (nicknameDuplicateCheck.isPresent()) {
            throw new CustomException(DUPLICATE_NICKNAME);
        }

        memberRepository.save(member);
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String inputEmail = loginRequestDto.getEmail();
        String inputPassword = loginRequestDto.getPassword();

        Member member = memberRepository.findByEmail(inputEmail).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        if (!passwordEncoder.matches(inputPassword, member.getPassword())) {
            throw new CustomException(INCORRECT_PASSWORD);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getEmail()));

        return memberMapper.toResponse(member);
    }

    @Transactional(readOnly = true)
    public void checkNickname(String nickname) {
        Optional<Member> nicknameCheck = memberRepository.findByNickname(nickname);
        if (nicknameCheck.isPresent()) {
            throw new CustomException(DUPLICATE_NICKNAME);
        }
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        List<Review> reviews = reviewRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);
        List<ReviewResponseDto> reviewList = new ArrayList<>();
        for (Review r : reviews) {
            reviewList.add(reviewMapper.toResponse(r));
        }
        return memberMapper.toResponse(member, reviewList);
    }

    @Transactional
    public void updateMember(Member member, UpdateMemberRequestDto updateMemberRequestDto, Long memberId) {
        if (!member.getId().equals(memberId)) {
            throw new CustomException(AUTHORIZATION_UPDATE_FAIL);
        }
        if (memberRepository.findByNickname(updateMemberRequestDto.getNickname()).isPresent()) {
            throw new CustomException(DUPLICATE_NICKNAME);
        }
        Member updateMember = memberRepository.findById(memberId).orElseThrow(()->new CustomException(MEMBER_NOT_FOUND));
        updateMember.update(
                updateMemberRequestDto.getNickname(),
                updateMemberRequestDto.getProfileImage(),
                updateMemberRequestDto.getAddress());
    }
}
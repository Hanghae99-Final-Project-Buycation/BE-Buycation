package com.example.buycation.members.member.service;

import com.example.buycation.common.exception.CustomException;
import com.example.buycation.mail.entity.EmailCheck;
import com.example.buycation.mail.repository.EmailCheckRepository;
import com.example.buycation.members.member.dto.LoginRequestDto;
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
import com.example.buycation.security.UserDetailsImpl;
import com.example.buycation.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_UPDATE_FAIL;
import static com.example.buycation.common.exception.ErrorCode.DUPLICATE_EMAIL;
import static com.example.buycation.common.exception.ErrorCode.DUPLICATE_NICKNAME;
import static com.example.buycation.common.exception.ErrorCode.EMAIL_CERTIFICATION_FAIL;
import static com.example.buycation.common.exception.ErrorCode.INCORRECT_PASSWORD;
import static com.example.buycation.common.exception.ErrorCode.INVALID_NICKNAME_PATTERN;
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
    private final EmailCheckRepository emailCheckRepository;

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

        //이메일 인증 확인
        EmailCheck emailCheck = emailCheckRepository.findByEmail(member.getEmail());
        //이메일 확인 객체가 없으면 실패
        if (emailCheck == null) {
            throw new CustomException(EMAIL_CERTIFICATION_FAIL);
        }
        //이메일 확인 객체가 false면 실패
        if (!emailCheck.isStatus()) {
            throw new CustomException(EMAIL_CERTIFICATION_FAIL);
        }
        emailCheckRepository.delete(emailCheck);

        memberRepository.save(member);
    }

    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String inputEmail = loginRequestDto.getEmail();
        String inputPassword = loginRequestDto.getPassword();

        Member member = memberRepository.findByEmail(inputEmail).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );

        //비밀번호 일치 확인
        if (!passwordEncoder.matches(inputPassword, member.getPassword())) {
            throw new CustomException(INCORRECT_PASSWORD);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getEmail()));
    }

    @Transactional(readOnly = true)
    public void checkNickname(String nickname, UserDetailsImpl userDetails) {
        //닉네임 유효성 체크
        if (!Pattern.matches("^(?=.*[a-z0*9가-힣])[a-z0-9가-힣]{2,10}$", nickname)) {
            throw new CustomException(INVALID_NICKNAME_PATTERN);
        }

        //중복체크
        if (userDetails != null) {
            //현재와 다른 닉네임으로 변경시 중복체크
            if (!nickname.equals(userDetails.getMember().getNickname())) {
                if (memberRepository.findByNickname(nickname).isPresent()) {
                    throw new CustomException(DUPLICATE_NICKNAME);
                }
            }
        } else {
            if (memberRepository.findByNickname(nickname).isPresent()) {
                throw new CustomException(DUPLICATE_NICKNAME);
            }
        }
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Long memberId, UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        boolean myProfile = false;
        if (userDetails != null) {
            if (userDetails.getMember().getId().equals(member.getId())) {
                myProfile = true;
            }
        }
        List<Review> reviews = reviewRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);
        List<ReviewResponseDto> reviewList = new ArrayList<>();
        for (Review r : reviews) {
            reviewList.add(reviewMapper.toResponse(r));
        }
        return memberMapper.toResponse(member, reviewList, myProfile);
    }

    @Transactional
    public void updateMember(Member member, UpdateMemberRequestDto updateMemberRequestDto, Long memberId) {
        //권한 체크
        if (!member.getId().equals(memberId)) {
            throw new CustomException(AUTHORIZATION_UPDATE_FAIL);
        }
        //닉네임 유효성 체크
        if (!Pattern.matches("^(?=.*[a-z0*9가-힣])[a-z0-9가-힣]{2,10}$", updateMemberRequestDto.getNickname())) {
            throw new CustomException(INVALID_NICKNAME_PATTERN);
        }
        //현재와 다른 닉네임으로 변경시 중복체크
        if (!updateMemberRequestDto.getNickname().equals(member.getNickname())) {
            if (memberRepository.findByNickname(updateMemberRequestDto.getNickname()).isPresent()) {
                throw new CustomException(DUPLICATE_NICKNAME);
            }
        }
        Member updateMember = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        updateMember.update(
                updateMemberRequestDto.getNickname(),
                updateMemberRequestDto.getProfileImage(),
                updateMemberRequestDto.getAddress());
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMyProfile(Member member) {
        List<Review> reviews = reviewRepository.findAllByMemberIdOrderByCreatedAtDesc(member.getId());
        List<ReviewResponseDto> reviewList = new ArrayList<>();
        for (Review r : reviews) {
            reviewList.add(reviewMapper.toResponse(r));
        }
        return memberMapper.toResponse(member, reviewList, true);
    }
}
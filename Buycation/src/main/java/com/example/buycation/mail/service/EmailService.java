package com.example.buycation.mail.service;

import com.example.buycation.common.exception.CustomException;
import com.example.buycation.mail.entity.EmailCheck;
import com.example.buycation.mail.mapper.EmailCheckMapper;
import com.example.buycation.mail.repository.EmailCheckRepository;
import com.example.buycation.members.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

import static com.example.buycation.common.exception.ErrorCode.DUPLICATE_EMAIL;
import static com.example.buycation.common.exception.ErrorCode.EMAIL_CHECK_FAIL;
import static com.example.buycation.common.exception.ErrorCode.EMAIL_SEND_FAIL;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final MemberRepository memberRepository;
    private final EmailCheckRepository emailCheckRepository;
    private final EmailCheckMapper emailCheckMapper;

    @Autowired
    JavaMailSender emailSender;

    private MimeMessage createMessage(String email, String code) throws Exception {
        System.out.println("보내는 대상 : " + email);
        System.out.println("인증 번호 : " + code);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);//보내는 대상
        message.setSubject("Buycation 이메일 인증");//제목

        //내용(상세)
        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> 안녕하세요 Buycation입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다.<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += code + "</strong><div><br/> ";
        msgg += "</div>";

        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("jihun1362@gmail.com", "Baekjihun"));//보내는 사람

        return message;
    }

    public String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0 -> key.append((char) ((int) (rnd.nextInt(26)) + 97));
                //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                case 1 -> key.append((char) ((int) (rnd.nextInt(26)) + 65));
                //  A~Z
                case 2 -> key.append((rnd.nextInt(10)));
                // 0~9
            }
        }
        return key.toString();
    }

    @Transactional
    public String sendSimpleMessage(String email) throws Exception {
        //이메일 중복체크
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
        String code = createKey();
        MimeMessage message = createMessage(email, code);
        try {//예외처리
            emailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new CustomException(EMAIL_SEND_FAIL);
        }
        //전에 보낸 인증 객체가 있다면 삭제 후 재생성
        try {
            EmailCheck emailCheck = emailCheckRepository.findById(email).get();
            emailCheckRepository.delete(emailCheck);
            emailCheckRepository.save(emailCheckMapper.toEmailCheck(email, code, false, 180));
        }catch (Exception e){
            emailCheckRepository.save(emailCheckMapper.toEmailCheck(email, code, false, 180));
        }
        return code;
    }

    @Transactional
    public void emailCheck(String email, String code) {
        EmailCheck emailCheck = emailCheckRepository.findById(email).get();
        if (!emailCheck.getCode().equals(code)) {
            throw new CustomException(EMAIL_CHECK_FAIL);
        }
        emailCheckRepository.delete(emailCheck);
        emailCheckRepository.save(emailCheckMapper.toEmailCheck(email, code, true, 600));
    }
}


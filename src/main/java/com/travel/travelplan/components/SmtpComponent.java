package com.travel.travelplan.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpComponent {

    private final JavaMailSender javaMailSender;    // 메일 보내는 객체

    @Value("${spring.mail.username}")
    private String from;

    @PostConstruct
    public void init() {
        log.debug("from: {}", from);
    }

    private final String NAME = "Travel 팀";

    public boolean mailSend(String to, String subject, String text) {
        log.debug("mailSend email: {}", to);
        boolean isSent = false;
		try {
            // 각 메일 전송마다 새로운 MimeMessage와 MimeMessageHelper를 생성
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

            mimeMessageHelper.setFrom(from, NAME);              // 발신인

			mimeMessageHelper.setTo(to);                        // 수신인
			mimeMessageHelper.setSubject(subject);              // 제목
			mimeMessageHelper.setText(text, true);         // 내용

			javaMailSender.send(message);                       // 전송

            isSent = true; // 네이버 smtp 서버를 이용하므로 진짜로 메일발송이 성공했는지는 확인 불가능

		} catch (MessagingException e) {
			log.error("메일 전송 실패", e);
		} catch (Exception e) {
            log.error("메일 전송 실패", e);
        }
		return isSent;
    }

}

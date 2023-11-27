package com.tour.hanbando.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/*
 * google 계정으로 이메일 보내기 위해서 앱 비밀번호를 생성해 둬야 한다.
 * 
 * 1. 구글에 로그인한다.
 * 2. [계정] - [보안]
 * 3. [2단계 인증] - [앱 비밀번호] - [App name] : myhome 입력
 * 4. 생성된 비밀번호를 복사해서 application.yml 파일에 붙여넣기한다.
 */

@PropertySource(value="classpath:application.yml")
@Component
public class MyJavaMailUtils {

  @Autowired
  private Environment env;
  
  public void sendJavaMail(String to, String subject, String content) {
    
    try {
      
      // Properties 객체 생성 (이메일 보내는 호스트 정보)
      Properties properties = new Properties();
      properties.put("mail.smtp.host", env.getProperty("spring.mail.host"));
      properties.put("mail.smtp.port", env.getProperty("spring.mail.host"));
      properties.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth"));
      properties.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
      
      // javax.mail.Session 객체 생성 (이메일 보내는 사용자 정보)
      Session session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
        }
      });
      
      // 메일 만들기 (보내는 사람 + 받는 사람 + 제목 + 내용)
      MimeMessage mimeMessage = new MimeMessage(session);
      mimeMessage.setFrom(new InternetAddress(env.getProperty("spring.mail.username"), "사이트관리자"));
      mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
      mimeMessage.setSubject(subject);
      mimeMessage.setContent(content, "text/html; charset=UTF-8");
      
      // 메일 보내기
      Transport.send(mimeMessage);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
}
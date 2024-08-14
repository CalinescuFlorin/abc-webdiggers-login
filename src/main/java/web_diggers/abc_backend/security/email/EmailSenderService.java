package web_diggers.abc_backend.security.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String email;

    public void sendMail(String address,
                         String subject,
                         String body){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(address);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

        System.out.println("Sent mail " + subject + " to " + address + " successfully.");


    }
}

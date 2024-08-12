package web_diggers.abc_backend.Security.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String address,
                         String subject,
                         String body){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("abc.backend.t1@gmail.com\n");
        message.setTo(address);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

        System.out.println("Sent mail " + subject + " to " + address + " successfully.");


    }
}

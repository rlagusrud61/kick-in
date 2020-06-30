package nl.utwente.di.team26.Security.EmailSender;

import nl.utwente.di.team26.Constants;
import nl.utwente.di.team26.Product.model.Authentication.User;
import org.apache.log4j.BasicConfigurator;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class EmailSender {

    Mailer mailer;

    public EmailSender() {
        BasicConfigurator.configure();
        mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 465, Constants.USER, Constants.PASSWORD)
                .withTransportStrategy(TransportStrategy.SMTPS).buildMailer();
    }

    public void sendMail(String password, User to) {
        Email email = EmailBuilder.startingBlank()
                .from("kickInTeam26", Constants.EMAIL)
                .to(to.getNickname(), to.getEmail())
                .withSubject("Your Login Credentials")
                .withPlainText("Your login is for this email is: " + password)
                .buildEmail();
        mailer.sendMail(email);
    }

}

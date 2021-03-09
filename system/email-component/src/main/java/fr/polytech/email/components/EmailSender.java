package fr.polytech.email.components;

import java.util.List;

import fr.polytech.email.errors.MessageNotSentException;

public interface EmailSender {
    void sendMultipleTemplateMessage(List<String> contacts, String subject, String text) throws MessageNotSentException;
    void sendSimpleMessage(String to, String subject, String text);
    void sendTemplateMessage(String to, String subject, String text) throws MessageNotSentException;
}

package com.skillsphere.reminderservice.service;

import com.skillsphere.reminderservice.integration.SkillServiceClient;
import com.skillsphere.reminderservice.integration.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final SkillServiceClient skillServiceClient;
    private final AuthServiceClient authServiceClient;
    private final JavaMailSender mailSender;
    private static final Logger log = LoggerFactory.getLogger(ReminderService.class);

    public void sendReminders(int days) {
        List<Long> userIds = skillServiceClient.getInactiveUserIds(days);
        if (userIds.isEmpty()) {
            log.info("No inactive users found for reminder.");
            return;
        }
        String userIdsCsv = userIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        Map<Long, String> emails = authServiceClient.getUserEmails(userIdsCsv);
        for (Map.Entry<Long, String> entry : emails.entrySet()) {
            sendEmail(entry.getValue(), "SkillSphere Practice Reminder", "Don't forget to log your practice today!");
            log.info("Sent reminder to userId {} at {}", entry.getKey(), entry.getValue());
        }
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
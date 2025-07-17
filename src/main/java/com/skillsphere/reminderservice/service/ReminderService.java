package com.skillsphere.reminderservice.service;

import com.skillsphere.reminderservice.integration.SkillServiceClient;
import com.skillsphere.reminderservice.integration.AuthServiceClient;
import com.skillsphere.reminderservice.model.ReminderPreference;
import com.skillsphere.reminderservice.model.ReminderFrequency;
import com.skillsphere.reminderservice.repository.ReminderPreferenceRepository;
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
    private final ReminderPreferenceRepository preferenceRepository;
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
            ReminderPreference pref = preferenceRepository.findById(entry.getKey())
                    .orElse(ReminderPreference.builder()
                            .userId(entry.getKey())
                            .email(entry.getValue())
                            .hourOfDay(8)
                            .frequency(ReminderFrequency.DAILY)
                            .build());
            sendReminder(entry.getValue(), pref);
            log.info("Sent reminder to userId {} at {} via EMAIL", entry.getKey(), entry.getValue());
        }
    }

    public void sendReminderForUser(Long userId) {
        ReminderPreference pref = preferenceRepository.findById(userId).orElse(null);
        if (pref == null)
            return;
        sendReminder(pref.getEmail(), pref);
    }

    private void runHourlyReminderLogic() {
        int currentHour = java.time.LocalTime.now().getHour();
        java.util.List<ReminderPreference> prefs = preferenceRepository.findAll();
        for (ReminderPreference pref : prefs) {
            if (pref.getHourOfDay() == currentHour &&
                    (pref.getFrequency() == ReminderFrequency.DAILY ||
                            (pref.getFrequency() == ReminderFrequency.WEEKLY
                                    && java.time.LocalDate.now().getDayOfWeek().getValue() == 1))) {
                sendReminderForUser(pref.getUserId());
            }
        }
    }

    private void sendReminder(String to, ReminderPreference pref) {
        switch (pref.getChannel()) {
            case "EMAIL" -> sendEmail(to, "SkillSphere Practice Reminder", "Don't forget to log your practice today!");
            case "SLACK" -> sendSlack(pref.getSlackChannel(), "Don't forget to log your practice today!");
            case "WEBHOOK" -> sendWebhook(pref.getWebhookUrl(), "Don't forget to log your practice today!");
            default -> sendEmail(to, "SkillSphere Practice Reminder", "Don't forget to log your practice today!");
        }
    }

    private void sendSlack(String slackChannel, String message) {
        // TODO: Implement Slack notification logic
        log.info("[Stub] Would send Slack message to {}: {}", slackChannel, message);
    }

    private void sendWebhook(String webhookUrl, String message) {
        // TODO: Implement webhook notification logic
        log.info("[Stub] Would POST to webhook {}: {}", webhookUrl, message);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
package com.skillsphere.reminderservice.scheduler;

import com.skillsphere.reminderservice.model.ReminderPreference;
import com.skillsphere.reminderservice.repository.ReminderPreferenceRepository;
import com.skillsphere.reminderservice.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {
    private final ReminderService reminderService;
    private final ReminderPreferenceRepository preferenceRepository;
    private static final Logger log = LoggerFactory.getLogger(ReminderScheduler.class);

    // Runs every day at 8am
    @Scheduled(cron = "0 0 8 * * *")
    public void runDailyReminder() {
        log.info("Running daily reminder job");
        reminderService.sendReminders(1);
    }

    // Runs every hour
    @Scheduled(cron = "0 0 * * * *")
    public void runHourlyReminder() {
        log.info("Running hourly reminder job");
        reminderService.runHourlyReminderLogic();
    }
}

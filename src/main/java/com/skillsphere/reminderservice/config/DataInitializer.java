package com.skillsphere.reminderservice.config;

import com.skillsphere.reminderservice.model.ReminderPreference;
import com.skillsphere.reminderservice.model.ReminderFrequency;
import com.skillsphere.reminderservice.repository.ReminderPreferenceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final ReminderPreferenceRepository preferenceRepository;

    @Override
    public void run(String... args) {
        if (preferenceRepository.count() == 0) {
            preferenceRepository.save(ReminderPreference.builder().userId
                    .email("user1@example.com")
                    .hourOfDay(8)
                    .frequency(ReminderFrequency.DAILY)
                    .build());
        }
    }
}
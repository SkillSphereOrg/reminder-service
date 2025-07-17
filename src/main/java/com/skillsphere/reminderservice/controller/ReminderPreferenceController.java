package com.skillsphere.reminderservice.controller;

import com.skillsphere.reminderservice.model.ReminderPreference;
import com.skillsphere.reminderservice.model.ReminderFrequency;
import com.skillsphere.reminderservice.repository.ReminderPreferenceRepository;
import com.skillsphere.reminderservice.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Reminder Preferences", description = "Endpoints for managing user reminder preferences and manual triggers")
@RestController
@RequestMapping("/api/v1/reminder-preferences")
@RequiredArgsConstructor
public class ReminderPreferenceController {
    private final ReminderPreferenceRepository preferenceRepository;
    private final ReminderService reminderService;

    @Operation(summary = "Get reminder preferences for a user")
    @GetMapping("/{userId}")
    public ResponseEntity<ReminderPreference> getPreference(@PathVariable Long userId) {
        Optional<ReminderPreference> pref = preferenceRepository.findById(userId);
        return pref.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Set or update reminder preferences for a user")
    @PostMapping
    public ResponseEntity<ReminderPreference> setPreference(@RequestBody ReminderPreference preference) {
        // Only allow email channel
        return ResponseEntity.ok(preferenceRepository.save(ReminderPreference.builder()
                .userId(preference.getUserId())
                .email(preference.getEmail())
                .hourOfDay(preference.getHourOfDay())
                .frequency(preference.getFrequency())
                .build()));
    }

    @Operation(summary = "Trigger a manual reminder for a user (for testing)")
    @PostMapping("/trigger/{userId}")
    public ResponseEntity<String> triggerReminder(@PathVariable Long userId) {
        reminderService.sendReminderForUser(userId);
        return ResponseEntity.ok("Reminder triggered");
    }
}
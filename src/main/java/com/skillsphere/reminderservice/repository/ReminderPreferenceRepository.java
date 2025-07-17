package com.skillsphere.reminderservice.repository;

import com.skillsphere.reminderservice.model.ReminderPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderPreferenceRepository extends JpaRepository<ReminderPreference, Long> {
}
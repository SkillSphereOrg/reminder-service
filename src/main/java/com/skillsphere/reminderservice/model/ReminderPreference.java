package com.skillsphere.reminderservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reminder_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReminderPreference {
    @Id
    private Long userId;
    private String email;
    private int hourOfDay; // 0-23
    @Enumerated(EnumType.STRING)
    private ReminderFrequency frequency;
}
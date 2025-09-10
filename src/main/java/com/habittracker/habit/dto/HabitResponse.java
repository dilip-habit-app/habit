package com.habittracker.habit.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HabitResponse {
    private Long id;
    private String title;
    private String description;
    private String frequency;
    private String reminderTime;
    private Boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer streakCount;
    private Integer completionCount;
}

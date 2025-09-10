package com.habittracker.habit.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HabitRequest {

    @NotBlank(message = "Title can not be blank")
    @Size(max = 150, message = "Title can not exceed 150 characters ")
    private String title;

    @NotBlank(message = "Description can not be blank")
    @Size(max= 1000, message = "Description can not exceed 1000 characters")
    private String description;

    @NotBlank(message = "Frequency is required (e.g. DAILY, WEEKLY, MONTHLY)")
    private String frequency;

    @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$", message = "Reminder time must be in HH:mm format")
    private String reminderTime;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @Future(message = "End date must be in the future")
    private LocalDate endDate;
}

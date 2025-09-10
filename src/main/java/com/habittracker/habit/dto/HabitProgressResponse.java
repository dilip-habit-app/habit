package com.habittracker.habit.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HabitProgressResponse {

    private Long habitId;
    private LocalDate date;
    private Boolean completed;
}

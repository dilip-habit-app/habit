package com.habittracker.habit.service;

import com.habittracker.habit.dto.HabitProgressResponse;
import com.habittracker.habit.dto.HabitRequest;
import com.habittracker.habit.dto.HabitResponse;

import java.util.List;

public interface HabitService {

    HabitResponse createHabit(HabitRequest request);
    HabitResponse getHabitById(Long id);
    List<HabitResponse> getHabitsByUser(Long userId);
    HabitResponse updateHabit(Long id, HabitRequest request);
    void deleteHabit(Long id);
    HabitProgressResponse completeHabit(Long id);
    HabitProgressResponse skipHabit(Long id);
    List<HabitProgressResponse> getHabitProgress(Long id);

    List<HabitResponse> getAllHabits();
}

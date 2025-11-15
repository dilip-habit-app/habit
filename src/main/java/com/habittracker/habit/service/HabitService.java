package com.habittracker.habit.service;

import com.habittracker.habit.dto.HabitProgressResponse;
import com.habittracker.habit.dto.HabitRequest;
import com.habittracker.habit.dto.HabitResponse;

import java.util.List;

public interface HabitService {

    HabitResponse createHabit(HabitRequest request, Long userId);
    HabitResponse getHabitById(Long id, Long userId);
    List<HabitResponse> getHabitsByUser(Long userId);
    HabitResponse updateHabit(Long id, HabitRequest request, Long userId );
    void deleteHabit(Long id, Long userId);
    HabitProgressResponse completeHabit(Long id, Long userId);
    HabitProgressResponse skipHabit(Long id, Long userId);
    List<HabitProgressResponse> getHabitProgress(Long id, Long userId);

    List<HabitResponse> getAllHabits();
}

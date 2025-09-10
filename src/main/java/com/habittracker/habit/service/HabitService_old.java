package com.habittracker.habit.service;

import com.habittracker.habit.entity.Habit;
import com.habittracker.habit.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService_old {
    @Autowired
    private HabitRepository habitRepository;

    public Habit createHabit(Habit habit) {
        return habitRepository.save(habit);
    }

    public List<Habit> getHabitsByUserId(Long userId) {
        return habitRepository.findByUserId(userId);
    }

    public void deleteHabit(Long habitId) {
        habitRepository.deleteById(habitId);
    }





}

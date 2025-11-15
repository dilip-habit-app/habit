package com.habittracker.habit.service.impl;

import com.habittracker.habit.dto.HabitProgressResponse;
import com.habittracker.habit.dto.HabitRequest;
import com.habittracker.habit.dto.HabitResponse;
import com.habittracker.habit.entity.Habit;
import com.habittracker.habit.entity.HabitProgress;
import com.habittracker.habit.exception.HabitNotFoundException;
import com.habittracker.habit.repository.HabitProgressRepository;
import com.habittracker.habit.repository.HabitRepository;
import com.habittracker.habit.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitServiceImpl implements HabitService {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private HabitProgressRepository progressRepository;

    @Override
    public HabitResponse createHabit(HabitRequest request, Long userId) {
        Habit habit = new Habit();
        habit.setTitle(request.getTitle());
        habit.setDescription(request.getDescription());
        habit.setFrequency(request.getFrequency());
        habit.setReminderTime(request.getReminderTime());
        habit.setStartDate(request.getStartDate());
        habit.setEndDate(request.getEndDate());

        Habit saved = habitRepository.save(habit);
        return mapToResponse(saved);
    }

    @Override
    public HabitResponse getHabitById(Long id, Long userId) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + id));
        return mapToResponse(habit);
    }

    @Override
    public List<HabitResponse> getHabitsByUser(Long userId) {
        return habitRepository.findByUserId(userId)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HabitResponse> getAllHabits() {
        return habitRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    @Override
    public HabitResponse updateHabit(Long id, HabitRequest request, Long userId) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + id));

        habit.setTitle(request.getTitle());
        habit.setDescription(request.getDescription());
        habit.setFrequency(request.getFrequency());
        habit.setReminderTime(request.getReminderTime());
        habit.setStartDate(request.getStartDate());
        habit.setEndDate(request.getEndDate());

        Habit updated = habitRepository.save(habit);
        return mapToResponse(updated);
    }

    @Override
    public void deleteHabit(Long id, Long userId) {
        if (!habitRepository.existsById(id)) {
            throw new HabitNotFoundException("Habit not found with id: " + id);
        }
        habitRepository.deleteById(id);
    }

    @Override
    public HabitProgressResponse completeHabit(Long id, Long userId) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + id));

        HabitProgress progress = new HabitProgress();
        progress.setHabitId(habit.getId());
        progress.setDate(LocalDate.now());
        progress.setCompleted(true);
        progressRepository.save(progress);

        habit.setCompletionCount(habit.getCompletionCount() + 1);
        habit.setStreakCount(habit.getStreakCount() + 1);
        habitRepository.save(habit);

        return mapToProgressResponse(progress);
    }

    @Override
    public HabitProgressResponse skipHabit(Long id, Long userId ) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + id));

        HabitProgress progress = new HabitProgress();
        progress.setHabitId(habit.getId());
        progress.setDate(LocalDate.now());
        progress.setCompleted(false);
        progressRepository.save(progress);

        habit.setStreakCount(0); // reset streak
        habitRepository.save(habit);

        return mapToProgressResponse(progress);
    }

    @Override
    public List<HabitProgressResponse> getHabitProgress(Long id, Long userId) {
        return progressRepository.findByHabitId(id)
                .stream().map(this::mapToProgressResponse)
                .collect(Collectors.toList());
    }


    private HabitResponse mapToResponse(Habit habit) {
        HabitResponse response = new HabitResponse();
        response.setId(habit.getId());
        response.setTitle(habit.getTitle());
        response.setDescription(habit.getDescription());
        response.setFrequency(habit.getFrequency());
        response.setReminderTime(habit.getReminderTime());
        response.setActive(habit.isActive());
        response.setStartDate(habit.getStartDate());
        response.setEndDate(habit.getEndDate());
        response.setStreakCount(habit.getStreakCount());
        response.setCompletionCount(habit.getCompletionCount());
        return response;
    }

    private HabitProgressResponse mapToProgressResponse(HabitProgress progress) {
        HabitProgressResponse response = new HabitProgressResponse();
        response.setHabitId(progress.getHabitId());
        response.setDate(progress.getDate());
        response.setCompleted(progress.getCompleted());
        return response;
    }
}


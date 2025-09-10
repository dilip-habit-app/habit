package com.habittracker.habit.controller;

import com.habittracker.habit.dto.HabitProgressResponse;
import com.habittracker.habit.dto.HabitRequest;
import com.habittracker.habit.dto.HabitResponse;
import com.habittracker.habit.entity.Habit;
import com.habittracker.habit.service.HabitService;
import com.habittracker.habit.service.HabitService_old;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    @Autowired
    HabitService habitService;
    /**
     * Create a new habit
     */
    @PostMapping
    public ResponseEntity<HabitResponse> createHabit(@Valid @RequestBody HabitRequest request) {
        HabitResponse response = habitService.createHabit(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get a habit by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<HabitResponse> getHabitById(@PathVariable Long id) {
        HabitResponse response = habitService.getHabitById(id);
        return ResponseEntity.ok(response);
    }
    /**
     * Get All habits
     */
    @GetMapping
    public ResponseEntity<List<HabitResponse>> getAllHabits(){
        List<HabitResponse> responses = habitService.getAllHabits();

        return  ResponseEntity.ok(responses);
    }
    /**
     * Get all habits for a specific user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HabitResponse>> getHabitsByUser(@PathVariable Long userId) {
        List<HabitResponse> habits = habitService.getHabitsByUser(userId);
        return ResponseEntity.ok(habits);
    }

    /**
     * Update a habit by ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<HabitResponse> updateHabit(@PathVariable Long id,@Valid @RequestBody HabitRequest request) {
        HabitResponse response = habitService.updateHabit(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a habit by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        habitService.deleteHabit(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Mark habit completed for today
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<HabitProgressResponse> completeHabit(@PathVariable Long id) {
        HabitProgressResponse response = habitService.completeHabit(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Skip habit for today
     */
    @PostMapping("/{id}/skip")
    public ResponseEntity<HabitProgressResponse> skipHabit(@PathVariable Long id) {
        HabitProgressResponse response = habitService.skipHabit(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get progress history for a habit
     */
    @GetMapping("/{id}/progress")
    public ResponseEntity<List<HabitProgressResponse>> getHabitProgress(@PathVariable Long id) {
        List<HabitProgressResponse> response = habitService.getHabitProgress(id);
        return ResponseEntity.ok(response);
    }
}

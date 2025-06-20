package com.habittracker.habit.controller;

import com.habittracker.habit.model.Habit;
import com.habittracker.habit.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    @Autowired
    HabitService habitService;

    @PostMapping
    public Habit createHabit(@RequestBody Habit habit){
        return  habitService.createHabit(habit);
    }
    @GetMapping("/user/{userId}")
    public List<Habit> getHabit(@PathVariable Long userId){
       return habitService.getHabitsByUserId(userId);
    }
    @DeleteMapping("/{userId}")
    public void deleteMapping(@PathVariable Long userId){
         habitService.deleteHabit(userId);
    }
}

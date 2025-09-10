package com.habittracker.habit.repository;

import com.habittracker.habit.dto.HabitProgressResponse;
import com.habittracker.habit.entity.HabitProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitProgressRepository extends JpaRepository<HabitProgress, Long> {

    List<HabitProgress> findByHabitId(Long habitId);
}

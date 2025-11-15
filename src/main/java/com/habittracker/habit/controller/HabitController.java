package com.habittracker.habit.controller;

import com.habittracker.habit.dto.HabitProgressResponse;
import com.habittracker.habit.dto.HabitRequest;
import com.habittracker.habit.dto.HabitResponse;
import com.habittracker.habit.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * HabitController secured with JWT.
 *
 * Assumptions:
 *  - JwtAuthenticationFilter sets Authentication and sets userId in auth.getDetails() (Long).
 *  - HabitService exposes user-aware methods such as:
 *      createHabit(HabitRequest request, Long userId)
 *      getHabitsByUser(Long userId)
 *      completeHabit(Long habitId, Long userId)
 *      skipHabit(Long habitId, Long userId)
 *      getHabitProgress(Long habitId, Long userId)
 *
 * If your current HabitService signatures are different, either add these overloads
 * or adapt the controller to set userId inside the request (and call existing methods).
 */
@RestController
@RequestMapping("/api/habits")
public class HabitController {

    @Autowired
    private HabitService habitService;

    // -------------------------
    // Helper: obtain authenticated user's id from SecurityContext
    // -------------------------
    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        Object details = auth.getDetails();
        if (details instanceof Long) {
            return (Long) details;
        }
        // If details contain something else (e.g., a Map or String), adapt parsing here.
        // Also you could resolve userId by calling User Service using auth.getName() (email)
        return null;
    }

    private <T> ResponseEntity<T> unauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private <T> ResponseEntity<T> forbiddenResponse() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Create a new habit for the authenticated user
     */
    @PostMapping
    public ResponseEntity<HabitResponse> createHabit(@Valid @RequestBody HabitRequest request) {
        Long userId = getAuthenticatedUserId();
        if (userId == null) {
            return unauthorizedResponse();
        }

        // Prefer service method that accepts userId explicitly
        // Implement this method in HabitService if not present:
        // HabitResponse response = habitService.createHabit(request, userId);

        // If your service currently only has createHabit(request),
        // you must either add an overload or set userId in the request/DTO
        // before calling the service. Below assumes an overload exists:
        HabitResponse response = habitService.createHabit(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get a habit by ID (only if it belongs to authenticated user OR user has admin role)
     */
    @GetMapping("/{id}")
    public ResponseEntity<HabitResponse> getHabitById(@PathVariable Long id) {
        Long userId = getAuthenticatedUserId();
        if (userId == null) return unauthorizedResponse();

        // Service should verify ownership internally; use a user-aware method
        HabitResponse response = habitService.getHabitById(id, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get All habits (for the authenticated user)
     * If you need a global admin endpoint to list all habits, create a separate admin endpoint.
     */
    @GetMapping
    public ResponseEntity<List<HabitResponse>> getAllHabits() {
        Long userId = getAuthenticatedUserId();
        if (userId == null) return unauthorizedResponse();

        List<HabitResponse> responses = habitService.getHabitsByUser(userId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all habits for a specific user (only allowed for the same user or admin)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HabitResponse>> getHabitsByUser(@PathVariable Long userId) {
        Long authUserId = getAuthenticatedUserId();
        if (authUserId == null) return unauthorizedResponse();

        // Simple authorization: only allow if same user. Add role checks if needed.
        if (!Objects.equals(authUserId, userId)) {
            return forbiddenResponse();
        }

        List<HabitResponse> habits = habitService.getHabitsByUser(userId);
        return ResponseEntity.ok(habits);
    }

    /**
     * Update a habit by ID (only if it belongs to authenticated user)
     */
    @PutMapping("/{id}")
    public ResponseEntity<HabitResponse> updateHabit(@PathVariable Long id, @Valid @RequestBody HabitRequest request) {
        Long userId = getAuthenticatedUserId();
        if (userId == null) return unauthorizedResponse();

        HabitResponse response = habitService.updateHabit(id, request, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a habit by ID (only if it belongs to authenticated user)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        Long userId = getAuthenticatedUserId();
        if (userId == null) return unauthorizedResponse();

        habitService.deleteHabit(id, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Mark habit completed for today (records progress tied to authenticated user)
     */
    @PostMapping("/{id}/complete")
    public ResponseEntity<HabitProgressResponse> completeHabit(@PathVariable Long id) {
        Long userId = getAuthenticatedUserId();
        if (userId == null) return unauthorizedResponse();

        HabitProgressResponse response = habitService.completeHabit(id, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Skip habit for today (records skipped progress tied to authenticated user)
     */
    @PostMapping("/{id}/skip")
    public ResponseEntity<HabitProgressResponse> skipHabit(@PathVariable Long id) {
        Long userId = getAuthenticatedUserId();
        if (userId == null) return unauthorizedResponse();

        HabitProgressResponse response = habitService.skipHabit(id, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get progress history for a habit (ensure habit belongs to authenticated user)
     */
    @GetMapping("/{id}/progress")
    public ResponseEntity<List<HabitProgressResponse>> getHabitProgress(@PathVariable Long id) {
        Long userId = getAuthenticatedUserId();
        if (userId == null) return unauthorizedResponse();

        List<HabitProgressResponse> response = habitService.getHabitProgress(id, userId);
        return ResponseEntity.ok(response);
    }
}

package com.gymapp.gym.progress;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/progress")
public class ProgressController {
    @Autowired
    private ProgressService progressService;

    @GetMapping
    public ResponseEntity<List<ProgressDto>> getAllByUser(HttpServletRequest request) throws IllegalAccessException {
        return ResponseEntity.ok(progressService.getByProfile(request));
    }

    @PostMapping("/add-progress")
    public ResponseEntity<ProgressDto> addProgressByProfile(HttpServletRequest request, @RequestBody ProgressFormData formData) throws IllegalAccessException {
        return progressService.addProgressToProfile(request, formData);
    }

    @DeleteMapping("/delete-progress/{exerciseId}")
    public ResponseEntity<String> deleteProgressById(HttpServletRequest request, @PathVariable UUID exerciseId) throws IllegalAccessException {
        return progressService.deleteProgressById(request, exerciseId);
    }

    @PatchMapping("/edit-progress/{exerciseId}")
    public ResponseEntity<String> editProgressById(HttpServletRequest request, @PathVariable UUID exerciseId, @RequestBody ProgressDto data) throws IllegalAccessException {
        return progressService.editProgressById(request, exerciseId, data);
    }
}

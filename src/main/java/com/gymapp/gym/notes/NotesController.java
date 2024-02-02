package com.gymapp.gym.notes;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/notes")
public class NotesController {
    @Autowired
    private NotesService notesService;


    @GetMapping
    public ResponseEntity<PaginatedNotesResponse> getNotes(HttpServletRequest request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam String category) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        try {
            PaginatedNotesResponse response = notesService.getAllNotesPaginated(request, pageable, category);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/add-note")
    public ResponseEntity<?> addNewNoteForUser(HttpServletRequest request, @RequestBody NotesDto data) {
        return ResponseEntity.ok(notesService.addNewNoteForUser(request, data));
    }

    @DeleteMapping("/delete-note/{noteId}")
    public ResponseEntity<Boolean> deleteNoteforUser(HttpServletRequest request, @PathVariable UUID noteId) {
        boolean success = notesService.deleteNoteForUser(request, noteId);
        if (success) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

}

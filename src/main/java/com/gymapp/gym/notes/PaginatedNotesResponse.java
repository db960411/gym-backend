package com.gymapp.gym.notes;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginatedNotesResponse {
    private List<NotesDto> notes;
    private long totalNotes;
}

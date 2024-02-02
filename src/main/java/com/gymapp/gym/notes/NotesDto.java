package com.gymapp.gym.notes;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotesDto {
    private UUID id;
    private String title;
    private String content;
    private String category;
}

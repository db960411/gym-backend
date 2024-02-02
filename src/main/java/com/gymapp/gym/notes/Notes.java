package com.gymapp.gym.notes;


import com.gymapp.gym.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
import java.util.Date;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notes {
    @GeneratedValue
    @Id
    private UUID id;

    private Date createdAt;

    private String title;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public NotesDto toDto() {
        NotesDto dto = new NotesDto();
        dto.setId(this.getId());
        dto.setTitle(this.getTitle());
        dto.setContent(this.getContent());
        dto.setCategory(this.getCategory());
        return dto;
    }

}

package com.gymapp.gym.fileUpload;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String type;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] data;
}

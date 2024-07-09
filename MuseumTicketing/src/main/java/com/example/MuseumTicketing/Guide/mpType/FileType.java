package com.example.MuseumTicketing.Guide.mpType;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "fileType")
public class FileType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "`fileType`")
    private String fileType;
}

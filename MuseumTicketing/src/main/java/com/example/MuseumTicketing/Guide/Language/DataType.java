package com.example.MuseumTicketing.Guide.Language;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@Entity
@Table(name = "dataType")
@CrossOrigin
public class DataType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dtId")
    private Integer dtId;

    @Column(name = "talk")
    private String talk;
}

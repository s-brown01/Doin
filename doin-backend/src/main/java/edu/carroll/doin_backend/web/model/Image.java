package edu.carroll.doin_backend.web.model;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Lob
    @Column(nullable = false)
    private byte[] imageData;
}

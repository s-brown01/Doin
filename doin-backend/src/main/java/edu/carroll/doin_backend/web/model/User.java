package edu.carroll.doin_backend.web.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String passwordHash;
    @Column(nullable = false)
    private String email;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @OneToOne
    @JoinColumn(name = "profile_picture_id")
    private Image profilePictureId;
    @ManyToOne
    @JoinColumn(name = "security_question_id")
    private SecurityQuestion securityQuestionId;
    @Column(name = "security_question_answer_hash")
    private  String securityQuestionAnswer;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}

package edu.carroll.doin_backend.web.model;

import jakarta.persistence.*;

@Entity
@Table(name = "security_questions")
public class SecurityQuestion {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String question;
}
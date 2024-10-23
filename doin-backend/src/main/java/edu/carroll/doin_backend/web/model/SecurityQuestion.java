package edu.carroll.doin_backend.web.model;

import jakarta.persistence.*;

@Entity
@Table(name = "security_questions")
public class SecurityQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String question;

    public SecurityQuestion() {
    }

    public SecurityQuestion(int id, String question) {
        this.id = id;
        this.question = question;
    }

    public Integer getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
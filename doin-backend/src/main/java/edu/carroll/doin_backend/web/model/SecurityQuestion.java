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

    public SecurityQuestion(String question) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SecurityQuestion that = (SecurityQuestion) o;
        // id and question should be the same
        return (
                this.id != null &&
                this.id.equals(that.id) &&
                this.question.equals(that.question)
                );
    }

    @Override
    public String toString() {
        return "SecurityQuestion{" +
                "id=" + id +
                ", question='" + question + '\'' +
                '}';
    }
}
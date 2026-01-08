package lnlearningsystem.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuizResult implements Serializable {
    private static final long serialVersionUID = 1L;
    public UUID id = UUID.randomUUID();
    public String username;
    public String category;
    public Level level;
    public LocalDateTime timestamp = LocalDateTime.now();
    public int totalQuestions;
    public int correct;
    public Map<UUID, Integer> answers = new HashMap<>(); // questionId -> chosenIndex

    public QuizResult(String username, String category, Level level, int totalQuestions) {
        this.username = username;
        this.category = category;
        this.level = level;
        this.totalQuestions = totalQuestions;
    }
}
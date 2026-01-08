package lnlearningsystem.model;

import java.io.Serializable;
import java.util.UUID;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    public UUID id = UUID.randomUUID();
    public String category;
    public Level level;
    public String text;
    public String[] options = new String[4]; // A-D
    public int correctIndex; // 0-3

    public Question(String category, Level level, String text, String a, String b, String c, String d, int correctIndex) {
        this.category = category;
        this.level = level;
        this.text = text;
        this.options[0] = a;
        this.options[1] = b;
        this.options[2] = c;
        this.options[3] = d;
        this.correctIndex = correctIndex;
    }

    public static Question get() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }
}
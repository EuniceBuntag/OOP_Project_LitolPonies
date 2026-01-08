package lnlearningsystem;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import lnlearningsystem.model.Level;
import lnlearningsystem.model.Question;
import lnlearningsystem.model.QuizResult;
import lnlearningsystem.model.Role;
import lnlearningsystem.model.User;

public class DataStore {

    private static final String USERS_FILE = "users.ser";
    private static final String QUESTIONS_FILE = "questions.ser";
    private static final String RESULTS_FILE = "results.ser";

    private Map<String, User> users = new HashMap<>();
    private Map<UUID, Question> questions = new HashMap<>();
    private Map<UUID, QuizResult> results = new HashMap<>();

    private static DataStore instance;

    private DataStore() {
        load();
    }

    /* ================= SINGLETON ================= */

    public static synchronized DataStore get() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    /* ================= RESULTS ACCESS ================= */
    
    // Retrieve all results for leaderboard display
    public static Collection<QuizResult> getAllResults() {
        return get().results.values();
    }

    // Add result for leaderboard (used after a quiz is finished)
    public static void addLeaderboardResult(QuizResult result) {
        get().results.put(result.id, result);
        User user = get().users.get(result.username);
        if (user != null) {
            user.resultIds.add(result.id);
        }
        get().saveAll();  // Save the results to the file
        
    }

    /* ================= STATIC HELPERS (FOR GUI) ================= */

    public static void addUser(User user) {
        get().users.put(user.username, user);
        DataStore.saveUsers();
    }

    public static User getUser(String username) {
        return get().users.get(username);
    }

    public static Collection<User> getAllUsers() {
        return get().users.values();
    }

    public static void saveUsers() {
        get().saveUsersInternal();
    }


    /* ================= LOAD / SAVE ================= */

    @SuppressWarnings("unchecked")
    private void load() {
        // USERS
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            users = (Map<String, User>) ois.readObject();
        } catch (Exception ignored) {}

        // QUESTIONS
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(QUESTIONS_FILE))) {
            questions = (Map<UUID, Question>) ois.readObject();
        } catch (Exception ignored) {}

        // RESULTS
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RESULTS_FILE))) {
            results = (Map<UUID, QuizResult>) ois.readObject();
        } catch (Exception ignored) {}

        // Default users
        if (users.isEmpty()) {
            users.put("admin", new User("admin", "admin", Role.ADMIN));
            users.put("student", new User("student", "student", Role.STUDENT));
            saveUsersInternal();
        }

        // Sample questions
        if (questions.isEmpty()) {
            populateSampleQuestions();
        }
    }

    private synchronized void saveUsersInternal() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (Exception e) {
        }
    }

    private synchronized void saveAll() {
        saveUsersInternal();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(QUESTIONS_FILE))) {
            oos.writeObject(questions);
        } catch (Exception e) {
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RESULTS_FILE))) {
            oos.writeObject(results);
        } catch (Exception e) {
        }
    }

    /* ================= QUESTIONS ================= */

    private void populateSampleQuestions() {
        addQuestion(new Question("Literacy", Level.BEGINNER,
                "Which word is a noun?", "Run", "Happy", "Dog", "Quickly", 2));

        addQuestion(new Question("Numeracy", Level.BEGINNER,
                "What is 2 + 3?", "4", "5", "6", "7", 1));

        saveAll();
    }

    public synchronized void addQuestion(Question q) {
        questions.put(q.id, q);
        saveAll();
    }

    public synchronized void updateQuestion(Question q) {
        questions.put(q.id, q);
        saveAll();
    }

    public synchronized void deleteQuestion(UUID id) {
        questions.remove(id);
        saveAll();
    }

    /* ================= RESULTS ================= */

    // This method is used to save the results after each quiz
    public synchronized void addResult(QuizResult r) {
        results.put(r.id, r);
        User u = users.get(r.username);
        if (u != null) {
            u.resultIds.add(r.id);
        }
        saveAll();
    }

    /* ================= QUERIES ================= */

    public List<String> getCategories() {
        return questions.values().stream()
                .map(q -> q.category)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Question> getQuestionsFiltered(String category, Level level) {
        return questions.values().stream()
                .filter(q -> (category == null || q.category.equals(category)) &&
                        (level == null || q.level == level))
                .collect(Collectors.toList());
    }

    public List<Question> getQuestions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public static QuizResult getResult(UUID rid) {
        return get().results.get(rid);
    }

    public List<User> getUsers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void clearAllResults() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearAllResults'");
    }

    public static void deleteUser(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }
}


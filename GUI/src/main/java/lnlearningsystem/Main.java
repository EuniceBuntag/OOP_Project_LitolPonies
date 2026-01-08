package lnlearningsystem;

// Importing necessary components
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Use SwingUtilities to ensure GUI changes happen on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize or load data from DataStore
                DataStore.get(); // Load data, handle serialized files
                
                // Create the main application frame
                AppFrame appFrame = new AppFrame();

                // Make the application frame visible
                appFrame.setVisible(true);

            } catch (Exception e) {
                // Catch and print exceptions during initialization
                System.out.println("An error occurred during the application initialization: " + e.getMessage());
            }
        });
    }
}
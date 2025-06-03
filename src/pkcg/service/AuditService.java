package pkcg.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service for logging application activity to an audit file.
 * 
 * Implements the Singleton pattern to ensure a single point of logging across the application.
 * Logs actions with timestamps to a CSV file for later analysis and compliance tracking.
 */
public class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";
    private PrintWriter writer;
    
    /**
     * Private constructor to prevent direct instantiation.
     * Opens the audit file for append operations.
     */
    private AuditService() {
        try {
            this.writer = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            System.err.println("Error creating audit service: " + e.getMessage());
        }
    }
    
    /**
     * Gets the singleton instance of the AuditService.
     * Creates the instance if it doesn't already exist.
     * 
     * @return The AuditService instance
     */
    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }
    
    /**
     * Logs an action to the audit file with the current timestamp.
     * 
     * @param actionName The name of the action being performed
     */
    public void logAction(String actionName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        writer.println(actionName + "," + timestamp);
        writer.flush();
    }
    
    /**
     * Closes the audit file writer, ensuring all logged data is flushed.
     * Should be called when the application terminates.
     */
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}
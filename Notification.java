import java.io.Serializable;
// Notification.java
// Simple message sent to a user. Stored in AirlineSystem so user can view inbox.

public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int counter = 0;
    private String notificationId;
    private User   recipient;
    private String message;
    private String date;
    private boolean read;

    public Notification(User recipient, String message) {
        this.notificationId = "N" + (++counter);
        this.recipient = recipient;
        this.message   = message;
        this.date      = java.time.LocalDate.now().toString();
        this.read      = false;
    }

    public void markAsRead() { this.read = true; }

    public String  getNotificationId() { return notificationId; }
    public User    getRecipient()      { return recipient; }
    public String  getMessage()        { return message; }
    public String  getDate()           { return date; }
    public boolean isRead()            { return read; }

    @Override
    public String toString() {
        return "[" + (read ? " " : "*") + "] " + date + " - " + message;
    }
}

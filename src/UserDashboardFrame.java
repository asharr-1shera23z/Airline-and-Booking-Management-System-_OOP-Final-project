// UserDashboardFrame.java
// Logged-in user's main window. Has buttons for each user action.
// Most actions open dialogs; booking opens a separate frame.

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class UserDashboardFrame extends JFrame {

    private User user;
    private AirlineSystem system;
    private StartFrame startFrame;
    private JTabbedPane tabs;

    public UserDashboardFrame(User user, AirlineSystem system, StartFrame startFrame) {
        this.user       = user;
        this.system     = system;
        this.startFrame = startFrame;

        setTitle("User Dashboard_ " + user.getName());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                FileHandler.saveSystem(system);
                startFrame.setVisible(true);
            }
        });

        UserGUI();
    }
    private void UserGUI() {
        JPanel main = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(30, 60, 120));
        top.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel hello = new JLabel("Welcome! " + user.getName());
        hello.setForeground(Color.WHITE);
        hello.setFont(new Font("Times New Roman", Font.BOLD, 16));
        top.add(hello, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);

        JButton updateBtn = new JButton("Update Profile");
        updateBtn.setBackground(new Color(0, 150, 0));
        updateBtn.addActionListener(e -> updateUserProfile());

        JButton deleteBtn = new JButton("Delete Account");
        deleteBtn.setBackground(new Color(220, 50, 50));
        deleteBtn.addActionListener(e -> deleteUserAccount());

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> { user.logout(); FileHandler.saveSystem(system);
            startFrame.setVisible(true);
            dispose();
        });
        
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(logoutBtn);
        top.add(buttonPanel, BorderLayout.EAST);
        main.add(top, BorderLayout.NORTH);
        
        tabs = new JTabbedPane();
        tabs.addTab("Search Flights",     FlightSearchPanel());
        tabs.addTab("Book Ticket",        BookingPanel());
        tabs.addTab("My Bookings",        BookingDisplayPanel());
        tabs.addTab("Cancel & Refund",    CancelBookingPanel());
        tabs.addTab("Notifications",      NotificationsPanel());

        main.add(tabs, BorderLayout.CENTER);
        setContentPane(main);

    }

    // _____________________ TAB 1: Search flights ____________________
    private JPanel  FlightSearchPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Set<String> citySet = new HashSet<>();
        List<Flight> allFlights = system.searchFlights();   
        for (Flight f : allFlights) {
            citySet.add(f.getSource().getCity());
            citySet.add(f.getDestination().getCity());
        }

        List<String> cities = new ArrayList<>(citySet);
        Collections.sort(cities);   // Alphabetical order
        
        cities.add(0, "____Select City____");

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        filter.setBorder(BorderFactory.createTitledBorder("Search Flights"));

        filter.add(new JLabel("Source City:"));
        JComboBox<String> sourceCombo = new JComboBox<>(cities.toArray(new String[0]));
        sourceCombo.setPreferredSize(new Dimension(160, 30));
        filter.add(sourceCombo);

        filter.add(new JLabel("Destination:"));
        JComboBox<String> destCombo = new JComboBox<>(cities.toArray(new String[0]));
        destCombo.setPreferredSize(new Dimension(160, 30));
        filter.add(destCombo);
        
        JButton searchBtn = new JButton("Search");
        JButton showAllBtn = new JButton("Show All");
        filter.add(searchBtn);
        filter.add(showAllBtn);
    
        JPanel topPanel = new JPanel(new BorderLayout(0,5));
        topPanel.add(filter, BorderLayout.CENTER);
        
        // Table to display flights
        String[] cols = {"Flight ID", "From", "To", "Departure", "Arrival", "Fare (Rs)", "Available", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(table);
        Runnable refresh = () -> {
            model.setRowCount(0);
            String s = (String) sourceCombo.getSelectedItem();
            String d = (String) destCombo.getSelectedItem();

            if (s.equals("____Select City____")) s = "";
            if (d.equals("____Select City____")) d = "";    
            
            List<Flight> flights = (s.isEmpty() || d.isEmpty()) ? system.searchFlights() : system.searchFlights(s, d);
            for (Flight f : flights) {
                model.addRow(new Object[]{
                    f.getFlightId(),
                    f.getSource().getCity(),
                    f.getDestination().getCity(),
                    f.getDepartureTime(),
                    f.getArrivalTime(),
                    f.getFare(),
                    f.getAvailableSeatsCount() + "/" + f.getCapacity(),
                    f.getStatus()
                });
            }
        };

        searchBtn.addActionListener(e -> refresh.run());
        showAllBtn.addActionListener(e -> { sourceCombo.setSelectedIndex(0); destCombo.setSelectedIndex(0); refresh.run(); });
        refresh.run();

        p.add (topPanel, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        // Refresh whenever this tab is shown
        tabsAddRefreshListener(p, refresh);

        return p;
    }
    // _____________________ TAB 2: Book a ticket  ____________________
    private JPanel BookingPanel(){ 
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(Color.WHITE);

        JLabel header = new JLabel("______Book Your Flight______", SwingConstants.CENTER);
        header.setFont(new Font("Times New Roman", Font.BOLD, 28));
        header.setForeground(new Color(30, 60, 120));   // Same as top bar
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        p.add(header, BorderLayout.NORTH);


        JLabel info = new JLabel(
            "<html><body style='width:500px'>"
          + "Click the button below to start a new booking. "
          + "You will choose a flight, select a seat, enter luggage weight, "
          + "and pay using your preferred method."
          + "</body></html>");

        info.setFont(new Font("Arial", Font.PLAIN, 13));
        p.add(info, BorderLayout.NORTH);

        JButton start = new JButton("Start New Booking");
        start.setFont(new Font("Times New Roman", Font.BOLD, 16));
        start.setBackground(new Color(30, 60, 120));
        start.setBackground(new Color(0, 0, 139));  
        
        // calling booking frame class
        start.addActionListener(e -> new BookingFrame(user, system, this).setVisible(true)); 

        JPanel center = new JPanel();
        center.add(start);
        p.add(center, BorderLayout.CENTER);
        return p;
    }
    // _____________________ TAB 3: View Booking/tickets  ____________________
    private JPanel BookingDisplayPanel() {
        Color darkBlue = new Color(0, 0, 139);
        Color lightBlue = new Color(235, 243, 255);
        
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBackground(lightBlue);
        p.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JPanel header = new JPanel(new BorderLayout(5, 5));
        header.setBackground(lightBlue);

        JLabel title = new JLabel("My Bookings");
        title.setFont(new Font("Times New Roman", Font.BOLD, 24));
        title.setForeground(darkBlue);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
            
        header.add(title, BorderLayout.NORTH);
        p.add(header, BorderLayout.NORTH);

        //  table
        String[] cols = {"Booking ID", "Flight", "Seat", "Date", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        
        table.setRowHeight(32);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setBackground(new Color(30, 60, 120));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(table);

        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Booking b : user.getBookings()) {
                model.addRow(new Object[]{
                    b.getBookingId(),
                    b.getFlight().getFlightId(),
                    b.getSeat().getSeatNumber() + " (" + b.getSeat().getClassType() + ")",
                    b.getBookingDate(),
                    "Rs " + b.getTotalAmount(),
                    b.getStatus()
                });
            }
        };

            
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton refreshBtn = new JButton("Refresh");
            refreshBtn.setFont(new Font("Arial", Font.BOLD, 14));
            refreshBtn.setBackground(new Color(0, 150, 0));

        JButton viewTicketBtn = new JButton("View Ticket");
            viewTicketBtn.setFont(new Font("Arial", Font.BOLD, 14));
            viewTicketBtn.setBackground(new Color(0, 150, 0));
        
            refreshBtn.addActionListener(e -> refresh.run());
            viewTicketBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row < 0) { JOptionPane.showMessageDialog(this, "Select a booking first."); return; }

                Booking b = user.getBookings().get(row);
                if (b.getTicket() == null) {
                    JOptionPane.showMessageDialog(this, "No ticket available for this booking.");
                    return;
                }

                JTextArea area = new JTextArea(b.getTicket().viewTicket());
                area.setEditable(false);
                area.setFont(new Font("Monospaced", Font.PLAIN, 13));
                area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


                JOptionPane.showMessageDialog(this, new JScrollPane(area), "Ticket", JOptionPane.PLAIN_MESSAGE);
            });
            refresh.run();

            JPanel bottom = new JPanel();
            bottom.add(refreshBtn);
            bottom.add(viewTicketBtn);

            p.add(header, BorderLayout.NORTH);
            p.add(scroll, BorderLayout.CENTER);
            p.add(bottom, BorderLayout.SOUTH);
            
            refresh.run();
            tabsAddRefreshListener(p, refresh);
            return p;
    }
    // _____________________ TAB 4: Cancel / Refund ____________________
    private JPanel CancelBookingPanel() {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBackground(new Color(250, 253, 255)); 
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 139), 3), " Cancel/ Refund your Bookings  ",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Times New Roman", Font.BOLD, 18),
            new Color(0, 0, 139)
        ),
        BorderFactory.createEmptyBorder(15, 15, 15, 15) ) );

        String[] cols = {"Booking ID", "Flight", "Seat", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(35);                   
        table.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.setSelectionBackground(new Color(0, 153, 255)); 

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));

        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Booking b : user.getBookings()) {
                if (b.getStatus().equals("Confirmed")) {
                    model.addRow(new Object[]{
                        b.getBookingId(),
                        b.getFlight().getFlightId(),
                        b.getSeat().getSeatNumber(),
                        "Rs " + b.getTotalAmount(),
                        b.getStatus()
                    });
                }
            }
        };

    JButton refreshBtn = new JButton("Refresh");
    refreshBtn.setBackground(new Color(0, 153, 76));  
    refreshBtn.setFont(new Font("Times New Roman", Font.BOLD, 14));
    refreshBtn.addActionListener(e -> refresh.run());

    JButton cancelBtn = new JButton("Cancel Booking & Request Refund");
    cancelBtn.setBackground(new Color(204, 40, 50));   
    cancelBtn.setFont(new Font("Times New Roman", Font.BOLD, 14));

    cancelBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a confirmed booking first.");
                return;
            }
            // Find matching booking
            String bookingId = (String) model.getValueAt(row, 0);
            if ("No confirmed bookings".equals(bookingId)) {
                JOptionPane.showMessageDialog(this, "No confirmed bookings to cancel!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Booking b = system.findBooking(bookingId);
            if (b == null) return;

            String reason = JOptionPane.showInputDialog(this, "Reason for cancellation:");
            if (reason == null || reason.trim().isEmpty()) return;

            b.cancelBooking();
            system.addRefundRequest(new RefundRequest(b, reason));
            FileHandler.saveSystem(system);

            JOptionPane.showMessageDialog(this, "Admin will review your request shortly.", "Submitted", JOptionPane.INFORMATION_MESSAGE);
            refresh.run();
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setBackground(new Color(240, 248, 255));
        bottom.add(refreshBtn);
        bottom.add(cancelBtn);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        refresh.run();
        tabsAddRefreshListener(p, refresh);
        return p;
    }
    // _____________________ TAB 5: Notifications ______________________
    private JPanel NotificationsPanel() {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBackground(new Color(240, 248, 255)); 

        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 139), 3),
                " My Notifications ",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Times New Roman", Font.BOLD, 18),
                new Color(0, 0, 139)
        ),BorderFactory.createEmptyBorder(15, 15, 15, 15) ));
        
        JLabel titleLabel = new JLabel(" Notifications ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 0, 139) );
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
            list.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            list.setFixedCellHeight(40);                    
            list.setSelectionBackground(new Color(0, 0, 140)); // Nice blue selection
            list.setBackground(new Color(250, 253, 255));
            list.setForeground(new Color(0, 0, 139));
            
            JScrollPane scrollPane = new JScrollPane(list);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));

        Runnable refresh = () -> {
            listModel.clear();
            List<Notification> mine = system.getNotificationsFor(user);
            if (mine.isEmpty()) {
                listModel.addElement("No notifications.");
            } else {
                for (Notification n : mine) {
                    listModel.addElement(n.toString());
                    n.markAsRead();
                }
            }
        };
        refresh.run();
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(new Color(0, 153, 76)); 
        refreshBtn.setFont(new Font("Times New Roman", Font.BOLD, 14));
        refreshBtn.addActionListener(e -> refresh.run());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(250, 253, 255));
        bottomPanel.add(refreshBtn);

        p.add(titleLabel, BorderLayout.NORTH);
        p.add(scrollPane, BorderLayout.CENTER);
        p.add(bottomPanel, BorderLayout.SOUTH);


        tabsAddRefreshListener(p, refresh);
        return p;
    }
    
//_____________________________________OTHER HELPER METHODS_____________________________________    

    //_______________ Update profile: change name and password _______________
    private void updateUserProfile() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Full Name:"));
        JTextField nameField = new JTextField(user.getName(), 20);
        panel.add(nameField);

        panel.add(new JLabel("New Password:"));
        JPasswordField passField = new JPasswordField(20);
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(this, panel,"Update Your Profile", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        String newName = nameField.getText().trim();
        String newPass = new String(passField.getPassword()).trim();
        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newName.equals(user.getName()) && newPass.isEmpty()) {
            if (newPass.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters.","Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        user.setName(newName);
        user.setPassword(newPass);
        FileHandler.saveSystem(system);

        JOptionPane.showMessageDialog(this, "Profile updated", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    // _______________ Delete account _______________
    private void deleteUserAccount() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete your account? ",  "Confirm ", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        FileHandler.saveSystem(system);
        JOptionPane.showMessageDialog(this, "Account deleted. ", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        startFrame.setVisible(true);
        dispose();
    }
    // Helper to add a listener that refreshes a tab's content whenever it's selected
    private void tabsAddRefreshListener(JPanel panel, Runnable refresh) {
        SwingUtilities.invokeLater(() -> {
            tabs.addChangeListener(e -> {
                if (tabs.getSelectedComponent() == panel) refresh.run();
            });
        });
    }
    // Called by BookingFrame when a booking is completed, to refresh tables
    public void refreshAllTables() {
        // Re-build to reflect latest data
        getContentPane().removeAll();
        UserGUI();
        revalidate();
        repaint();
    }
}

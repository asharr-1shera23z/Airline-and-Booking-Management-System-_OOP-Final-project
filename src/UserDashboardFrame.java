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
    

    private DefaultTableModel flightModel;
    private JTable flightTable;
    private JComboBox<String> seatCombo;
    private JTextField luggageField;
    private JComboBox<String> paymentCombo;
    private JLabel totalLabel;
    private Flight selectedFlight;

    private void UserGUI() {
        JPanel main = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(0, 0, 130));
        top.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel hello = new JLabel("Welcome! " + user.getName());
        hello.setForeground(Color.WHITE);
        hello.setFont(new Font("Times New Roman", Font.BOLD, 16));
        top.add(hello, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);
        
        JButton updateBtn = new JButton("Update Profile") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setColor(new Color(0, 150, 80));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFont(new Font("Times New Roman", Font.BOLD, 13));
        updateBtn.setFocusPainted(false);
        updateBtn.setBorderPainted(false);
        updateBtn.setContentAreaFilled(false);
        updateBtn.setOpaque(false);
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateBtn.setPreferredSize(new Dimension(120, 22));
        updateBtn.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        updateBtn.addActionListener(e -> updateUserProfile());

        JButton deleteBtn = new JButton("Delete Account") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setColor(new Color(220, 50, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                g2.dispose();

                // text show karne ke liye
                super.paintComponent(g);
            }
        };
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Times New Roman", Font.BOLD, 13));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setOpaque(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.setPreferredSize(new Dimension(120, 22));
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        deleteBtn.addActionListener(e -> deleteUserAccount());

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> { 
            user.logout(); 
            FileHandler.saveSystem(system);
            startFrame.setVisible(true);
            dispose();
        });
        
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(logoutBtn);
        top.add(buttonPanel, BorderLayout.EAST);

        main.add(top, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Times New Roman", Font.BOLD, 14));
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

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
         Color darkBlue = new Color(0, 0, 139);

        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBackground(Color.WHITE);

        TitledBorder titled = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 139), 3),
            " Search Flights ", TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Times New Roman", Font.BOLD, 20),
            new Color(0, 0, 139)
        );
        p.setBorder(BorderFactory.createCompoundBorder(titled, BorderFactory.createEmptyBorder(8, 8, 8, 8)));

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
        filter.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JLabel sourceLabel = new JLabel("Source City:");
        sourceLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        sourceLabel.setForeground(darkBlue);
        filter.add(sourceLabel);
        
        JComboBox<String> sourceCombo = new JComboBox<>(cities.toArray(new String[0]));
        sourceCombo.setPreferredSize(new Dimension(160, 27));
        sourceCombo.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        sourceCombo.setForeground(darkBlue);        
        sourceCombo.setBackground(Color.WHITE);
        filter.add(sourceCombo);

        JLabel destLabel = new JLabel("Destination:");
        destLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        destLabel.setForeground(darkBlue);
        filter.add(destLabel);

        JComboBox<String> destCombo = new JComboBox<>(cities.toArray(new String[0]));
        destCombo.setPreferredSize(new Dimension(160, 27));
        destCombo.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        destCombo.setForeground(darkBlue);
        destCombo.setBackground(Color.WHITE);
        filter.add(destCombo);

        Font font = new Font("Times New Roman", Font.BOLD, 16);

        JButton searchBtn = new JButton("Search") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(darkBlue);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(font);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setContentAreaFilled(false);
        searchBtn.setOpaque(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.setPreferredSize(new Dimension(110, 28));

        JButton showAllBtn = new JButton("Show All") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(darkBlue);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        showAllBtn.setForeground(Color.WHITE);
        showAllBtn.setFont(font);
        showAllBtn.setFocusPainted(false);
        showAllBtn.setBorderPainted(false);
        showAllBtn.setContentAreaFilled(false);
        showAllBtn.setOpaque(false);
        showAllBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showAllBtn.setPreferredSize(new Dimension(110, 28));

        filter.add(searchBtn);
        filter.add(showAllBtn);

       
        String[] cols = {"Flight ID", "From", "To", "Departure", "Arrival", "Fare (Rs)", "Available", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        
        table.setRowHeight(22);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        table.setShowGrid(true);
        table.setGridColor(new Color(215, 215, 215));
        table.getTableHeader().setPreferredSize(new Dimension(0, 25));
        table.getTableHeader().setBackground(new Color(0, 0, 139));
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 17));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 0, 100)));

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

        p.add (filter, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
     
        tabsAddRefreshListener(p, refresh);

        return p;
    }
    // _____________________ TAB 2: Book a ticket  ____________________
    private JPanel BookingPanel() { 
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBackground(Color.WHITE);
        TitledBorder titled = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 139), 3),
            " Book New Ticket ", TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Times New Roman", Font.BOLD, 20),
            new Color(0, 0, 139)
        ); p.setBorder(BorderFactory.createCompoundBorder(titled, BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        Color darkBlue = new Color(0, 0, 139);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(darkBlue, 2),
            "1. Select a Flight", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Times New Roman", Font.BOLD, 16), darkBlue));

        flightModel = new DefaultTableModel(
            new String[] { "Flight ID", "From", "To", "Departure", "Fare", "Available Seats" }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        flightTable = new JTable(flightModel);
        flightTable.setRowHeight(30);
        flightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScroll = new JScrollPane(flightTable);
        loadFlights();  

        flightTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onFlightPicked();
        });

        top.add(tableScroll, BorderLayout.CENTER);

    
        JPanel mid = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        mid.setBackground(Color.WHITE);

        TitledBorder bookingBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(darkBlue, 2), "2. Booking Details",
            TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Times New Roman", Font.BOLD, 18), darkBlue);

        mid.setBorder(BorderFactory.createCompoundBorder(bookingBorder, BorderFactory.createEmptyBorder(8, 6, 8, 6)));

        seatCombo = new JComboBox<>();
        seatCombo.setPreferredSize(new Dimension(140, 28));
        mid.add(new JLabel("Available Seats:"));
        mid.add(seatCombo);

        luggageField = new JTextField("0");
        luggageField.setPreferredSize(new Dimension(60, 28));
        mid.add(new JLabel("Luggage (kg):"));
        mid.add(luggageField);

        paymentCombo = new JComboBox<>(new String[]{"Card", "Cash", "Bank"});
        paymentCombo.setPreferredSize(new Dimension(100, 28));
        mid.add(new JLabel("Payment:"));
        mid.add(paymentCombo);

        JButton calcBtn = new JButton("Calculate") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(darkBlue);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setFont(new Font("Times New Roman", Font.BOLD, 13));
        calcBtn.setFocusPainted(false);
        calcBtn.setBorderPainted(false);
        calcBtn.setContentAreaFilled(false);
        calcBtn.setOpaque(false);
        calcBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        calcBtn.setPreferredSize(new Dimension(100, 28));
        calcBtn.addActionListener(e -> updateTotal());
        mid.add(calcBtn);

        totalLabel = new JLabel("Total: Rs 0", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        totalLabel.setForeground(darkBlue);
        mid.add(totalLabel);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        bottom.setBackground(Color.WHITE);

        JButton confirm = new JButton("Confirm Booking") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 150, 80));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        confirm.setForeground(Color.WHITE);
        confirm.setFont(new Font("Times New Roman", Font.BOLD, 16));
        confirm.setFocusPainted(false);
        confirm.setBorderPainted(false);
        confirm.setContentAreaFilled(false);
        confirm.setOpaque(false);
        confirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirm.setPreferredSize(new Dimension(170, 40));
        confirm.addActionListener(e -> doConfirm());

        JButton cancel = new JButton("Cancel") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(220, 50, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cancel.setForeground(Color.WHITE);
        cancel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        cancel.setFocusPainted(false);
        cancel.setBorderPainted(false);
        cancel.setContentAreaFilled(false);
        cancel.setOpaque(false);
        cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancel.setPreferredSize(new Dimension(120, 40));
        cancel.addActionListener(e -> {
           
            flightTable.clearSelection();
            seatCombo.removeAllItems();
            luggageField.setText("0");
            totalLabel.setText("Total: Rs 0");
        });

        bottom.add(confirm);
        bottom.add(cancel);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(top, BorderLayout.CENTER);
        centerPanel.add(mid, BorderLayout.SOUTH);

        p.add(centerPanel, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }
    // _____________________ TAB 3: View Booking/tickets  ____________________
    private JPanel BookingDisplayPanel() {
        Color darkBlue = new Color(0, 0, 139);
        
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBackground(Color.WHITE);
        TitledBorder titled = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 139), 3),
            " My Bookings ", TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Times New Roman", Font.BOLD, 20),
            new Color(0, 0, 139)
        ); p.setBorder(BorderFactory.createCompoundBorder(titled, BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        
        Font font = new Font("Times New Roman", Font.BOLD, 16);

        String[] cols = {"Booking ID", "Flight", "Seat", "Date", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(22);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        table.setShowGrid(true);
        table.setGridColor(new Color(215, 215, 215));
        table.getTableHeader().setPreferredSize(new Dimension(0, 30));
        table.getTableHeader().setBackground(new Color(0, 0, 139));
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 19));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 0, 100)));
        
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

        JButton refreshBtn = new JButton("Refresh") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(darkBlue);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(font);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.setOpaque(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setPreferredSize(new Dimension(120, 28));
        refreshBtn.addActionListener(e -> refresh.run());

        JButton viewTicketBtn = new JButton("View Ticket") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(darkBlue);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        viewTicketBtn.setForeground(Color.WHITE);
        viewTicketBtn.setFont(font);
        viewTicketBtn.setFocusPainted(false);
        viewTicketBtn.setBorderPainted(false);
        viewTicketBtn.setContentAreaFilled(false);
        viewTicketBtn.setOpaque(false);
        viewTicketBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewTicketBtn.setPreferredSize(new Dimension(140, 28));
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(viewTicketBtn);

        p.add(scroll, BorderLayout.CENTER);
        p.add(buttonPanel, BorderLayout.SOUTH);
            
        refresh.run();
        tabsAddRefreshListener(p, refresh);
        return p;
    }
    // _____________________ TAB 4: Cancel / Refund ____________________
    private JPanel CancelBookingPanel() {
        Color darkBlue = new Color(0, 0, 139);
        Font font = new Font("Times New Roman", Font.BOLD, 16);

        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBackground(Color.WHITE);

        TitledBorder titled = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 139), 3),
            " Cancel & Refund ", TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Times New Roman", Font.BOLD, 20),
            new Color(0, 0, 139)
        );
        p.setBorder(BorderFactory.createCompoundBorder(titled, BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        String[] cols = {"Booking ID", "Flight", "Seat", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        JTable table = new JTable(model);
        table.setRowHeight(22);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        table.setShowGrid(true);
        table.setGridColor(new Color(215, 215, 215));
        table.getTableHeader().setPreferredSize(new Dimension(0, 30));
        table.getTableHeader().setBackground(new Color(0, 0, 139));
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 18));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 0, 100)));

        JScrollPane scrollPane = new JScrollPane(table);
     

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

        JButton refreshBtn = new JButton("Refresh") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(darkBlue);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(font);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.setOpaque(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setPreferredSize(new Dimension(120, 28));

        JButton cancelBtn = new JButton("Cancel Booking & Request Refund") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(220, 50, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(font);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setOpaque(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setPreferredSize(new Dimension(280, 28));
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
        bottom.setBackground(Color.WHITE);
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
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBackground(Color.WHITE);

        TitledBorder titled = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 139), 3),
            " My Notifications ", TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Times New Roman", Font.BOLD, 20),
            new Color(0, 0, 139)
        );  p.setBorder(BorderFactory.createCompoundBorder(titled, BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        
        Color darkBlue = new Color(0, 0, 139);
        Font font = new Font("Times New Roman", Font.BOLD, 16);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> list = new JList<>(listModel);
        list.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        list.setFixedCellHeight(42);
        list.setSelectionBackground(new Color(0, 0, 139));
        list.setSelectionForeground(Color.WHITE);
        list.setForeground(darkBlue);
            
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 139), 2));

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
        
        JButton refreshBtn = new JButton("Refresh") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(darkBlue);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(font);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setContentAreaFilled(false);
        refreshBtn.setOpaque(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setPreferredSize(new Dimension(120, 28));
        refreshBtn.addActionListener(e -> refresh.run());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 12));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(refreshBtn);

        p.add(scrollPane, BorderLayout.CENTER);
        p.add(bottomPanel, BorderLayout.SOUTH);

        tabsAddRefreshListener(p, refresh);
        return p;
    }
    
//_____________________________________OTHER HELPER METHODS____________________________________
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
    
    //_____________________Booking Frame helper Methods_____________________
    private void loadFlights() {
        flightModel.setRowCount(0);
        for (Flight f : system.searchFlights()) {
            if (f.getAvailableSeatsCount() == 0) continue;
            flightModel.addRow(new Object[] {
                    f.getFlightId(),
                    f.getSource().getCity(),
                    f.getDestination().getCity(),
                    f.getDepartureTime(),
                    "Rs " + f.getFare(),
                    f.getAvailableSeatsCount() + "/" + f.getCapacity()
            });
        }
    }

    private void onFlightPicked() {
        int row = flightTable.getSelectedRow();
        if (row < 0) {
            selectedFlight = null;
            return;
        }
        String id = (String) flightModel.getValueAt(row, 0);
        selectedFlight = system.findFlight(id);

        seatCombo.removeAllItems();
        if (selectedFlight != null) {
            for (Seat s : selectedFlight.getAvailableSeats()) {
                seatCombo.addItem("Seat " + s.getSeatNumber() + " (" + s.getClassType() + ")");
            }
        }
    }

    private void updateTotal() {
        if (selectedFlight == null) {
            JOptionPane.showMessageDialog(this, "Please select a flight first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String luggageText = luggageField.getText().trim();
        double luggage;
        try {
            luggage = Double.parseDouble(luggageText);
        } catch (NumberFormatException e) {
            return;
        }
        if (luggage < 0 || luggage > 40) {
            JOptionPane.showMessageDialog(this, "Luggage weight must be between 0 and 40 kg!", "Limit Exceeded", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double total = user.calculateFare(selectedFlight, luggage);
        totalLabel.setText("Total: Rs " + String.format("%.0f", total));
    }

    private void doConfirm() {
        if (selectedFlight == null) {
            JOptionPane.showMessageDialog(this, "Pick a flight first.");
            return;
        }
        if (seatCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No seats available.");
            return;
        }
        Seat seat = pickedSeat();
        if (seat == null) return;

     double luggage;

        try {
            luggage = Double.parseDouble(luggageField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid luggage weight.");
            return;
        }

        if (luggage < 0) {
            JOptionPane.showMessageDialog(this, "Luggage weight cannot be negative.");
            return;
        }

        if (luggage > 50) {
            JOptionPane.showMessageDialog(this, "Luggage limit exceeded! Maximum allowed luggage is 50 kg.");
            return;
        }

        double total = user.calculateFare(selectedFlight, luggage);

        PaymentMethod method = pickPaymentMethod();
        if (method == null) return;

        Booking booking = new Booking(user, selectedFlight, seat, luggage);
        booking.setTotalAmount(total);
        Payment pay = new Payment(total, method);

        if (!pay.processPayment()) {
            JOptionPane.showMessageDialog(this, "Payment failed. Booking aborted.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        booking.setPayment(pay);
        booking.confirmBooking();

        user.addBooking(booking);
        system.addBooking(booking);
        system.addNotification(new Notification(user, "Booking " + booking.getBookingId() + " confirmed."));

        FileHandler.saveSystem(system);

        JOptionPane.showMessageDialog(this, "Booking confirmed!\n\n" + booking.getTicket().viewTicket(), "Success", JOptionPane.INFORMATION_MESSAGE);

        refreshAllTables();
    }

    private Seat pickedSeat() {
        String s = (String) seatCombo.getSelectedItem();
        if (s == null) return null;
        try {
            int num = Integer.parseInt(s.split(" ")[1]);
            for (Seat seat : selectedFlight.getSeats()) {
                if (seat.getSeatNumber() == num) return seat;
            }
        } catch (Exception ignored) {}
        return null;
    }

    private PaymentMethod pickPaymentMethod() {
        String choice = (String) paymentCombo.getSelectedItem();
        switch (choice) {
            case "Card": {
                String num = JOptionPane.showInputDialog(this, "Card Number (16 Digits):");
                if (num == null || num.trim().isEmpty()) return null;
                String holder = JOptionPane.showInputDialog(this, "Card Holder Name:");
                if (holder == null || holder.trim().isEmpty()) return null;
                String exp = JOptionPane.showInputDialog(this, "Expiry (MM/YY):");
                if (exp == null || exp.trim().isEmpty()) return null;
                String cvv = JOptionPane.showInputDialog(this, "CVV (3-4 Digits):");
                if (cvv == null || cvv.trim().isEmpty()) return null;
                return new CardPayment(num.trim(), holder.trim(), exp.trim(), cvv.trim());
            }
            case "Cash":
                return new CashPayment();
            case "Bank":
                String acc = JOptionPane.showInputDialog(this, "Account Number:");
                if (acc == null || acc.trim().isEmpty()) return null;
                String bk = JOptionPane.showInputDialog(this, "Bank Name:");
                if (bk == null || bk.trim().isEmpty()) return null;
                return new BankTransferPayment(acc.trim(), bk.trim());
        }
        return null;
    }

    private double parseDouble(String s, double dflt) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return dflt;
        }
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

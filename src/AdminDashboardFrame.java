// AdminDashboardFrame.java
// Admin's main window with tabs for each major operation:
// Airports, Aircrafts, Flights, Crew, Bookings, Refunds, Reports.

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class AdminDashboardFrame extends JFrame {

    private Admin admin; // the logged-in admin user
    private AirlineSystem system;
    private StartFrame startFrame;

    public AdminDashboardFrame(Admin admin, AirlineSystem system, StartFrame startFrame) {
        this.admin = admin;
        this.system = system;
        this.startFrame = startFrame;

        setTitle("Admin Dashboard_ " + admin.getName());
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                FileHandler.saveSystem(system);
                startFrame.setVisible(true);
            }
        });

        AdminGUI();
    }

    private void AdminGUI() {
        JPanel main = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(0, 0, 139));
        top.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel hello = new JLabel("Admin Dashboard_ " + admin.getName());
        hello.setForeground(Color.WHITE);
        hello.setFont(new Font("Times New Roman", Font.BOLD, 18));
        top.add(hello, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

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
        updateBtn.addActionListener(e -> updateAdminProfile());

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
        deleteBtn.addActionListener(e -> deleteAdminAccount());

        JButton logoutBtun = new JButton("Logout");
        logoutBtun.setFocusPainted(false);
        logoutBtun.addActionListener(e -> {
            admin.logout();
            FileHandler.saveSystem(system);
            startFrame.setVisible(true);
            dispose();
        });

        rightPanel.add(updateBtn);
        rightPanel.add(deleteBtn);
        rightPanel.add(logoutBtun);
        top.add(rightPanel, BorderLayout.EAST);
        main.add(top, BorderLayout.NORTH);

        UIManager.put("TabbedPane.tabInsets", new Insets(8, 14, 8, 14));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Times New Roman", Font.BOLD, 14));
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        tabs.addTab("Manage Airports", AirportsTab());
        tabs.addTab("Manage Aircrafts", AircraftsTab());
        tabs.addTab("Manage Flights", FlightsTab());
        tabs.addTab("Manage Crew", CrewTab());
        tabs.addTab("Manage Bookings", BookingsTab());
        tabs.addTab("Manage Refunds", RefundsTab());
        tabs.addTab("Reports", ReportsTab());

        main.add(tabs, BorderLayout.CENTER);
        setContentPane(main);
    }
    // _______________________________________________________________________________________________________________________________________________________________________________________________________________//_______________________________________________________________________________________________________________________________________________________________________________________________________________

    // 1. Managing airports (adding, updating, deleting, listing)
    private JPanel AirportsTab() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBackground(Color.WHITE);

        TitledBorder titled = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 0, 139), 3),
            " Manage Airports ", TitledBorder.CENTER, TitledBorder.TOP,
            new Font("Times New Roman", Font.BOLD, 20),
            new Color(0, 0, 139)
        );
        p.setBorder(BorderFactory.createCompoundBorder( titled,  BorderFactory.createEmptyBorder(8, 8, 8, 8) ));

        String[] columns = { "Code", "Name", "City", "Country", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) { 
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
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

        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Airport a : system.getAirports()) {
                model.addRow(new Object[] { a.getAirportCode(), a.getName(), a.getCity(),
                        a.getCountry(), a.getStatus()
                });
            }
        };
        refresh.run();

        JPanel buttons = new JPanel();

        Color darkBlue = new Color(0, 0, 139);
        Font font = new Font("Times New Roman", Font.BOLD, 16);
    
        // Adding airports______________________________________________________
        JButton addBtn = new JButton("Add Airport") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(darkBlue);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(font);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setContentAreaFilled(false);
        addBtn.setOpaque(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setPreferredSize(new Dimension(120, 26));
        addBtn.addActionListener(e -> {
            while (true) {
                JTextField code = new JTextField(), name = new JTextField(), city = new JTextField(),
                        country = new JTextField();

                Object[] msg = { "Code(3 letters):", code, "Name:", name, "City:", city, "Country:", country };
                int r = JOptionPane.showConfirmDialog(this, msg, "            Add a new Airport",
                        JOptionPane.OK_CANCEL_OPTION);

                if (r != JOptionPane.OK_OPTION)
                    return;

                // ok krny ka bad hi sab validate and confirm hoga
                String cod = code.getText().trim().toUpperCase();
                String nam = name.getText().trim();
                String cit = city.getText().trim();
                String coun = country.getText().trim();

                // validatiions
                if (cod.isEmpty() || cod.length() != 3 || !cod.equals(cod.toUpperCase())) {
                    err("Code must be 3 uppercase letters (like  KHI, LHR)");
                    continue;
                }

                if (system.findAirport(cod) != null) {
                    err("This airport already exists");
                    continue;
                }

                if (nam.isEmpty() || cit.isEmpty() || coun.isEmpty()) {
                    err("Name, City and Country cannot be empty, so enter again.");
                    continue;
                }

                String validation = "[a-zA-Z ]+";
                if (!nam.matches(validation) || !cit.matches(validation) || !coun.matches(validation)) {
                    err("Name, City and Country can only be letters, no numbers or anyelse");
                    continue;
                }

                system.addAirport(new Airport(cod, nam, cit, coun, "Active"));
                FileHandler.saveSystem(system);
                refresh.run();

                JOptionPane.showMessageDialog(this, "Airport added successfully!:)", "Added!!",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        });
         
        // updating airports____________________________________________________
        JButton updateBtn = new JButton("Update Airport"){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(darkBlue);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFont(font);
        updateBtn.setFocusPainted(false);
        updateBtn.setBorderPainted(false);
        updateBtn.setContentAreaFilled(false);
        updateBtn.setOpaque(false);
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateBtn.setPreferredSize(new Dimension(140, 26));
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select an airport first in the table.");
                return;
            }

            String c = (String) model.getValueAt(row, 0);
            Airport a = system.findAirport(c);
            if (a == null) {
                err("Airport not found.");
                refresh.run();
                return;
            }

            String nameValue = a.getName();
            String cityValue = a.getCity();
            String countryValue = a.getCountry();
            String statusValue = a.getStatus();

            while (true) {
                JTextField name = new JTextField(nameValue);
                JTextField city = new JTextField(cityValue);
                JTextField country = new JTextField(countryValue);
                JTextField status = new JTextField(statusValue);

                Object[] msg = { "Name:", name, "City:", city, "Country:", country, "Status:", status };

                int r = JOptionPane.showConfirmDialog(this, msg, "Update Airport", JOptionPane.OK_CANCEL_OPTION);
                if (r != JOptionPane.OK_OPTION)
                    return;

                nameValue = name.getText().trim();
                cityValue = city.getText().trim();
                countryValue = country.getText().trim();
                statusValue = (String) status.getText();

                if (nameValue.isEmpty() || cityValue.isEmpty() || countryValue.isEmpty()) {
                    err("Name, City and Country can't be void. fill them again.");
                    continue;
                }
                String textPattern = "[a-zA-Z ]+";
                if (!nameValue.matches(textPattern) || !cityValue.matches(textPattern)
                        || !countryValue.matches(textPattern)) {
                    err("Name, City and Country must contain only alphabets and spaces.");
                    continue;
                }

                a.setName(nameValue);
                a.setCity(cityValue);
                a.setCountry(countryValue);
                a.setStatus(statusValue);

                FileHandler.saveSystem(system);
                refresh.run();
                JOptionPane.showMessageDialog(this, "Airport updated! Check out ", "Updated!!",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        });     

        // refresh button____________________________________________________
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
        refreshBtn.setPreferredSize(new Dimension(120, 26));
        refreshBtn.addActionListener(e -> refresh.run());

        // deleting airports____________________________________________________
        JButton deleteBtn = new JButton("Delete Airport") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(220, 50, 50));  
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(font);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setOpaque(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.setPreferredSize(new Dimension(140, 26));
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select an airport first in the table.");
                return;
            }

            String c = (String) model.getValueAt(row, 0);
            if (system.isAirportInUse(c)) {
                err("Cannot delete as flights are using this airport.");
                return;
            }

            system.removeAirport(c);
            FileHandler.saveSystem(system);
            refresh.run();
        });

        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(refreshBtn);
        buttons.add(deleteBtn);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    // 2. Managing aircrafts (adding, updating status, listing)
    private JPanel AircraftsTab() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = { "ID", "Model", "Capacity", "Airline", "Maintenance", "Availability" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);

        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Aircraft a : system.getAircrafts()) {
                model.addRow(new Object[] { a.getAircraftId(), a.getModel(), a.getCapacity(), a.getAirlineName(),
                        a.getMaintenanceStatus(), a.getAvailabilityStatus()
                });
            }
        };
        refresh.run();

        JButton addBtn = new JButton("Add Aircraft");
        JButton updateBtn = new JButton("Update Status");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        // Adding the aircraft__________________________________________________
        addBtn.addActionListener(e -> {
            while (true) {
                JTextField id = new JTextField(), model2 = new JTextField(), cap = new JTextField(),
                        airline = new JTextField();

                Object[] msg = { "Aircraft ID:", id, "Model:", model2, "Capacity:", cap, "Airline:", airline };

                int r = JOptionPane.showConfirmDialog(this, msg, "Add Aircraft", JOptionPane.OK_CANCEL_OPTION);
                if (r != JOptionPane.OK_OPTION)
                    return;

                String aid = id.getText().trim();
                String mod = model2.getText().trim();
                String airl = airline.getText().trim();

                if (aid.isEmpty() || system.findAircraft(aid) != null) {
                    err("Empty or duplicate ID issue. Enter again plz.");
                    return;
                }
                if (mod.isEmpty() || airl.isEmpty()) {
                    err("Model and airline cannot be empty, Enter again");
                    return;
                }
                if (!mod.matches("[a-zA-Z0-9 -]+")) {
                    err("Model can contain only letters, numbers, spaces and hyphen.");
                    continue;
                }
                if (!airl.matches("[a-zA-Z ]+")) {
                    err("Airline name can contain only alphabets, enter again.");
                    continue;
                }
                int cp;
                try {
                    cp = Integer.parseInt(cap.getText().trim());
                } catch (NumberFormatException ex) {
                    err("Invalid capacity.");
                    return;
                }

                if (cp <= 0 || cp > 900) {
                    err("Capacity must be +VE and less than 900.");
                    continue;
                }

                // aircraft object creeation from calling constrcytor of aircraft class.
                system.addAircraft(new Aircraft(aid, model2.getText(), cp, airline.getText(), "Good", "Available"));
                FileHandler.saveSystem(system);
                refresh.run();
                JOptionPane.showMessageDialog(this, "Aircraft added", "Added!!", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        });
        // updating the aircraft________________________________________________
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select an aircraft from the table to update it.");
                return;
            }

            String id = (String) model.getValueAt(row, 0); // id yani row 0
            Aircraft a = system.findAircraft(id);
            if (a == null) {
                err("Aircraft not found. Please refresh the table.");
                refresh.run();
                return;
            }
            String[] options = { "Available", "Assigned", "Under Maintenance" };
            String s = (String) JOptionPane.showInputDialog(this, "New status:", "Update Status",
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, a.getAvailabilityStatus());

            if (s != null) {
                a.setAvailabilityStatus(s);
                FileHandler.saveSystem(system);
                refresh.run();
            }
        });
        // deleting the aircraft________________________________________________
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select an aircraft first in the table.");
                return;
            }

            String aid = (String) model.getValueAt(row, 0);
            Aircraft a = system.findAircraft(aid);

            // Agar aircraft in use hai (Assigned) to delete nahi karne dena
            if (a.getAvailabilityStatus().equals("Assigned")) {
                err("Cannot delete: This aircraft is currently assigned to a flight.");
                return;
            }

            // Delete karo
            system.removeAircraft(aid); // AirlineSystem ka method call
            FileHandler.saveSystem(system);
            refresh.run();

            JOptionPane.showMessageDialog(this,
                    "Aircraft " + aid + " deleted successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        refreshBtn.addActionListener(e -> refresh.run());

        JPanel buttons = new JPanel();
        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(refreshBtn);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    // 3. Managing flights (adding, updating status/fare, deleting, listing)
    private JPanel FlightsTab() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = { "Flight ID", "From", "To", "Departure", "Arrival", "Fare", "Capacity", "Available",
                "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);

        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Flight f : system.getFlights()) {
                model.addRow(new Object[] { f.getFlightId(), f.getSource().getCity(), f.getDestination().getCity(),
                        f.getDepartureTime(),
                        f.getArrivalTime(), f.getFare(), f.getCapacity(), f.getAvailableSeatsCount(), f.getStatus()
                });
            }
        };
        refresh.run();

        JButton addBtn = new JButton("Add Flight");
        JButton updateBtn = new JButton("Update");
        JButton removeBtn = new JButton("Remove");
        JButton refreshBtn = new JButton("Refresh");

        // Adding flights______________________________________________________
        addBtn.addActionListener(e -> {
            while (true) {
                if (system.getAirports().size() < 2) {
                    err("Need at least 2 airports first.");
                    return;
                }
                if (system.getAircrafts().isEmpty()) {
                    err("Need at least 1 aircraft first.");
                    return;
                }

                JTextField idField = new JTextField();
                JComboBox<Airport> sourceBox = new JComboBox<>(system.getAirports().toArray(new Airport[0]));
                JComboBox<Airport> destinationBox = new JComboBox<>(system.getAirports().toArray(new Airport[0]));
                JComboBox<Aircraft> aircraftBox = new JComboBox<>();
                for (Aircraft a : system.getAircrafts()) {
                    if (a != null && "Available".equalsIgnoreCase(a.getAvailabilityStatus())) {
                        aircraftBox.addItem(a);
                    }
                }
                if (aircraftBox.getItemCount() == 0) {
                    err("No available aircrafts.");
                    return;
                }

                JTextField depField = new JTextField("2026-06-01 09:00");
                JTextField arrField = new JTextField("2026-06-01 11:00");
                JTextField fareField = new JTextField("15000");

                Object[] msg = { "Flight ID:", idField, "Source:", sourceBox, "Destination:", destinationBox,
                        "Aircraft:", aircraftBox, "Departure (YYYY-MM-DD HH:mm):", depField,
                        "Arrival (YYYY-MM-DD HH:mm):", arrField,
                        "Fare (Rs):", fareField
                };

                int r = JOptionPane.showConfirmDialog(this, msg, "Add Flight", JOptionPane.OK_CANCEL_OPTION);
                if (r != JOptionPane.OK_OPTION)
                    return;

                String fid = idField.getText().trim();
                if (fid.isEmpty() || system.findFlight(fid) != null) {
                    err("Flight ID cannot be empty or already exists, enter again.");
                    continue;
                }

                Airport src = (Airport) sourceBox.getSelectedItem();
                Airport dst = (Airport) destinationBox.getSelectedItem();
                if (src == dst) {
                    err("Source and destination cannot be the same airport!");
                    continue;
                }
                Aircraft ac = (Aircraft) aircraftBox.getSelectedItem();

                double fareVal;
                try {
                    fareVal = Double.parseDouble(fareField.getText().trim());
                } catch (NumberFormatException ex) {
                    err("Fare must be a valid number!");
                    continue;
                }
                if (fareVal <= 0 || fareVal > 100000) {
                    err("Fare must be greater than 0 and below 1 lac.");
                    continue;
                }
                String depText = depField.getText().trim();
                String arrText = arrField.getText().trim();

                java.time.format.DateTimeFormatter format = java.time.format.DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm");

                java.time.LocalDateTime depTime;
                java.time.LocalDateTime arrTime;

                try {
                    depTime = java.time.LocalDateTime.parse(depText, format);
                    arrTime = java.time.LocalDateTime.parse(arrText, format);

                } catch (java.time.format.DateTimeParseException ex) {
                    err("Invalid date/time format. write as 2026-06-01 09:00");
                    continue;
                }

                if (!arrTime.isAfter(depTime)) {
                    err("Arrival time must be after departure time.");
                    continue;
                }

                Flight f = new Flight(fid, src, dst, depText, arrText, fareVal, ac);
                system.addFlight(f);
                ac.setAvailabilityStatus("Assigned");
                FileHandler.saveSystem(system);
                refresh.run();

                JOptionPane.showMessageDialog(this, "Flight added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        });
        // Updating flights____________________________________________________
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select a flight.");
                return;
            }

            String id = (String) model.getValueAt(row, 0);
            Flight f = system.findFlight(id);
            if (f == null) {
                err("Flight not found. Please refresh the table.");
                refresh.run();
                return;
            }

            JTextField fare = new JTextField(String.valueOf(f.getFare()));
            String[] statuses = { "Scheduled", "Delayed", "Cancelled", "Completed" };

            JComboBox<String> statusBox = new JComboBox<>(statuses);
            statusBox.setSelectedItem(f.getStatus());

            Object[] msg = { "New Fare:", fare, "Status:", statusBox };
            int r = JOptionPane.showConfirmDialog(this, msg, "Update Flight",
                    JOptionPane.OK_CANCEL_OPTION);
            if (r != JOptionPane.OK_OPTION)
                return;

            double newFare;

            try {
                newFare = Double.parseDouble(fare.getText().trim());
            } catch (NumberFormatException ex) {
                err("Fare must be a valid number.");
                return;
            }
            if (newFare <= 0 || newFare > 100000) {
                err("Fare must be greater than 0 and below 1 lac.");
                return;
            }
            if (statusBox.getSelectedItem() == null) {
                err("Select a valid flight status.");
                return;
            }
            f.updateStatus((String) statusBox.getSelectedItem());
            FileHandler.saveSystem(system);
            refresh.run();
            JOptionPane.showMessageDialog(this, "Flight updated successfully!", "Updated!!",
                    JOptionPane.INFORMATION_MESSAGE);

        });
        // removing flights___________________________________________________
        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select a flight.");
                return;
            }

            String id = (String) model.getValueAt(row, 0);
            int c = JOptionPane.showConfirmDialog(this, "Remove flight " + id + "?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION)
                return;

            Flight f = system.findFlight(id);
            if (f != null && f.getAircraft() != null) {
                f.getAircraft().setAvailabilityStatus("Available");
            }

            system.removeFlight(id);
            FileHandler.saveSystem(system);
            refresh.run();
            JOptionPane.showMessageDialog(this, "Flight removed successfully!", "Removed!!",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        refreshBtn.addActionListener(e -> refresh.run());

        JPanel buttons = new JPanel();
        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(removeBtn);
        buttons.add(refreshBtn);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    // 4. Managing crew (adding, assigning to flights, listing)
    private JPanel CrewTab() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = { "Emp ID", "Name", "Role", "Available", "Assigned Flights" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);

        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Crew c : system.getCrewMembers()) {
                model.addRow(new Object[] {
                        c.getEmployeeId(), c.getName(), c.getRole(),
                        c.isAvailable() ? "Yes" : "No",
                        c.getAssignedFlights().size()
                });
            }
        };
        refresh.run();

        JButton addBtn = new JButton("Add Crew");
        JButton assignBtn = new JButton("Assign to Flight");
        JButton removeBtn = new JButton("Remove Crew");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(e -> {
            while (true) { // ← Galat input pe dobara maangega
                String[] types = { "Pilot", "CabinCrew", "GroundStaff" };
                String type = (String) JOptionPane.showInputDialog(this, "Select Crew Type:", "Add Crew",
                        JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
                if (type == null)
                    return;

                JTextField empIdField = new JTextField();
                JTextField nameField = new JTextField();
                JTextField phoneField = new JTextField();
                JTextField addrField = new JTextField();
                JTextField dutyField = new JTextField();
                JTextField extra1 = new JTextField();
                JTextField extra2 = new JTextField();

                Object[] msg;
                if (type.equals("Pilot")) {
                    msg = new Object[] {
                            "Employee ID:", empIdField, "Name:", nameField,
                            "Phone:", phoneField, "Address:", addrField,
                            "Duty Schedule:", dutyField,
                            "License No:", extra1, "Experience (years):", extra2
                    };
                } else if (type.equals("CabinCrew")) {
                    msg = new Object[] {
                            "Employee ID:", empIdField, "Name:", nameField,
                            "Phone:", phoneField, "Address:", addrField,
                            "Duty Schedule:", dutyField, "Section:", extra1
                    };
                } else {
                    msg = new Object[] {
                            "Employee ID:", empIdField, "Name:", nameField,
                            "Phone:", phoneField, "Address:", addrField,
                            "Duty Schedule:", dutyField, "Department:", extra1
                    };
                }

                int r = JOptionPane.showConfirmDialog(this, msg, "Add " + type, JOptionPane.OK_CANCEL_OPTION);
                if (r != JOptionPane.OK_OPTION)
                    return;

                String empId = empIdField.getText().trim();
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String addr = addrField.getText().trim();
                String duty = dutyField.getText().trim();

                if (empId.isEmpty() || name.isEmpty() || phone.isEmpty() || addr.isEmpty() || duty.isEmpty()) {
                    err("Employee ID, Name, Phone, Address and Duty Schedule cannot be empty!");
                    continue;
                }
                if (!phone.matches("\\d{10,13}")) {
                    err("Phone number must be 10-13 digits only, enter again ");
                    continue;
                }
                if (name.matches(".*\\d.*") || addr.matches(".*\\d.*")) {
                    err("Name and Address cannot contain numbers, enter again");
                    continue;
                }

                // person id ka concept
                String pid = "P" + (system.getCrewMembers().size() + 1);
                Crew c;

                if (type.equals("Pilot")) {
                    int yrs = 0;
                    try {
                        yrs = Integer.parseInt(extra2.getText().trim());
                    } catch (Exception ex) {
                    }
                    if (extra1.getText().trim().isEmpty()) {
                        err("License Number is required for Pilot!");
                        continue;
                    }
                    c = new Pilot(pid, name, phone, addr, empId, duty, extra1.getText().trim(), yrs);
                } else if (type.equals("CabinCrew")) {
                    if (extra1.getText().trim().isEmpty()) {
                        err("Section is required for Cabin Crew!");
                        continue;
                    }
                    c = new CabinCrew(pid, name, phone, addr, empId, duty, extra1.getText().trim());
                } else {
                    if (extra1.getText().trim().isEmpty()) {
                        err("Department is required for Ground Staff!");
                        continue;
                    }
                    c = new GroundStaff(pid, name, phone, addr, empId, duty, extra1.getText().trim());
                }

                system.addCrew(c);
                FileHandler.saveSystem(system);
                refresh.run();

                JOptionPane.showMessageDialog(this, type + " added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        });

        assignBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select a crew member.");
                return;
            }

            String eid = (String) model.getValueAt(row, 0);
            Crew chosen = null;

            for (Crew c : system.getCrewMembers()) {
                if (c.getEmployeeId().equals(eid)) {
                    chosen = c;
                    break;
                }
            }
            if (chosen == null)
                return;

            if (system.getFlights().isEmpty()) {
                err("No flights to assign.");
                return;
            }

            Flight f = (Flight) JOptionPane.showInputDialog(this, "Choose flight:", "Assign",
                    JOptionPane.QUESTION_MESSAGE, null,
                    system.getFlights().toArray(), system.getFlights().get(0));
            if (f == null)
                return;

            f.assignCrew(chosen);
            chosen.assignFlight(f);
            FileHandler.saveSystem(system);
            refresh.run();
        });

        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select a crew member.");
                return;
            }

            String eId = (String) model.getValueAt(row, 0);
            Crew chosen = null;

            for (Crew c : system.getCrewMembers()) {
                if (c.getEmployeeId().equals(eId)) {
                    chosen = c;
                    break;
                }
            }
            if (chosen == null)
                return;
            if (!chosen.getAssignedFlights().isEmpty()) {
                err("Cannot remove crew member as he is assigned to a flight. Unassign from flights first.");
                return;
            }

            int c = JOptionPane.showConfirmDialog(this, "Remove crew member" + chosen.getName() + "?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION)
                return;

            system.removeCrew(eId);
            FileHandler.saveSystem(system);
            refresh.run();
            JOptionPane.showMessageDialog(this, "Crew member removed successfully!", "Removed!!",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        refreshBtn.addActionListener(e -> refresh.run());

        JPanel buttons = new JPanel();
        buttons.add(addBtn);
        buttons.add(assignBtn);
        buttons.add(removeBtn);
        buttons.add(refreshBtn);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    // 5. Managing bookings (viewing, updating status, deleting)
    private JPanel BookingsTab() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = { "Booking ID", "User", "Flight", "Seat", "Date", "Amount", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);

        Runnable refresh = () -> {
            model.setRowCount(0);
            for (Booking b : system.getBookings()) {
                model.addRow(new Object[] { b.getBookingId(), b.getUser().getName(), b.getFlight().getFlightId(),
                        b.getSeat().getSeatNumber(), b.getBookingDate(), "Rs " + b.getTotalAmount(),
                        b.getStatus()
                });
            }
        };
        refresh.run();

        JButton viewBtn = new JButton("View Ticket");
        JButton cancelBtn = new JButton("Cancel & Refund");
        JButton refreshBtn = new JButton("Refresh");

        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select a booking first.");
                return;
            }

            String id = (String) model.getValueAt(row, 0);
            Booking b = system.findBooking(id);
            if (b == null || b.getTicket() == null) {
                err("No ticket available for this booking.");
                return;
            }

            JTextArea area = new JTextArea(b.getTicket().viewTicket());
            area.setEditable(false);
            area.setFont(new Font("Times New Roman", Font.PLAIN, 14));

            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Ticket Details - " + id,
                    JOptionPane.PLAIN_MESSAGE);
        });

        cancelBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select a booking.");
                return;
            }

            String id = (String) model.getValueAt(row, 0);
            Booking b = system.findBooking(id);
            if (b == null)
                return;
            if (!b.getStatus().equals("Confirmed")) {
                err("Already cancelled or not active.");
                return;
            }

            int c = JOptionPane.showConfirmDialog(this, "Cancel booking " + id + " and refund the user?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (c != JOptionPane.YES_OPTION)
                return;

            b.setStatus("Cancelled by Admin");
            b.getSeat().releaseSeat();

            if (b.getPayment() != null)
                b.getPayment().refund();
            system.addNotification(
                    new Notification(b.getUser(), "Booking " + id + " was cancelled by admin. Refund processed."));
            FileHandler.saveSystem(system);
            refresh.run();
            JOptionPane.showMessageDialog(this, "Booking cancelled and user refunded.", "Cancelled",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        refreshBtn.addActionListener(e -> refresh.run());

        JPanel buttons = new JPanel();
        buttons.add(viewBtn);
        buttons.add(cancelBtn);
        buttons.add(refreshBtn);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    // 6. Managing refund requests (viewing, approving/rejecting)
    private JPanel RefundsTab() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = { "Request ID", "Booking", "User", "Reason", "Date", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);

        Runnable refresh = () -> {
            model.setRowCount(0);
            for (RefundRequest r : system.getRefundRequests()) {
                model.addRow(new Object[] { r.getRequestId(),
                        r.getBooking().getBookingId(),
                        r.getBooking().getUser().getName(),
                        r.getReason(), r.getRequestDate(), r.getStatus()
                });
            }
        };
        refresh.run();
        JButton viewBtn = new JButton("View Booking Details");
        JButton approveBtn = new JButton("Approve");
        JButton rejectBtn = new JButton("Reject");
        JButton refreshBtn = new JButton("Refresh");

        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select a refund request first.");
                return;
            }

            String rid = (String) model.getValueAt(row, 0);
            RefundRequest req = system.getRefundRequest(rid);
            if (req == null)
                return;

            String details = "Request ID : " + req.getRequestId() + "\n" +
                    "Booking    : " + req.getBooking().getBookingId() + "\n" +
                    "User       : " + req.getBooking().getUser().getName() + "\n" +
                    "Reason     : " + req.getReason() + "\n" +
                    "Date       : " + req.getRequestDate() + "\n" +
                    "Status     : " + req.getStatus();

            JTextArea area = new JTextArea(details);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Refund Request Details",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        approveBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select a request.");
                return;
            }

            String rid = (String) model.getValueAt(row, 0);
            RefundRequest req = system.getRefundRequest(rid);
            if (req == null || !req.getStatus().equals("Pending")) {
                err("Request is not pending.");
                return;
            }

            req.approve();
            Booking b = req.getBooking();
            b.setStatus("Refunded");
            b.getSeat().releaseSeat();

            if (b.getPayment() != null)
                b.getPayment().refund();
            system.addNotification(
                    new Notification(b.getUser(), "Refund APPROVED for booking " + b.getBookingId() + "."));
            FileHandler.saveSystem(system);
            refresh.run();
            JOptionPane.showMessageDialog(this, "Refund approved and user notified.", "Approved",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        rejectBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                err("Select a request.");
                return;
            }

            String rid = (String) model.getValueAt(row, 0);
            RefundRequest req = system.getRefundRequest(rid);
            if (req == null || !req.getStatus().equals("Pending")) {
                err("Request is not pending.");
                return;
            }

            req.reject();
            req.getBooking().setStatus("Confirmed");

            system.addNotification(new Notification(req.getBooking().getUser(),
                    "Refund REJECTED for booking " + req.getBooking().getBookingId() + "."));
            FileHandler.saveSystem(system);
            refresh.run();
            JOptionPane.showMessageDialog(this, "Refund rejected and user notified.", "Rejected",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        refreshBtn.addActionListener(e -> refresh.run());

        JPanel buttons = new JPanel();
        buttons.add(viewBtn);
        buttons.add(approveBtn);
        buttons.add(rejectBtn);
        buttons.add(refreshBtn);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    // 7. Generating reports (bookings, flights, revenue, refunds)
    private JPanel ReportsTab() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        JPanel buttons = new JPanel();
        buttons.add(reportButton("Bookings", area, () -> bookingReportText()));
        buttons.add(reportButton("Flights", area, () -> flightReportText()));
        buttons.add(reportButton("Revenue", area, () -> revenueReportText()));
        buttons.add(reportButton("Refunds", area, () -> refundReportText()));
        buttons.add(reportButton("Full Report", area, () -> fullReportText()));
        buttonPanel.add(buttons);
        p.add(buttonPanel, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        area.setText(fullReportText());
        return p;
    }

    private JButton reportButton(String label, JTextArea area, java.util.function.Supplier<String> producer) {
        JButton b = new JButton(label);
        b.addActionListener(e -> {
            area.setText(producer.get());
            area.setCaretPosition(0);
        });
        return b;
    }

    private String bookingReportText() {
        StringBuilder sb = new StringBuilder("____________BOOKINGS REPORT____________\n\n");
        if (system.getBookings().isEmpty())
            return sb.append("No bookings found yet.\n").toString();

        for (Booking b : system.getBookings()) {
            sb.append(b.getBookingDetails()).append("\n");
        }
        sb.append("\nTotal Bookings: ").append(system.getBookings().size());
        return sb.toString();
    }

    private String flightReportText() {
        StringBuilder sb = new StringBuilder("____________FLIGHTS REPORT____________\n\n");
        if (system.getFlights().isEmpty())
            return sb.append("No flights available.\n").toString();

        for (Flight f : system.getFlights()) {
            int booked = f.getCapacity() - f.getAvailableSeatsCount();
            double occ = f.getCapacity() == 0 ? 0 : ((double) booked / f.getCapacity()) * 100;
            sb.append(f).append("\n");
            sb.append(String.format("    Booked: %d/%d (%.1f%% occupied)%n", booked, f.getCapacity(), occ));
        }
        return sb.toString();
    }

    private String revenueReportText() {
        double total = 0, refunded = 0;
        for (Booking b : system.getBookings()) {
            if ("Confirmed".equals(b.getStatus()))
                total += b.getTotalAmount();
            else if ("Refunded".equals(b.getStatus()) || "Cancelled by Admin".equals(b.getStatus()))
                refunded += b.getTotalAmount();
        }
        return "____________REVENUE REPORT____________\n\n" +
                "Confirmed Revenue   : Rs " + total + "\n" +
                "Refunded Amount     : Rs " + refunded + "\n" +
                "──────────────────────────────\n" +
                "NET REVENUE         : Rs " + (total - refunded);
    }

    private String refundReportText() {
        StringBuilder sb = new StringBuilder("____________REFUNDS REPORT____________\n\n");
        if (system.getRefundRequests().isEmpty())
            return sb.append("No refund requests yet.\n").toString();

        int p = 0, a = 0, j = 0;
        for (RefundRequest r : system.getRefundRequests()) {
            sb.append(r).append("\n");
            switch (r.getStatus()) {
                case "Pending":
                    p++;
                    break;
                case "Approved":
                    a++;
                    break;
                case "Rejected":
                    j++;
                    break;
            }
        }
        sb.append("\nSummary → Pending: ").append(p)
                .append(" | Approved: ").append(a)
                .append(" | Rejected: ").append(j);
        return sb.toString();
    }

    private String fullReportText() {
        return "___________________FULL SYSTEM REPORT____________________\n\n" +
                "Users      : " + system.getUsers().size() + "\n" +
                "Airports   : " + system.getAirports().size() + "\n" +
                "Aircrafts  : " + system.getAircrafts().size() + "\n" +
                "Flights    : " + system.getFlights().size() + "\n" +
                "Crew       : " + system.getCrewMembers().size() + "\n" +
                "Bookings   : " + system.getBookings().size() + "\n" +
                "Refunds    : " + system.getRefundRequests().size() + "\n\n" +
                bookingReportText() + "\n\n" +
                flightReportText() + "\n\n" +
                revenueReportText() + "\n\n" +
                refundReportText();
    }

    // other methods. .. like update admin profile, delete account, error message
    // dialogue, etc.
    private void updateAdminProfile() {
        JTextField nameField = new JTextField(admin.getName());
        JTextField phoneField = new JTextField(admin.getPhoneNumber() != null ? admin.getPhoneNumber() : "");
        JTextField addressField = new JTextField(admin.getAddress() != null ? admin.getAddress() : "");
        JTextField emailField = new JTextField(admin.getEmail());
        JPasswordField passField = new JPasswordField(admin.getPassword());

        Object[] msg = {
                "Name:", nameField,
                "Phone:", phoneField,
                "Address:", addressField,
                "Email:", emailField,
                "New Password:", passField
        };

        int result = JOptionPane.showConfirmDialog(this, msg, "Update Admin Profile", JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION)
            return;

        String newName = nameField.getText().trim();
        String newPhone = phoneField.getText().trim();
        String newAddr = addressField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newPass = new String(passField.getPassword()).trim();

        if (newName.isEmpty() || newEmail.isEmpty()) {
            err("Name and Email cannot be empty.");
            return;
        }
        // Existing method call (Account class se)
        admin.updateProfile(newName, newPhone, newAddr);

        // Extra fields update
        admin.setEmail(newEmail);
        if (!newPass.isEmpty() && newPass.length() >= 4) {
            admin.setPassword(newPass);
        }

        FileHandler.saveSystem(system);

        // Refresh title
        setTitle("Admin Dashboard - " + admin.getName());
        JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteAdminAccount() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "You are about to DELETE the only Admin account.\n" +
                        "Are you ABSOLUTELY sure you want to delete this admin account?",
                "Delete Admin Account",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION)
            return;

        // Delete admin from system
        system.setAdmin(null); // Important: system se admin remove
        FileHandler.saveSystem(system);

        JOptionPane.showMessageDialog(this, "Admin has been deleted. \nYou are now logged out.", "Account Deleted",
                JOptionPane.INFORMATION_MESSAGE);

        admin.logout();
        startFrame.setVisible(true);
        dispose();
    }

    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

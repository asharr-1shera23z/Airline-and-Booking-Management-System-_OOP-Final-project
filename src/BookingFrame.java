// BookingFrame.java
// Dedicated booking window. Step-by-step: pick flight, pick seat, enter
// luggage, choose payment, confirm.

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class BookingFrame extends JFrame {

    private User user;
    private AirlineSystem system;
    private UserDashboardFrame parent;

    private JTable flightTable;
    private DefaultTableModel flightModel;
    private JComboBox<String> seatCombo;
    private JTextField luggageField;
    private JComboBox<String> paymentCombo;
    private JLabel totalLabel;

    private Flight selectedFlight;

    public BookingFrame(User user, AirlineSystem system, UserDashboardFrame parent) {
        this.user = user;
        this.system = system;
        this.parent = parent;

        setTitle("Book Your Ticket " + user.getName());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Bookinggui();
    }

    private void Bookinggui() {
        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        main.setBackground(Color.WHITE);

        Color darkBlue = new Color(0, 0, 139);
        Color green = new Color(46, 125, 50);
        Color red = new Color(220, 0, 0);

        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(Color.WHITE);

        // ---------- Top: flight selection ----------
        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(darkBlue, 2),
                "1. Select a Flight", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Times New Roman", Font.BOLD, 16), darkBlue));

        flightModel = new DefaultTableModel(
                new String[] { "Flight ID", "From", "To", "Departure", "Fare", "Available Seats" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        flightTable = new JTable(flightModel);
        flightTable.setRowHeight(30);
        flightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScroll = new JScrollPane(flightTable);
        tableScroll.setPreferredSize(new Dimension(900, 280));
        loadFlights();

        flightTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                onFlightPicked();
        });

        top.add(new JScrollPane(flightTable), BorderLayout.CENTER);
        
        // ---------- Middle: booking details ----------
        JPanel mid = new JPanel(new GridLayout() );
        mid.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(darkBlue, 2),
                "2. Booking Details", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Times New Roman", Font.BOLD, 16), darkBlue));

        mid.add(new JLabel("Available Seats:"));
        seatCombo = new JComboBox<>();
        seatCombo.setPreferredSize(new Dimension(220, 35));
        mid.add(seatCombo);

        mid.add(new JLabel("Luggage Weight (kg):"));
        luggageField = new JTextField("0");
        mid.add(luggageField);

        mid.add(new JLabel("Payment Method:"));
        paymentCombo = new JComboBox<>(new String[] { "Card", "Cash", "Bank Transfer" });
        mid.add(paymentCombo);

        JButton calcBtn = new JButton("Calculate Total");
        calcBtn.setBackground(new Color(30, 60, 120));
        calcBtn.setFont(new Font("Times New Roman", Font.BOLD, 8));
        mid.add(calcBtn);

        totalLabel = new JLabel("Total_Rs:", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Times New Roman", Font.BOLD, 10));
        totalLabel.setForeground(darkBlue);
        mid.add(totalLabel);

        calcBtn.addActionListener(e -> updateTotal());
        centerPanel.add(top, BorderLayout.CENTER);
        centerPanel.add(mid, BorderLayout.SOUTH);
        
        main.add(centerPanel, BorderLayout.CENTER);

        // ---------- Bottom: confirm / cancel ----------
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        bottom.setBackground(Color.WHITE);

        JButton confirm = new JButton("Confirm Booking");
        confirm.setFont(new Font("Times New Roman", Font.BOLD, 14));
        confirm.setBackground(green);
        JButton cancel = new JButton("Cancel");
        cancel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        cancel.setBackground(red);

        confirm.addActionListener(e -> doConfirm());
        cancel.addActionListener(e -> dispose());

        bottom.add(confirm);
        bottom.add(cancel);
        main.add(bottom, BorderLayout.SOUTH);

        setContentPane(main);
    }

    private void loadFlights() {
        flightModel.setRowCount(0);
        for (Flight f : system.searchFlights()) {
            if (f.getAvailableSeatsCount() == 0)
                continue;

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
            System.out.println("Invalid luggage weight entered.");
            return;
        }
        if (luggage < 0 || luggage > 40) {
            JOptionPane.showMessageDialog(this, "Luggage weight must be between 0 and 40 kg!", "Limit Exceeded",
                    JOptionPane.WARNING_MESSAGE);
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
        if (seat == null)
            return;

        double luggage = parseDouble(luggageField.getText(), 0);
        double total = user.calculateFare(selectedFlight, luggage);

        PaymentMethod method = pickPaymentMethod();
        if (method == null)
            return; // user cancelled

        Booking booking = new Booking(user, selectedFlight, seat, luggage);
        booking.setTotalAmount(total);
        Payment pay = new Payment(total, method);

        if (!pay.processPayment()) {
            JOptionPane.showMessageDialog(this, "Payment failed. Booking aborted.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        booking.setPayment(pay);
        booking.confirmBooking();

        // Save to BOTH lists - same object reference
        user.addBooking(booking);
        system.addBooking(booking);

        system.addNotification(new Notification(user,
                "Booking " + booking.getBookingId() + " confirmed."));

        FileHandler.saveSystem(system);

        JOptionPane.showMessageDialog(this,
                "Booking confirmed!\n\n" + booking.getTicket().viewTicket(),
                "Success", JOptionPane.INFORMATION_MESSAGE);

        if (parent != null)
            parent.refreshAllTables();
        dispose();
    }

    private Seat pickedSeat() {
        String s = (String) seatCombo.getSelectedItem();
        if (s == null)
            return null;
        try {
            int num = Integer.parseInt(s.split(" ")[1]);
            for (Seat seat : selectedFlight.getSeats()) {
                if (seat.getSeatNumber() == num)
                    return seat;
            }
        } catch (Exception ignored) {
        }
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

            case "Bank Transfer": {
                String acc = JOptionPane.showInputDialog(this, "Account Number:");
                if (acc == null || acc.trim().isEmpty()) return null;

                String bk = JOptionPane.showInputDialog(this, "Bank Name:");
                if (bk == null || bk.trim().isEmpty()) return null;

                return new BankTransferPayment(acc.trim(), bk.trim());
            }
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
}

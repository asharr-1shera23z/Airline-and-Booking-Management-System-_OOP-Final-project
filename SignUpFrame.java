// SignUpFrame.java
// Sign-up form. Different fields for user vs admin (admin needs secret key).

import java.awt.*;
import javax.swing.*;

public class SignUpFrame extends JFrame {

    private AirlineSystem system;
    private String role;
    private StartFrame parent;

    private JTextField usernameField, nameField, emailField, phoneField,
                       addressField, cnicField, cityField, secretKeyField;
    private JPasswordField passwordField;

    private static final String ADMIN_SECRET_KEY = "AERO2025";

    // constructor
    public SignUpFrame(AirlineSystem system, String role, StartFrame parent) {
        this.system = system;
        this.role   = role;
        this.parent = parent;

        setTitle((role.equals("admin") ? "Admin SignUp" : "User SignUp"));  
        setSize(450, role.equals("admin") ? 520 : 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                parent.setVisible(true);
            }
        });

        signupgui();
    }

        // gui method
    private void signupgui() {
        JPanel main = new JPanel(new BorderLayout(8, 8));
        main.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Title
        JLabel title = new JLabel((role.equals("admin") ? "______Admin SignUp______" : "______User SignUp______"),SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 18));
        title.setForeground(new Color(30, 60, 120));
        main.add(title, BorderLayout.NORTH);

        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(3, 8, 3, 8);
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.HORIZONTAL;

        int row = 0;

        if (role.equals("admin")) {
            addRow(form, g, row++, "Secret Key:", secretKeyField = new JTextField());
        }
        addRow(form, g, row++, "Username:", usernameField = new JTextField());
        addRow(form, g, row++, "Password:", passwordField = new JPasswordField());
        addRow(form, g, row++, "Full Name:", nameField  = new JTextField());
        addRow(form, g, row++, "Email:",     emailField = new JTextField());
        addRow(form, g, row++, "Phone:",     phoneField = new JTextField());
        addRow(form, g, row++, "Address:",   addressField = new JTextField());

        if (role.equals("user")) {
            addRow(form, g, row++, "CNIC:",  cnicField = new JTextField());
            addRow(form, g, row++, "City:",  cityField = new JTextField());
        }

        main.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton signUpBtn = new JButton("SignUp");
        signUpBtn.addActionListener(e -> doSignUp());
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> { parent.setVisible(true); dispose(); });
        buttons.add(signUpBtn);
        buttons.add(backBtn);
        main.add(buttons, BorderLayout.SOUTH);

        setContentPane(main);
    }

    private void addRow(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0; g.gridy = row; g.weightx = 0.3;
        form.add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 0.7;
        form.add(field, g);
    }

    // sign up validation method
    private void doSignUp() {
        String u    = usernameField.getText().trim();
        String p    = new String(passwordField.getPassword()).trim();
        String n    = nameField.getText().trim();
        String em   = emailField.getText().trim();
        String ph   = phoneField.getText().trim();
        String addr = addressField.getText().trim();

        if (u.isEmpty() || p.isEmpty() || n.isEmpty() || em.isEmpty() || ph.isEmpty() || addr.isEmpty()) {
            err("Kindly fill all required fields.");
            return;
        }
        if (p.length() < 4) { err("Pass must be at least 4 characters."); return; }
        if (!em.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
        err("Invalid email format.");
        return;
        }      
        
        if (!ph.matches("\\d{10,15}")) {
        err("Phone must be 10-15 digits.\nExample: 03001234567");
        return;
        }   
        // Username uniqueness check
        if (usernameTaken(u)) { err("Username already exists."); return; }

        if (role.equals("admin")) {
            
            if (system.getAdmin() != null) {
                err("An admin already exists. Only one admin allowed.");
                return;
            }
            String key = secretKeyField.getText().trim();
            if (!key.equals(ADMIN_SECRET_KEY)) {
                err("Wrong secret key.");
                return;
            }
            Admin admin = new Admin( n, ph, addr, u, p, em, key);
            system.setAdmin(admin);
            JOptionPane.showMessageDialog(this, "Admin account created. Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String cnic = cnicField.getText().trim();
            String city = cityField.getText().trim();

        if (cnic.isEmpty() || city.isEmpty()) {
            err("CNIC and City are required for users!");
            return;
        }
        if (!cnic.matches("\\d{13}")) {
            err("CNIC must be exactly 13 digits.\nExample: 1234567890123");
            return;
        }
            
            User user = new User( n, ph, addr, u, p, em, cnic, city);
            system.addUser(user);
            JOptionPane.showMessageDialog(this, "Account created. Please log in.",  "Success", JOptionPane.INFORMATION_MESSAGE);
        }

        FileHandler.saveSystem(system);
        parent.setVisible(true);
        dispose();
    }

    private boolean usernameTaken(String u) {
        for (User user : system.getUsers()) {
            if (user.getUsername().equalsIgnoreCase(u)) return true;
        }
        return system.getAdmin() != null
            && system.getAdmin().getUsername().equalsIgnoreCase(u);
    }

    private void err(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

// SignUpFrame.java
// Sign-up form. Different fields for user vs admin (admin needs secret key).

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class SignUpFrame extends JFrame {

    private AirlineSystem system;
    private String role;
    private StartFrame parent;

    private JTextField usernameField, nameField, emailField, phoneField, addressField, cnicField, cityField, secretKeyField;
    private JPasswordField passwordField;

    private static final String ADMIN_SECRET_KEY = "AERO2025";

    // constructor
    public SignUpFrame(AirlineSystem system, String role, StartFrame parent) {
        this.system = system;
        this.role   = role;
        this.parent = parent;

        setTitle((role.equals("admin") ? "Admin SignUp" : "User SignUp"));  
        setSize(480, role.equals("admin") ? 600 : 640);
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
        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(Color.WHITE);
        main.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
 
        JLabel title = new JLabel(role.equals("admin") ? "Admin SignUp" : "User SignUp", SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 28));
        title.setForeground(new Color(0, 0, 139)); // Dark Blue
        main.add(title, BorderLayout.NORTH);

        // Title
 
        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
       g.insets = new Insets(6, 8, 6, 8);     // Balanced spacing
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        if (role.equals("admin")) {
            addRow(form, g, row++, "Secret Key:", secretKeyField = new JTextField());
        }

        addRow(form, g, row++, "Username:", usernameField = new JTextField());
        addRow(form, g, row++, "Password:", passwordField = new JPasswordField());
        addRow(form, g, row++, "Full Name:", nameField = new JTextField());
        addRow(form, g, row++, "Email:", emailField = new JTextField());
        addRow(form, g, row++, "Phone:", phoneField = new JTextField());
        addRow(form, g, row++, "Address:", addressField = new JTextField());

        if (role.equals("user")) {
            addRow(form, g, row++, "CNIC:", cnicField = new JTextField());
            addRow(form, g, row++, "City:", cityField = new JTextField());
        }

        main.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 12));
        buttons.setBackground(Color.WHITE);
        // Green SignUp Button
        JButton signUpBtn = new JButton("Sign Up") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 128, 0));  // Green
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleButton(signUpBtn, new Color(0, 128, 0), 17);

        // Red Back Button
        JButton backBtn = new JButton("Back") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(220, 50, 50));  // Red
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleButton(backBtn, new Color(220, 50, 50), 17);

        signUpBtn.addActionListener(e -> doSignUp());
        backBtn.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });

        buttons.add(signUpBtn);
        buttons.add(backBtn);
        main.add(buttons, BorderLayout.SOUTH);

        setContentPane(main);
    }

    private void addRow(JPanel form, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0; 
        g.gridy = row; 
        g.weightx = 0.28;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        form.add(lbl, g);

        g.gridx = 1; 
        g.weightx = 0.72;
        
        // Apply thick blue border to all input fields
        if (field instanceof JTextField || field instanceof JPasswordField) {
            field.setFont(new Font("Times New Roman", Font.PLAIN, 15));
            field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(0, 0, 139), 2),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            field.setPreferredSize(new Dimension(300, 40 ));
        }
        
        form.add(field, g);
    }

    private void styleButton(JButton btn, Color bgColor, int fontSize) {
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Times New Roman", Font.BOLD, fontSize));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 35));
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

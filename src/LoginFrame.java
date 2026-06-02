// LoginFrame.java
// Login window. Same frame is used for both user and admin (parameterized by role).
// On success, opens the right dashboard frame.

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;


public class LoginFrame extends JFrame {

    private AirlineSystem system;
    private String role;
    private StartFrame parent; // prevous frame ka reference to go back to it when this closes
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(AirlineSystem system, String role, StartFrame parent) {
        this.system = system;
        this.role   = role;
        this.parent = parent;

        setTitle((role.equals("admin")) ? "Admin Login" : "User Login");
             setSize(440, 400);// ye window ky sizes
        setLocationRelativeTo(null); // centre on screen pr open hoga iss fucntion se 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //dispose se onlyloginframe close hogi not the whole app that why iu write dispose instead of close

        // Going back to start frame when this closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                parent.setVisible(true);
            }
        });

        loginstart();
    }

    private void loginstart() {
        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(Color.WHITE);
        main.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel(role.equals("admin") ? "Admin Login" : "User Login", SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 28));
        title.setForeground(new Color(0, 0, 139)); // Dark Blue
        main.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 6, 10));
        form.setBackground(Color.WHITE);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        usernameField = new JTextField();
        usernameField.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 0, 139), 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 0, 139), 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        form.add(userLabel);
        form.add(usernameField);
        form.add(passLabel);
        form.add(passwordField);

        main.add(form, BorderLayout.CENTER);

       
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttons.setBackground(Color.WHITE);

        // Login Button (Dark Blue)
       JButton loginBtn = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 128, 0));  // Green
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleButton(loginBtn, new Color(0, 128, 0), 19);

        // Back Button (Red)
        JButton backBtn = new JButton("Back") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(220, 50, 50)); // Red
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleButton(backBtn, new Color(220, 50, 50), 19);

        loginBtn.addActionListener(e -> doLogin());
        backBtn.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });

        buttons.add(loginBtn);
        buttons.add(backBtn);
        main.add(buttons, BorderLayout.SOUTH);

        setContentPane(main);

        main.add(buttons, BorderLayout.SOUTH);

        setContentPane(main);

        // Press Enter in password field = login
        passwordField.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword()).trim();

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter again",
                "Missing! ", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (u.length() < 3) {
         err("Username must be at least 3 characters.");
         return;
        }
        if (p.length() < 4) {
         err("Password must be at least 4 characters.");
         return;    
        }

        Account acc = null; 
    
        try {
            if (role.equals("admin")) {
                Admin a = system.getAdmin();
                if (a != null && a.login(u, p)) acc = a;
            } 
            else if  ("user".equalsIgnoreCase(role)) { 
                
                if (system.getUsers() == null || system.getUsers().isEmpty()) {
                    JOptionPane.showMessageDialog(this,"No user account found. Please sign up first.","User Not Found", JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                for (User user : system.getUsers()) {
                    if (user != null && user.login(u, p)) { acc = user; break; } 
                }
            }

            // if wrong ones
            if (acc == null) {
                JOptionPane.showMessageDialog(this, "Wrong username or password ! Enter again","Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Success → open dashboard
            if (acc instanceof Admin) {
                new AdminDashboardFrame((Admin) acc, system, parent).setVisible(true);
            } else {
                new UserDashboardFrame((User) acc, system, parent).setVisible(true);
            }
            dispose(); // close login frame after opening dashboard
        } catch (HeadlessException ex) {


            }
        }

    private void err(String msg) {
    JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
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

}

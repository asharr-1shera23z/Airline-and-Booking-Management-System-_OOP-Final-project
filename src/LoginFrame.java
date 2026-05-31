// LoginFrame.java
// Login window. Same frame is used for both user and admin (parameterized by role).
// On success, opens the right dashboard frame.

import java.awt.*;
import javax.swing.*;


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
        setSize(400, 280); // ye window ky sizes
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
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel((role.equals("admin") ? "______Admin Login______" : "______User Login______"), SwingConstants.CENTER); // title of the login frame
        title.setFont(new Font("Times New Roman", Font.BOLD, 18));
        title.setForeground(new Color(30, 60, 120));
        main.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10)); 
        form.add(new JLabel("Username:"));
        usernameField = new JTextField();
        form.add(usernameField);
        form.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        form.add(passwordField);
        main.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> doLogin()); // login button pr click krne se doLogin function call hoga
        JButton backBtn = new JButton("Back");
        
        backBtn.addActionListener(e -> {
            parent.setVisible(true); // back button pr click krne se previous frame visible hoga
            dispose(); // aur current login frame close ho jayega
        });
    
        buttons.add(loginBtn);
        buttons.add(backBtn);
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
}

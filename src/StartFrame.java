import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StartFrame extends JFrame {

    private AirlineSystem system;

    public StartFrame(AirlineSystem system) {
        this.system = system;

        setTitle("Airline Management & Booking System");
        setSize(600, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                FileHandler.saveSystem(system);
            }
        });

        startUI();
    }

    private void startUI() {
        JPanel main = new JPanel(new BorderLayout(10, 15));
        main.setBackground(Color.WHITE);             
        main.setBorder(BorderFactory.createEmptyBorder(25, 45, 18, 45));

        // ---------- TITLE AREA ----------
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel welcome = new JLabel("WELCOME TO", SwingConstants.CENTER);
        welcome.setFont(new Font("Times New Roman", Font.BOLD, 22));
        welcome.setForeground(Color.DARK_GRAY);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Airline Management & Booking System", SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD, 27));
        title.setForeground(new Color(0, 0, 139));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

 

        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(welcome);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(title);


        main.add(titlePanel, BorderLayout.NORTH);

        // ---------- CENTER BUTTON AREA ----------
        JPanel center = new JPanel(new GridLayout(5, 1, 0, 14));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(18, 80, 25, 80));

        
        JLabel subtitle = new JLabel("Select the option", SwingConstants.CENTER);
        subtitle.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        subtitle.setForeground(Color.DARK_GRAY);
        center.add(subtitle);

        JButton userLoginButton = makeButton("User Login");
        userLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openLogin("user");
            }
        });

        JButton userSignupButton = makeButton("User SignUp");
        userSignupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openSignUp("user");
            }
        });

        JButton adminLoginButton = makeButton("Admin Login");
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openLogin("admin");
            }
        });

        JButton adminSignupButton = makeButton("Admin SignUp");
        adminSignupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openSignUp("admin");
            }
        });

        center.add(userLoginButton);
        center.add(userSignupButton);
        center.add(adminLoginButton);
        center.add(adminSignupButton);

        main.add(center, BorderLayout.CENTER);

        // ---------- FOOTER ----------
        JLabel by = new JLabel("By", SwingConstants.CENTER);
        by.setFont(new Font("Times New Roman", Font.ITALIC, 14));
        by.setForeground(Color.GRAY);
        main.add(by, BorderLayout.SOUTH);

        JLabel footer = new JLabel("Faizan Ahmad, M. Ashar Sheraz, M. Usman Azeem, Zarak Khan", SwingConstants.CENTER);
        footer.setFont(new Font("Times New Roman", Font.ITALIC, 16));
        footer.setForeground(Color.GRAY);
        main.add(footer, BorderLayout.SOUTH);

        setContentPane(main);
    }

    private JButton makeButton(String text) {
        JButton button = new RoundedButton(text);
        button.setFont(new Font("Times New Roman", Font.BOLD, 17));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setPreferredSize(new Dimension(200, 40)); 
        return button;
    }

    // Simple rounded grey button
    private static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(new Color(200, 200, 200));
            } else if (getModel().isRollover()) {
                g2.setColor(new Color(220, 220, 220));
            } else {
                g2.setColor(new Color(235, 235, 235));
            }

            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
            g2.setColor(new Color(170, 170, 170));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
            g2.dispose();

            super.paintComponent(g);
        }
    }

    private void openLogin(String role) {
        new LoginFrame(system, role, this).setVisible(true);
        setVisible(false);
    }

    private void openSignUp(String role) {
        new SignUpFrame(system, role, this).setVisible(true);
        setVisible(false);
    }
}

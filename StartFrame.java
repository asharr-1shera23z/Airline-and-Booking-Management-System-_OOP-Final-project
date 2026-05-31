// StartFrame.java (Updated - Better Colors + Black Text)
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StartFrame extends JFrame {

    private AirlineSystem system;

    public StartFrame(AirlineSystem system) {
        this.system = system;

        setTitle("Airline Management & Booking System");
        setSize(580, 520);                   
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
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(new Color(245, 248, 252));   

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel welcome = new JLabel( "WELCOME TO", SwingConstants.CENTER);
        welcome.setFont(new Font("Times New Roman", Font.BOLD, 22)   );
        welcome.setForeground(new Color(30, 60, 120));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("_________Airline Management & Booking System_________", SwingConstants.CENTER);
        title.setFont(new Font("Times New Roman", Font.BOLD,  19));
        title.setForeground(new Color(0, 51, 102));     
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
  
        titlePanel.add(Box.createVerticalStrut(20));
        titlePanel.add(welcome);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(title);

        main.add(titlePanel, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Choose an option", SwingConstants.CENTER);
        subtitle.setFont(new Font("Times New Roman", Font.PLAIN, 19));

        JPanel center = new JPanel(new GridLayout(5, 1, 12, 12));
        center.setBorder(BorderFactory.createEmptyBorder(10, 60, 40, 60));
        center.setOpaque(false);
        center.add(subtitle);

            // USERS BUTTONS
        JButton userLoginButton = makeButton("User Login");
        userLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openLogin("user");
            }
        } );
      
        JButton userSignupButton = makeButton("User SignUp");
        userSignupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openSignUp("user");
                }
            } );
            
            // ADMINS BUTTONS
        JButton adminLoginButton = makeButton("Admin Login");
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openLogin("admin");
            }
        }  );

        JButton adminSignupButton = makeButton("Admin SignUp");
        adminSignupButton.addActionListener(new ActionListener() {
        @Override 
        public void actionPerformed(java.awt.event.ActionEvent e) {
            openSignUp("admin");
        }
        } );

        center.add(userLoginButton);
        center.add(userSignupButton);
        center.add(adminLoginButton);
        center.add(adminSignupButton);
        main.add(center, BorderLayout.CENTER);

        JLabel footer = new JLabel("BY     Ashar    Usman     Zarak     Faizan", SwingConstants.CENTER);
        footer.setFont(new Font("Times New Roman", Font.ITALIC, 17));
        footer.setForeground(Color.GRAY);
        main.add(footer, BorderLayout.SOUTH);

        setContentPane(main);
    }

    private JButton makeButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setBackground(new Color(0, 0, 139));
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
            BorderFactory.createEmptyBorder(12, 0, 12, 0)
        ));

        return button;
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
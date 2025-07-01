import Entities.Account;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

public class RegisterFrame extends JFrame {
    private JTextField nameField;
    private JPasswordField passField, confirmPassField;
    private JRadioButton customerRadio, adminRadio;
    private ButtonGroup userTypeGroup;

    public RegisterFrame() {
        super("Account Registration");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 180, 60));

        // Title
        JLabel titleLabel = new JLabel("CREATE NEW ACCOUNT");
        titleLabel.setBounds(150, 30, 300, 40);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);
        panel.add(titleLabel);

        // Username
        JLabel nameLabel = new JLabel("Username:");
        nameLabel.setBounds(150, 100, 100, 25);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(nameLabel);
        
        nameField = new JTextField();
        nameField.setBounds(250, 100, 200, 30);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(nameField);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(150, 150, 100, 25);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(passLabel);
        
        passField = new JPasswordField();
        passField.setBounds(250, 150, 200, 30);
        passField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(passField);

        // Confirm Password
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setBounds(100, 200, 150, 25);
        confirmPassLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(confirmPassLabel);
        
        confirmPassField = new JPasswordField();
        confirmPassField.setBounds(250, 200, 200, 30);
        confirmPassField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(confirmPassField);

        // User Type
        JLabel userTypeLabel = new JLabel("Register As:");
        userTypeLabel.setBounds(150, 250, 100, 25);
        userTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(userTypeLabel);

        customerRadio = new JRadioButton("Customer", true);
        customerRadio.setBounds(250, 250, 100, 25);
        customerRadio.setBackground(panel.getBackground());
        customerRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(customerRadio);

        adminRadio = new JRadioButton("Admin");
        adminRadio.setBounds(350, 250, 100, 25);
        adminRadio.setBackground(panel.getBackground());
        adminRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(adminRadio);

        userTypeGroup = new ButtonGroup();
        userTypeGroup.add(customerRadio);
        userTypeGroup.add(adminRadio);

        // Buttons
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(200, 300, 100, 35);
        registerButton.setBackground(Color.GREEN);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.addActionListener(e -> registerUser());
        panel.add(registerButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(320, 300, 100, 35);
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(e -> {
            dispose();
            new Frame().setVisible(true);
        });
        panel.add(backButton);

        add(panel);
    }

    private void registerUser() {
        String username = nameField.getText().trim();
        String password = new String(passField.getPassword());
        String confirmPassword = new String(confirmPassField.getPassword());
        String userType = customerRadio.isSelected() ? "customer" : "admin";

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Account newAccount = new Account(username, password, userType);
        if (newAccount.addAccount()) {
            JOptionPane.showMessageDialog(this, 
                "Registration Successful!\nUsername: " + username + 
                "\nUser Type: " + userType, "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new Frame().setVisible(true);
        }
    }
}
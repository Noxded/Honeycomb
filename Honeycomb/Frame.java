
import Entities.Account;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Frame extends JFrame implements ActionListener {
    JLabel nameLabel, passLabel, imgLabel, loginAsLabel;
    JButton login, signUp, exitButton;
    JRadioButton customerRadio, adminRadio;
    ButtonGroup loginGroup;
    JTextField textField;
    JPasswordField passwordField;
    JPanel panel, formPanel;

    public Frame() {
        super("Honeycomb");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Font font = new Font("Segoe UI", Font.BOLD, 16);
        panel = new JPanel(null);
        panel.setBackground(new Color(26, 26, 26));

        // Background Image
        ImageIcon bee = new ImageIcon("Pics\\bee.jpg");
        Image scaledImage = bee.getImage().getScaledInstance(450, 500, Image.SCALE_SMOOTH);
        imgLabel = new JLabel(new ImageIcon(scaledImage));
        imgLabel.setBounds(0, 0, 450, 500);
        panel.add(imgLabel);

        // Form Panel
        formPanel = new JPanel(null);
        formPanel.setBounds(450, 0, 450, 500);
        formPanel.setBackground(new Color(255, 179, 0));

        // Username
        nameLabel = new JLabel("Username:");
        nameLabel.setFont(font);
        nameLabel.setBounds(60, 80, 100, 25);
        nameLabel.setForeground(Color.BLACK);
        formPanel.add(nameLabel);

        textField = new JTextField();
        textField.setFont(font);
        textField.setBounds(180, 80, 200, 30);
        textField.setBackground(new Color(253, 246, 227));
        textField.setForeground(Color.BLACK);
        formPanel.add(textField);

        // Password
        passLabel = new JLabel("Password:");
        passLabel.setFont(font);
        passLabel.setBounds(60, 130, 100, 25);
        passLabel.setForeground(Color.BLACK);
        formPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(font);
        passwordField.setBounds(180, 130, 200, 30);
        passwordField.setBackground(new Color(253, 246, 227));
        passwordField.setForeground(Color.BLACK);
        formPanel.add(passwordField);

        // User Type
        loginAsLabel = new JLabel("Login As:");
        loginAsLabel.setFont(font);
        loginAsLabel.setBounds(60, 180, 100, 25);
        loginAsLabel.setForeground(Color.BLACK);
        formPanel.add(loginAsLabel);

        customerRadio = new JRadioButton("Customer", true);
        customerRadio.setFont(font);
        customerRadio.setBounds(180, 180, 100, 25);
        customerRadio.setBackground(formPanel.getBackground());
        customerRadio.setForeground(Color.BLACK);
        customerRadio.setFocusPainted(false);
        formPanel.add(customerRadio);

        adminRadio = new JRadioButton("Admin");
        adminRadio.setFont(font);
        adminRadio.setBounds(280, 180, 100, 25);
        adminRadio.setBackground(formPanel.getBackground());
        adminRadio.setForeground(Color.BLACK);
        adminRadio.setFocusPainted(false);
        formPanel.add(adminRadio);

        loginGroup = new ButtonGroup();
        loginGroup.add(customerRadio);
        loginGroup.add(adminRadio);

        // Buttons
        login = new JButton("Login");
        login.setFont(font);
        login.setBounds(60, 240, 100, 35);
        login.setBackground(new Color(0, 200, 83));
        login.setForeground(Color.WHITE);
        login.setFocusPainted(false);
        login.setBorder(BorderFactory.createEmptyBorder());
        login.addActionListener(this);
        formPanel.add(login);

        signUp = new JButton("Sign Up");
        signUp.setFont(font);
        signUp.setBounds(180, 240, 100, 35);
        signUp.setBackground(new Color(41, 121, 255));
        signUp.setForeground(Color.WHITE);
        signUp.setFocusPainted(false);
        signUp.setBorder(BorderFactory.createEmptyBorder());
        signUp.addActionListener(this);
        formPanel.add(signUp);

        exitButton = new JButton("Exit");
        exitButton.setFont(font);
        exitButton.setBounds(300, 240, 80, 35);
        exitButton.setBackground(new Color(213, 0, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createEmptyBorder());
        exitButton.addActionListener(this);
        formPanel.add(exitButton);

        panel.add(formPanel);
        add(panel);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == signUp) {
            setVisible(false);
            new RegisterFrame().setVisible(true);
            return;
        }

        if (ae.getSource() == login) {
            String username = textField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter both username and password");
                return;
            }

            Account account = new Account();
            if (account.validateLogin(username, password)) {
                String accountType = account.getUserType(username);
                String selectedType = customerRadio.isSelected() ? "customer" : "admin";

                if (!accountType.equals(selectedType)) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid user type selected. Please select the correct user type.");
                    return;
                }

                JOptionPane.showMessageDialog(null, "Login Successful!");
                setVisible(false);
                new Homepage(username, this).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                textField.setText("");
                passwordField.setText("");
                textField.requestFocus();
            }
        } else if (ae.getSource() == exitButton) {
            System.exit(0);
        }
    }
}
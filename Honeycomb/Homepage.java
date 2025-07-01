import Entities.Account;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;


public class Homepage extends JFrame {
    private JLabel welcomeLabel;
    private JButton logoutButton;
    private String currentUser;
    private JFrame loginFrame;
    ImageIcon img;
    
    
    private static Map<String, Integer> honeyTypes = new HashMap<>();
    private static Map<String, Double> honeyPrices = new HashMap<>();
    private JLabel quantityLabel;
    private JTextField quantityField;
    private JButton setQuantityButton;
    private JComboBox<String> honeyTypeCombo;
    private JButton addHoneyTypeButton;
    private JButton deleteHoneyTypeButton;
    

    private JTable honeyTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> customerHoneyCombo;
    private JSpinner buySpinner;
    private JButton buyButton;
    
    private  String DATA_FILE = "honey_data.txt";
    private  String TRANSACTIONS_FILE = "transactions.txt";

    public Homepage(String username, JFrame loginFrame) {
        super("Homepage - Honeycomb");
        this.currentUser = username;
        this.loginFrame = loginFrame;
        
        loadHoneyData();
        
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(255, 248, 220)); // Cornsilk background
        
        // Header section with improved styling
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setBounds(0, 0, 900, 100);
        headerPanel.setBackground(new Color(255, 215, 0)); // Gold background
        headerPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        
        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setBounds(50, 20, 600, 40);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(139, 69, 19)); // Saddle brown
        headerPanel.add(welcomeLabel);
        
        quantityLabel = new JLabel("Total Honey Types: " + honeyTypes.size());
        quantityLabel.setBounds(50, 60, 300, 25);
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        quantityLabel.setForeground(new Color(160, 82, 45)); // Saddle brown
        headerPanel.add(quantityLabel);
        
        panel.add(headerPanel);

        JLabel contentLabel = new JLabel();
        contentLabel.setBounds(50, 120, 700, 30);
        contentLabel.setFont(new Font("Arial", Font.BOLD, 20));
        contentLabel.setForeground(new Color(139, 69, 19));
        
        Account account = new Account();
        String userType = account.getUserType(username);
        
        if(userType.equals("admin")) {
            contentLabel.setText("Administrator Dashboard - Honey Inventory Management");
            setupAdminComponents(panel);
        } else if(userType.equals("customer")) {
            contentLabel.setText("Customer Dashboard - Buy Fresh Honey");
            setupCustomerComponents(panel);
        } else {
            contentLabel.setText("User Dashboard");
        }
        panel.add(contentLabel);
        
        logoutButton = new JButton("Logout");
        logoutButton.setBounds(400, 520, 100, 40);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(220, 20, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(BorderFactory.createRaisedBevelBorder());
        logoutButton.addActionListener(e -> logout());
        logoutButton.setFocusPainted(false);
        panel.add(logoutButton);

        add(panel);
    }
    
    
    
    private void setupAdminComponents(JPanel panel) {
        // Create a main content panel with border
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(null);
        adminPanel.setBounds(50, 170, 800, 330);
        adminPanel.setBackground(Color.WHITE);
        adminPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel adminTitle = new JLabel("Inventory Management System");
        adminTitle.setBounds(20, 20, 400, 30);
        adminTitle.setFont(new Font("Arial", Font.BOLD, 18));
        adminTitle.setForeground(new Color(139, 69, 19));
        adminPanel.add(adminTitle);
        
        // Honey Type Selection Section
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(null);
        selectionPanel.setBounds(20, 70, 350, 80);
        selectionPanel.setBackground(new Color(255, 250, 240));
        selectionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
            "Select Honey Type",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(139, 69, 19)
        ));
        
        JLabel honeyTypeLabel = new JLabel("Honey Type:");
        honeyTypeLabel.setBounds(15, 25, 100, 25);
        honeyTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        selectionPanel.add(honeyTypeLabel);
        
        honeyTypeCombo = new JComboBox<>();
        updateHoneyTypeCombo();
        honeyTypeCombo.setBounds(15, 50, 200, 25);
        honeyTypeCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        honeyTypeCombo.setBackground(Color.WHITE);
        selectionPanel.add(honeyTypeCombo);
        
        adminPanel.add(selectionPanel);
        
        // Quantity Management Section
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(null);
        quantityPanel.setBounds(400, 70, 350, 80);
        quantityPanel.setBackground(new Color(240, 255, 240));
        quantityPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            "Manage Quantity",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(139, 69, 19)
        ));
        
        JLabel setQuantityLabel = new JLabel("Quantity:");
        setQuantityLabel.setBounds(15, 25, 80, 25);
        setQuantityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityPanel.add(setQuantityLabel);
        
        quantityField = new JTextField();
        quantityField.setBounds(100, 25, 80, 25);
        quantityField.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityField.setBorder(BorderFactory.createLoweredBevelBorder());
        quantityPanel.add(quantityField);
        
        JLabel unitsLabel = new JLabel("units");
        unitsLabel.setBounds(190, 25, 50, 25);
        unitsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityPanel.add(unitsLabel);
        
        adminPanel.add(quantityPanel);
        
        // Action Buttons Section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(20, 170, 730, 120);
        buttonPanel.setBackground(new Color(248, 248, 255));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Actions",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(139, 69, 19)
        ));
        
        // First row of buttons
        setQuantityButton = new JButton("Set Quantity");
        setQuantityButton.setBounds(20, 30, 140, 35);
        setQuantityButton.setFont(new Font("Arial", Font.BOLD, 12));
        setQuantityButton.setBackground(new Color(34, 139, 34));
        setQuantityButton.setForeground(Color.WHITE);
        setQuantityButton.setBorder(BorderFactory.createRaisedBevelBorder());
        setQuantityButton.setFocusPainted(false);
        setQuantityButton.addActionListener(e -> updateHoneyQuantity());
        buttonPanel.add(setQuantityButton);
        
        JButton addQuantityButton = new JButton("Add to Stock");
        addQuantityButton.setBounds(180, 30, 140, 35);
        addQuantityButton.setFont(new Font("Arial", Font.BOLD, 12));
        addQuantityButton.setBackground(new Color(30, 144, 255));
        addQuantityButton.setForeground(Color.WHITE);
        addQuantityButton.setBorder(BorderFactory.createRaisedBevelBorder());
        addQuantityButton.setFocusPainted(false);
        addQuantityButton.addActionListener(e -> addToHoneyQuantity());
        buttonPanel.add(addQuantityButton);
        
        // Second row of buttons
        addHoneyTypeButton = new JButton("Add New Type");
        addHoneyTypeButton.setBounds(20, 75, 140, 35);
        addHoneyTypeButton.setFont(new Font("Arial", Font.BOLD, 12));
        addHoneyTypeButton.setBackground(new Color(255, 165, 0));
        addHoneyTypeButton.setForeground(Color.WHITE);
        addHoneyTypeButton.setBorder(BorderFactory.createRaisedBevelBorder());
        addHoneyTypeButton.setFocusPainted(false);
        addHoneyTypeButton.addActionListener(e -> addNewHoneyType());
        buttonPanel.add(addHoneyTypeButton);
        
        deleteHoneyTypeButton = new JButton("Delete Type");
        deleteHoneyTypeButton.setBounds(180, 75, 140, 35);
        deleteHoneyTypeButton.setFont(new Font("Arial", Font.BOLD, 12));
        deleteHoneyTypeButton.setBackground(new Color(220, 20, 60));
        deleteHoneyTypeButton.setForeground(Color.WHITE);
        deleteHoneyTypeButton.setBorder(BorderFactory.createRaisedBevelBorder());
        deleteHoneyTypeButton.setFocusPainted(false);
        deleteHoneyTypeButton.addActionListener(e -> deleteHoneyType());
        buttonPanel.add(deleteHoneyTypeButton);
        
        adminPanel.add(buttonPanel);
        panel.add(adminPanel);
    }
    
    private void setupCustomerComponents(JPanel panel) {
        JLabel customerTitle = new JLabel("Available Honey Products");
        customerTitle.setBounds(50, 170, 300, 25);
        customerTitle.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(customerTitle);
        
        createCustomerTable(panel);
        
       
        JLabel purchaseTitle = new JLabel("Make a Purchase");
        purchaseTitle.setBounds(50, 420, 200, 25);
        purchaseTitle.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(purchaseTitle);
        
        JLabel selectHoneyLabel = new JLabel("Select Honey Type:");
        selectHoneyLabel.setBounds(50, 460, 150, 25);
        selectHoneyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(selectHoneyLabel);
        
        customerHoneyCombo = new JComboBox<>();
        updateCustomerHoneyCombo();
        customerHoneyCombo.setBounds(200, 460, 200, 25);
        customerHoneyCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        customerHoneyCombo.addActionListener(e -> updateBuySpinner());
        panel.add(customerHoneyCombo);
        
        JLabel buyLabel = new JLabel("Quantity:");
        buyLabel.setBounds(50, 500, 80, 25);
        buyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(buyLabel);
        
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, getMaxQuantityForSelectedHoney(), 1);
        buySpinner = new JSpinner(spinnerModel);
        buySpinner.setBounds(130, 500, 80, 25);
        buySpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(buySpinner);
        
        JLabel unitsLabel = new JLabel("units");
        unitsLabel.setBounds(220, 500, 50, 25);
        unitsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(unitsLabel);
        
        buyButton = new JButton("Buy Honey");
        buyButton.setBounds(50, 540, 120, 35);
        buyButton.setFont(new Font("Arial", Font.BOLD, 12));
        buyButton.setBackground(new Color(255, 140, 0)); 
        buyButton.setForeground(Color.WHITE);
        buyButton.setFocusPainted(false);
        buyButton.addActionListener(e -> buyHoney());
        panel.add(buyButton);
    }
    
    private void createCustomerTable(JPanel panel) {
        String[] columnNames = {"Honey Type", "Available Quantity", "Price per Unit", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        honeyTable = new JTable(tableModel);
        honeyTable.setFont(new Font("Arial", Font.PLAIN, 12));
        honeyTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        honeyTable.setRowHeight(25);
        
        updateCustomerTable();
        
        JScrollPane scrollPane = new JScrollPane(honeyTable);
        scrollPane.setBounds(50, 200, 600, 200);
        panel.add(scrollPane);
    }
    
    private void updateCustomerTable() {
        tableModel.setRowCount(0);
        for (Map.Entry<String, Integer> entry : honeyTypes.entrySet()) {
            String honeyType = entry.getKey();
            int quantity = entry.getValue();
            double price = honeyPrices.getOrDefault(honeyType, 5.00);
            String status = quantity > 0 ? "Available" : "Out of Stock";
            
            tableModel.addRow(new Object[]{
                honeyType, 
                quantity + " units", 
                "$" + String.format("%.2f", price),
                status
            });
        }
    }
    
    private void updateHoneyTypeCombo() {
        honeyTypeCombo.removeAllItems();
        for (String honeyType : honeyTypes.keySet()) {
            honeyTypeCombo.addItem(honeyType);
        }
    }
    
    private void updateCustomerHoneyCombo() {
        customerHoneyCombo.removeAllItems();
        for (Map.Entry<String, Integer> entry : honeyTypes.entrySet()) {
            if (entry.getValue() > 0) { // Only show available items
                customerHoneyCombo.addItem(entry.getKey());
            }
        }
    }
    
    private void updateBuySpinner() {
        String selectedHoney = (String) customerHoneyCombo.getSelectedItem();
        if (selectedHoney != null) {
            int maxQuantity = honeyTypes.getOrDefault(selectedHoney, 0);
            SpinnerNumberModel model = (SpinnerNumberModel) buySpinner.getModel();
            model.setMaximum(Math.max(1, maxQuantity));
            buyButton.setEnabled(maxQuantity > 0);
        }
    }
    
    private int getMaxQuantityForSelectedHoney() {
        String selectedHoney = (String) customerHoneyCombo.getSelectedItem();
        if (selectedHoney != null) {
            return Math.max(1, honeyTypes.getOrDefault(selectedHoney, 0));
        }
        return 1;
    }
    
    private void addNewHoneyType() {
        String newType = JOptionPane.showInputDialog(this, "Enter new honey type name:", "Add Honey Type", JOptionPane.QUESTION_MESSAGE);
        if (newType != null && !newType.trim().isEmpty()) {
            newType = newType.trim();
            if (honeyTypes.containsKey(newType)) {
                JOptionPane.showMessageDialog(this, "This honey type already exists!", "Duplicate Entry", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String priceStr = JOptionPane.showInputDialog(this, "Enter price per unit for " + newType + ":", "Set Price", JOptionPane.QUESTION_MESSAGE);
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                try {
                    double price = Double.parseDouble(priceStr.trim());
                    if (price < 0) {
                        JOptionPane.showMessageDialog(this, "Price cannot be negative!", "Invalid Price", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    honeyTypes.put(newType, 0);
                    honeyPrices.put(newType, price);
                    updateHoneyTypeCombo();
                    quantityLabel.setText("Total Honey Types: " + honeyTypes.size());
                    saveHoneyData();
                    
                    String transaction = getCurrentTimestamp() + " - ADMIN (" + currentUser + "): Added new honey type '" + newType + "' with price $" + String.format("%.2f", price);
                    saveTransaction(transaction);
                    
                    JOptionPane.showMessageDialog(this, "New honey type '" + newType + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid price!", "Invalid Price", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void deleteHoneyType() {
        String selectedType = (String) honeyTypeCombo.getSelectedItem();
        if (selectedType == null) {
            JOptionPane.showMessageDialog(this, "Please select a honey type to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete '" + selectedType + "'?\nThis action cannot be undone.", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            honeyTypes.remove(selectedType);
            honeyPrices.remove(selectedType);
            updateHoneyTypeCombo();
            quantityLabel.setText("Total Honey Types: " + honeyTypes.size());
            saveHoneyData();
            
            String transaction = getCurrentTimestamp() + " - ADMIN (" + currentUser + "): Deleted honey type '" + selectedType + "'";
            saveTransaction(transaction);
            
            JOptionPane.showMessageDialog(this, "Honey type '" + selectedType + "' deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateHoneyQuantity() {
        String selectedType = (String) honeyTypeCombo.getSelectedItem();
        if (selectedType == null) {
            JOptionPane.showMessageDialog(this, "Please select a honey type.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String input = quantityField.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a quantity.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int newQuantity = Integer.parseInt(input);
            if (newQuantity < 0) {
                JOptionPane.showMessageDialog(this, "Quantity cannot be negative.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            honeyTypes.put(selectedType, newQuantity);
            quantityField.setText("");
            
            saveHoneyData();
            String transaction = getCurrentTimestamp() + " - ADMIN (" + currentUser + "): Set " + selectedType + " quantity to " + newQuantity + " units";
            saveTransaction(transaction);
            
            JOptionPane.showMessageDialog(this, 
                selectedType + " quantity updated successfully!\nNew quantity: " + newQuantity + " units", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addToHoneyQuantity() {
        String selectedType = (String) honeyTypeCombo.getSelectedItem();
        if (selectedType == null) {
            JOptionPane.showMessageDialog(this, "Please select a honey type.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String input = quantityField.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a quantity to add.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int addQuantity = Integer.parseInt(input);
            if (addQuantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity to add must be positive.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int currentQuantity = honeyTypes.get(selectedType);
            int newQuantity = currentQuantity + addQuantity;
            honeyTypes.put(selectedType, newQuantity);
            quantityField.setText("");
            
            saveHoneyData();
            String transaction = getCurrentTimestamp() + " - ADMIN (" + currentUser + "): Added " + addQuantity + " units to " + selectedType + " (Total: " + newQuantity + ")";
            saveTransaction(transaction);
            
            JOptionPane.showMessageDialog(this, 
                addQuantity + " units added to " + selectedType + "!\nTotal quantity: " + newQuantity + " units", 
                "Stock Added", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buyHoney() {
        String selectedHoney = (String) customerHoneyCombo.getSelectedItem();
        if (selectedHoney == null) {
            JOptionPane.showMessageDialog(this, "Please select a honey type.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int availableQuantity = honeyTypes.getOrDefault(selectedHoney, 0);
        if (availableQuantity <= 0) {
            JOptionPane.showMessageDialog(this, "Sorry, " + selectedHoney + " is out of stock!", "Out of Stock", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int quantityToBuy = (Integer) buySpinner.getValue();
        
        if (quantityToBuy > availableQuantity) {
            JOptionPane.showMessageDialog(this, 
                "Insufficient stock! Available: " + availableQuantity + " units", 
                "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double pricePerUnit = honeyPrices.getOrDefault(selectedHoney, 5.00);
        double totalPrice = quantityToBuy * pricePerUnit;
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Purchase Summary:\n" +
            "Product: " + selectedHoney + "\n" +
            "Quantity: " + quantityToBuy + " units\n" +
            "Price per unit: $" + String.format("%.2f", pricePerUnit) + "\n" +
            "Total Price: $" + String.format("%.2f", totalPrice) + "\n\n" +
            "Confirm purchase?", 
            "Confirm Purchase", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            int newQuantity = availableQuantity - quantityToBuy;
            honeyTypes.put(selectedHoney, newQuantity);
            updateCustomerTable();
            updateCustomerHoneyCombo();
            updateBuySpinner();
            
            saveHoneyData();
            String transaction = getCurrentTimestamp() + " - CUSTOMER (" + currentUser + "): Purchased " + 
                                quantityToBuy + " units of " + selectedHoney + " for $" + String.format("%.2f", totalPrice) + 
                                " (Remaining: " + newQuantity + ")";
            saveTransaction(transaction);
            
            JOptionPane.showMessageDialog(this, 
                "Purchase successful!\n" +
                "You bought " + quantityToBuy + " units of " + selectedHoney + " for $" + String.format("%.2f", totalPrice) + "\n" +
                "Thank you for your purchase!", 
                "Purchase Successful", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void logout() {
        this.dispose(); 
        loginFrame.setVisible(true); 
        
        if (loginFrame instanceof Frame) {
            Frame frame = (Frame) loginFrame;
            frame.textField.setText("");
            frame.passwordField.setText("");
            frame.textField.requestFocus();
        }
    }
    
    private void saveHoneyData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Map.Entry<String, Integer> entry : honeyTypes.entrySet()) {
                String honeyType = entry.getKey();
                int quantity = entry.getValue();
                double price = honeyPrices.getOrDefault(honeyType, 5.00);
                writer.println(honeyType + "," + quantity + "," + price);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadHoneyData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String honeyType = parts[0];
                    int quantity = Integer.parseInt(parts[1]);
                    double price = Double.parseDouble(parts[2]);
                    honeyTypes.put(honeyType, quantity);
                    honeyPrices.put(honeyType, price);
                }
            }
        } catch (FileNotFoundException e) {
            
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveTransaction(String transaction) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.println(transaction);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving transaction: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
}
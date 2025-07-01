package Entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class Account {
    private String uname;
    private String upass;
    private String userType;
    private static final String FILE_PATH = "Data.txt";

    public Account() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error creating data file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public Account(String uname, String upass, String userType) {
        this();  // Call default constructor to ensure file exists
        this.uname = uname;
        this.upass = upass;
        this.userType = userType.toLowerCase(); 
    }

    public boolean addAccount() {
        if (usernameExists(uname)) {
            JOptionPane.showMessageDialog(null, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            out.println(uname + "\t" + upass + "\t" + userType);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving account", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean usernameExists(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length > 0 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateLogin(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUserType(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 3 && parts[0].equals(username)) {
                    return parts[2].toLowerCase();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unknown";
    }
}
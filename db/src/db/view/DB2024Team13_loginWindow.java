package db.view;

import javax.swing.*;

import db.controller.DB2024Team13_login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DB2024Team13_loginWindow {
    public static void displayLoginWindow() {
        // Create login frame
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(800, 600);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null); // Center the frame

        // Create login panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginFrame.add(loginPanel);
        placeComponents(loginPanel, loginFrame);

        // Make the frame visible
        loginFrame.setVisible(true);
    }

    private static void placeComponents(JPanel loginPanel, JFrame loginFrame) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Create title label
        JLabel titleLabel = new JLabel("EwhaRibbon");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        loginPanel.add(titleLabel, constraints);

        // Create student ID label
        JLabel studentIdLabel = new JLabel("학번:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        loginPanel.add(studentIdLabel, constraints);

        // Create student ID text field
        JTextField studentIdTextField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        loginPanel.add(studentIdTextField, constraints);

        // Create password label
        JLabel passwordLabel = new JLabel("비밀번호:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        loginPanel.add(passwordLabel, constraints);

        // Create password field
        JPasswordField passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 2;
        loginPanel.add(passwordField, constraints);

        // Create login button
        JButton loginBtn = new JButton("로그인");
        loginBtn.setBackground(new Color(0, 80, 0));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginBtn, constraints);

        // Add action listener to login button
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = studentIdTextField.getText();
                String password = new String(passwordField.getPassword());

                if (DB2024Team13_login.validateLogin(user, password)) {
                    JOptionPane.showMessageDialog(loginPanel, "로그인 성공!");
                    showMainWindow(loginFrame);
                } else {
                    JOptionPane.showMessageDialog(loginPanel, "잘못된 학번 또는 비밀번호");
                }
            }
        });
    }

    // Display main window method
    private static void showMainWindow(JFrame loginFrame) {
        // Remove all components from the login frame
        loginFrame.getContentPane().removeAll();
        loginFrame.repaint();
        loginFrame.setTitle("EwhaRibbon");

        // Create main window components
        DB2024Team13_mainWindow mainWindow = new DB2024Team13_mainWindow();
        loginFrame.setLayout(new BorderLayout());
        loginFrame.add(mainWindow.createTitleBar(), BorderLayout.NORTH);
        loginFrame.add(mainWindow.createSidebar(), BorderLayout.WEST);
        loginFrame.add(mainWindow.getMainPanel(), BorderLayout.CENTER);

        // Refresh the frame
        loginFrame.revalidate();
        loginFrame.repaint();
    }
}

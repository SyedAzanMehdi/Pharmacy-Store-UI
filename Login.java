import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private String storedUsername = "admin";
    private String storedPassword = "4880";  // Initially set password


    public Login() {
        setTitle("Pharmacy Store Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 800, 600, 30, 30));

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 249, 252));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header panel with logo and title
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setBackground(new Color(245, 249, 252));

        ImageIcon logoIcon = loadLogo("logo.png");
        logoIcon = new ImageIcon(logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

        JLabel logoLabel = new JLabel(logoIcon);
        JLabel titleLabel = new JLabel("MediNexa Pharmacy ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(0, 102, 153));

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Login form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 249, 252));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel loginTitle = new JLabel("Login to Your Account", SwingConstants.CENTER);
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        loginTitle.setForeground(new Color(70, 70, 70));
        formPanel.add(loginTitle, gbc);

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(100, 100, 100));
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        styleTextField(usernameField);
        formPanel.add(usernameField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(100, 100, 100));
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        formPanel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("LOGIN");
        styleButton(loginButton);
        loginButton.setPreferredSize(new Dimension(200, 45));
        gbc.insets = new Insets(30, 0, 10, 0);
        formPanel.add(loginButton, gbc);
        JButton forgotPasswordButton = new JButton("Forgot Password?");
        styleLinkButton(forgotPasswordButton);
        forgotPasswordButton.addActionListener(e -> showForgotPasswordDialog());
        gbc.insets = new Insets(5, 0, 15, 0);
        formPanel.add(forgotPasswordButton, gbc);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals(storedUsername) && password.equals(storedPassword)) {
                    // Open MedicalStore window
                    MedicalStore medicalStore = new MedicalStore();  // Corrected variable declaration
                    medicalStore.setVisible(true);
                    dispose(); // Close the login window
                } else {
                    JOptionPane.showMessageDialog(Login.this,
                            "Invalid username or password",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        // Close button
        JButton closeButton = new JButton("Close Application");
        styleSecondaryButton(closeButton);
        closeButton.addActionListener(e -> System.exit(0));
        formPanel.add(closeButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(245, 249, 252));

        JLabel creditsLabel = new JLabel("Made By SYED AZAN MEHDI SHAH", SwingConstants.CENTER);
        creditsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        creditsLabel.setForeground(new Color(100, 100, 100));

        footerPanel.add(creditsLabel, BorderLayout.NORTH);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

    }
    private void styleLinkButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        button.setForeground(new Color(0, 102, 204));
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBackground(null);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(0, 51, 153));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(0, 102, 204));
            }
        });




    }
    private void showForgotPasswordDialog() {
        JDialog dialog = new JDialog(this, "Forgot Password", true);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Step 1: Username input
        JLabel userLabel = new JLabel("Enter your username:");
        JTextField usernameField = new JTextField(20);
        JButton nextButton = new JButton("Next");
        styleButton(nextButton);

        panel.add(userLabel, gbc);
        panel.add(usernameField, gbc);
        panel.add(nextButton, gbc);

        dialog.add(panel, BorderLayout.CENTER);

        nextButton.addActionListener(e -> {
            String username = usernameField.getText().trim();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Username required.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!username.equals(storedUsername)) {
                JOptionPane.showMessageDialog(dialog, "Username not found.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Username is correct, move to step 2: new password
                panel.removeAll();
                dialog.setSize(400, 220);  // Adjust size for password field

                JLabel passLabel = new JLabel("Enter new password:");
                JPasswordField passField = new JPasswordField(20);
                JButton resetButton = new JButton("Reset Password");
                styleButton(resetButton);

                panel.add(passLabel, gbc);
                panel.add(passField, gbc);
                panel.add(resetButton, gbc);

                panel.revalidate();
                panel.repaint();

                resetButton.addActionListener(ev -> {
                    String newPass = new String(passField.getPassword()).trim();
                    if (newPass.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Password cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        storedPassword = newPass; // ✅ Update password in memory
                        JOptionPane.showMessageDialog(dialog, "Password successfully reset!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                    }
                });
            }
        });

        dialog.setVisible(true);
    }


    private void styleTextField(JComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(350, 45));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(0, 122, 204));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 98, 163));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 122, 204));
            }
        });
    }

    private void styleSecondaryButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(new Color(100, 100, 100));
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(0, 122, 204));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(100, 100, 100));
            }
        });
    }

    private ImageIcon loadLogo(String filename) {
        try {
            URL imageUrl = getClass().getResource(filename);
            if (imageUrl != null) {
                return new ImageIcon(imageUrl);
            } else {
                System.out.println("Image not found: " + filename);
                return createPlaceholderLogo();
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            return createPlaceholderLogo();
        }
    }

    private ImageIcon createPlaceholderLogo() {
        BufferedImage image = new BufferedImage(80, 80, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 122, 204), 80, 80, new Color(0, 153, 255));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 80, 80);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 48));
        g2d.drawString("M", 20, 55);

        g2d.dispose();
        return new ImageIcon(image);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}
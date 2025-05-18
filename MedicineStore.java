import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class PharmacyStore {
    public static void main(String[] args) {
        Login.showLogin();
    }
}

class Medicine {
    private int id;
    private String name;
    private String company;
    private String expiryDate;
    private double price;
    private int quantity;
    private String category;

    public Medicine(int id, String name, String company, double price, int quantity, String expiryDate, String category) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.price = price;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.category = category;
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCompany() {
        return company;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getExpiryDate() {
        return expiryDate;
    }
    public String getCategory() {
        return category;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}

class MedicalStore extends JFrame {
    private ArrayList<Medicine> medicines = new ArrayList<>();
    private JTable table;
    private DefaultTableModel model;
    private JTextField idField;
    private JTextField  nameField;
    private JTextField  companyField;
    private JTextField  priceField;
    private JTextField  qtyField;
    private JTextField  expiryField;
    private JComboBox<String> categoryCombo;

    public MedicalStore() {
        setTitle("Pharmacy Billing Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(240, 248, 255));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1000, 70);
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setLayout(null);

        JLabel header = new JLabel("Pharmacy Inventory Management System");
        header.setBounds(250, 10, 500, 40);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        headerPanel.add(header);

        JLabel nameLabel = new JLabel("Developed By: SYED AZAN MEHDI SHAH");
        nameLabel.setBounds(150, 40, 700, 20);
        nameLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        nameLabel.setForeground(Color.WHITE);
        headerPanel.add(nameLabel);
        add(headerPanel);

        // Input Fields Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setBounds(20, 80, 350, 450);
        inputPanel.setBackground(new Color(230, 240, 250));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Medicine Details"));
        inputPanel.setLayout(null);

        String[] labels = {"ID:", "Name:", "Company:", "Price:", "Quantity:", "Expiry Date:", "Category:"};
        int yPos = 30;
        for (String label : labels) {
            JLabel lbl = new JLabel(label);
            lbl.setBounds(20, yPos, 100, 25);
            lbl.setFont(new Font("Arial", Font.BOLD, 13));
            inputPanel.add(lbl);
            yPos += 40;
        }

        idField = new JTextField();
        idField.setBounds(130, 30, 200, 30);
        inputPanel.add(idField);

        nameField = new JTextField();
        nameField.setBounds(130, 70, 200, 30);
        inputPanel.add(nameField);

        companyField = new JTextField();
        companyField.setBounds(130, 110, 200, 30);
        inputPanel.add(companyField);

        priceField = new JTextField();
        priceField.setBounds(130, 150, 200, 30);
        inputPanel.add(priceField);

        qtyField = new JTextField();
        qtyField.setBounds(130, 190, 200, 30);
        inputPanel.add(qtyField);

        expiryField = new JTextField();
        expiryField.setBounds(130, 230, 200, 30);
        inputPanel.add(expiryField);

        categoryCombo = new JComboBox<>(new String[]{"Tablet", "Capsule", "Syrup", "Injection", "Cream Medicines"});
        categoryCombo.setBounds(130, 270, 200, 30);
        inputPanel.add(categoryCombo);

        add(inputPanel);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(400, 80, 550, 80);
        buttonPanel.setBackground(new Color(230, 240, 250));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Operations"));
        buttonPanel.setLayout(new GridLayout(2, 4, 10, 10));

        String[] btnLabels = {"Add", "Search", "Update", "Delete", "Save", "Load", "Billing", "Clear"};
        for (String label : btnLabels) {
            JButton button = new JButton(label);
            button.setPreferredSize(new Dimension(100, 30));
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("SansSerif", Font.BOLD, 13));

            switch (label) {
                case "Add": button.addActionListener(e -> addMedicine()); break;
                case "Search": button.addActionListener(e -> searchMedicine()); break;
                case "Update": button.addActionListener(e -> updateMedicine()); break;
                case "Delete": button.addActionListener(e -> deleteMedicine()); break;
                case "Save": button.addActionListener(e -> saveToFile()); break;
                case "Load": button.addActionListener(e -> loadFromFile()); break;
                case "Billing": button.addActionListener(e -> openBillingDialog()); break;
                case "Clear": button.addActionListener(e -> clearFields()); break;
            }
            buttonPanel.add(button);
        }
        add(buttonPanel);

        // Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(400, 180, 550, 450);
        tablePanel.setBackground(new Color(230, 240, 250));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Medicine Inventory"));
        tablePanel.setLayout(new BorderLayout());

        String[] columns = {"ID", "Name", "Company", "Price", "Quantity", "Expiry", "Category"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setBackground(new Color(224, 255, 255));
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(25);

        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(540, 430));
        tablePanel.add(pane);
        add(tablePanel);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    idField.setText(model.getValueAt(row, 0).toString());
                    nameField.setText(model.getValueAt(row, 1).toString());
                    companyField.setText(model.getValueAt(row, 2).toString());
                    priceField.setText(model.getValueAt(row, 3).toString());
                    qtyField.setText(model.getValueAt(row, 4).toString());
                    expiryField.setText(model.getValueAt(row, 5).toString());
                    categoryCombo.setSelectedItem(model.getValueAt(row, 6).toString());
                    idField.setEditable(false);
                }
            }
        });

        // Load initial data
        loadFromFile();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addMedicine() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String company = companyField.getText().trim();
            String expiry = expiryField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = Integer.parseInt(qtyField.getText().trim());

            if (name.isEmpty() || company.isEmpty() || expiry.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            Medicine m = new Medicine(id, name, company, price, qty, expiry, category);
            medicines.add(m);
            model.addRow(new Object[]{id, name, company, price, qty, expiry, category});
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numbers in ID, Price or Quantity");
        }
    }

    private void updateMedicine() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update");
            return;
        }
        try {
            String name = nameField.getText().trim();
            String company = companyField.getText().trim();
            String expiry = expiryField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = Integer.parseInt(qtyField.getText().trim());

            model.setValueAt(name, row, 1);
            model.setValueAt(company, row, 2);
            model.setValueAt(price, row, 3);
            model.setValueAt(qty, row, 4);
            model.setValueAt(expiry, row, 5);
            model.setValueAt(category, row, 6);

            int id = Integer.parseInt(idField.getText());
            for (Medicine m : medicines) {
                if (m.getId() == id) {
                    m.setName(name);
                    m.setCompany(company);
                    m.setPrice(price);
                    m.setQuantity(qty);
                    m.setExpiryDate(expiry);
                    m.setCategory(category);
                    break;
                }
            }
            clearFields();
            idField.setEditable(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numeric values");
        }
    }

    private void searchMedicine() {
        String input = JOptionPane.showInputDialog("Enter Medicine ID or Name:");
        if (input == null || input.isEmpty()) return;

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(input) ||
                    model.getValueAt(i, 1).toString().equalsIgnoreCase(input)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Medicine not found");
    }

    private void deleteMedicine() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            medicines.removeIf(m -> m.getId() == id);
            model.removeRow(row);
            clearFields();
            idField.setEditable(true);
        }
    }

    private void saveToFile() {
        try (PrintWriter out = new PrintWriter("medicines.txt")) {
            for (Medicine m : medicines) {
                out.println(m.getId() + "," + m.getName() + "," + m.getCompany() + "," +
                        m.getPrice() + "," + m.getQuantity() + "," + m.getExpiryDate() + "," + m.getCategory());
            }
            JOptionPane.showMessageDialog(this, "Data saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File("medicines.txt");
        if (file.exists()) {
            try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                medicines.clear();
                model.setRowCount(0);
                String line;
                while ((line = in.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 7) {
                        Medicine m = new Medicine(
                                Integer.parseInt(parts[0]),
                                parts[1],
                                parts[2],
                                Double.parseDouble(parts[3]),
                                Integer.parseInt(parts[4]),
                                parts[5],
                                parts[6]
                        );
                        medicines.add(m);
                        model.addRow(new Object[]{m.getId(), m.getName(), m.getCompany(), m.getPrice(), m.getQuantity(), m.getExpiryDate(), m.getCategory()});
                    }
                }
            } catch (IOException | NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error loading medicines: " + e.getMessage());
            }
        }
    }

    private void openBillingDialog() {
        JDialog billDialog = new JDialog(this, "Billing", true);
        billDialog.setSize(600, 700);
        billDialog.setLayout(new BorderLayout());
        billDialog.getContentPane().setBackground(new Color(240, 248, 255));

        // Header Panel
        JPanel billHeaderPanel = new JPanel();
        billHeaderPanel.setBackground(new Color(70, 130, 180));
        JLabel billHeader = new JLabel("Pharmacy Billing System");
        billHeader.setFont(new Font("Arial", Font.BOLD, 20));
        billHeader.setForeground(Color.WHITE);
        billHeaderPanel.add(billHeader);
        billDialog.add(billHeaderPanel, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel nameLabel = new JLabel("Medicine Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(150, 30));

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField qtyField = new JTextField();

        JButton addBtn = new JButton("Add to Bill");
        addBtn.setBackground(new Color(70, 130, 180));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(qtyLabel);
        inputPanel.add(qtyField);
        inputPanel.add(new JLabel()); // Empty label for spacing
        inputPanel.add(addBtn);

        JPanel inputContainer = new JPanel(new BorderLayout());
        inputContainer.add(inputPanel, BorderLayout.NORTH);
        billDialog.add(inputContainer, BorderLayout.NORTH);

        // Bill Area
        JTextArea billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        billArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(billArea);
        scrollPane.setPreferredSize(new Dimension(550, 400));
        billDialog.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton generateBtn = new JButton("Generate Bill");
        JButton printBtn = new JButton("Print Bill");
        JButton clearBtn = new JButton("Clear Bill");

        for (JButton btn : new JButton[]{generateBtn, printBtn, clearBtn}) {
            btn.setBackground(new Color(70, 130, 180));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setPreferredSize(new Dimension(120, 35));
        }

        buttonPanel.add(generateBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(clearBtn);
        billDialog.add(buttonPanel, BorderLayout.SOUTH);

        java.util.List<Medicine> billingList = new ArrayList<>();

        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String qtyStr = qtyField.getText().trim();
            if (name.isEmpty() || qtyStr.isEmpty()) {
                JOptionPane.showMessageDialog(billDialog, "Enter medicine name and quantity");
                return;
            }

            try {
                int qty = Integer.parseInt(qtyStr);
                if (qty <= 0) {
                    JOptionPane.showMessageDialog(billDialog, "Quantity must be positive");
                    return;
                }

                Medicine found = null;
                for (Medicine m : medicines) {
                    if (m.getName().equalsIgnoreCase(name)) {
                        if (m.getQuantity() < qty) {
                            JOptionPane.showMessageDialog(billDialog, "Not enough stock available");
                            return;
                        }
                        found = new Medicine(m.getId(), m.getName(), m.getCompany(), m.getPrice(), qty, m.getExpiryDate(), m.getCategory());
                        break;
                    }
                }
                if (found == null) {
                    JOptionPane.showMessageDialog(billDialog, "Medicine not found");
                } else {
                    billingList.add(found);
                    nameField.setText("");
                    qtyField.setText("");
                    JOptionPane.showMessageDialog(billDialog, "Added to bill");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(billDialog, "Invalid quantity.");
            }
        });

        generateBtn.addActionListener(e -> {
            if (billingList.isEmpty()) {
                JOptionPane.showMessageDialog(billDialog, "No medicines added to bill");
                return;
            }
            StringBuilder bill = new StringBuilder();
            bill.append("========== Pharmacy Bill ==========\n");
            bill.append(String.format("%-5s %-20s %-10s %-10s %-10s\n", "ID", "Name", "Qty", "Price", "Subtotal"));
            bill.append("--------------------------------------------------\n");
            double total = 0;

            for (Medicine m : billingList) {
                double subtotal = m.getPrice() * m.getQuantity();
                bill.append(String.format("%-5d %-20s %-10d %-10.2f %-10.2f\n",
                        m.getId(), m.getName(), m.getQuantity(), m.getPrice(), subtotal));
                total += subtotal;
            }

            bill.append("--------------------------------------------------\n");
            bill.append(String.format("Total Amount: Rs %.2f\n", total));
            bill.append("Thank you for your purchase!\n");
            bill.append("===================================\n");
            billArea.setText(bill.toString());
        });

        printBtn.addActionListener(e -> {
            try {
                if (billArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(billDialog, "Generate bill first");
                    return;
                }
                billArea.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(billDialog, "Error printing bill");
            }
        });

        clearBtn.addActionListener(e -> {
            billingList.clear();
            billArea.setText("");
            JOptionPane.showMessageDialog(billDialog, "Bill cleared");
        });

        billDialog.setLocationRelativeTo(this);
        billDialog.setVisible(true);
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        companyField.setText("");
        priceField.setText("");
        qtyField.setText("");
        expiryField.setText("");
        categoryCombo.setSelectedIndex(0);
        idField.setEditable(true);
    }
}

class Login {
    public static void showLogin() {
        JFrame frame = new JFrame("Pharmacy Store Login");
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(230, 245, 255));

        try {
            ImageIcon icon = new ImageIcon("phr.png");
            frame.setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Icon image not found");
        }

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(50, 30, 300, 200);
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createTitledBorder("Login Credentials"));
        mainPanel.setLayout(null);

        JLabel title = new JLabel("Pharmacy Store Login");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(50, 20, 250, 30);
        title.setForeground(new Color(0, 51, 102));
        mainPanel.add(title);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        userLabel.setBounds(30, 70, 80, 25);
        passLabel.setBounds(30, 110, 80, 25);
        userLabel.setFont(new Font("Arial", Font.BOLD, 13));
        passLabel.setFont(new Font("Arial", Font.BOLD, 13));

        JTextField userField = new JTextField();
        userField.setBounds(120, 70, 150, 25);
        JPasswordField passField = new JPasswordField();
        passField.setBounds(120, 110, 150, 25);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 150, 100, 30);
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        mainPanel.add(userLabel);
        mainPanel.add(passLabel);
        mainPanel.add(userField);
        mainPanel.add(passField);
        mainPanel.add(loginButton);
        frame.add(mainPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loginButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            if (user.equals("Azan") && pass.equals("4880")) {
                frame.dispose();
                SwingUtilities.invokeLater(() -> new MedicalStore());
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add Enter key listener for login
        ActionListener loginAction = e -> loginButton.doClick();
        userField.addActionListener(loginAction);
        passField.addActionListener(loginAction);
    }
}

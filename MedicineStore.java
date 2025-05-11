import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
public class MedicineStore {
    public static void main(String[] args) {
        Login.showLogin();
    }
}

class Medicine {
    private int id;
    private String name, company, expiryDate;
    private double price;
    private int quantity;

    public Medicine(String name, String company, double price, int quantity) {
        this.id = id; this.name = name; this.company = company;
        this.price = price; this.quantity = quantity; this.expiryDate = expiryDate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCompany() { return company; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getExpiryDate() { return expiryDate; }

    public void setName(String name) { this.name = name; }
    public void setCompany(String company) { this.company = company; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}

class MedicalStore extends JFrame {
    private ArrayList<Medicine> medicines = new ArrayList<>();
    private JTable table;
    private DefaultTableModel model;
    private JTextField idField, nameField, companyField, priceField, qtyField, expiryField;

    public MedicalStore() {
        setTitle("Pharmacy Billing Management System");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(240, 248, 255));

        JLabel header = new JLabel("Medicine Inventory");
        header.setBounds(300, 10, 400, 30);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(new Color(0, 102, 153));
        add(header);

        String[] labels = {"ID", "Name", "Company", "Price", "Quantity", "Expiry Date"};
        JTextField[] fields = new JTextField[6];
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i] + ":");
            lbl.setBounds(20, 50 + i * 30, 100, 25);
            lbl.setFont(new Font("Arial", Font.BOLD, 13));
            fields[i] = new JTextField();
            fields[i].setBounds(120, 50 + i * 30, 180, 25);
            add(lbl); add(fields[i]);
        }

        idField = fields[0]; nameField = fields[1]; companyField = fields[2];
        priceField = fields[3]; qtyField = fields[4]; expiryField = fields[5];

        String[] btnLabels = {"Add", "Search", "Update", "Delete", "Save", "Load", "Billing"};
        JButton[] buttons = new JButton[btnLabels.length];
        for (int i = 0; i < btnLabels.length; i++) {
            buttons[i] = new JButton(btnLabels[i]);
            buttons[i].setBounds(320 + i * 80, 70, 80, 30);
            buttons[i].setBackground(new Color(102, 153, 255));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFont(new Font("SansSerif", Font.BOLD, 13));
            add(buttons[i]);
        }

        String[] columns = {"ID", "Name", "Company", "Price", "Quantity", "Expiry"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setBackground(new Color(224, 255, 255));
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(20, 250, 840, 230);
        add(pane);

        buttons[0].addActionListener(e -> addMedicine());
        buttons[1].addActionListener(e -> searchMedicine());
        buttons[2].addActionListener(e -> updateMedicine());
        buttons[3].addActionListener(e -> deleteMedicine());
        buttons[4].addActionListener(e -> saveToFile());
        buttons[5].addActionListener(e -> loadFromFile());
        buttons[6].addActionListener(e -> openBillingDialog());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                idField.setText(model.getValueAt(row, 0).toString());
                nameField.setText(model.getValueAt(row, 1).toString());
                companyField.setText(model.getValueAt(row, 2).toString());
                priceField.setText(model.getValueAt(row, 3).toString());
                qtyField.setText(model.getValueAt(row, 4).toString());
                expiryField.setText(model.getValueAt(row, 5).toString());
                idField.setEditable(false);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addMedicine() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            if (isDuplicateID(id)) {
                JOptionPane.showMessageDialog(this, "Duplicate ID not allowed");
                return;
            }
            String name = nameField.getText().trim(), company = companyField.getText().trim(), expiry = expiryField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = Integer.parseInt(qtyField.getText().trim());
            if (name.isEmpty() || company.isEmpty() || expiry.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            Medicine m = new Medicine(name, company, price, qty);
            medicines.add(m);
            model.addRow(new Object[]{id, name, company, price, qty, expiry});
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numbers in ID, Price or Quantity");
        }
    }

    private void searchMedicine() {
        String input = JOptionPane.showInputDialog("Enter Medicine ID or Name:");
        if (input == null || input.isEmpty()) return;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(input) ||
                    model.getValueAt(i, 1).toString().equalsIgnoreCase(input)) {
                table.setRowSelectionInterval(i, i);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Medicine not found");
    }

    private void updateMedicine() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update");
            return;
        }
        try {
            String name = nameField.getText().trim(), company = companyField.getText().trim(), expiry = expiryField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = Integer.parseInt(qtyField.getText().trim());

            model.setValueAt(name, row, 1); model.setValueAt(company, row, 2);
            model.setValueAt(price, row, 3); model.setValueAt(qty, row, 4);
            model.setValueAt(expiry, row, 5);

            int id = Integer.parseInt(idField.getText());
            for (Medicine m : medicines) {
                if (m.getId() == id) {
                    m.setName(name); m.setCompany(company);
                    m.setPrice(price); m.setQuantity(qty); m.setExpiryDate(expiry);
                    break;
                }
            }
            clearFields(); idField.setEditable(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numeric values");
        }
    }

    private void deleteMedicine() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete?");
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            medicines.removeIf(m -> m.getId() == id);
            model.removeRow(row);
            clearFields(); idField.setEditable(true);
        }
    }

    private void saveToFile() {
        try (PrintWriter out = new PrintWriter("medicines.txt")) {
            for (Medicine m : medicines) {
                out.println(m.getId() + "," + m.getName() + "," + m.getCompany() + "," +
                        m.getPrice() + "," + m.getQuantity() + "," + m.getExpiryDate());
            }
            JOptionPane.showMessageDialog(this, "Data saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Error saving file.");
        }
    }

    private void loadFromFile() {
        try (BufferedReader in = new BufferedReader(new FileReader("medicines.txt"))) {
            medicines.clear(); model.setRowCount(0);
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(",");
                Medicine m = new Medicine(parts[1], parts[2],
                        Double.parseDouble(parts[3]), Integer.parseInt(parts[4]));
                medicines.add(m);
                model.addRow(new Object[]{m.getId(), m.getName(), m.getCompany(), m.getPrice(), m.getQuantity(), m.getExpiryDate()});
            }
            JOptionPane.showMessageDialog(this, "📂 Data loaded successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Error loading file.");
        }

    }

    private void openBillingDialog() {
        JDialog billDialog = new JDialog(this, "🧾 Billing", true);
        billDialog.setSize(500, 600);
        billDialog.setLayout(new BorderLayout());

        JTextArea billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        billArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(billArea);
        billDialog.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField nameField = new JTextField(10);
        JTextField qtyField = new JTextField(5);
        JButton addBtn = new JButton("Add to Bill");
        inputPanel.add(new JLabel("Medicine Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(qtyField);
        inputPanel.add(addBtn);
        billDialog.add(inputPanel, BorderLayout.NORTH);

        JButton generateBtn = new JButton("Generate Bill");
        JButton printBtn = new JButton("Print Bill");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(generateBtn);
        bottomPanel.add(printBtn);
        billDialog.add(bottomPanel, BorderLayout.SOUTH);

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
                Medicine found = null;
                for (Medicine m : medicines) {
                    if (m.getName().equalsIgnoreCase(name)) {
                        found = new Medicine(m.getName(), m.getCompany(), m.getPrice(), qty);
                        break;
                    }
                }
                if (found == null) {
                    JOptionPane.showMessageDialog(billDialog, " Medicine not found");
                } else {
                    billingList.add(found);
                    JOptionPane.showMessageDialog(billDialog, "Added to bill");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(billDialog, "🔢 Invalid quantity.");

            }


        });

        generateBtn.addActionListener(e -> {
            if (billingList.isEmpty()) {
                JOptionPane.showMessageDialog(billDialog, "No medicines added to bill");
                return;
            }
            StringBuilder bill = new StringBuilder();
            bill.append("========== Pharmacy Bill ==========\n");
            bill.append(String.format("%-5s %-15s %-10s %-10s\n", "ID", "Name", "Qty", "Price"));
            bill.append("-----------------------------------\n");
            double total = 0;

            for (Medicine m : billingList) {
                double subtotal = m.getPrice() * m.getQuantity();
                bill.append(String.format("%-5d %-15s %-10d %.2f\n", m.getId(), m.getName(), m.getQuantity(), subtotal));
                total += subtotal;
            }

            bill.append("-----------------------------------\n");
            bill.append(String.format("Total Amount: Rs %.2f\n", total));
            bill.append("Thank you for your purchase!\n");
            bill.append("===================================\n");
            billArea.setText(bill.toString());
        });

        printBtn.addActionListener(e -> {
            try {
                billArea.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error printing bill");
            }
        });

        billDialog.setLocationRelativeTo(this);
        billDialog.setVisible(true);
    }

    private boolean isDuplicateID(int id) {
        for (Medicine m : medicines) if (m.getId() == id) return true;
        return false;
    }

    private void clearFields() {
        idField.setText(""); nameField.setText(""); companyField.setText("");
        priceField.setText(""); qtyField.setText(""); expiryField.setText("");
    }
}

class Login {
    public static void showLogin() {
        JFrame frame = new JFrame("Login");
        frame.setSize(350, 230);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(230, 245, 255));

        // ✅ Set logo/icon image
        ImageIcon icon = new ImageIcon("phr.png"); // Make sure logo.png is in the project directory
        frame.setIconImage(icon.getImage());

        JLabel title = new JLabel("Pharmacy Store Login");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(85, 10, 200, 30);
        title.setForeground(new Color(0, 51, 102));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        userLabel.setBounds(30, 60, 80, 25);
        passLabel.setBounds(30, 100, 80, 25);

        JTextField userField = new JTextField();
        userField.setBounds(120, 60, 170, 25);
        JPasswordField passField = new JPasswordField();
        passField.setBounds(120, 100, 170, 25);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(120, 140, 100, 30);
        loginButton.setBackground(new Color(102, 153, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        frame.add(title);
        frame.add(userLabel);
        frame.add(passLabel);
        frame.add(userField);
        frame.add(passField);
        frame.add(loginButton);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loginButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            if (user.equals("Azan") && pass.equals("4880")) {
                frame.dispose();
                new MedicalStore(); // Assuming MedicalStore is another JFrame class
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid login!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

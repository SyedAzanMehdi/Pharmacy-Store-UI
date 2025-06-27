import javax.imageio.ImageIO;
import javax.swing.*;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.*;
import javax.swing.table.*;

public class MedicalStore extends JFrame {
    private ArrayList<Medicine> medicines = new ArrayList<>();
    private JTable table;
    private DefaultTableModel model;
    private JTextField idField, nameField, companyField, priceField, qtyField;
    private DatePicker expiryPicker; 
    private JComboBox<String> categoryCombo;

    public MedicalStore() {
        initializeUI();
        loadFromFile(); 
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    private void initializeUI() {
        setTitle(" MEDINEXA Pharmacy");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 250, 255)); // Lighter blue background for main content pane

        // Add components to frame sections
        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(new Color(245, 250, 255)); // Lighter blue background

        centerPanel.add(createInputPanel(), BorderLayout.WEST);
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        // Main header panel with BorderLayout
        JPanel headerPanel = new JPanel(new BorderLayout());
        // Changed background to white for better logo visibility
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(1000, 80));
        // Added a bottom border for a clean separation line
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        // Main padding
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));// Add padding

        // --- Logo and Title Panel ---
        // This panel will hold the logo on the left and the title in the center
        JPanel titleAndLogoPanel = new JPanel(new BorderLayout(15, 0)); // Horizontal gap of 15px
        titleAndLogoPanel.setOpaque(false); // Make it transparent to show the headerPanel's background

        // 1. Load and display the logo
        JLabel logoLabel = new JLabel();
        try {
            // Load the image from the project's resources folder.
            // IMPORTANT: Make sure 'logo.png' is in your project's classpath (e.g., a 'resources' folder).
            URL logoUrl = getClass().getResource("/logo.png");
            if (logoUrl != null) {
                Image logoImage = ImageIO.read(logoUrl);
                // Resize the image to fit the header nicely (e.g., 60x60 pixels)
                Image scaledLogo = logoImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaledLogo));
            } else {
                // Handle case where logo is not found
                System.err.println("Logo not found! Make sure 'logo.png' is in the classpath.");
                logoLabel.setText("Logo"); // Fallback text
            }
        } catch (IOException e) {
            e.printStackTrace();
            logoLabel.setText("Logo");
        }
        titleAndLogoPanel.add(logoLabel, BorderLayout.WEST);

        JLabel headerLabel = new JLabel(" MEDI NEXA PHARMACY");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(70, 130, 180));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleAndLogoPanel.add(headerLabel, BorderLayout.CENTER);


        headerPanel.add(titleAndLogoPanel, BorderLayout.CENTER);


        JPanel creditPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        creditPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Developed By: SYED AZAN MEHDI SHAH");
        nameLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        nameLabel.setForeground(Color.GRAY);
        creditPanel.add(nameLabel);
        headerPanel.add(creditPanel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setPreferredSize(new Dimension(350, 0));
        inputPanel.setBackground(new Color(230, 240, 250));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "Medicine Details",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(0, 51, 102))
        );
        inputPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize fields and components
        idField = new JTextField();
        nameField = new JTextField();
        companyField = new JTextField();
        priceField = new JTextField();
        qtyField = new JTextField();

        // DatePicker setup
        DatePickerSettings dateSettings = new DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd"); // Set date format
        expiryPicker = new DatePicker(dateSettings);
        expiryPicker.setPreferredSize(new Dimension(200, 30));
        expiryPicker.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        expiryPicker.getComponentDateTextField().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Add a subtle border
        expiryPicker.getComponentToggleCalendarButton().setBackground(new Color(70, 130, 180)); // Button color
        expiryPicker.getComponentToggleCalendarButton().setForeground(Color.WHITE);


        categoryCombo = new JComboBox<>(new String[]{"Tablet", "Capsule", "Syrup", "Injection", "Cream"});
        categoryCombo.setPreferredSize(new Dimension(200, 30));
        categoryCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ((JTextField)categoryCombo.getEditor().getEditorComponent()).setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Add border to combobox text field

        // Define labels and their corresponding input components in order
        JComponent[][] inputComponents = {
                {new JLabel("ID:"), idField},
                {new JLabel("Name:"), nameField},
                {new JLabel("Company:"), companyField},
                {new JLabel("Price:"), priceField},
                {new JLabel("Quantity:"), qtyField},
                {new JLabel("Expiry Date:"), expiryPicker},
                {new JLabel("Category:"), categoryCombo}
        };

        for (int i = 0; i < inputComponents.length; i++) {
            JLabel label = (JLabel) inputComponents[i][0];
            JComponent component = inputComponents[i][1];

            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            inputPanel.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            component.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            if (component instanceof JTextField) {
                ((JTextField) component).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
            inputPanel.add(component, gbc);
        }

        return inputPanel;
    }


    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 8, 10, 10)); // 1 row, 8 columns, with gaps
        buttonPanel.setPreferredSize(new Dimension(0, 60));
        buttonPanel.setBackground(new Color(230, 240, 250)); // Light blue-grey background
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 220)), // Light blue-grey border
                "Operations",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(70, 130, 180)) // Steel blue title font
        );

        String[] btnLabels = {"Add", "Search", "Update", "Delete", "Save", "Load", "Billing", "Clear"};
        String[] toolTips = {
                "Add new medicine record to inventory",
                "Search for an existing medicine by ID or Name",
                "Update details of the selected medicine record",
                "Delete the selected medicine record from inventory",
                "Save all current records to 'medicines.txt' file",
                "Load medicine records from 'medicines.txt' file",
                "Open the billing system to generate a customer bill",
                "Clear all input fields for a new entry",
        };

        for (int i = 0; i < btnLabels.length; i++) {
            JButton button = createStyledButton(btnLabels[i], toolTips[i]);
            buttonPanel.add(button);
        }

        return buttonPanel;
    }

    private JButton createStyledButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(new Color(70, 130, 180)); // Steel blue background
        button.setForeground(Color.BLACK); // Set text color to BLACK
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // More padding for better visual
        button.setToolTipText(tooltip); // Set tooltip
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

        // Add mouse listener for hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(85, 155, 200)); // Slightly lighter blue on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180)); // Original color on exit
            }
        });

        // Add action listeners based on button text
        switch (text) {
            case "Add": button.addActionListener(e -> addMedicine()); break;
            case "Search": button.addActionListener(e -> searchMedicine()); break;
            case "Update": button.addActionListener(e -> updateMedicine()); break;
            case "Delete": button.addActionListener(e -> deleteMedicine()); break;
            case "Save": button.addActionListener(e -> saveToFile()); break;
            case "Load": button.addActionListener(e -> loadFromFile()); break;
            case "Billing": button.addActionListener(e -> openBillingDialog()); break;
            case "Clear": button.addActionListener(e -> clearFields()); break;
        }

        return button;
    }

    /**
     * Creates and returns the table panel displaying the medicine inventory.
     * Colors adjusted for a simpler, cohesive UI.
     * @return A JPanel containing the JTable.
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(230, 240, 250)); // Light blue-grey background
        tablePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 220)), // Light blue-grey border
                "Medicine Inventory",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(0, 51, 102)) // Dark blue title font
        );

        // Table Model setup
        String[] columns = {"ID", "Name", "Company", "Price", "Quantity", "Expiry", "Category"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        // Table setup
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28); // Slightly increased row height
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one row can be selected
        table.setBackground(Color.WHITE); // White background for table rows
        table.setSelectionBackground(new Color(173, 216, 230)); // Light blue selection
        table.setGridColor(new Color(220, 220, 220)); // Lighter grid lines
        table.setShowGrid(true);
        table.setFillsViewportHeight(true); // Table fills the height of its scroll pane
        table.setAutoCreateRowSorter(true); // Enable sorting by clicking column headers

        // Table header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(200, 230, 255)); // Light blue header background
        header.setForeground(new Color(0, 51, 102)); // Darker blue header text

        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 220), 1)); // Border for scroll pane
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add a ListSelectionListener to populate input fields when a row is selected
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Ensure this event is not an intermediate update
                int row = table.getSelectedRow();
                if (row >= 0) { // If a row is actually selected
                    // Populate input fields with data from the selected row
                    idField.setText(model.getValueAt(row, 0).toString());
                    nameField.setText(model.getValueAt(row, 1).toString());
                    companyField.setText(model.getValueAt(row, 2).toString());
                    priceField.setText(model.getValueAt(row, 3).toString());
                    qtyField.setText(model.getValueAt(row, 4).toString());
                    // Parse and set the expiry date for the DatePicker
                    try {
                        LocalDate selectedDate = LocalDate.parse(model.getValueAt(row, 5).toString());
                        expiryPicker.setDate(selectedDate);
                    } catch (DateTimeParseException ex) {
                        expiryPicker.setDate(null); // Clear if date format is invalid
                    }
                    categoryCombo.setSelectedItem(model.getValueAt(row, 6).toString());
                    idField.setEditable(false); // ID should not be changed for an existing record
                }
            }
        });

        return tablePanel;
    }

    /**
     * Adds a new medicine record to the inventory.
     * Validates input and checks for duplicate IDs.
     */
    private void addMedicine() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String company = companyField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = Integer.parseInt(qtyField.getText().trim());

            LocalDate expiry = expiryPicker.getDate(); // Get date directly from DatePicker

            // Basic input validation
            if (name.isEmpty() || company.isEmpty() || expiry == null) {
                JOptionPane.showMessageDialog(this, "Please fill all fields, including Expiry Date.");
                return;
            }

            // Check if ID already exists
            for (Medicine m : medicines) {
                if (m.getId() == id) {
                    JOptionPane.showMessageDialog(this, "Medicine ID already exists. Please use a unique ID.");
                    return;
                }
            }

            // Create new Medicine object and add to list and table model
            Medicine m = new Medicine(id, name, company, price, qty, expiry, category);
            medicines.add(m);
            model.addRow(new Object[]{id, name, company, price, qty, expiry.toString(), category});
            clearFields(); // Clear fields for next entry
            JOptionPane.showMessageDialog(this, "Medicine added successfully.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numbers in ID, Price or Quantity. Please enter valid numeric values.");
        }
    }

    /**
     * Updates an existing medicine record based on the selected row in the table.
     * The ID of the medicine cannot be changed.
     */
    private void updateMedicine() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update.");
            return;
        }
        try {
            // Get values from input fields
            String name = nameField.getText().trim();
            String company = companyField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = Integer.parseInt(qtyField.getText().trim());
            LocalDate expiry = expiryPicker.getDate(); // Get date directly from DatePicker

            // Basic validation
            if (name.isEmpty() || company.isEmpty() || expiry == null) {
                JOptionPane.showMessageDialog(this, "Please fill all fields, including Expiry Date.");
                return;
            }

            // Update the table model
            model.setValueAt(name, row, 1);
            model.setValueAt(company, row, 2);
            model.setValueAt(price, row, 3);
            model.setValueAt(qty, row, 4);
            model.setValueAt(expiry.toString(), row, 5); // Store date as string in table
            model.setValueAt(category, row, 6);

            // Update the corresponding Medicine object in the ArrayList
            int id = Integer.parseInt(idField.getText()); // ID is not editable, so it's from the selected row
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
            clearFields(); // Clear fields after update
            idField.setEditable(true); // Make ID field editable again for new entries
            JOptionPane.showMessageDialog(this, "Medicine updated successfully.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numeric values for Price and Quantity.");
        }
    }

    /**
     * Searches for a medicine by ID or Name and selects it in the table.
     */
    private void searchMedicine() {
        String input = JOptionPane.showInputDialog(this, "Enter Medicine ID or Name:", "Search Medicine", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.isEmpty()) {
            return; // User cancelled or entered nothing
        }

        // Iterate through table rows to find a match
        for (int i = 0; i < model.getRowCount(); i++) {
            // Check ID (column 0) or Name (column 1)
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(input) ||
                    model.getValueAt(i, 1).toString().equalsIgnoreCase(input)) {
                table.setRowSelectionInterval(i, i); // Select the matching row
                table.scrollRectToVisible(table.getCellRect(i, 0, true)); // Scroll to make it visible
                return; // Found, so exit
            }
        }
        JOptionPane.showMessageDialog(this, "Medicine not found."); // Not found after checking all rows
    }

    /**
     * Deletes the selected medicine record from the inventory.
     * Requires user confirmation before deletion.
     */
    private void deleteMedicine() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }
        // Confirm deletion with the user
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this medicine record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            // Remove from ArrayList
            medicines.removeIf(m -> m.getId() == id);
            // Remove from table model
            model.removeRow(row);
            clearFields(); // Clear fields
            idField.setEditable(true); // Make ID field editable again
            JOptionPane.showMessageDialog(this, "Medicine record deleted successfully.");
        }
    }

    /**
     * Saves the current list of medicines to a text file named "medicines.txt".
     */
    private void saveToFile() {
        try (PrintWriter out = new PrintWriter("medicines.txt")) {
            for (Medicine m : medicines) {
                // Format: ID,Name,Company,Price,Quantity,ExpiryDate(YYYY-MM-DD),Category
                out.println(m.getId() + "," + m.getName() + "," + m.getCompany() + "," +
                        m.getPrice() + "," + m.getQuantity() + "," +
                        m.getExpiryDate().toString() + "," + m.getCategory());
            }
            JOptionPane.showMessageDialog(this, "Data saved successfully to medicines.txt.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads medicine records from "medicines.txt" file.
     * If the file does not exist, it creates some sample data.
     */
    private void loadFromFile() {
        File file = new File("medicines.txt");
        if (!file.exists()) {
            // If file doesn't exist, create some initial sample data
            JOptionPane.showMessageDialog(this, "No existing data found. Creating sample data.");
            createSampleData();
            return;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            medicines.clear(); // Clear current data
            model.setRowCount(0); // Clear table

            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) { // Ensure all 7 expected parts are present
                    try {
                        // Parse each part of the line
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[1];
                        String company = parts[2];
                        double price = Double.parseDouble(parts[3]);
                        int quantity = Integer.parseInt(parts[4]);
                        LocalDate expiryDate = LocalDate.parse(parts[5]); // Parse date string
                        String category = parts[6];

                        // Create Medicine object and add to list and table
                        Medicine m = new Medicine(id, name, company, price, quantity, expiryDate, category);
                        medicines.add(m);
                        model.addRow(new Object[]{
                                m.getId(), m.getName(), m.getCompany(),
                                m.getPrice(), m.getQuantity(),
                                m.getExpiryDate().toString(), m.getCategory()
                        });
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.err.println("Skipping malformed record in file: " + line + " - Error: " + e.getMessage());
                        JOptionPane.showMessageDialog(this, "Warning: Skipped a malformed record in medicines.txt. Check console for details.", "Load Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    System.err.println("Skipping incomplete record in file: " + line);
                    JOptionPane.showMessageDialog(this, "Warning: Skipped an incomplete record in medicines.txt. Check console for details.", "Load Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates and adds some initial sample medicine data to the inventory and table.
     * This is called if "medicines.txt" does not exist upon application start.
     */
    private void createSampleData() {
        medicines.add(new Medicine(101, "Paracetamol", "GSK", 5.50, 100, LocalDate.now().plusYears(1), "Tablet"));
        medicines.add(new Medicine(102, "Amoxicillin", "Pfizer", 12.75, 50, LocalDate.now().plusMonths(8), "Capsule"));
        medicines.add(new Medicine(103, "Ibuprofen", "Bayer", 8.25, 75, LocalDate.now().plusYears(2), "Tablet"));
        medicines.add(new Medicine(104, "Cough Syrup", "Johnson", 150.00, 30, LocalDate.now().plusMonths(6), "Syrup"));
        medicines.add(new Medicine(105, "Vitamin C", "Roche", 25.00, 200, LocalDate.now().plusMonths(10), "Tablet"));
        medicines.add(new Medicine(106, "Insulin", "Novo Nordisk", 500.00, 20, LocalDate.now().plusMonths(3), "Injection"));

        // Add sample data to the table model
        for (Medicine m : medicines) {
            model.addRow(new Object[]{
                    m.getId(), m.getName(), m.getCompany(),
                    m.getPrice(), m.getQuantity(),
                    m.getExpiryDate().toString(), m.getCategory()
            });
        }
        // Save the newly created sample data to file
        saveToFile();
    }

    /**
     * Opens a new dialog for the billing system, allowing users to select medicines,
     * specify quantities, apply discounts, and generate a bill.
     * Gradients removed and colors adjusted for a simpler, cohesive UI.
     */
    private void openBillingDialog() {
        JDialog billDialog = new JDialog(this, "Pharmacy Billing System", true); // Modal dialog
        billDialog.setSize(650, 750);
        billDialog.setLayout(new BorderLayout());
        billDialog.getContentPane().setBackground(new Color(245, 250, 255)); // Light blue-grey background

        // --- Header Panel for Billing Dialog ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Solid Steel Blue background
        headerPanel.setPreferredSize(new Dimension(600, 60));
        JLabel title = new JLabel("Pharmacy Billing System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        headerPanel.add(title);
        billDialog.add(headerPanel, BorderLayout.NORTH);

        // --- Input Panel for adding items to bill ---
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10)); // Grid for labels and fields
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding
        inputPanel.setBackground(new Color(230, 240, 250)); // Light blue-grey background

        JLabel nameLabel = new JLabel("Medicine Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> nameCombo = new JComboBox<>();
        // Populate combo box with medicine names from current inventory
        for (Medicine m : medicines) {
            nameCombo.addItem(m.getName());
        }
        nameCombo.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Add a subtle border

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField qtyField = new JTextField();
        qtyField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel discountLabel = new JLabel("Discount (%):");
        discountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField discountField = new JTextField("0"); 
        discountField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JButton addBtn = new JButton("Add to Bill");
        addBtn.setBackground(new Color(70, 130, 180)); 
        addBtn.setForeground(Color.BLACK); 
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); 
        addBtn.addMouseListener(new MouseAdapter() { 
            @Override
            public void mouseEntered(MouseEvent e) { addBtn.setBackground(new Color(85, 155, 200)); }
            @Override
            public void mouseExited(MouseEvent e) { addBtn.setBackground(new Color(70, 130, 180)); }
        });


        inputPanel.add(nameLabel); inputPanel.add(nameCombo);
        inputPanel.add(qtyLabel); inputPanel.add(qtyField);
        inputPanel.add(discountLabel); inputPanel.add(discountField);
        inputPanel.add(new JLabel());
        inputPanel.add(addBtn);

        billDialog.add(inputPanel, BorderLayout.NORTH);

        JTextArea billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        billArea.setEditable(false); 
        billArea.setBackground(Color.WHITE); 
        billArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 220)), 
                BorderFactory.createEmptyBorder(10, 10, 10, 10) 
        ));
        JScrollPane scrollPane = new JScrollPane(billArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        billDialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); 
        buttonPanel.setBackground(new Color(230, 240, 250)); 
        JButton generateBtn = new JButton("Generate Bill");
        JButton printBtn = new JButton("Print Bill");
        JButton clearBtn = new JButton("Clear Bill");

      
        for (JButton btn : new JButton[]{generateBtn, printBtn, clearBtn}) {
            btn.setBackground(new Color(70, 130, 180));
            btn.setForeground(Color.BLACK); // Set text color to BLACK
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setPreferredSize(new Dimension(130, 40));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // More padding
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new MouseAdapter() { // Hover effect
                @Override
                public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(85, 155, 200)); }
                @Override
                public void mouseExited(MouseEvent e) { btn.setBackground(new Color(70, 130, 180)); }
            });
        }

        buttonPanel.add(generateBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(clearBtn);
        billDialog.add(buttonPanel, BorderLayout.SOUTH);

        List<Medicine> billingList = new ArrayList<>();


        addBtn.addActionListener(e -> {
            String name = nameCombo.getSelectedItem().toString().trim();
            String qtyStr = qtyField.getText().trim();

            if (name.isEmpty() || qtyStr.isEmpty()) {
                JOptionPane.showMessageDialog(billDialog, "Please select a medicine and enter quantity.");
                return;
            }

            try {
                int qty = Integer.parseInt(qtyStr);
                if (qty <= 0) {
                    JOptionPane.showMessageDialog(billDialog, "Quantity must be a positive number.");
                    return;
                }

                Medicine found = null;
                int foundIndex = -1;

                for (int i = 0; i < medicines.size(); i++) {
                    Medicine m = medicines.get(i);

                    if (m.getName().equalsIgnoreCase(name)) {

                        if (m.getQuantity() < qty) {
                            JOptionPane.showMessageDialog(billDialog,
                                    "Not enough stock available for " + m.getName() + ". Available: " + m.getQuantity());
                            return;
                        }

                        found = new Medicine(m.getId(), m.getName(), m.getCompany(),
                                m.getPrice(), qty, m.getExpiryDate(), m.getCategory());
                        foundIndex = i;
                        break;
                    }
                }

                if (found == null) {
                    JOptionPane.showMessageDialog(billDialog, "Medicine not found in inventory.");
                } else {
                    Medicine original = medicines.get(foundIndex);
                    original.setQuantity(original.getQuantity() - qty); 

                    for (int i = 0; i < model.getRowCount(); i++) {
                        Object idObj = model.getValueAt(i, 0); 
                        if (idObj != null && idObj.equals(original.getId())) {
                            model.setValueAt(original.getQuantity(), i, 4); 
                            break;
                        }
                    }

                    if (model instanceof AbstractTableModel) {
                        ((AbstractTableModel) model).fireTableDataChanged();
                    }

                    billingList.add(found);

                    qtyField.setText("");

                    JOptionPane.showMessageDialog(billDialog, "Added " + qty + " of " + name + " to bill.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(billDialog, "Invalid quantity. Please enter a whole number.");
            }
        });

        generateBtn.addActionListener(e -> {
            if (billingList.isEmpty()) {
                JOptionPane.showMessageDialog(billDialog, "No medicines have been added to the bill yet.");
                return;
            }

            double discountPercent = 0;
            try {
                discountPercent = Double.parseDouble(discountField.getText().trim());
                discountPercent = Math.max(0, Math.min(discountPercent, 100));
            } catch (NumberFormatException ignored) {
                discountPercent = 0; 
            }

            StringBuilder bill = new StringBuilder();
            bill.append("=========== YOUR PHARMACY ===========\n");
            bill.append(String.format("%-5s %-15s %-6s %-7s %-8s\n", "ID", "Name", "Qty", "Price", "Sub"));
            bill.append("-------------------------------------\n");

            double total = 0;
            for (Medicine m : billingList) {
                double sub = m.getPrice() * m.getQuantity();
                total += sub;
                bill.append(String.format("%-5d %-15s %-6d %-7.2f %-8.2f\n",
                        m.getId(), m.getName(), m.getQuantity(), m.getPrice(), sub));
            }

            double discountAmt = total * (discountPercent / 100);
            double netTotal = total - discountAmt;

            bill.append("-------------------------------------\n");
            bill.append(String.format("Subtotal: Rs %.2f\n", total));
            bill.append(String.format("Discount: %.0f%% (Rs %.2f)\n", discountPercent, discountAmt));
            bill.append(String.format("Net Total: Rs %.2f\n", netTotal));
            bill.append("Thank you for shopping with us!\n");
            bill.append("=====================================\n");

            billArea.setText(bill.toString());
        });

        printBtn.addActionListener(e -> {
            try {
                if (billArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(billDialog, "Please generate the bill first before printing.");
                    return;
                }
                billArea.print(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(billDialog, "Error printing bill: " + ex.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearBtn.addActionListener(e -> {
            for (Medicine billItem : billingList) {
                for (Medicine inventoryItem : medicines) {
                    if (inventoryItem.getId() == billItem.getId()) {
                        inventoryItem.setQuantity(inventoryItem.getQuantity() + billItem.getQuantity());
                        for (int i = 0; i < model.getRowCount(); i++) {
                            if (model.getValueAt(i, 0).equals(inventoryItem.getId())) {
                                model.setValueAt(inventoryItem.getQuantity(), i, 4);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            billingList.clear(); 
            billArea.setText(""); 
            qtyField.setText(""); 
            discountField.setText("0"); 
            JOptionPane.showMessageDialog(billDialog, "Bill has been cleared and stock adjusted.");
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
        expiryPicker.setDate(null); 
        categoryCombo.setSelectedIndex(0); 
        idField.setEditable(true); 
        table.clearSelection(); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new MedicalStore(); 
            } catch (Exception e) {
                e.printStackTrace(); 
            }
        });
    }
}

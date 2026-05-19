package hospitalmanagementsystem.gui;

import hospitalmanagementsystem.entity.Patient;
import hospitalmanagementsystem.fileio.PatientFileIO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.io.IOException;

public class HospitalGUI extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField diseaseField;
    private JTextField searchField;

    private JTable table;
    private DefaultTableModel tableModel;

    public HospitalGUI() {

        setTitle("Hospital Management System");

        setSize(750, 500);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout(30, 30));

        
        // INPUT PANEL
        

        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 8, 8));

     inputPanel.setBorder(
        BorderFactory.createTitledBorder(
                null,
                "Patient Details",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP));

        inputPanel.add(new JLabel("Patient ID (8 digits):"));

        idField = new JTextField();

        inputPanel.add(idField);

        inputPanel.add(new JLabel("Patient Name:"));

        nameField = new JTextField();

        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age:"));

        ageField = new JTextField();

        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Disease:"));

        diseaseField = new JTextField();

        inputPanel.add(diseaseField);


         
        // SEARCH PANEL
        

        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));

        searchPanel.setBorder(
                BorderFactory.createTitledBorder("Search by ID or Name"));

        searchField = new JTextField();

        JButton searchBtn = new JButton("Search");

        searchPanel.add(searchField, BorderLayout.CENTER);

        searchPanel.add(searchBtn, BorderLayout.WEST);

       

        
        // BUTTON PANEL
        

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addBtn = new JButton("Add");

        JButton updateBtn = new JButton("Update");

        JButton deleteBtn = new JButton("Delete");

        JButton viewAllBtn = new JButton("View All");

        JButton clearBtn = new JButton("Clear");

        buttonPanel.add(addBtn);

        buttonPanel.add(updateBtn);

        buttonPanel.add(deleteBtn);

        buttonPanel.add(viewAllBtn);

        buttonPanel.add(clearBtn);

        
        // NORTH PANEL
       

        JPanel northPanel = new JPanel(new BorderLayout(5, 5));

        northPanel.add(inputPanel, BorderLayout.SOUTH);

        northPanel.add(searchPanel, BorderLayout.CENTER);

        
        // TABLE
        

        String[] columns = {
                "ID",
                "Name",
                "Age",
                "Disease"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };

        table = new JTable(tableModel);

        table.setRowHeight(22);

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(
        BorderFactory.createTitledBorder(
                null,
                "Patient Records",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP));

        
        // ADD COMPONENTS
        

        add(northPanel, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.SOUTH);

        
        // BUTTON ACTIONS
        

        addBtn.addActionListener(e -> addPatient());

        updateBtn.addActionListener(e -> updatePatient());

        deleteBtn.addActionListener(e -> deletePatient());

        searchBtn.addActionListener(e -> searchPatient());

        viewAllBtn.addActionListener(e -> {

            searchField.setText("");

            viewAll();
        });

        clearBtn.addActionListener(e -> clearFields());

        
        // TABLE CLICK
       

        table.getSelectionModel().addListSelectionListener(e -> {

            int row = table.getSelectedRow();

            if (row >= 0) {

                idField.setText(
                        String.valueOf(tableModel.getValueAt(row, 0)));

                nameField.setText(
                        String.valueOf(tableModel.getValueAt(row, 1)));

                ageField.setText(
                        String.valueOf(tableModel.getValueAt(row, 2)));

                diseaseField.setText(
                        String.valueOf(tableModel.getValueAt(row, 3)));
            }
        });

       
        // FILE CREATE
        

        try {

            PatientFileIO.createFileIfNotExists();

        } catch (IOException ex) {

            showError("Error creating file: " + ex.getMessage());
        }

        viewAll();

        setLocationRelativeTo(null);

        setVisible(true);
    }

    
    // VALIDATION
   

    private boolean isValidId(String id) {

        if (id.isEmpty()) {

            showError("ID is required!");

            return false;
        }

        if (!id.matches("\\d{8}")) {

            showError(
                    "ID must be exactly 8 digits.");

            return false;
        }

        return true;
    }

    private boolean isValidAllFields(
            String id,
            String name,
            String age,
            String disease) {

        if (name.isEmpty()
                || age.isEmpty()
                || disease.isEmpty()) {

            showError("All fields are required!");

            return false;
        }

        if (!isValidId(id))
            return false;

        if (name.contains(",")
                || age.contains(",")
                || disease.contains(",")) {

            showError("Commas are not allowed!");

            return false;
        }

        try {

            Integer.parseInt(age);

        } catch (NumberFormatException ex) {

            showError("Age must be a number!");

            return false;
        }

        return true;
    }

    // ADD PATIENT
 

    private void addPatient() {

        String id = idField.getText().trim();

        String name = nameField.getText().trim();

        String age = ageField.getText().trim();

        String disease = diseaseField.getText().trim();

        if (!isValidAllFields(id, name, age, disease))
            return;

        if (PatientFileIO.idExists(id)) {

            showError("Duplicate ID!");

            return;
        }

        try {

            PatientFileIO.addPatient(
                    new Patient(id, name, age, disease));

            showInfo("Patient added successfully!");

            clearFields();

            viewAll();

        } catch (IOException ex) {

            showError("Error: " + ex.getMessage());
        }
    }

    
    // UPDATE PATIENT
    

    private void updatePatient() {

        String id = idField.getText().trim();

        String name = nameField.getText().trim();

        String age = ageField.getText().trim();

        String disease = diseaseField.getText().trim();

        if (!isValidAllFields(id, name, age, disease))
            return;

        try {

            boolean updated =
                    PatientFileIO.updatePatient(
                            new Patient(id, name, age, disease));

            if (updated) {

                showInfo("Patient updated successfully!");

                clearFields();

                viewAll();

            } else {

                showError("Patient ID not found!");
            }

        } catch (IOException ex) {

            showError("Error: " + ex.getMessage());
        }
    }

   
    // DELETE PATIENT
    

    private void deletePatient() {

        String id = idField.getText().trim();

        if (!isValidId(id))
            return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete patient ID: " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION)
            return;

        try {

            boolean deleted =
                    PatientFileIO.deletePatient(id);

            if (deleted) {

                showInfo("Patient deleted successfully!");

                clearFields();

                viewAll();

            } else {

                showError("Patient ID not found!");
            }

        } catch (IOException ex) {

            showError("Error: " + ex.getMessage());
        }
    }

    
    // SEARCH PATIENT
  

    private void searchPatient() {

        String keyword =
                searchField.getText().trim();

        if (keyword.isEmpty()) {

            showError("Enter ID or Name!");

            return;
        }

        Object[][] results =
                PatientFileIO.searchPatients(keyword);

        tableModel.setRowCount(0);

        for (int i = 0; i < results.length; i++) {

            tableModel.addRow(results[i]);
        }

        if (results.length == 0)
            showInfo("No matching patient found.");
    }

    // VIEW ALL
    

    private void viewAll() {

        Object[][] rows =
                PatientFileIO.getAllPatients();

        tableModel.setRowCount(0);

        for (int i = 0; i < rows.length; i++) {

            if (rows[i][0] != null)
                tableModel.addRow(rows[i]);
        }
    }

    // CLEAR FIELDS
    

    private void clearFields() {

        idField.setText("");

        nameField.setText("");

        ageField.setText("");

        diseaseField.setText("");

        searchField.setText("");

        table.clearSelection();
    }


    // MESSAGE BOXES
   

    private void showInfo(String msg) {

        JOptionPane.showMessageDialog(
                this,
                msg,
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String msg) {

        JOptionPane.showMessageDialog(
                this,
                msg,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
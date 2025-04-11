import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SmartTaskManager extends JFrame {
    private JTextField taskField;
    private JComboBox<String> priorityBox;
    private JTable taskTable;
    private DefaultTableModel tableModel;

    Connection connection;

    public SmartTaskManager() {
        setTitle("Smart Task Manager");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        connectDatabase();
        initUI();
        loadTasks();
    }

    private void connectDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:tasks.db");
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, priority TEXT, status TEXT)");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Connection Error: " + e.getMessage());
        }
    }

    private void initUI() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        taskField = new JTextField(20);
        priorityBox = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        JButton addButton = new JButton("Add Task");

        addButton.addActionListener(e -> addTask());

        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskField);
        inputPanel.add(new JLabel("Priority:"));
        inputPanel.add(priorityBox);
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Task", "Priority", "Status"}, 0);
        taskTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton completeButton = new JButton("Mark as Done");
        completeButton.addActionListener(e -> markAsDone());
        add(completeButton, BorderLayout.SOUTH);
    }

    private void addTask() {
        String taskName = taskField.getText().trim();
        String priority = (String) priorityBox.getSelectedItem();
        if (!taskName.isEmpty()) {
            try {
                PreparedStatement pstmt = connection.prepareStatement("INSERT INTO tasks(name, priority, status) VALUES(?, ?, ?)");
                pstmt.setString(1, taskName);
                pstmt.setString(2, priority);
                pstmt.setString(3, "Pending");
                pstmt.executeUpdate();
                taskField.setText("");
                loadTasks();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Insert Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Task cannot be empty");
        }
    }

    private void loadTasks() {
        try {
            tableModel.setRowCount(0);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tasks");
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getString("priority"), rs.getString("status")});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Load Error: " + ex.getMessage());
        }
    }

    private void markAsDone() {
        int row = taskTable.getSelectedRow();
        if (row != -1) {
            int id = (int) taskTable.getValueAt(row, 0);
            try {
                PreparedStatement pstmt = connection.prepareStatement("UPDATE tasks SET status = 'Completed' WHERE id = ?");
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                loadTasks();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Update Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to mark as done");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SmartTaskManager().setVisible(true));
    }
} 
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class PeopleMenu implements ActionListener {
  public JPanel mainPanel;
  public JTable table;
  public String[] cols = { "ID", "Name", "Birth Year", "Email" };
  public DefaultTableModel tableModel;
  public JButton addButton;
  public JButton delButton;
  public JButton updateButton;
  public JTextField idField;
  public JTextField nameField;
  public JTextField birthYearField;
  public JTextField emailField;
  public Database db = new Database();

  public void main(String[] args) {
    JFrame frame = new JFrame();
    PeopleMenu list = new PeopleMenu();
    frame.setSize(400, 400);
    frame.add(list.mainPanel);

    frame.setVisible(true);
  }

  public PeopleMenu() {
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

    // ID input field
    idField = new JTextField("", 10);
    JLabel idLabel = new JLabel("ID:");
    JPanel idRow = new JPanel(new FlowLayout(0));
    idRow.setAlignmentX(Component.LEFT_ALIGNMENT);
    idRow.add(idLabel);
    idRow.add(idField);
    mainPanel.add(idRow);

    // name input field
    nameField = new JTextField("", 10);
    JLabel nameLabel = new JLabel("Name:");
    JPanel nameRow = new JPanel(new FlowLayout(0));
    nameRow.setAlignmentX(Component.LEFT_ALIGNMENT);
    nameRow.add(nameLabel);
    nameRow.add(nameField);
    mainPanel.add(nameRow);

    // birth year input field
    birthYearField = new JTextField("", 10);
    JLabel birthYearLabel = new JLabel("Birth Year:");
    JPanel birthYearRow = new JPanel(new FlowLayout(0));
    birthYearRow.setAlignmentX(Component.LEFT_ALIGNMENT);
    birthYearRow.add(birthYearLabel);
    birthYearRow.add(birthYearField);
    mainPanel.add(birthYearRow);

    // email input field
    emailField = new JTextField("", 10);
    JLabel emailLabel = new JLabel("Email:");
    JPanel emailRow = new JPanel(new FlowLayout(0));
    emailRow.setAlignmentX(Component.LEFT_ALIGNMENT);
    emailRow.add(emailLabel);
    emailRow.add(emailField);
    mainPanel.add(emailRow);

    // ADD button
    addButton = new JButton("ADD");
    addButton.addActionListener(this);
    mainPanel.add(addButton);

    // DEL button
    delButton = new JButton("DELETE");
    delButton.addActionListener(this);
    mainPanel.add(delButton);

    // UPDATE button
    updateButton = new JButton("UPDATE");
    updateButton.addActionListener(this);
    mainPanel.add(updateButton);

    // person table
    DefaultTableModel tableModel = createTable(db);
    table = new JTable(tableModel);
    JScrollPane pane = new JScrollPane(table);
    pane.setMaximumSize(new Dimension(300, 40));
    pane.setAlignmentX(Component.LEFT_ALIGNMENT);
    mainPanel.add(pane);
  }

  public DefaultTableModel createTable(Database db) {
    DefaultTableModel tmp = new DefaultTableModel(null, cols);
    try {
      ResultSet resultSet = db.selectQuery("SELECT * FROM person");
      int i = 0;
      while (resultSet.next()) {
        Object[] row = new Object[5];
        row[0] = resultSet.getString("ID");
        row[1] = resultSet.getString("Name");
        row[2] = resultSet.getString("Birth_year");
        row[3] = resultSet.getString("Email");
        tmp.addRow(row);
        i++;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return tmp;
  }

  // https://stackoverflow.com/a/576713
  static public void setTableHeight(JTable table, int rows) {
    int width = table.getPreferredSize().width;
    int height = rows * table.getRowHeight();
    table.setPreferredScrollableViewportSize(new Dimension(width, height));
  }

  // updates table based on people list
  public void updateTable() {
    DefaultTableModel tableModel = createTable(db);
    table.setModel(tableModel);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == addButton) {
      // add new person
      db.insertUpdateDeleteQuery(
          "INSERT INTO person VALUES('" + idField.getText() + "', '" + nameField.getText() + "', '"
              + birthYearField.getText() + "', '" + emailField.getText() + "')");

      updateTable();
      JOptionPane.showMessageDialog(null, "A new soul has been claimed by the mortal realm...");
    } else if (e.getSource() == delButton) {
      JOptionPane.showMessageDialog(null, "Banish thee unto thy land of despair?");

      // find and delete the person
      db.insertUpdateDeleteQuery("DELETE FROM Person WHERE id = " + idField.getText());
      updateTable();
      JOptionPane.showMessageDialog(null, "Begone, fool!");
    } else if (e.getSource() == updateButton) {
      // find person to update
      String name = "name = '" + nameField.getText() + "',";
      String birthYear = "birth_year = '" + birthYearField.getText() + "',";
      String email = "email = '" + emailField.getText() + "'";
      db.insertUpdateDeleteQuery(
          "UPDATE Person SET " + name + birthYear + email + " WHERE id = " + idField.getText());
      updateTable();
      JOptionPane.showMessageDialog(null, "An old face has been turned anew!");
    }
  }
}

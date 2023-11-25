import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AddressBookGUI extends JFrame {
    private JTextArea contactList;
    private JButton addButton;
    private JButton deleteButton;
    private JButton showButton;

    public AddressBookGUI() {
        setTitle("Address Book");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Create a JTextArea to display the contact list
        contactList = new JTextArea();
        contactList.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(contactList);
        add(scrollPane, BorderLayout.CENTER);

        // Create buttons for adding, deleting, and showing contacts
        addButton = new JButton("Add Contact");
        deleteButton = new JButton("Delete Contact");
        showButton = new JButton("Show Contacts");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(showButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // ActionListener for the "Add Contact" button
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Prompt the user to enter contact information
                String name = JOptionPane.showInputDialog("Enter name:");
                String phone = JOptionPane.showInputDialog("Enter phone number:");
                String email = JOptionPane.showInputDialog("Enter email:");

                // Create a string with the contact information
                String contactInfo = "[name]\nname=" + name + "\nphone=" + phone + "\nemail=" + email + "\n";

                try {
                    // Open the abook.txt file in append mode
                    FileWriter fileWriter = new FileWriter("abook.txt", true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    // Write the contact information to the file
                    bufferedWriter.write(contactInfo);
                    bufferedWriter.close();
                    JOptionPane.showMessageDialog(null, "Contact added successfully!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error writing to file.");
                }
            }
        });

        // ActionListener for the "Delete Contact" button
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Prompt the user to enter the name of the contact to delete
                String name = JOptionPane.showInputDialog("Enter the name of the contact to delete:");

                try {
                    // Open the abook.txt file for reading
                    File inputFile = new File("abook.txt");
                    // Create a temporary file
                    File tempFile = new File("temp.txt");

                    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                    String lineToRemove = "[name]\nname=" + name;
                    String currentLine;

                    // Read the file line by line
                    while ((currentLine = reader.readLine()) != null) {
                        if (currentLine.equals(lineToRemove)) {
                            // Skip lines of the contact to be deleted
                            for (int i = 0; i < 4; i++) {
                                reader.readLine();
                            }
                        } else {
                            // Write the line to the temporary file
                            writer.write(currentLine + System.getProperty("line.separator"));
                        }
                    }

                    writer.close();
                    reader.close();

                    // Delete the original file and rename the temporary file
                    if (inputFile.delete()) {
                        tempFile.renameTo(inputFile);
                        JOptionPane.showMessageDialog(null, "Contact deleted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error deleting contact.");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error reading from or writing to file.");
                }
            }
        });

        // ActionListener for the "Show Contacts" button
        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Open the abook.txt file for reading
                    BufferedReader reader = new BufferedReader(new FileReader("abook.txt"));
                    StringBuilder contacts = new StringBuilder();
                    String line;

                    // Read the file line by line and append to the StringBuilder
                    while ((line = reader.readLine()) != null) {
                        contacts.append(line);
                        contacts.append(System.getProperty("line.separator"));
                    }

                    reader.close();
                    // Set the text of the contactList JTextArea
                    contactList.setText(contacts.toString());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error reading from file.");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AddressBookGUI().setVisible(true);
            }
        });
    }
}

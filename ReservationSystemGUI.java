import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ReservationSystemGUI extends JFrame {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;
    private final ArrayList<Account> accounts;
    private JComboBox<Lab> labComboBox;
    private JComboBox<String> monthComboBox;
    private JCheckBox eclipseCheckBox;
    private JCheckBox safeExamCheckBox;
    private JCheckBox internetCheckBox;
    private JCheckBox printerCheckBox;
    private final JTextField idField;
    private final JPasswordField passwordField;
    private JPanel panel;
    private Admin admin;


    public ReservationSystemGUI(Admin admin) {
        this.admin = admin;
        Lab[] labs;
        labs = new Lab[20];
        labComboBox = new JComboBox<>();
        admin = new Admin("a", 100, 9999);
        accounts = new ArrayList<>();
        accounts.add(admin);
        accounts.add(new Employee("e1", 2024, 3434, "Eng"));
        accounts.add(new Employee("e2", 2018, 1818, "Dr"));
        accounts.add(new Employee("e3", 2022, 1212, "Dr"));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Lab Reservation System");
        setSize(500, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));

        JLabel usernameLabel = new JLabel("id:");
        panel.add(usernameLabel);
        idField = new JTextField();
        panel.add(idField);

        JLabel passwordLabel = new JLabel("pass:");
        panel.add(passwordLabel);
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("log");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        JButton clearButton = new JButton("clear");
        clearButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                idField.setText("");
                passwordField.setText("");
            }
        });
        JButton viewReservationsButton = new JButton("view lab reservations");
        viewReservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewLabReservations();
            }
        });
        panel.add(viewReservationsButton);
        panel.add(clearButton);
        panel.add(loginButton);
        updateLabComboBox();

        getContentPane().add(panel);
        setVisible(true);
    }

    private void refreshLabs() {
        // Code to update the labComboBox with the latest labs from the admin object
        updateLabComboBox();
    }


    private void viewLabReservations() {
        StringBuilder reservationInfo = new StringBuilder("lab reservations:\n");
        Lab[] labs = admin.getLabs().toArray(new Lab[0]);
        for (Lab lab : labs) {
            if (lab != null) {
                //print lab status
                System.out.println("lab " + lab.getNumber() + " status: " + (lab.isStatus() ? "Reserved" : "Available"));

                String status;
                if (lab.isStatus()) {
                    status = "Reserved on " + lab.getBookingDate().toString();
                } else {
                    status = "Available";
                }
                reservationInfo.append("Lab ").append(lab.getNumber()).append(" - ").append(lab.getLocation()).append(" - Capacity: ").append(lab.getCapacity()).append(" - Status: ").append(status).append("\n");
            }
        }
        JOptionPane.showMessageDialog(this, reservationInfo.toString(), "Lab Reservations", JOptionPane.INFORMATION_MESSAGE);

        //rrevalidate thn repaint frame to reflect the changes in the UI
        revalidate();
        repaint();
    }


    private void login() {
        String username = idField.getText();
        String password = new String(passwordField.getPassword());

        System.out.println("ttempting login with username: " + username + ", password: " + password);

        Account user = authenticate(username, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            System.out.println("Login successful for user: " + user.getName());

            //gui display based on role
            if (user instanceof Admin) {
                System.out.println("User is an admin");
                showAdminGUI();
            } else if (user instanceof Employee) {
                System.out.println("User is an employee");
                showEmployeeGUI((Employee) user);
            }
            //close log screen
            setVisible(false);
        }

        //make appropriate gui visibl
        setVisible(true);
    }



    private Account authenticate(String id, String password) {
        for (Account account : accounts) {
            if (account.getId().equals(id) && account.getPassword() == Integer.parseInt(password)) {
                return account;
            }
        }
        return null; //failrp
    }






    private void showEmployeeGUI(Employee employee) {
        setTitle("Lab Reservation System - Employee: " + employee.getName() + " (" + employee.getRole() + ")");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        List<Lab> labs = admin.getLabs();

        JLabel labLabel = new JLabel("Lab:");
        panel.add(labLabel);
        labComboBox = new JComboBox<>();
        updateLabComboBox();
        panel.add(labComboBox);

        JLabel monthLabel = new JLabel("Month:");
        panel.add(monthLabel);
        monthComboBox = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        panel.add(monthComboBox);

        JLabel dayLabel = new JLabel("Day:");
        panel.add(dayLabel);
        JComboBox<String> dayComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayComboBox.addItem(String.valueOf(i));
        }
        panel.add(dayComboBox);

        JLabel timeLabel = new JLabel("Time:");
        panel.add(timeLabel);
        JComboBox<String> timeComboBox = new JComboBox<>();
        for (int i = 8; i <= 18; i++) { // assuming lab hours from 8 AM to 6 PM
            timeComboBox.addItem(i + ":00");
        }
        panel.add(timeComboBox);

        JLabel requirementsLabel = new JLabel("Requirements:");
        panel.add(requirementsLabel);
        eclipseCheckBox = new JCheckBox("Eclipse software");
        panel.add(eclipseCheckBox);
        safeExamCheckBox = new JCheckBox("Safe Exam Browser");
        panel.add(safeExamCheckBox);
        internetCheckBox = new JCheckBox("Internet");
        panel.add(internetCheckBox);
        printerCheckBox = new JCheckBox("Printer");
        panel.add(printerCheckBox);

        JButton reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int month = monthComboBox.getSelectedIndex();
                int day = Integer.parseInt((String) dayComboBox.getSelectedItem());
                String time = (String) timeComboBox.getSelectedItem();
                reserveLab(month, day, time);
            }
        });

        panel.add(reserveButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshLabs();
                setVisible(false);
                new ReservationSystemGUI(admin);
            }
        });
        panel.setLayout(new GridLayout(6, 8, 9, 17));
        panel.add(backButton);

        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }



    private void showAdminGUI() {
        setTitle("Lab Reservation System - Admin");
        panel = new JPanel(); // nitialize the panel
        JLabel welcomeLabel = new JLabel("Welcome " + admin.getName());
        panel.add(welcomeLabel);

        //text fields
        JTextField labNumberField = new JTextField();
        labNumberField.setPreferredSize(new Dimension(150, 25));
        panel.add(new JLabel("Lab Number:"));
        panel.add(labNumberField);

        JTextField locationField = new JTextField();
        locationField.setPreferredSize(new Dimension(150, 25));
        panel.add(new JLabel("Location:"));
        panel.add(locationField);

        JTextField capacityField = new JTextField();
        capacityField.setPreferredSize(new Dimension(150, 25));
        panel.add(new JLabel("Capacity:"));
        panel.add(capacityField);

        JButton addLabButton = new JButton("Add New Lab");
        addLabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int number = Integer.parseInt(labNumberField.getText());
                    String location = locationField.getText();
                    int capacity = Integer.parseInt(capacityField.getText());

                    //aedd new labs to admin's labs
                    admin.addLab(new Lab(number, capacity, location, false));

                    JOptionPane.showMessageDialog(null, "Lab added", "Success", JOptionPane.INFORMATION_MESSAGE);

                    viewLabReservations();

                    refreshUI();

                    updateLabComboBox();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid lab details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(addLabButton);


        JButton viewReservationsButton = new JButton("View Lab Reservations");
        viewReservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewLabReservations();
            }
        });
        panel.add(viewReservationsButton);
        //add back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                //main frame
                refreshLabs();
                new ReservationSystemGUI(admin);
            }
        });
        panel.add(backButton);


        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }





    private void reserveLab(int month, int day, String time) {
        Lab selectedLab = (Lab) labComboBox.getSelectedItem();
        if (selectedLab == null) {
            JOptionPane.showMessageDialog(this, "Please select a lab", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedLab.isStatus()) {
            JOptionPane.showMessageDialog(this, "Lab is already reserved", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!eclipseCheckBox.isSelected() && !safeExamCheckBox.isSelected() && !internetCheckBox.isSelected() && !printerCheckBox.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select at least one requirement", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        admin.reserveLab(selectedLab, new Time(day, month + 1, 2024, Integer.parseInt(time.split(":")[0]), 0));

        JOptionPane.showMessageDialog(this, "Reservation successful", "Success", JOptionPane.INFORMATION_MESSAGE);

        printToFile(selectedLab);

        viewLabReservations();

        updateLabComboBox();
    }


    private void updateLabComboBox() {
        labComboBox.removeAllItems();
        for (Lab lab : admin.getLabs()) {
            if (lab != null && !lab.isStatus()) {
                labComboBox.addItem(lab);
            }
        }
    }




    private void printToFile(Lab lab) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("lab_reservations.txt", true))) {
            //write lab reservation information to the file
            writer.println("Lab Number: " + lab.getNumber());
            writer.println("Location: " + lab.getLocation());
            writer.println("Capacity: " + lab.getCapacity());
            writer.println("Reserved on: " + lab.getBookingDate());
            writer.println(); //add an empty line for separation
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }



    /*/
    private void printToFile(Lab lab) {
    String filePath = "C:/your/directory/lab_reservations.txt";
    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
        //write lab reservation information to the file
        writer.println("Lab Number: " + lab.getNumber());
        writer.println("Location: " + lab.getLocation());
        writer.println("Capacity: " + lab.getCapacity());
        writer.println("Reserved on: " + lab.getBookingDate());
        writer.println(); //add an empty line for separation
    } catch (IOException e) {
        System.err.println("Error writing to file: " + e.getMessage());
    }
}



    /*/

    private void refreshUI() {
        getContentPane().removeAll(); // take all components off the content pane
        // start and rebuild admin GUI
        showAdminGUI();
        revalidate(); // revalidt the container hierarchy
        repaint(); // Request a repaint to reflect the changes
    }



}

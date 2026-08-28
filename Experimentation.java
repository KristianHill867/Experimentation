import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Experimentation 
{
    private static JLabel createDateTimeLabel() 
    {
        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy  hh:mm:ss a");

        timeLabel.setText(LocalDateTime.now().format(formatter));

        Timer timer = new Timer(1000, e -> timeLabel.setText(LocalDateTime.now().format(formatter)));
        timer.start();

        return timeLabel;
    }

    public static class DescriptionFrame extends JFrame 
    {
        public DescriptionFrame(String description) 
        {
            super("Description");
            setSize(300, 150);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTextArea text = new JTextArea(description);
            text.setEditable(false);
            text.setLineWrap(true);
            text.setWrapStyleWord(true);

            add(new JScrollPane(text), BorderLayout.CENTER);
        }
    }

    public static class PasswordDialog extends JDialog 
    {
        private boolean unlocked = false;

        public boolean isUnlocked() 
        {
            return unlocked;
        }

        public PasswordDialog() 
        {
            super((Frame) null, "Enter Code", true); // modal dialog
            setSize(260, 130);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setLayout(new FlowLayout());

            JLabel label = new JLabel("Enter 4-digit code: ");
            JPasswordField passField = new JPasswordField(4);
            passField.setEchoChar('*');

            JButton submit = new JButton("Submit");

            ActionListener submitAction = e -> 
            {
                String code = new String(passField.getPassword());

                if ("5150".equals(code)) 
                {
                    unlocked = true;
                    dispose();
                } 
                else 
                {
                    JOptionPane.showMessageDialog(this, "Incorrect code.");
                    passField.setText("");
                }
            };

            submit.addActionListener(submitAction);
            passField.addActionListener(submitAction);

            add(label);
            add(passField);
            add(submit);
        }
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> 
        {
            PasswordDialog dialog = new PasswordDialog();
            dialog.setVisible(true);

            if (!dialog.isUnlocked()) 
            {
                System.exit(0);
            }

            SimpleFrame frame = new SimpleFrame("File Selection Menu");
            frame.setMinimumSize(new Dimension(300, 200));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static class SimpleFrame extends JFrame 
    {
        private JLabel myLabel;

        public SimpleFrame(String label) 
        {
            super(label);
            setLayout(new BorderLayout());

            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            rightPanel.setOpaque(false);
            rightPanel.add(createDateTimeLabel());
            headerPanel.add(rightPanel, BorderLayout.EAST);
            add(headerPanel, BorderLayout.NORTH);

            JPanel mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));

            Automobile acura = new Automobile(2010, "Acura", "MDX", "Blue", "123468");
            Automobile honda = new Automobile(2016, "Honda", "Civic", "Green", "65468484");
            Automobile ford = new Automobile(2004, "Ford", "F150", "Black", "351616484");
            Automobile porsche = new Automobile(2005, "Porsche", "Boxter", "White", "44618445");

            mainPanel.add(createCarButton("Blue", acura));
            mainPanel.add(createCarButton("Green", honda));
            mainPanel.add(createCarButton("Black", ford));
            mainPanel.add(createCarButton("White", porsche));

            add(mainPanel, BorderLayout.CENTER);

            myLabel = new JLabel("Please Select A File", SwingConstants.CENTER);
            add(myLabel, BorderLayout.SOUTH);

            new OtherFrame("Menu Bar");

            pack();
        }

        private JButton createCarButton(String color, Automobile car) 
        {
            JButton button = new JButton(color);
            button.addActionListener(e -> {
                myLabel.setText("Selection Process Complete");
                showDescription(car);
            });
            return button;
        }

        private void showDescription(Automobile auto) 
        {
            DescriptionFrame df = new DescriptionFrame(auto.getCustomDescription());
            df.addWindowListener(new WindowAdapter() 
            {
                @Override
                public void windowClosed(WindowEvent e) 
                {
                    myLabel.setText("Please Select A File");
                }
            });
            df.setVisible(true);
        }
    }

    public static class OtherFrame extends JFrame 
    {
        protected DefaultListModel<Automobile> listModel = new DefaultListModel<>();
        protected JList<Automobile> listView = new JList<>(listModel);

        public OtherFrame(String title) 
        {
            super(title);
            setLocation(100, 100);
            setMinimumSize(new Dimension(300, 200));
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JMenuBar menuBar = new JMenuBar();
            setJMenuBar(menuBar);

            JMenu fileMenu = new JMenu("File");
            menuBar.add(fileMenu);

            JMenuItem openItem = new JMenuItem("Open");
            openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
            openItem.addActionListener(e -> openFile());

            JMenuItem exitItem = new JMenuItem("Exit");
            exitItem.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
            exitItem.addActionListener(e -> dispose());

            fileMenu.add(openItem);
            fileMenu.add(exitItem);

            add(new JScrollPane(listView), BorderLayout.CENTER);

            pack();
            setVisible(true);
        }

        private void openFile() 
        {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) 
            {
                JOptionPane.showMessageDialog(this, 
                    "Selected: " + chooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    public static class Automobile 
    {
        protected int year;
        protected String make, model, color, vin;

        public Automobile() {}

        public Automobile(int year, String make, String model, String color, String vin) 
        {
            this.year = year;
            this.make = make;
            this.model = model;
            this.color = color;
            this.vin = vin;
        }

        public String getCustomDescription() 
        {
            return make + " " + model + " (" + year + "), Color: " + color + ", VIN: " + vin;
        }

        @Override
        public String toString() 
        {
            return getCustomDescription();
        }
    }
}

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Experimentation 
{
    private static final String PASSWORD = "5150";
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy  hh:mm:ss a");

    /* ---------- Clock helpers ---------- */
    private static JLabel createDateTimeLabel() 
    {
        JLabel label = new JLabel();
        label.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        label.setText(LocalDateTime.now().format(FORMATTER));
        return label;
    }

    private static Timer startClock(JLabel label) 
    {
        Timer timer = new Timer(1000, e -> 
            label.setText(LocalDateTime.now().format(FORMATTER)));
        timer.start();
        return timer;
    }

    /* ---------- Description popup ---------- */
    public static class DescriptionFrame extends JFrame 
    {
        private static final long serialVersionUID = 1L;
        private final Timer timer;

        public DescriptionFrame(String description) 
        {
            super("Description");
            setSize(350, 200);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            // Clock in upper-right
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            right.setOpaque(false);
            JLabel timeLabel = createDateTimeLabel();
            timer = startClock(timeLabel);
            right.add(timeLabel);
            header.add(right, BorderLayout.EAST);
            add(header, BorderLayout.NORTH);

            JTextArea text = new JTextArea(description);
            text.setEditable(false);
            text.setLineWrap(true);
            text.setWrapStyleWord(true);
            text.setFont(new Font("Times New Roman", Font.PLAIN, 14));

            add(new JScrollPane(text), BorderLayout.CENTER);

            addWindowListener(new WindowAdapter() 
            {
                @Override
                public void windowClosing(WindowEvent e) 
                {
                    timer.stop();
                }
            });
        }
    }

    /* ---------- Password dialog ---------- */
    public static class PasswordDialog extends JDialog 
    {
        private static final long serialVersionUID = 1L;
        private boolean unlocked = false;

        public boolean isUnlocked() 
        {
            return unlocked;
        }

        public PasswordDialog() 
        {
            super((Frame) null, "Enter Code", true);
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
                if (PASSWORD.equals(code)) 
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

    /* ---------- Main entry ---------- */
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
            frame.setMinimumSize(new Dimension(400, 300));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    /* ---------- Main car-selection frame ---------- */
    public static class SimpleFrame extends JFrame 
    {
        private static final long serialVersionUID = 1L;
        private JLabel statusLabel;
        private Timer clockTimer;

        public SimpleFrame(String label) 
        {
            super(label);
            setLayout(new BorderLayout());

            // Clock in upper-right
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            rightPanel.setOpaque(false);
            JLabel timeLabel = createDateTimeLabel();
            clockTimer = startClock(timeLabel);
            rightPanel.add(timeLabel);
            headerPanel.add(rightPanel, BorderLayout.EAST);
            add(headerPanel, BorderLayout.NORTH);

            // Car buttons
            JPanel mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));

            Automobile acura   = new Automobile(2010, "Acura",   "MDX",    "Blue",  "123468");
            Automobile honda   = new Automobile(2016, "Honda",   "Civic",  "Green", "65468484");
            Automobile ford    = new Automobile(2004, "Ford",    "F150",   "Black", "351616484");
            Automobile porsche = new Automobile(2005, "Porsche", "Boxter", "White", "44618445");

            mainPanel.add(createCarButton("Blue",  acura));
            mainPanel.add(createCarButton("Green", honda));
            mainPanel.add(createCarButton("Black", ford));
            mainPanel.add(createCarButton("White", porsche));

            add(mainPanel, BorderLayout.CENTER);

            statusLabel = new JLabel("Please Select A File", SwingConstants.CENTER);
            add(statusLabel, BorderLayout.SOUTH);

            // Open the list frame and populate it
            OtherFrame listFrame = new OtherFrame("Menu Bar");
            listFrame.addAutomobile(acura);
            listFrame.addAutomobile(honda);
            listFrame.addAutomobile(ford);
            listFrame.addAutomobile(porsche);

            pack();
            setSize(500, 400);

            addWindowListener(new WindowAdapter() 
            {
                @Override
                public void windowClosing(WindowEvent e) 
                {
                    if (clockTimer != null) clockTimer.stop();
                }
            });
        }

        private JButton createCarButton(String color, Automobile car) 
        {
            JButton button = new JButton(color);
            button.addActionListener(e -> 
            {
                statusLabel.setText("Selection Process Complete");
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
                    statusLabel.setText("Please Select A File");
                }
            });
            df.setVisible(true);
        }
    }

    /* ---------- List / menu frame ---------- */
    public static class OtherFrame extends JFrame 
    {
        private static final long serialVersionUID = 1L;
        protected DefaultListModel<Automobile> listModel = new DefaultListModel<>();
        protected JList<Automobile> listView = new JList<>(listModel);
        private Timer clockTimer;

        public OtherFrame(String title) 
        {
            super(title);
            setLocation(100, 100);
            setMinimumSize(new Dimension(350, 250));
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            // Clock in upper-right
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            rightPanel.setOpaque(false);
            JLabel timeLabel = createDateTimeLabel();
            clockTimer = startClock(timeLabel);
            rightPanel.add(timeLabel);
            headerPanel.add(rightPanel, BorderLayout.EAST);
            add(headerPanel, BorderLayout.NORTH);

            // Menu bar
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
            
            addWindowListener(new WindowAdapter() 
            {
                @Override
                public void windowClosing(WindowEvent e) 
                {
                    if (clockTimer != null) clockTimer.stop();
                }
            });
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
        
        public void addAutomobile(Automobile auto) 
        {
            listModel.addElement(auto);
        }
    }

    /* ---------- Data class ---------- */
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
            return String.format("%s %s (%d), Color: %s, VIN: %s", 
                make, model, year, color, vin);
        }

        @Override
        public String toString() 
        {
            return getCustomDescription();
        }
    }
}

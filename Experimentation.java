import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Experimentation
{
    public static class DescriptionFrame extends JFrame
    {
        public DescriptionFrame(String description)
        {
            super("Description");
            setSize(300,150);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTextArea text = new JTextArea(description);
            text.setEditable(false);
            text.setLineWrap(true);
            text.setWrapStyleWord(true);

            add(new JScrollPane(text), BorderLayout.CENTER);
            setVisible(true);
        }
    }

    class PasswordFrame extends JFrame
    {
        private boolean unlocked = false;

        public boolean isUnlocked()
        {
            return unlocked;
        }

        public PasswordFrame()
        {
            super("Enter Code");
            setSize(260,130);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new FlowLayout());

            JLabel label = new JLabel("Enter 4-digit code: ");
            JPasswordField passField = new JPasswordField(4);
            passField.setEchoChar('*');

            JButton submit = new JButton("Submit");

            ActionListener submitAction = e -> 
            {
                String code = new String(passField.getPassword());

                if (code.equals("5150"))
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
            setVisible(true);
        }
    }

    public static void main (String[] args)
    {
        Experimentation outer = new Experimentation();
        PasswordFrame pf = outer.new PasswordFrame();

        while (pf.isShowing())
        {
            try { Thread.sleep(100); } catch (Exception ex) {}
        }

        if (!pf.isUnlocked()) System.exit(0);

        SimpleFrame frame = new SimpleFrame("File Selection Menu");
        frame.setMinimumSize(new Dimension(300,200));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static class SimpleFrame extends JFrame
    {
        private JLabel myLabel;

        public SimpleFrame (String label)
        {
            super(label);
            setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel(new GridLayout(2,2,10,10));

            Automobile acura = new Automobile(2010, "Acura", "MDX", "Blue", "123468");
            Automobile honda = new Automobile(2016, "Honda", "Civic", "Green", "65468484");
            Automobile ford  = new Automobile(2004, "Ford",  "F150", "Black",  "351616484");
            Automobile porsche = new Automobile(2005, "Porsche", "Boxter", "White", "44618445");

            JButton button1 = new JButton("Blue");
            JButton button2 = new JButton("Green");
            JButton button3 = new JButton("Black");
            JButton button4 = new JButton("White");

            mainPanel.add(button1);
            mainPanel.add(button2);
            mainPanel.add(button3);
            mainPanel.add(button4);

            add(mainPanel, BorderLayout.CENTER);

            myLabel = new JLabel("Please Select A File", SwingConstants.CENTER);
            add(myLabel, BorderLayout.SOUTH);

            ActionListener selectionListener = e -> myLabel.setText("Selection Process Complete");
            button1.addActionListener(selectionListener);
            button2.addActionListener(selectionListener);
            button3.addActionListener(selectionListener);
            button4.addActionListener(selectionListener);

            button1.addActionListener(e -> showDescription(acura));
            button2.addActionListener(e -> showDescription(honda));
            button3.addActionListener(e -> showDescription(ford));
            button4.addActionListener(e -> showDescription(porsche));

            new OtherFrame("Menu Bar");

            pack();
            setVisible(true);
        }

        private void showDescription(Object item)
        {
            String text;
            if (item instanceof Automobile auto)
            {
                text = auto.getCustomDescription();
            }
            else
            {
                text = item.toString();
            }

            DescriptionFrame df = new DescriptionFrame(text);
            df.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e)
                {
                    myLabel.setText("Please Select A File");
                }
            });
        }
    }

    public static class OtherFrame extends JFrame
    {
        protected DefaultListModel<Automobile> listModel = new DefaultListModel<>();
        protected JList<Automobile> listView = new JList<>(listModel);

        public OtherFrame (String title)
        {
            super(title);
            setLocation(100,100);
            setMinimumSize(new Dimension(300,200));
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JMenuBar menuBar = new JMenuBar();
            setJMenuBar(menuBar);

            JMenu fileMenu = new JMenu("File");
            menuBar.add(fileMenu);

            JMenuItem openItem = new JMenuItem("Open");
            openItem.setAccelerator(KeyStroke.getKeyStroke("crtl O"));
            fileOpenMenuItem.addActionListener(e -> openFile());

            JMenuItem exitItem = new JMenuItem("Exit");
            exitItem.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
            fileExitMenuItem.addActionListener(e -> dispose());

            add(new JScrollPane(listView), BorderLayout.CENTER);

            pack();
            setVisible(true);
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
    }
}
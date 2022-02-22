import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class Phase2 {
    static List<Person> records = new LinkedList<>();
    static JTextArea textArea;
    static JComboBox<String> dropDown;
    static JRadioButton ascRadioB, desRadioB;

    public static void listOfRecords() {
        //Initializes Records Output
        textArea = new JTextArea("\tNAMES\tBIRTHDAY\tAGE\n");
        textArea.setEditable(false);
        JScrollPane firstPart = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        //Initializes Sorting
        JLabel sortBy = new JLabel("Sort By:    ");
        sortBy.setHorizontalAlignment(JLabel.RIGHT);
        //Sort 1
        dropDown = new JComboBox();
        dropDown.addItem("Name");
        dropDown.addItem("Birthday");
        dropDown.addItem("Age");
        dropDown.addActionListener(e -> updateSort());
        //Sort 2
        ascRadioB = new JRadioButton("Ascending");
        ascRadioB.setSelected(true);
        desRadioB = new JRadioButton("Descending");
        ascRadioB.addActionListener(e -> {
            desRadioB.setSelected(!ascRadioB.isSelected());
            updateSort();
        });
        desRadioB.addActionListener(e -> {
            ascRadioB.setSelected(!desRadioB.isSelected());
            updateSort();
        });
        ascRadioB.setHorizontalAlignment(JRadioButton.CENTER);
        desRadioB.setHorizontalAlignment(JRadioButton.CENTER);
        //Manages Sort Layout
        JPanel secondPart = new JPanel(new GridLayout(2,3));
        add(secondPart, sortBy, dropDown, ascRadioB, fill(), fill(), desRadioB);

        //Initializes all Buttons and their Action Listener using lambda
        JButton addRecord = new JButton("Add a Record");
        addRecord.addActionListener(e -> addRecords());
        JButton removeRecord = new JButton("Remove a Record");
        removeRecord.addActionListener(e -> removeRecords());
        JButton exportRecord = new JButton("Export to CSV File");
        exportRecord.addActionListener(e -> {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuuMMddHHmmss"))+".csv"));
                String s = "NAMES,BIRTHDAY,AGE\n";
                for(Person p: records) s += p.getName()+","+p.getBirthDay()+","+p.getAge()+"\n";
                bw.write(s);
                bw.close();
            } catch (IOException ignored) { }
        });
        //Manages Button Layout
        JPanel thirdPart = new JPanel(new GridLayout(1,3, 10, 0));
        add(thirdPart, addRecord, removeRecord, exportRecord);

        ///Merges Sort and Buttons for Bottom Layout
        JPanel southAlign = new JPanel(new GridLayout(2,1));
        add(southAlign,secondPart,thirdPart);

        //Managing Overall Layout
        JFrame mainFrame = new JFrame("List of Records");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(firstPart, BorderLayout.CENTER);
        mainFrame.add(fill(), BorderLayout.WEST);
        mainFrame.add(fill(), BorderLayout.EAST);
        mainFrame.add(fill(), BorderLayout.NORTH);
        mainFrame.add(southAlign, BorderLayout.SOUTH);
        mainFrame.setBounds(700,150,440,475);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    private static void addRecords() {
        //Initializes Add to Records Main Frame
        JDialog addFrame = new JDialog();
        addFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        addFrame.setTitle("Add Records");

        //Initializes all JLabels
        JLabel name = new JLabel("Name:");
        JLabel bday = new JLabel("Birthday:     ");
        JLabel mm = new JLabel("mm");
        JLabel dd = new JLabel("dd");
        JLabel yyyy = new JLabel("yyyy");

        //Initializes all Inputs
        JTextField nameInput = new JTextField();
        JComboBox<String> month = new JComboBox();
        List<String> months = Arrays.asList("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
        for(String perMonth:months) month.addItem(perMonth);
        JComboBox<Integer> day = new JComboBox();
        for(int i = 1; i <= 31; i++) day.addItem(i);
        JComboBox<Integer> year = new JComboBox();
        for(int i = 1900; i <= Integer.parseInt(LocalDate.now().toString().substring(0,4)); i++) year.addItem(i);

        //Managing Layout for Labels and Inputs
        JPanel centerPanel = new JPanel(new GridBagLayout());
        addGridBag(centerPanel, name,0 ,0);
        addGridBag(centerPanel, bday, 0, 1);
        addGridBag(centerPanel, month, 1, 1);
        addGridBag(centerPanel, day, 2, 1);
        addGridBag(centerPanel, year, 3, 1);
        addGridBag(centerPanel, mm,1, 2);
        addGridBag(centerPanel, dd,2, 2);
        addGridBag(centerPanel, yyyy,3, 2);
        addGridBag(centerPanel, nameInput, 1, 0, GridBagConstraints.HORIZONTAL, 3);

        //Initializes all Buttons
        JButton saveAndBack = new JButton("Save and Go Back");
        saveAndBack.addActionListener(e -> {
            try {
                Period p = Period.between(LocalDate.of((int) year.getSelectedItem(), months.indexOf(month.getSelectedItem()) + 1, (int) day.getSelectedItem()), LocalDate.now());
                if (p.getDays() < 0) throw new IllegalArgumentException();
                records.add(new Person(nameInput.getText(), LocalDate.of((int) year.getSelectedItem(), months.indexOf(month.getSelectedItem()) + 1, (int) day.getSelectedItem())));
                updateSort();
                addFrame.dispose();
            } catch (DateTimeException | IllegalArgumentException exception) { JOptionPane.showMessageDialog(null, "An IllegalArgumentException Caught: Invalid Date!",  "Error Screen", JOptionPane.ERROR_MESSAGE); }
        });
        JButton saveAndAdd = new JButton("Save & Add Another");
        saveAndAdd.addActionListener(e -> {
            try {
                Period p = Period.between(LocalDate.of((int)year.getSelectedItem(), months.indexOf(month.getSelectedItem())+1, (int)day.getSelectedItem()), LocalDate.now());
                if (p.getDays() < 0 || p.getMonths() < 0) throw new IllegalArgumentException();
                records.add(new Person(nameInput.getText(), LocalDate.of((int)year.getSelectedItem(), months.indexOf(month.getSelectedItem())+1, (int)day.getSelectedItem())));
                updateSort();
            }
            catch (DateTimeException | IllegalArgumentException exception) { JOptionPane.showMessageDialog(null, "An IllegalArgumentException Caught: Invalid Date!",  "Error Screen", JOptionPane.ERROR_MESSAGE); }
        });
        JButton back = new JButton("Back");
        back.addActionListener(e -> addFrame.dispose());
        JPanel buttons = new JPanel(new GridLayout(1, 3));
        add(buttons, saveAndBack, saveAndAdd, back);

        //Managing Overall Layout
        addFrame.add(centerPanel, BorderLayout.CENTER);
        addFrame.add(buttons, BorderLayout.SOUTH);
        addFrame.pack();
        addFrame.setResizable(false);
        addFrame.setVisible(true);
    }

    private static void removeRecords() {
        //Initializes Remove a Record Main Frame
        JDialog removeFrame = new JDialog();
        removeFrame.setTitle("Remove Record");
        removeFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        //Initializes Name and Input
        JLabel name = new JLabel("Name:");
        JTextField nameInput = new JTextField();

        //Initializes Buttons
        JButton removeAndBack = new JButton("Remove and Go Back");
        removeAndBack.addActionListener(e -> {
            try {
                boolean found = false;
                Iterator<Person> iterator = records.iterator();
                while(iterator.hasNext()){
                    Person nextP = iterator.next();
                    if(nextP.getName().equals(nameInput.getText())) {
                        found = true;
                        iterator.remove();
                        updateSort();
                        removeFrame.dispose();
                    }
                }
                if (!found) throw new IllegalArgumentException();
            } catch (IllegalArgumentException exception){
                JOptionPane.showMessageDialog(null, "An IllegalArgumentException Caught: Name Not Found!",  "Error Screen", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton saveAndRemove = new JButton("Save and Remove");
        saveAndRemove.addActionListener(e -> {
            try {
                boolean found = false;
                Iterator<Person> iterator = records.iterator();
                while(iterator.hasNext()){
                    Person nextP = iterator.next();
                    if(nextP.getName().equals(nameInput.getText())) {
                        found = true;
                        iterator.remove();
                        updateSort();
                    }
                }
                if (!found) throw new IllegalArgumentException();
            } catch (IllegalArgumentException exception){
                JOptionPane.showMessageDialog(null, "An IllegalArgumentException Caught: Name Not Found!",  "Error Screen", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton back = new JButton("Back");
        back.addActionListener(e -> removeFrame.dispose());
        JPanel buttons = new JPanel(new GridLayout(1, 3));
        add(buttons, removeAndBack, saveAndRemove, back);

        //Manages Overall Layout
        removeFrame.add(buttons, BorderLayout.SOUTH);
        removeFrame.add(name, BorderLayout.WEST);
        removeFrame.add(nameInput, BorderLayout.CENTER);
        removeFrame.pack();
        removeFrame.setResizable(false);
        removeFrame.setVisible(true);

    }

    private static void addGridBag(Container C, Component c, int x, int y) {
        addGridBag(C, c, x, y, 0, 0);
    }

    private static void addGridBag(Container C, Component c,  int x, int y, int constraint, int thickness) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = x;
        gc.gridy = y;
        if(constraint == GridBagConstraints.HORIZONTAL) gc.gridwidth = thickness;
        else if(constraint == GridBagConstraints.VERTICAL) gc.gridheight = thickness;
        if(constraint != 0) gc.fill = constraint;
        C.add(c, gc);
    }

    private static void add(Container c, Component... parts){
        for(Component part: parts) c.add(part);
    }

    private static void updateSort(){
        textArea.setText("\tNAMES\tBIRTHDAY\tAGE\n");
        if(dropDown.getSelectedItem().equals("Name")) {
            Collections.sort(records, new Comparator<Person>() {
                @Override
                public int compare(Person o1, Person o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
        else if(dropDown.getSelectedItem().equals("Age")) { Collections.sort(records, Comparator.comparingInt(Person::getAge)); }
        else{
            Collections.sort(records, new Comparator<Person>() {
                @Override
                public int compare(Person o1, Person o2) {
                    return o2.getBDay().compareTo(o1.getBDay());
                }
            });
        }
        if (desRadioB.isSelected()) Collections.reverse(records);
        for (Person p: records) textArea.append("\t"+p.getName()+"\t"+p.getBirthDay()+"\t"+p.getAge()+"\n");
    }

    private static JPanel fill() { return new JPanel(); }
}

class Person{
    private String name;
    private LocalDate birthDay;
    private int age;

    Person(){
        this.name = "";
        this.birthDay = null;
        this.age = 0;
    }

    Person(String name, LocalDate birthDay){
        this.name = name;
        this.birthDay = birthDay;
        this.age = computeAge();
    }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getBDay(){
        return this.birthDay.toString();
    }
    public String getBirthDay() { return DateTimeFormatter.ofPattern("MM/dd/uuuu").format(birthDay); }
    public void setBirthDay(LocalDate birthDay) { if(birthDay != null) this.birthDay = birthDay; }

    public int getAge() { return this.age; }
    public void setAge(int age) { this.age = age; }

    public int computeAge(){
        LocalDate today = LocalDate.now();
        LocalDate birthday = this.birthDay;
        Period p = Period.between(birthday, today);
        return p.getYears();
    }
}

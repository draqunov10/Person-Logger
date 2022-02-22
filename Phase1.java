//GUI
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

//Button ActionHandlers
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.*;
import javax.swing.JOptionPane;

//Exceptions
import java.io.IOException;

public class Phase1 implements ActionListener{
    //instatiate containers
    private JFrame frame;
    private JPanel panel;

    //instantiate components
    private  JLabel userLabel,pwLabel;
    private  JTextField userTF;
    private  JPasswordField pwPF;
    private  JButton loginBT;
    private int FailLoginCounter = 0;

    Phase1(){
        //TitleBar
        frame = new JFrame("Login Screen");

        //Body
        panel = new JPanel();
        userLabel = new JLabel("Username: ");
        userTF = new JTextField(20);
        pwLabel = new JLabel("Password: ");
        pwPF = new JPasswordField(20);
        loginBT = new JButton("Login");
    }

    public void launchFrame(){
        //Sizes
        frame.setSize(new Dimension(325,160));
        panel.setLayout(null);
        userLabel.setBounds(35, 20, 80, 25);
        userTF.setBounds(105, 20, 165, 25);
        pwLabel.setBounds(35, 45, 80, 25);
        pwPF.setBounds(105, 45, 165, 25);
        loginBT.setBounds(105, 85, 80, 25);

        //GUI
        frame.add(panel);
        panel.add(userLabel);
        panel.add(userTF);
        panel.add(pwLabel);
        panel.add(pwPF);
        panel.add(loginBT);
        frame.setVisible(true);

        //Functions
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginBT.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae){
        File file = new File("loginCredentials.txt");
        if (!file.exists()) JOptionPane.showMessageDialog(null, "loginCredentials.txt is not found", "Error Screen", JOptionPane.ERROR_MESSAGE);
        else {
            //Creates a HashMap of loginCredentials.txt
            HashMap<String, String> user_pw = new HashMap<>();

            //Reads loginCredentials.txt
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String s = br.readLine();
                do user_pw.put(s, br.readLine());
                while ((s = br.readLine()) != null);
            } catch (IOException ignore) { }

            //Verify Account
            if (String.valueOf(pwPF.getPassword()).equals(user_pw.get(userTF.getText()))) {
                frame.dispose();
                Phase2.listOfRecords();
            }
            else {
                //Incorrect Username/Password
                JOptionPane.showMessageDialog(null, "Incorrect Username/Password", "Error Screen", JOptionPane.ERROR_MESSAGE);
                userTF.setText("");
                pwPF.setText("");
                if (++FailLoginCounter == 3) {
                    JOptionPane.showMessageDialog(null, "Sorry, you have reached the limit of 3 tries, good bye!", "Error Screen", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
        }
    }
    public static void main(String[] args) {
        Phase1 login = new Phase1();
        login.launchFrame();
    }
}
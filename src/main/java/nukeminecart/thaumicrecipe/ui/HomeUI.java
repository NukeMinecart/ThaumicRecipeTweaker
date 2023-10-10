package nukeminecart.thaumicrecipe.ui;

import javax.swing.*;

public class HomeUI extends JFrame{
    private JPanel home;
    private JButton createButton;
    private JButton loadButton;
    private JTextField createConfig;
    private JTextField loadConfig;

    public HomeUI(){
        setTitle("Home");
        setContentPane(home);
        setVisible(true);
    }
}

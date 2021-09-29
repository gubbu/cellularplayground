import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.awt.Point;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.awt.Color;
import automata_base.CellularAutomata;
import automata_base.RuleBase;
import java.util.Random;

public class GridAffectorUI implements ActionListener {
    static final int WIDTH = 20;
    static final int HEIGHT = 20;
    CellularAutomata cellular_automata = new CellularAutomata(WIDTH, HEIGHT);
    JFrame thisframe;
    HashMap<Point, JButton> buttonmap;
    JButton play_button;
    JButton clear_button;
    JButton randomize_button;
    JTextArea textarea_for_weight_setting;
    JComboBox<String> ruleset_selector;

    {
        buttonmap = new HashMap<>();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                String xfromat = String.format("%02d", x);
                String yfromat = String.format("%02d", y);
                String coordinate_format = "(" + xfromat + "," + yfromat + ")";
                JButton btn = new JButton(coordinate_format);
                btn.setBackground(new Color(0xff000000));
                buttonmap.put(new Point(x, y), btn);
                btn.addActionListener(this);
            }
        }
    }

    {
        thisframe = new JFrame();
        thisframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    {
        thisframe.getContentPane().setLayout(new GridBagLayout());
        for (var entry : buttonmap.entrySet()) {
            Point position = entry.getKey();
            JButton btn = entry.getValue();
            GridBagConstraints cons = new GridBagConstraints();
            cons.gridx = position.y;
            cons.gridy = position.x;
            thisframe.getContentPane().add(btn, cons);
        }
    }

    {
        // adding a test button for layout constraints...
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = HEIGHT;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.gridwidth = 3;
        clear_button = new JButton("Clear");
        thisframe.getContentPane().add(clear_button, cons);
        clear_button.addActionListener(this);
    }

    {
        // adding play button
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 3;
        cons.gridy = HEIGHT;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.gridwidth = 3;
        play_button = new JButton("Play");
        play_button.addActionListener(this);
        thisframe.getContentPane().add(play_button, cons);
    }

    {
        // adding Entry
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 6;
        cons.gridy = HEIGHT;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.gridwidth = 3;
        textarea_for_weight_setting = new JTextArea();
        textarea_for_weight_setting.setText("1");
        thisframe.getContentPane().add(textarea_for_weight_setting, cons);
    }

    {
        // adding randomize Button
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = HEIGHT + 1;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.gridwidth = 3;
        randomize_button = new JButton("Randomize");
        randomize_button.addActionListener(this);
        thisframe.getContentPane().add(randomize_button, cons);
    }

    {
        // adding randomize Button
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 3;
        cons.gridy = HEIGHT + 1;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.gridwidth = 3;
        String[] choices = RuleBase.RULES_BASE.keySet().toArray(String[]::new);
        ruleset_selector = new JComboBox<String>(choices);
        // randomize_button.addActionListener(this);
        thisframe.getContentPane().add(ruleset_selector, cons);
    }

    {
        thisframe.pack();
    }

    public GridAffectorUI() {
        this.thisframe.setVisible(true);
    }

    public static void main(String[] args) {
        new GridAffectorUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (var entry : this.buttonmap.entrySet()) {
            if (e.getSource() == entry.getValue()) {
                System.out.println(entry.getKey().toString() + "was pressed");
                int userinteger = 0;
                try {
                    userinteger = Integer.parseInt(textarea_for_weight_setting.getText());
                } catch (NumberFormatException exc) {
                    System.out.println("Cant parse your input as an integer.");
                }

                int xcoordinate = entry.getKey().x;
                int ycoordinate = entry.getKey().y;
                cellular_automata.set_int(xcoordinate, ycoordinate, userinteger);
                /*
                 * System.out.println("User Integer: "+userinteger); if (userinteger == 0){
                 * entry.getValue().setBackground(Color.BLACK); }else{
                 * entry.getValue().setBackground(Color.GREEN); }
                 */
            }
        }
        if (e.getSource() == play_button) {
            System.out.println("play: " + ruleset_selector.getSelectedItem());
            CellularAutomata.RulesInterface selected_game = RuleBase.RULES_BASE.get(ruleset_selector.getSelectedItem());
            cellular_automata.apply_rules(0, selected_game);
        } else if (e.getSource() == clear_button) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    cellular_automata.set_int(x, y, 0);
                }
            }
        } else if (e.getSource() == randomize_button) {
            this.randomize();
        }

        update_button_grid_colors();
    }

    public void randomize() {
        var rand = new Random();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int ther = rand.nextInt(10000);
                // chances of 31,25%
                if (ther < 3125) {
                    cellular_automata.set_int(x, y, 1);
                } else {
                    cellular_automata.set_int(x, y, 0);
                }
            }
        }
    }

    public void update_button_grid_colors() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int gridint = cellular_automata.get_int(x, y);
                Point gridpoint = new Point(x, y);
                // TODO: allow color shemes...
                Color background = Color.BLACK;
                if (gridint >= 1) {
                    background = Color.GREEN;
                }
                this.buttonmap.get(gridpoint).setBackground(background);
            }
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * MenuApp
 *
 * A Swing GUI with a top menu bar containing one menu of four items:
 *   1. Show Date/Time  - prints the current date and time into a text box
 *   2. Save to Log      - writes the text box contents to log.txt
 *   3. Random Green Hue - changes the frame background to a random green
 *                         hue, chosen once per program run and reused
 *                         every time this item is selected
 *   4. Exit              - closes the program
 */
public class MenuApp extends JFrame implements ActionListener {

    private final JTextArea textArea;
    private final JMenuItem dateTimeItem;
    private final JMenuItem saveLogItem;
    private final JMenuItem greenHueItem;
    private final JMenuItem exitItem;
    private Color randomGreenHue;

    public MenuApp() {
        super("Menu Application");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");

        dateTimeItem = new JMenuItem("Show Date/Time");
        saveLogItem = new JMenuItem("Save to Log");
        greenHueItem = new JMenuItem("Random Green Hue");
        exitItem = new JMenuItem("Exit");

        dateTimeItem.addActionListener(this);
        saveLogItem.addActionListener(this);
        greenHueItem.addActionListener(this);
        exitItem.addActionListener(this);

        optionsMenu.add(dateTimeItem);
        optionsMenu.add(saveLogItem);
        optionsMenu.add(greenHueItem);
        optionsMenu.add(exitItem);

        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);

        // Pick one random green hue for this run; reused on every selection.
        randomGreenHue = generateRandomGreenHue();
    }

    /**
     * Builds a random shade of green by varying the hue within the
     * green portion of the HSB color wheel (roughly 90-150 degrees),
     * matching the range shown on the HTML color picker for "green".
     */
    private Color generateRandomGreenHue() {
        Random random = new Random();
        float hue = 90f / 360f + random.nextFloat() * (60f / 360f);
        float saturation = 0.6f + random.nextFloat() * 0.4f;
        float brightness = 0.6f + random.nextFloat() * 0.4f;
        return Color.getHSBColor(hue, saturation, brightness);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == dateTimeItem) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d, yyyy hh:mm:ss a");
            textArea.append(formatter.format(new Date()) + System.lineSeparator());

        } else if (source == saveLogItem) {
            try (FileWriter writer = new FileWriter("log.txt")) {
                writer.write(textArea.getText());
                JOptionPane.showMessageDialog(this,
                        "Text box contents saved to log.txt",
                        "Saved",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Unable to write log.txt: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } else if (source == greenHueItem) {
            getContentPane().setBackground(randomGreenHue);
            textArea.setBackground(randomGreenHue);

        } else if (source == exitItem) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuApp app = new MenuApp();
            app.setVisible(true);
        });
    }
}

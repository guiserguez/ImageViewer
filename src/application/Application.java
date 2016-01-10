package application;

import control.Command;
import control.NextImageCommand;
import control.PrevImageCommand;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Image;
import view.ImageDisplay;

public class Application extends JFrame {

    private final Map<String, Command> commands = new HashMap<>();
    private ImageDisplay imageDisplay;

    public static void main(String[] args) {
        new Application().setVisible(true);
    }

    public Application() {      
        this.deployComponents();
        this.createCommands();
    }

    private void deployComponents() {
        this.setTitle("Image Viewer");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 600));
        this.setLocationRelativeTo(null);
        this.getContentPane().add(imagePanel());
        this.getContentPane().add(toolbar(), BorderLayout.SOUTH);
    }

    private void createCommands() {
        commands.put("next", new NextImageCommand(imageDisplay));
        commands.put("prev", new PrevImageCommand(imageDisplay));
    }

    private ImagePanel imagePanel() {
        ImagePanel panel = new ImagePanel(firstImage());
        this.imageDisplay = panel;
        return panel;
    }

    private Image firstImage() {
        return new FileImageViewer(this.getFolderPath()).read();
    }
    
    private String getFolderPath() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);       
        if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) System.exit(ABORT);
        return fileChooser.getSelectedFile().getAbsolutePath();
    }

    private JPanel toolbar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(prevButton());
        panel.add(nextButton());
        return panel;
    }

    private JButton prevButton() {
        JButton button = new JButton("<");
        button.addActionListener(doCommand("prev"));
        return button;
    }

    private JButton nextButton() {
        JButton button = new JButton(">");
        button.addActionListener(doCommand("next"));
        return button;
    }

    private ActionListener doCommand(String operation) {
        return (ActionEvent event) -> Application.this.commands.get(operation).execute();
    }

}

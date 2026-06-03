
/*Functionality: created an airline object Loads saved data, 
then opens first GUI StartFrame. File saving happens when window closes.*/ 
// import javax.swing.SwingUtilities;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Main {
    public static void main(String[] args) {
        try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

        AirlineSystem system = new AirlineSystem();

        FileHandler.loadSystem(system);

        SwingUtilities.invokeLater(() -> {
            StartFrame startgui = new StartFrame(system);
            startgui.setVisible(true);
        } );
    }
} 


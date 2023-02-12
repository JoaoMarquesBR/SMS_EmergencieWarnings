import javax.swing.*;

public class ExceptionsChecks {
    static JFrame warning;

    public static void empIDCheck(){
        warning = new JFrame();
        JOptionPane.showMessageDialog(warning,"Employee ID Hasn't been properly inserted.");

    }

    public static void phoneCheck(){
        warning = new JFrame();
        JOptionPane.showMessageDialog(warning,"Number field hasn't been full filled.");
    }
    public static void phoneUsed(){
        warning = new JFrame();
        JOptionPane.showMessageDialog(warning,"PhoneNumber already in use.");

    }
    public static void nameCheck(){
        warning = new JFrame();
        JOptionPane.showMessageDialog(warning,"Name hasn't been set.");
    }
    public static void sectionCheck(){
        warning = new JFrame();
        JOptionPane.showMessageDialog(warning,"Employee should have one department selected.");
    }
    public static void sectionCheck2(){
        warning = new JFrame();
        JOptionPane.showMessageDialog(warning,"Employee should have only ONE dept selected.");
    }

    public static void messageWasSent(){
        warning = new JFrame();
        JOptionPane.showMessageDialog(warning,"Message was sent.");
    }
}

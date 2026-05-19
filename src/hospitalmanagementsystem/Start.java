package hospitalmanagementsystem;

import hospitalmanagementsystem.gui.HospitalGUI;

import javax.swing.SwingUtilities;

public class Start
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(HospitalGUI::new);
    }
}
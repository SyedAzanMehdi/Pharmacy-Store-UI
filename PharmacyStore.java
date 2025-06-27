import javax.swing.*;
class PharmacyStore{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                JFrame.setDefaultLookAndFeelDecorated(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Login login = new Login();
            login.setVisible(true);
        });
    }
}
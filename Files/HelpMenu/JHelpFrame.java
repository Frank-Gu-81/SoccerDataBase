package HelpMenu;


import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.Document;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class JHelpFrame extends JFrame implements HyperlinkListener {

    protected Container contentPane;

    JEditorPane help;

    public JHelpFrame(String helpFileName) {
        contentPane = getContentPane();
        URL url = ClassLoader.getSystemResource(helpFileName);
        try {
            help = new JEditorPane(url);
            help.addHyperlinkListener(this);
            help.setEditable(false);

            // Set up the scrollpane.
            JScrollPane helpScroll = new JScrollPane();
            helpScroll.getViewport().add(help);
            contentPane.add(BorderLayout.CENTER, helpScroll);
            setSize(800, 600);

            // Dispose of window on close.
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(this, "Could not find "+
                    helpFileName, "Help Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not find "+
                    helpFileName, "Help Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            final URL link = e.getURL();

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    Document lastPage = help.getDocument();
                    try {
                        help.setPage(link);
                    } catch (Exception ex) {
                        help.setDocument(lastPage);
                    }
                    Container parent = help.getParent();
                    parent.repaint();
                }
            });
        }
    }
}

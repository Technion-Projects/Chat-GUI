package chatPackage;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;

public class UserDialog extends JPanel implements Observer {
    private static final int ENTER = 10;
    private static final long serialVersionUID = 1L;

    private JTextPane textArea;
    private JTextPane textInsertion;

    private Chat chat;
    private User user;


    public UserDialog(User user, Chat chat, JFrame frame){
        this.user = user;
        this.chat = chat;

        textArea = new JTextPane();
        textArea.setEditable(false);
        JScrollPane scrlTextArea = new JScrollPane(textArea);
        scrlTextArea.setPreferredSize(new Dimension(400, 70));
        JLabel lblTextArea = new JLabel(user.getName() + " Chat:");
        lblTextArea.setLabelFor(textArea);

        textInsertion = new JTextPane();
        textInsertion.setEditable(true);
        textInsertion.setPreferredSize(new Dimension(50, 20));
        JLabel lblTextInsertion = new JLabel("Enter Text:");
        lblTextInsertion.setLabelFor(textInsertion);



        textInsertion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == ENTER) {
                    chat.addLine(new ChatLine(user, textInsertion.getText()));
                    textInsertion.setText("");
                    textInsertion.requestFocusInWindow();
                    textInsertion.setCaretPosition(0);
                    keyEvent.consume();
                }

            }
        });
        // arrange components on grid
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(gridbag);

        c.fill = GridBagConstraints.BOTH;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(0,0,0,0);
        gridbag.setConstraints(lblTextArea, c);
        this.add(lblTextArea);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 5;
        c.insets = new Insets(0,0,0,0);
        gridbag.setConstraints(scrlTextArea, c);
        this.add(scrlTextArea);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(0,20,0,0);
        gridbag.setConstraints(lblTextInsertion, c);
        this.add(lblTextInsertion);

        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.insets = new Insets(0,20,20,0);
        gridbag.setConstraints(textInsertion, c);
        this.add(textInsertion);

    }

    public void update() {
        textArea.setText("");
        StyledDocument doc = textArea.getStyledDocument();

        Style owner = textArea.addStyle("owner", null);
        StyleConstants.setForeground(owner, Color.GREEN);

        Style otherUser = textArea.addStyle("otherUser", null);
        StyleConstants.setForeground(otherUser, Color.black);

        textArea.setFont(new Font(chat.getFont(), chat.getBold(), 12));

        Iterator it = chat.getChatLines();
        Style currentStyle;
        while (it.hasNext()){
            ChatLine line = (ChatLine)it.next();
            if (line.identify() == user.getID())
                currentStyle = owner;
            else{
                currentStyle = otherUser;
            }
            try { doc.insertString(doc.getLength(), line.toString(),currentStyle); }
            catch (BadLocationException e){}
        }
    }
}

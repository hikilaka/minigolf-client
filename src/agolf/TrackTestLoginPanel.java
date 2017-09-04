package agolf;

import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
class TrackTestLoginPanel extends Panel implements ActionListener, KeyListener {

    private GameApplet gameApplet;
    private int width;
    private int height;
    private TextField textFieldName;
    private TextField textFieldPassword;
    private Button buttonOk;
    private Label labelError;
    private Pattern pattern;
    private Label labelName;
    private Label labelName2;
    private Label labelPassword;
    private Label labelPassword2;
    private Image backgroundImage;

    protected TrackTestLoginPanel(GameApplet gameApplet, int width, int height) {
        this.gameApplet = gameApplet;
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        pattern = Pattern.compile("[^a-zA-Z0-9 ]");
        this.create();
    }

    public void addNotify() {
        super.addNotify();
        this.repaint();
    }

    public void paint(Graphics var1) {
        this.update(var1);
    }

    public void update(Graphics g) {
        g.setColor(GameApplet.colourGameBackground);
        g.fillRect(0, 0, this.width, this.height);
        
        if (backgroundImage != null) {
        	int x = (width / 2) - (backgroundImage.getWidth(null) / 2);
        	g.drawImage(backgroundImage, x, 5, null);
        }
    }

    public void actionPerformed(ActionEvent evt) {
        String username = this.textFieldName.getText().trim();
        String password = this.textFieldPassword.getText().trim();
        this.gameApplet.trackTestLogin(username, password);
    }

    public void keyPressed(KeyEvent evt) {
        if(evt.getKeyCode() == KeyEvent.VK_ENTER && buttonOk.isEnabled()) {
            actionPerformed(null);
            return;
        }
        boolean found = pattern.matcher(textFieldName.getText()).find() || textFieldName.getText().trim().equals("");
        if(found) {
            labelError.setVisible(true);
            buttonOk.setEnabled(false);
        } else {
            labelError.setVisible(false);
            buttonOk.setEnabled(true);
        }
    }

    private int NAMEOFF = 10; // -60;
    private int PASSOFF = NAMEOFF + 50; // -10;
    private int OKOFF = PASSOFF + 60; // +50;
    private int ERROFF = NAMEOFF + 25; // -35;

    private void create() {
        setLayout(null);
        backgroundImage = gameApplet.imageManager.getImage("login-banner");
        textFieldName = new TextField("");//("(name)");
        textFieldName.setBounds(width / 2 - 75, (height / 2) + NAMEOFF, 150, 25);
        textFieldName.setBackground(Color.white);
        textFieldName.setForeground(Color.black);
        textFieldName.addKeyListener(this);
        add(textFieldName);
        textFieldName.requestFocus();
        textFieldPassword = new TextField("");//("(password)");
        textFieldPassword.setBounds(width / 2 - 75, (height / 2) + PASSOFF, 150, 25);
        textFieldPassword.setBackground(Color.white);
        textFieldPassword.setForeground(Color.black);
        textFieldPassword.setEchoChar('*');
        add(textFieldPassword);
        buttonOk = new Button("OK");
        buttonOk.setBounds(width / 2 - 75, (height / 2) + OKOFF, 75, 25);
        buttonOk.addActionListener(this);
        add(this.buttonOk);
        labelError = new Label("Only spaces, alphabetical and numerical characters are allowed");
        labelError.setBounds(width / 2 - 75, (height / 2) + ERROFF, 400, 25);
        labelError.setForeground(Color.red);
        labelError.setVisible(false);
        add(labelError);
        labelName = new Label("Name:");
        labelName.setBounds(width / 2 - 150, (height / 2) + NAMEOFF, 75, 25);
        add(labelName);
        labelName2 = new Label("(optional)");
        labelName2.setBounds(width / 2 + 80, (height / 2) + NAMEOFF, 75, 25);
        labelName2.setForeground(Color.red);
        add(labelName2);
        labelPassword = new Label("Password:");
        labelPassword.setBounds(width / 2 - 150, (height / 2) + PASSOFF, 75, 25);
        add(labelPassword);
        labelPassword2 = new Label("(optional)");
        labelPassword2.setBounds(width / 2 + 80, (height / 2) + PASSOFF, 75, 25);
        labelPassword2.setForeground(Color.red);
        add(labelPassword2);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}

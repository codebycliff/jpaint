package edu.iastate.cs319.javapainter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import edu.iastate.cs319.javapainter.UserCredentials.Credentials;


public class LoginDialog extends JDialog {

    
    public LoginDialog(Frame c, int maxAttempts) {
        mJavaPainter = (JavaPainter)c;
        mMaxAttempts = maxAttempts;
        mLoginAttempts = 0;
        initComponents();
    }
 
    private void initComponents() {

        setTitle("Java-Painter: Login");
        
        mUserNameField = new JTextField();
        mPasswordField = new JPasswordField();
        mNewUserButton = new JButton();
        mClearButton = new JButton();
        mLoginButton = new JButton();
        
        FormListener formListener = new FormListener();

        mUserNameField.addKeyListener(formListener);
        mPasswordField.addKeyListener(formListener);
        mNewUserButton.setText("New User");
        mNewUserButton.setEnabled(false);
        mNewUserButton.addActionListener(formListener);
        mClearButton.setText("Clear");
        mClearButton.setEnabled(false);
        mClearButton.addActionListener(formListener);
        mLoginButton.setText("Login");
        mLoginButton.setEnabled(false);
        mLoginButton.addActionListener(formListener);
      

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 1, -100, 0));
        formPanel.add(new JLabel("Username"));
        formPanel.add(mUserNameField);
        formPanel.add(new JLabel("Password"));
        formPanel.add(mPasswordField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));        
        buttonPanel.add(mNewUserButton);
        buttonPanel.add(mClearButton);
        buttonPanel.add(mLoginButton);

        getContentPane().add(formPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setResizable(false);
        pack();
    }

    private class FormListener implements ActionListener,  KeyListener {
        FormListener() {}
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == mNewUserButton) {
                LoginDialog.this.registerNewUser(evt);
            }
            else if (evt.getSource() == mClearButton) {
                LoginDialog.this.clearFormContents(evt);
            }
            else if (evt.getSource() == mLoginButton) {
                LoginDialog.this.attemptLogin(evt);
            }
        }

        public void keyPressed(KeyEvent evt) {
        }

        public void keyReleased(KeyEvent evt) {
        }

        public void keyTyped(KeyEvent evt) {
            if (evt.getSource() == mPasswordField) {
                LoginDialog.this.fieldContentsChanged(evt);
            }
            else if (evt.getSource() == mUserNameField) {
                LoginDialog.this.fieldContentsChanged(evt);
            }
        }
    }

    private void registerNewUser(ActionEvent evt) {
        String userName = mUserNameField.getText();
        char[] password = mPasswordField.getPassword();

        if(!userName.isEmpty() && password.length > 0) {
            UserCredentials.addUser(userName, password);
            UserCredentials.write();
            JOptionPane.showMessageDialog(this, "User Added!");
            
            setVisible(false);
            mJavaPainter.open(userName);
        }

       
        
    }

    private void clearFormContents(ActionEvent evt) {
        mPasswordField.setText("");
        mUserNameField.setText("");
        mClearButton.setEnabled(false);
        mNewUserButton.setEnabled(false);
        mLoginButton.setEnabled(false);
    }

    private void fieldContentsChanged(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldContentsChanged
        boolean shouldEnable = false;
        if(!mUserNameField.getText().isEmpty() && mPasswordField.getPassword().length > 0) {
            shouldEnable = true;
        }
        if(UserCredentials.hasUsers()) {
            mLoginButton.setEnabled(shouldEnable);
        }
        mClearButton.setEnabled(shouldEnable);
        mNewUserButton.setEnabled(shouldEnable);
    }

    private void attemptLogin(ActionEvent evt) {
        String userName = mUserNameField.getText();
        char[] password = mPasswordField.getPassword();
        boolean isValid = UserCredentials.validate(new Credentials(userName, password));
        if(isValid) {
            setVisible(false);
            JavaPainter painter = new JavaPainter();
            painter.open(userName);
        } else {
            JOptionPane.showMessageDialog(this, "The username/password typed is invalid!", "Invalid Credentials", JOptionPane.WARNING_MESSAGE);
            clearFormContents(evt);
            if(++mLoginAttempts == mMaxAttempts) {
                JOptionPane.showMessageDialog(this, "You have reach the max login attempts!\n\nLogin has been locked", "Login Locked", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        }
    }
    private JButton mClearButton;
    private JButton mLoginButton;
    private JButton mNewUserButton;
    private JPasswordField mPasswordField;
    private JTextField mUserNameField;
    private JavaPainter mJavaPainter;
    private int mMaxAttempts;
    private int mLoginAttempts;
}

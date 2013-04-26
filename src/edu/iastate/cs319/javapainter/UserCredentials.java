package edu.iastate.cs319.javapainter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JOptionPane;

class UserCredentials {

    
    static {
        mCredentialMap = new HashMap<String, String>();
        mCredentialsFile = new File("Credentials.txt");
        load();
    }
    
    public static boolean hasUsers() {
        return !mCredentialMap.isEmpty();
    }
    
    public static void load() {
        if(!mCredentialsFile.exists()) {
            return;
        }
        
        BufferedReader reader;
        String line;
        try {
            reader = new BufferedReader(new FileReader(mCredentialsFile));
            
            line = reader.readLine();
            while(line != null) {
                String[] credentials = line.split("=");
                String username = credentials[0].trim();
                String password = credentials[1].trim();
                mCredentialMap.put(username, password);
                line = reader.readLine();
            }
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage() + "\n\n");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println(e.getMessage() + "\n\n");
            e.printStackTrace();
        }
        
    }

    public static void write() {
        try {
            FileWriter writer = new FileWriter(mCredentialsFile);
            
            for(String key : mCredentialMap.keySet()) {
                writer.write(key + "=" + mCredentialMap.get(key) + "\n");
            }
            
            writer.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage() + "\n\n");
            e.printStackTrace();
        }
    }
    
    public static boolean validate(Credentials creds) {
        String user = creds.getUserName();
        String pass = new String(creds.getPassword());
        Integer passHash = pass.hashCode();
        String passString = passHash.toString();
        
        if (userExists(user) && mCredentialMap.get(user).compareTo(passString) == 0) {
            return true;
        }
        JOptionPane.showMessageDialog(null, passString + " != " + mCredentialMap.get(user));
        return false;

    }
    
    public static void addUser(String username, char[] password) {
        Integer passHash = new String(password).hashCode();
        String passString = passHash.toString();
        mCredentialMap.put(username, passString);
    }
    
    public static boolean userExists(String username) {
        return mCredentialMap.containsKey(username);
    }
    
    //--------------------------------------------------------- Private Methods
    
    public static class Credentials {
        
        public Credentials(String username, char[] password) {
            mUserName = username;
            mPassword = password;
        }
        
        public String getUserName() {
            return mUserName;
        }
        
        public char[] getPassword() {
            return mPassword;
        }
        
        private String mUserName;
        private char[] mPassword;
    }

    //---------------------------------------------------------- Private Fields
    
    private static HashMap<String, String> mCredentialMap;
    private static File mCredentialsFile;
}

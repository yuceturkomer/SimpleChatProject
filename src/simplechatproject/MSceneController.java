package commpy;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author aliyasineser
 */
public class MSceneController implements Initializable {

    public Button connectButton;
    public TextField messageText;
    public OutputStream ostream;
    public InputStream istream;
    public Socket commSock;
    public ListView messageScreen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            commSock = new Socket("localhost", 21200);
            ostream = commSock.getOutputStream();
            istream = commSock.getInputStream();

            addMessageToOutputStream(ostream, new Date().toString());
            
            Thread recvThread = new Thread(new Runnable() {

                @Override
                public void run() {

                    try {

                        String message;
                        for (;;) {
                            System.out.println("Start to read.");
                            byte[] bytes = new byte[0x10000]; /* 0x10000 = 65536 */

                            int readsize = istream.read(bytes);
                            byte[] messageBytes = new byte[readsize];
                            for (int i = 0; i < readsize; i++) {
                                messageBytes[i] = bytes[i];
                            }
                            message = new String(messageBytes);
                            System.out.println("|" + message + "|");

                            addMessageToChat(message);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            recvThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeInfo() {
 
        try {
            addMessageToOutputStream(ostream, messageText.getText());

            ObservableList converstaion = messageScreen.getItems();
            Label label = new Label(messageText.getText());
            label.setAlignment(Pos.CENTER_RIGHT);
            converstaion.add(label);
            messageScreen.setItems(converstaion);

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }

    }

    public static byte[] getBytesFromInputStream(InputStream is) throws java.io.IOException {
        ByteArrayOutputStream res = new ByteArrayOutputStream();
        byte[] bytes = new byte[0x10000]; /* 0x10000 = 65536 */

        int numRead = 0;
        while ((numRead = is.read(bytes, 0, bytes.length)) >= 0) {
            res.write(bytes, 0, numRead);
        }

        return res.toByteArray();
    }

    public static void addMessageToOutputStream(OutputStream os, String message) throws java.io.IOException {

        os.write(message.getBytes());
        os.flush();

    }

    public void addMessageToChat(String messageString) {
        class OneShotTask implements Runnable {

            String messageToAddToChat;

            OneShotTask(String s) {
                messageToAddToChat = s;
            }

            public void run() {

                ObservableList converstaion = messageScreen.getItems();
                Label label = new Label(messageToAddToChat);
                
                converstaion.add(label);
                messageScreen.setItems(converstaion);

            }
        }
        Platform.runLater(new OneShotTask(messageString));
    }

}

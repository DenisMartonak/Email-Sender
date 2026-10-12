package utb.fai;

import java.net.*;
import java.io.*;

public class EmailSender {
    private Socket socket;
    private InputStream input;
    private OutputStream output;

    /*
     * Constructor opens Socket to host/port. If the Socket throws an exception
     * during opening,
     * the exception is not handled in the constructor.
     */
    public EmailSender(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        input = socket.getInputStream();
        output = socket.getOutputStream();

        readInput();
        sendCommand("HELO " + host);
        readInput();
    }

    /*
     * Sends email from an email address to an email address with some subject and
     * text.
     * If the Socket throws an exception during sending, the exception is not
     * handled by this method.
     */
    public void send(String from, String to, String subject, String text) throws IOException {
        sendCommand("MAIL FROM:<" + from + ">");
        readInput();

        sendCommand("RCPT TO:<" + to + ">");
        readInput();

        sendCommand("DATA");
        readInput();

        sendCommand("From: " + from);
        sendCommand("To: " + to);
        sendCommand("Subject: " + subject);
        sendCommand("");
        sendCommand(text);
        sendCommand(".");
        readInput();
    }

    /*
     * Sends QUIT and closes the socket
     */
    public void close() {
        try {
            sendCommand("QUIT");
            readInput();
            socket.close();
        } catch (IOException e) {
        }
    }

    private void sendCommand(String command) throws IOException {
        command += "\r\n";
        byte[] buffer = command.getBytes();
        output.write(buffer, 0, buffer.length);
        output.flush();
    }

    private void readInput() throws IOException {
        final byte[] response = new byte[1024];
        int len = input.read(response);
        if (len > 0) {
            System.out.write(response, 0, len);
        }
    }
}

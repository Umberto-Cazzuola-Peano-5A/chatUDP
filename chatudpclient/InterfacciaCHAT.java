/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatudpclient;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Umberto Cazzuola
 */
public class InterfacciaCHAT extends JFrame implements ActionListener {

    private JTextField toclient = new JTextField();
    private JTextField username = new JTextField();
    private JTextArea display = new JTextArea();
    private JButton send = new JButton("Send/Start Client");
    byte[] buffer, buffer1;
    DatagramSocket client;
    String messaggio;
    String IP_address = "127.0.0.1";
    InetAddress address = InetAddress.getByName(IP_address);

    public InterfacciaCHAT() throws SocketException, UnknownHostException {
        JPanel input = new JPanel();
        input.setLayout(new GridLayout(1, 3));
        input.add(username);
        input.add(toclient);
        input.add(send);

        JPanel output = new JPanel();
        output.setLayout(new GridLayout(1, 1));
        output.add(display);

        JPanel pnl = new JPanel();
        JScrollPane scroll = new JScrollPane(pnl);
        pnl.setLayout(new GridLayout(2, 3));
        this.setLayout(new GridLayout(2, 1));
        this.add(input);
        pnl.add(output);
        buffer = new byte[1024];
        buffer1 = new byte[1024];
        this.add(scroll);
        send.addActionListener(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setTitle("Chat Client");
        setSize(500, 300);
        setVisible(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client = new DatagramSocket();
                    while (true) {
                        DatagramPacket datapack = new DatagramPacket(buffer1, buffer1.length);
                        client.receive(datapack);
                        String msg = new String(datapack.getData());
                        display.append("\nServer:" + msg + "\n");
                    }
                } catch (Exception e) {
                }
            }
        }).start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(send)) {
            try {
                String message = toclient.getText();
                String username1 = username.getText();
                String msgFin = message + " " + username1;
                buffer1 = msgFin.getBytes();
                DatagramPacket sendpack = new DatagramPacket(buffer1, buffer1.length, InetAddress.getLoopbackAddress(), 9999);
                client.send(sendpack);
                display.append(username1 + " ha inviato: " + message + "\n");
                toclient.setText("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
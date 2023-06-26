package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class CustomFrame extends JFrame {
    private JTextArea textArea;
    private HandleFrameRequest handleFrameRequest;

    private void handleBtnRequest(List<Wine> wineList) {
        String result = handleFrameRequest.transformToJson(wineList);
        updateTextArea(result);
    }

    private void updateTextArea(String text) {
        textArea.setText(text);
    }

    private void newConex(){
        handleFrameRequest.closeConnections();
        Socket clientSocket = null;
        try {
            clientSocket = new Socket("127.0.0.1", 1234);
            System.out.println("New Request");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        handleFrameRequest = new HandleFrameRequest(clientSocket);

    }
    public CustomFrame(Socket clientSocket) {
        setTitle("Wines");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        handleFrameRequest = new HandleFrameRequest(clientSocket);

        JButton btnRed = new JButton("Show red wines");
        JButton btnWhite = new JButton("Show white wines");
        JButton btnName = new JButton("Sort by name");
        JButton btnPrice = new JButton("Sort by price");
        JButton btnExit = new JButton("EXIT");
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        JLabel label = new JLabel("JSON ricevuto:");
        container.add(label, BorderLayout.PAGE_START);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnRed);
        buttonPanel.add(btnWhite);
        buttonPanel.add(btnName);
        buttonPanel.add(btnPrice);
        buttonPanel.add(btnExit);

        container.add(buttonPanel, BorderLayout.PAGE_END);
        container.add(scrollPane, BorderLayout.CENTER);

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFrameRequest.closeConex();
                System.out.println("conx closed");
                dispose();  // Chiude l'interfaccia grafica
            }
        });

        btnRed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Wine> wineList = handleFrameRequest.getResponse("red");
                handleBtnRequest(wineList);

            }

        });
        btnWhite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Wine> wineList = handleFrameRequest.getResponse("white");
                handleBtnRequest(wineList);

            }

        });
        btnPrice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Wine> wineList = handleFrameRequest.getResponse("sorted_by_price");
                handleBtnRequest(wineList);
            }
        });



        btnName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Wine> wineList = handleFrameRequest.getResponse("sorted_by_name");
                handleBtnRequest(wineList);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Socket clientSocket = new Socket("127.0.0.1", 1234);
                    CustomFrame frame = new CustomFrame(clientSocket);
                    frame.setSize(600, 600);
                    frame.setVisible(true);
                    frame.setResizable(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
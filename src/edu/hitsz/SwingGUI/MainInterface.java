package edu.hitsz.SwingGUI;

import edu.hitsz.application.Game;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.template.EasyGame;
import edu.hitsz.template.HardGame;
import edu.hitsz.template.NormalGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

public class MainInterface {
    private JButton easyButton;
    private JPanel mainPanel;
    private JButton normalButton;
    private JButton hardButton;
    private JComboBox voiceBox;
    private JLabel voiceLabel;

    private String degree;
    private boolean bgmOn = true;


    public JPanel getMainPanel() {
        return mainPanel;
    }
    private Game game;

    public MainInterface() {
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                degree = "Easy";
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg.jpg"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                game = new EasyGame();
                game.degreeDefaultSet();
                game.setGameDegree(degree);
                game.setBgmOn(bgmOn);
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();
            }
        });

        normalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                degree = "Normal";
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg3.jpg"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                game = new NormalGame();
                game.degreeDefaultSet();
                game.setGameDegree(degree);
                game.setBgmOn(bgmOn);
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();
            }
        });

        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                degree = "Hard";

                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg5.jpg"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                game = new HardGame();
                game.degreeDefaultSet();
                game.setGameDegree(degree);
                game.setBgmOn(bgmOn);
                Main.cardPanel.add(game);
                Main.cardLayout.last(Main.cardPanel);
                game.action();
            }
        });

        voiceBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = voiceBox.getSelectedIndex();
                if (index == 0) {
                    bgmOn = true;
                } else {
                    bgmOn = false;
                }

            }
        });
    }
}

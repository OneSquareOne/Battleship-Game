import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;

public class Sound {

    private SoundEffect playerHitOpponent;
    private SoundEffect playerMissOpponent;
    private SoundEffect opponentHitPlayer;
    private SoundEffect opponentMissPlayer;

    public Sound() {

        /*
         * JFrame window = new JFrame();
         * window.setLayout(null);
         * window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         * window.setBounds(0, 0, 500, 500);
         * JPanel panel = new JPanel();
         * panel.setBounds(0, 0, 500, 500);
         * JButton button = new JButton();
         * button.setBounds(200,200,50,50);
         * button.addActionListener(new ActionListener() {
         * public void actionPerformed(ActionEvent e){
         * se.play();
         * System.out.println("Sound played");
         * }
         * });
         * 
         * window.add(panel);
         * panel.add(button);
         * window.setVisible(true);
         * panel.setVisible(true);
         * button.setVisible(true);
         */
        playerHitOpponent = new SoundEffect();
        playerMissOpponent = new SoundEffect();
        opponentHitPlayer = new SoundEffect();
        opponentMissPlayer = new SoundEffect();
        
        playerHitOpponent.setFile("./Sounds/ShotHit.wav");
        playerMissOpponent.setFile("./Sounds/ShotMiss.wav");
        opponentHitPlayer.setFile("./Sounds/ShotHitO.wav");
        opponentMissPlayer.setFile("./Sounds/ShotMissO.wav");
    }

    private class SoundEffect {
        Clip clip;

        public void setFile(String soundFileName) {
            try {
                File file = new File(soundFileName);
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
            } catch (Exception e) {
                System.out.println("Didn't work");
            }
        }

        private void play() {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void playHitO(){
        playerHitOpponent.play();
    }

    public void playMissO(){
        playerMissOpponent.play();
    }

    public void playHitP(){
        opponentHitPlayer.play();
    }

    public void playMissP(){
        opponentMissPlayer.play();
    }
}

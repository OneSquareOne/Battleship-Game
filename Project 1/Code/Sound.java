/* Sound loads and play sound files appropriate for the situation in the battleship game.  In this
 * format, .wav files at a 16-bit sample rate are functional.  Other format functionality is unknown
 * Author: Ryan Collins, John Schmidt
 * Late Update: 10/29/2022
 */

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

    private SoundEffect playerHitOpponent;
    private SoundEffect playerMissOpponent;
    private SoundEffect opponentHitPlayer;
    private SoundEffect opponentMissPlayer;

    //constructor
    public Sound() {

        playerHitOpponent = new SoundEffect();
        playerMissOpponent = new SoundEffect();
        opponentHitPlayer = new SoundEffect();
        opponentMissPlayer = new SoundEffect();
        
        playerHitOpponent.setFile("./Sounds/ShotHit.wav");
        playerMissOpponent.setFile("./Sounds/ShotMiss.wav");
        opponentHitPlayer.setFile("./Sounds/ShotHitO.wav");
        opponentMissPlayer.setFile("./Sounds/ShotMissO.wav");
    }

    //basic loading and processing for each sound effect
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

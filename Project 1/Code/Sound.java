/* Sound loads and play sound files appropriate for the situation in the battleship game.  In this
 * format, .wav files at a 16-bit sample rate are functional.  Other format functionality is unknown
 * Authors: Ryan Collins, John Schmidt
 * Late Update: 10/29/2022
 */

import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

    private SoundEffect click;
    private SoundEffect lastShip;
    private SoundEffect playerHitOpponent;
    private SoundEffect playerMissOpponent;
    private SoundEffect opponentHitPlayer;
    private SoundEffect opponentMissPlayer;
    private SoundEffect mainBackgroundLoop;
    private SoundEffect startFiring;
    private SoundEffect victory;
    private SoundEffect loss;
    private SoundEffect shipChime;
    private SoundEffect embark;
    private ArrayList<SoundEffect> allSoundEffects;

    // constructor
    public Sound() {

        allSoundEffects = new ArrayList<SoundEffect>();

        // assign file locations to sound files
        mainBackgroundLoop = newSoundEffect("./Sounds/mainPlayLoop1.wav");
        lastShip = newSoundEffect("./Sounds/lastShip.wav");
        playerHitOpponent = newSoundEffect("./Sounds/ShotHit.wav");
        playerMissOpponent = newSoundEffect("./Sounds/ShotMiss.wav");
        opponentHitPlayer = newSoundEffect("./Sounds/ShotHitO.wav");
        opponentMissPlayer = newSoundEffect("./Sounds/ShotMissO.wav");
        startFiring = newSoundEffect("./Sounds/startShooting.wav");
        victory = newSoundEffect("./Sounds/victory.wav");
        loss = newSoundEffect("./Sounds/loss.wav");
        click = newSoundEffect("./Sounds/click.wav");
        shipChime = newSoundEffect("./Sounds/shipChime.wav");
        embark = newSoundEffect("./Sounds/embark.wav");
    }

    // basic loading and processing for each sound effect
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

        private void playLoop() {
            clip.setFramePosition(0);
            clip.start();
            clip.loop(100);
        }

        private void stop() {
            clip.stop();
        }
    }

    // get new sound effect
    public SoundEffect newSoundEffect(String filePath) {
        SoundEffect temp = new SoundEffect();
        temp.setFile(filePath);
        allSoundEffects.add(temp);
        return temp;
    }

    public void playClick() {
        click.play();
    }

    public void playShipChime(){
        shipChime.play();
    }
    
    public void playEmbark(){
        embark.play();
    }

    // player hits the opponent
    public void playHitO() {
        playerHitOpponent.play();
    }

    // player misses the opponent
    public void playMissO() {
        playerMissOpponent.play();
    }

    // opponent hit the player
    public void playHitP() {
        opponentHitPlayer.play();
    }

    // opponent misses the player
    public void playMissP() {
        opponentMissPlayer.play();
    }

    // main background sound
    public void mainPlayLoop() {
        stopAll();
        mainBackgroundLoop.playLoop();
    }

    public void lastShipLoop() {
        stopAll();
        lastShip.playLoop();
    }

    // player only has one ship remaining
    public void startShooting() {
        startFiring.play();
    }

    // new game selection screen
    public void playVictory() {
        stopAll();
        victory.play();
    }

    // new game selection screen
    public void playLoss() {
        stopAll();
        loss.play();
    }

    // stops playing any sound effect
    public void stopAll() {
        for (int i = 0; i < allSoundEffects.size(); i++) {
            allSoundEffects.get(i).stop();
        }
    }
}

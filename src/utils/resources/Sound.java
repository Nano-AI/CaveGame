/**
 * Sound class
 */
package utils.resources;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    // volume
    public enum Volume {
        MUTE, LOW, MEDIUM, HIGH;
    }

    // set volume
    public Volume volume = Volume.MEDIUM;
    // audio clip
    private final Clip clip;

    /**
     * Load sound
     * @param fileName File to load
     */
    public Sound(String fileName) {
        clip = Assets.getClip(fileName);
    }

    /**
     * Play sound
     */
    public void play() {
        // if volume isn't mute
        if (volume != Volume.MUTE) {
            // if it's not running
            if (clip.isRunning()) {
                // stop
                clip.stop();
            }
            // reset position of audio file
            clip.setFramePosition(0);
            // replay
            clip.start();
        }
    }

    public void setVolume(int level) {
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
        if (volume != null) {
            volume.setValue((float) (level / 100.0));
        }
    }
}

package berkfatih;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	File filePath;
	Clip clip;

	public Sound(File file) {
		this.filePath = file;
		Clip tempClip = initializeClip(filePath);
		this.clip = tempClip;
	}

	public Clip initializeClip(File file) {
		Clip clip = null;
		AudioInputStream ais = null;

		try {
			clip = AudioSystem.getClip();
			ais = AudioSystem.getAudioInputStream(file);
			clip.open(ais);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return clip;
	}

	public void start() {
		clip = initializeClip(filePath);
		clip.start();
	}

	public void stop() {
		clip.close();
	}

	public void restart() {
		clip.close();
		clip = initializeClip(filePath);
		clip.start();
	}
}

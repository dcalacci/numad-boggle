package edu.madcourse.dancalacci.boggle;

import android.content.Context;
import android.media.MediaPlayer;
import edu.madcourse.dancalacci.R;


public abstract class Sounds {
	
	public static void playClick(Context c) {
        MediaPlayer mp = MediaPlayer.create(c, R.raw.button);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();
	}
	
	public static void playWrong(Context c) {
        MediaPlayer mp = MediaPlayer.create(c, R.raw.wrong);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();
	}
	
	public static void playRight(Context c, String sound) {
        MediaPlayer mp = MediaPlayer.create(c, R.raw.correct_word);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();
	}
}
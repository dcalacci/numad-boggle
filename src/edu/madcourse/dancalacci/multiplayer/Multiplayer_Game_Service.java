package edu.madcourse.dancalacci.multiplayer;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Multiplayer_Game_Service extends Multiplayer_Game_Intent_Service {
	public Multiplayer_Game_Service() {
		super("AppService");
	}

	@Override
	protected void doWakefulWork(Intent intent) {

		Log.d("Wakeful", "doing stuff");
		Log.d("Wakeful", "doing stuff");

		Toast.makeText(this, "wakeful", Toast.LENGTH_LONG).show();

	}
}
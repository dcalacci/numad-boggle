package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;
import edu.madcourse.dancalacci.boggle.Multiplayer_Received_Requests.Multiplayer_Received_Request_Adaptor;
import android.app.Activity;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Multiplayer_Login_Form extends Activity{
	private String TAG = "Multiplayer_Login_Form";

	private static final String BOGGLE_PREF = "edu.madcourse.dancalacci.multiplayer";
	public static final String PREF_USER = "prefUser";
	public static final String PREF_PASS = "prefPass";

	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private Button btnRegister;
	private Button btnCancel;
	private ServerAccessor sa = new ServerAccessor();
	public void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Login Form!!!");

		setContentView(R.layout.multiplayer_login_form);

		etUsername = (EditText)findViewById(R.id.login_form_editText_username);
		etPassword = (EditText)findViewById(R.id.login_form_editText_password);
		btnLogin = (Button)findViewById(R.id.login_form_button_Login);
		btnCancel = (Button)findViewById(R.id.login_form_button_Cancel);
		btnRegister = (Button)findViewById(R.id.login_form_button_Register);
		btnRegister.setVisibility(Button.GONE);

		// Set Click Listener
		btnLogin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Check Login
				final String username = etUsername.getText().toString();
				final String password = etPassword.getText().toString();

				sa.login(username, password, new OnBooleanReceivedListener() {
					public void run(Boolean loggedIn) {
						if (loggedIn) {
							Toast.makeText(getBaseContext(), "Login Sucessful. Welcome " + username, Toast.LENGTH_SHORT).show();
							
							//TODO: Should this be BOGGLE_PREF or MULTI_PREF?
							SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
							SharedPreferences.Editor edit = pref.edit();

							edit.putString(PREF_USER, username).commit();
							edit.putString(PREF_PASS, password).commit();

							Log.d(TAG, "Login Form: "+ pref.getString(TAG, "PREF_USER"));
							finish();
						}else{
							Toast.makeText(getBaseContext(), "Login Failed. Username and/or password doesn't match.", Toast.LENGTH_SHORT).show();
							btnRegister.setVisibility(Button.VISIBLE);
						}
					}
				});
			}
		});

		btnRegister.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Check Login
				final String username = etUsername.getText().toString();
				final String password = etPassword.getText().toString();
				sa.register(username, password, new OnBooleanReceivedListener() {
					public void run(Boolean registered) {
						if (registered) {
							Toast.makeText(getBaseContext(), "Registration Sucessful. " +
									"Welcome " + username, Toast.LENGTH_SHORT).show();
							etUsername.setText(username);
							etPassword.setText(password);
						}else{
							Toast.makeText(getBaseContext(), 
									"Registration Failed. Either the username " +
									"already exists, or your username or password " +
									"contained commas", Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Close the application
				finish();
			}
		});
	}
}



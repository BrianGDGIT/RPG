package com.brian.rpg;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.util.Log;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.brian.rpg.RPG;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.content.ContentValues.TAG;

public class AndroidLauncher extends AndroidApplication implements PlayServices {
	// Client used to sign in with Google APIs
	private GoogleSignInClient googleSignInClient;
	//The currently signed in account
	private GoogleSignInAccount signedInAccount = null;

	// Client used to interact with the real time multiplayer system.
	private RealTimeMultiplayerClient realTimeMultiplayerClient = null;

	private String mPlayerId;

	// Request code used to invoke sign in user interactions.
	private static final int RC_SIGN_IN = 9001;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;
		config.useCompass = false;

		//Create the client used to sign in
		googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

		initialize(new RPG(), config);
	}

	/**
	 * Start a sign in activity.  To properly handle the result, call tryHandleSignInResult from
	 * your Activity's onActivityResult function
	 */
	private void startSignInIntent(){
		startActivityForResult(googleSignInClient.getSignInIntent(), RC_SIGN_IN);
	}

	@Override
	public void onSignInButtonClicked() {

	}

	@Override
	public void onSignOutButtonClicked() {

	}

	@Override
	public boolean isSignedIn() {
		return false;
	}

	@Override
	public void signInSilently() {
		Log.d(TAG, "signInSileently()");

		googleSignInClient.silentSignIn().addOnCompleteListener(this,
				new OnCompleteListener<GoogleSignInAccount>() {
					@Override
					public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
						if(task.isSuccessful()){
							Log.d(TAG, "signInSilently(): success");
							onConnected(task.getResult());
						}else{
							Log.d(TAG, "signInSilently(): failure");
							onDisconnected();
						}
					}
				});
	}

	@Override
	public void submitScore(String leaderboardId, int highScore) {

	}

	@Override
	public void showLeaderboard(String leaderboardId) {

	}

	@Override
	public void setTrackerScreenName(String screenName) {

	}

	//Google Games API callbacks
	private void onConnected(GoogleSignInAccount googleSignInAccount){
		Log.d(TAG, "onConnected(): connected to Google APIs");
		if(signedInAccount != googleSignInAccount){
			signedInAccount = googleSignInAccount;

			//update the clients
			realTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(this, googleSignInAccount);

			//Get playerID from the PlayersClient
			PlayersClient playersClient = Games.getPlayersClient(this, googleSignInAccount);
			playersClient.getCurrentPlayer()
					.addOnSuccessListener(new OnSuccessListener<Player>() {
						@Override
						public void onSuccess(Player player) {
							mPlayerId = player.getPlayerId();
							//switch to mainscreen?
						}
					});
		}

	}

	private void onDisconnected(){
		Log.d(TAG, "onDisconnected()");

		realTimeMultiplayerClient = null;
		//switch to mainMenu?
	}
}

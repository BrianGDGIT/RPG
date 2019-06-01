package com.brian.rpg;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.brian.rpg.RPG;
import com.brian.rpg.Views.MainMenuScreen;
import com.brian.rpg.Views.PlayScreen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.*;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AndroidLauncher extends AndroidApplication implements PlayServices {
	private RPG game;
	// Client used to sign in with Google APIs
	private GoogleSignInClient googleSignInClient;
	//The currently signed in account
	private GoogleSignInAccount signedInAccount = null;
	private String playerId;

	// Client used to interact with the real time multiplayer system.
	private RealTimeMultiplayerClient realTimeMultiplayerClient = null;

	//Holds the configuration of the current room
	private RoomConfig roomConfig;
	String roomId = null;

	ArrayList<Participant> participants = null;
	//participant id in currently active game
	String myId = null;

	//Message buffers for sending messages
	byte[] msgBuf = new byte[8];
	byte[] attackMsgBuf = new byte[1];


	//Request codes for the UIs that we show with startActivityForResult:
	final static int RC_WAITING_ROOM = 10002;

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
		game = new RPG(this);
		initialize(game, config);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume()");

		// Since the state of the signed in user can change when the activity is not active
		// it is recommended to try and sign in silently from when the app resumes.
		signInSilently();
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
		startSignInIntent();
	}

	@Override
	public void onSignOutButtonClicked() {
		signOut();
	}

	@Override
	public void onQuickGameButtonClicked() { startQuickGame(); }


	@Override
	public boolean isSignedIn() {
		return false;
	}

	@Override
	public void signInSilently() {
		Log.d(TAG, "signInSilently()");

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

	public void signOut(){
		Log.d(TAG, "signOut()");

		googleSignInClient.signOut().addOnCompleteListener(this,
				new OnCompleteListener<Void>() {
					@Override
					public void onComplete(@NonNull Task<Void> task) {
						if(task.isSuccessful()){
							Log.d(TAG, "signOut(): success");
						}else{
							//Handle exception
						}

						onDisconnected();
					}
				});
	}

	public void startQuickGame(){
		final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS, MAX_OPPONENTS, 0);

		RoomConfig roomConfig = RoomConfig.builder(roomUpdateCallback)
				.setOnMessageReceivedListener(onRealTimeMessageReceivedListener)
				.setRoomStatusUpdateCallback(roomStatusUpdateCallback)
				.setAutoMatchCriteria(autoMatchCriteria)
				.build();
		realTimeMultiplayerClient.create(roomConfig);
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

	// Show the waiting room UI to track the progress of other players as they enter the
	// room and get connected.
	void showWaitingRoom(Room room){
		// minimum number of players required for our game
		// For simplicity, we require everyone to join the game before we start it
		// (this is signaled by Integer.MAX_VALUE).
		final int MIN_PLAYERS = Integer.MAX_VALUE;
		realTimeMultiplayerClient.getWaitingRoomIntent(room, MIN_PLAYERS)
				.addOnSuccessListener(new OnSuccessListener<Intent>() {
					@Override
					public void onSuccess(Intent intent) {
						//show waiting room UI
						startActivityForResult(intent, RC_WAITING_ROOM);
					}
				});
	}

	void showGameError() {
		new AlertDialog.Builder(this)
				.setMessage(R.string.game_error)
				.setNeutralButton(android.R.string.ok, null).create();
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
							playerId = player.getPlayerId();
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

	private RoomUpdateCallback roomUpdateCallback = new RoomUpdateCallback(){
		@Override
		public void onRoomCreated(int statusCode, @Nullable Room room) {
			Log.d(TAG, "onRoomCreated:(" +statusCode + ", " + room + ")");
			if(statusCode != GamesCallbackStatusCodes.OK){
				Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
				showGameError();
				return;
			}

			//Save room ID so we can leave cleanly before the game starts
			roomId = room.getRoomId();

			//show the waiting room UI
			showWaitingRoom(room);
		}

		@Override
		public void onJoinedRoom(int statusCode, @Nullable Room room) {
			Log.d(TAG, "onJoinedRoom:(" + statusCode + ", " + room + ")");
			if(statusCode != GamesCallbackStatusCodes.OK){
				Log.e(TAG, "*** Error: onJoinedRoom, status " + statusCode);
				showGameError();
				return;
			}

			//show the waiting room UI
			showWaitingRoom(room);
		}

		@Override
		public void onLeftRoom(int statusCode, @NonNull String s) {
			//Left room return to mainscreen
			Log.d(TAG, "onLeftRoom, code " + statusCode);
			//Switch to main screen
		}

		@Override
		public void onRoomConnected(int statusCode, @Nullable Room room) {
			Log.d(TAG, "onRoomConnected:(" + statusCode + ", " + room + ")");
			if(statusCode != GamesCallbackStatusCodes.OK){
				Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
				showGameError();
				return;
			}
			updateRoom(room);
		}
	};

	void updateRoom(Room room){
		if(room != null){
			participants = room.getParticipants();
		}
	}

	private RoomStatusUpdateCallback roomStatusUpdateCallback = new RoomStatusUpdateCallback(){
		@Override
		public void onConnectedToRoom(@Nullable Room room) {
			Log.d(TAG, "onConnectedToRoom()");

			//get participants and ID
			participants = room.getParticipants();
			myId = room.getParticipantId(playerId);

			// save room ID if its not initialized in onRoomCreated() so we can leave cleanly before the game starts.
			if(roomId == null){
				roomId = room.getRoomId();
			}

			// print out the list of participants (for debug purposes)
			Log.d(TAG, "Room ID: " + roomId);
			Log.d(TAG, "My ID " + myId);
			Log.d(TAG, "<< CONNECTED TO ROOM>>");
		}

		@Override
		public void onDisconnectedFromRoom(@Nullable Room room) {
			roomId = null;
			roomConfig = null;
			showGameError();
		}

		@Override
		public void onRoomConnecting(@Nullable Room room) {updateRoom(room);}

		@Override
		public void onRoomAutoMatching(@Nullable Room room) {updateRoom(room);}


		@Override
		public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {updateRoom(room);}


		@Override
		public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {updateRoom(room);}


		@Override
		public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {updateRoom(room);}


		@Override
		public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {updateRoom(room);}


		@Override
		public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
			updateRoom(room);
			Log.d(TAG, "onP2PConnected: ");
			game.setIsMultiplayer();
			if(participants.get(1).getParticipantId() != myId){
				game.setIsPlayer2();
			}
			game.getGamePlayScreen().spawnPlayers();
		}

		@Override
		public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {updateRoom(room);}


		@Override
		public void onP2PConnected(@NonNull String s) {

		}

		@Override
		public void onP2PDisconnected(@NonNull String s) {

		}
	};

	/*
	 * COMMUNICATIONS SECTION. Methods that implement the game's network
	 * protocol.
	 */
	OnRealTimeMessageReceivedListener onRealTimeMessageReceivedListener = new OnRealTimeMessageReceivedListener() {
		@Override
		public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
			byte[] buf = realTimeMessage.getMessageData();

			//Syncing player
			if(buf[0] == 'A') {
				//Sync player attacking
				game.getGamePlayScreen().getPlayer2().setHasAttacked();
			}else if(buf[0] == 'S'){
				//Get information from buffer and copy to new byte array starting from position 1 after indicator byte 'S'
				byte[] spellArray = new byte[buf.length - 1];
				System.arraycopy(buf, 1, spellArray, 0, buf.length - 1);

				//Convert byte array to String
				String spell = spellArray.toString();

				//Sync spell
				game.getGamePlayScreen().getPlayer2().activeSpell.equals(spell);
			}else{
				//Sync player position
				Vector2 position = new Vector2(ByteBuffer.wrap(buf).getFloat(0), ByteBuffer.wrap(buf).getFloat(4));
				game.getGamePlayScreen().getPlayer2().updatePlayerPosition(position);
			}




		}
	};

	//Broadcast player position to everyone else
	@Override
	public void broadcastPlayerPosition(Vector2 position) {
		ByteBuffer bb = ByteBuffer.wrap(msgBuf).putFloat(position.x).putFloat(position.y);

		for(Participant p : participants){
			if(p.getParticipantId().equals(myId)){
				continue;
			}
			if(p.getStatus() != Participant.STATUS_JOINED){
				continue;
			}

			//broadcastPlayerPosition position
			realTimeMultiplayerClient.sendUnreliableMessage(msgBuf, roomId, p.getParticipantId());
		}

	}

	@Override
	public void broadcastPlayerAttack() {
		//Set byte indicator to A for player attack
		attackMsgBuf[0] = 'A';
		for(Participant p : participants){

			if(p.getParticipantId().equals(myId)){
				continue;
			}
			if(p.getStatus() != Participant.STATUS_JOINED){
				continue;
			}

			//broadcastPlayerPosition position
			realTimeMultiplayerClient.sendUnreliableMessage(attackMsgBuf, roomId, p.getParticipantId());
		}
	}

	@Override
	public void broadcastPlayerSpell(String spell) {
		Log.d(TAG, "broadcastPlayerSpell:");
		byte [] msgBuf;

		//Get spell string and convert to bytes
		msgBuf = spell.getBytes();

		//used to copy array into to add byte indicator
		byte [] destinationBuf = new byte[msgBuf.length + 1];

		//add byte indicator
		destinationBuf[0] = 'S';

		//Copy array containing spell string into the new array that contains the byte indicator S
		System.arraycopy(msgBuf, 0, destinationBuf, 1, msgBuf.length);


		for(Participant p : participants){
			if(p.getParticipantId().equals(myId)){
				continue;
			}
			if(p.getStatus() != Participant.STATUS_JOINED){
				continue;
			}

			//broadcast Player spell
			realTimeMultiplayerClient.sendUnreliableMessage(destinationBuf, roomId, p.getParticipantId());
		}

	}




}

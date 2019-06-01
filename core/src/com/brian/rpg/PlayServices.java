package com.brian.rpg;

import com.badlogic.gdx.math.Vector2;

public interface PlayServices {
    public void onSignInButtonClicked();
    public void onSignOutButtonClicked();
    public void onQuickGameButtonClicked();
    public boolean isSignedIn();
    public void signInSilently();
    public void submitScore(String leaderboardId, int highScore);
    public void showLeaderboard(String leaderboardId);
    public void setTrackerScreenName(String screenName);
    public void broadcastPlayerPosition(Vector2 position);
    public void broadcastPlayerAttack();
    public void broadcastPlayerSpell(String spell);
}

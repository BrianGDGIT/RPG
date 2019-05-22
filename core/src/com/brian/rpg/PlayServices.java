package com.brian.rpg;

public interface PlayServices {
    public void onSignInButtonClicked();
    public void onSignOutButtonClicked();
    public void onQuickGameButtonClicked();
    public boolean isSignedIn();
    public void signInSilently();
    public void submitScore(String leaderboardId, int highScore);
    public void showLeaderboard(String leaderboardId);
    public void setTrackerScreenName(String screenName);
}

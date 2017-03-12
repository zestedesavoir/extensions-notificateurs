package com.zestedesavoir.android.login.managers;

import com.zestedesavoir.android.login.models.Token;

import rx.Observable;

/**
 * Allow the authentication to Zeste de Savoir and save all tokens of the current
 * user authenticated necessary to re-authenticate him automatically.
 *
 * @author Gerard Paligot
 */
public interface Session {
    /**
     * Authenticate a user by login and password.
     *
     * @param login    User's login.
     * @param password User's password.
     */
    Observable<Token> authenticate(String login, String password);

    /**
     * Disconnect the current authenticated user.
     */
    Observable<Void> disconnect();

    /**
     * Check if we have a session already saved in the system.
     */
    Observable<Boolean> isAuthenticated();
}

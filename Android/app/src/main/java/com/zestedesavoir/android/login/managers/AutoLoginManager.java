package com.zestedesavoir.android.login.managers;

import com.zestedesavoir.android.login.models.Token;

import retrofit2.Call;

interface AutoLoginManager {
    /**
     * Authenticate a user by its refresh token. This token is saved in intern.
     * If the refresh token isn't valid, think to redirect the user to the login
     * page.
     */
    Call<Token> authenticateByToken();
}

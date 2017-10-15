package com.pleximus.pet_app.ui.register.core.model;

import android.content.Context;
import android.text.TextUtils;

import com.pleximus.pet_app.R;
import com.pleximus.pet_app.core.model.DBUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by pleximus on 05/05/17.
 */

public class RegisterModel implements IRegisterModel {

    public static String VALID = "valid";
    public static final int MIN_LENGTH = 3;
    public static final int MAX_LENGTH = 15;

    private Context context;

    public RegisterModel(Context context) {
        this.context = context;
    }

    @Override
    public String userInputValidation(String validatedString, String whatTovalidate) {

        String message = VALID;
        if (TextUtils.isEmpty(validatedString)) {
            message = String.format(context.getResources().getString(R.string.blank_field_msg), whatTovalidate);
            return message;
        }

        int length = validatedString.length();
        if ((length < MIN_LENGTH) || (length > MAX_LENGTH)) {
            message = String.format(context.getResources().getString(R.string.string_length_validation), whatTovalidate);
            return message;
        }

        return message;
    }

    /**
     * validate registration input params from user
     *
     * @param firstname
     * @param lastname
     * @param nickname
     * @param password
     * @param emailId
     * @return
     */
    public List<String> validateRegistertionInput(String firstname, String lastname, String nickname, String password, String emailId) {
        List<String> errors = new ArrayList<String>();
        if (TextUtils.isEmpty(firstname))
            errors.add(String.format(context.getResources().getString(R.string.blank_field_msg), "First Name"));
        if (TextUtils.isEmpty(lastname))
            errors.add(String.format(context.getResources().getString(R.string.blank_field_msg), "Last Name"));
        if (TextUtils.isEmpty(nickname))
            errors.add(String.format(context.getResources().getString(R.string.blank_field_msg), "Nickname"));
        if (TextUtils.isEmpty(emailId))
            errors.add(String.format(context.getResources().getString(R.string.blank_field_msg), "Email Id"));
        if (TextUtils.isEmpty(password))
            errors.add(String.format(context.getResources().getString(R.string.blank_field_msg), "Password"));
        return errors;
    }

    /**
     * get user object
     *
     * @param firstname
     * @param lastname
     * @param nickname
     * @param password
     * @param emailId
     * @return
     */
    public DBUser getUserDetails(String firstname, String lastname, String nickname, String password, String emailId) {
        DBUser dbUser = new DBUser();
        dbUser.setuFirstName(firstname);
        dbUser.setuLastName(lastname);
        dbUser.setuEmail(emailId);
        dbUser.setuNickName(nickname);
        dbUser.setuPassWord(password);
        return dbUser;
    }

}

package org.abhyuday.treeplantation.loginmodule;

/**
 * This is the conf file used for the login part.
 */
public class LoginConfig {

    //public static final String LOGIN_URL = "http://10.0.2.2/androidPHP/userLogin.php";
    public static final String LOGIN_URL = "http://10.129.139.139:9898/userLogin.php";
    //public static final String REGISTER_URL = "http://10.0.2.2/androidPHP/registerUser.php";
    public static final String REGISTER_URL = "http://10.129.139.139:9898/registerUser.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_ID = "Id";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_PASSWORD = "Password";

    //If server response is equal to this that means login/register is unsuccessful
    public static final String LOGIN_FAILURE = "failure";

    //If server response is equal to this that means login/register is successful
    public static final String LOGIN_SUCCESS = "Success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //This would be used to store the email of current logged in user
    public static final String USER_ID_SHARED_PREF = "id";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    public static final String JSON_ARRAY = "result";

}

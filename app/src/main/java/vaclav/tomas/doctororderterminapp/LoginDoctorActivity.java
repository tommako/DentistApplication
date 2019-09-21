package vaclav.tomas.doctororderterminapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static vaclav.tomas.doctororderterminapp.MainActivity.CODE_GET_REQUEST;
import static vaclav.tomas.doctororderterminapp.MainActivity.CODE_POST_REQUEST;

public class LoginDoctorActivity extends AppCompatActivity {

    private static final String TAG = LoginDoctorActivity.class.getSimpleName();
    private static EditText username, password;
    private static ImageButton loginButton;
    private static TextView texViewResult;
    private ProgressDialog progressDialog;
    private DoctorTerminsActivity doctorTerminsActivity;
    private Doctor doctor;
    private DoctorSession doctorSession;
    // shared preferencies for share username and password
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String N = "n";
    public static final String S = "s";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);

        progressDialog  = new ProgressDialog(this);
        doctor = new Doctor(this);
        doctorSession = new DoctorSession(this);

        if(doctorSession.isDoctorLoggedin()){
            startActivity(new Intent(this, DoctorTerminsActivity.class));
            finish();
        }

        //test shared preferencies
        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        // log variant with hash encoding
        //LoginUserVariant2();

        // log variant without hash encoding
        LoginVariant3();

    }

    //https://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;

    }

    // Uzitocne linky
    // Voley login and register with hash
    //https://www.youtube.com/watch?v=yS5n40h4Wlg
    // Voley login and register withou hash
    //https://www.youtube.com/watch?v=fAcdRVfccQ8
    //https://www.simplifiedcoding.net/android-mysql-tutorial-to-perform-basic-crud-operation/
    // Voley load data to list view
    //https://www.android-examples.com/volley-json-parsing-listview/
    //https://www.youtube.com/watch?v=Yw7Lx9wqyGs
    // tutorial z videa
    // https://www.simplifiedcoding.net/retrieve-data-mysql-database-android/
    //http://www.worldbestlearningcenter.com/tips/Android-populate-listview-from-json-using-volley.htm



    // login variant with hash coding - unfinished
    public void LoginUserVariant2(){

        username = (EditText)findViewById(R.id.UsernameEditText);
        password = (EditText)findViewById(R.id.PasswordEditText);

        loginButton = (ImageButton)findViewById(R.id.LoginButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Username = username.getText().toString().trim();
                String Password = password.getText().toString().trim();
                System.out.println("Username is " + Username + " and Password is " + Password);
                Log(Username, Password);
                System.out.println("Email: " );

                //dokoncit
                //https://www.youtube.com/watch?v=yS5n40h4Wlg
                //https://www.youtube.com/watch?v=fAcdRVfccQ8
                //https://www.simplifiedcoding.net/android-mysql-tutorial-to-perform-basic-crud-operation/

                //PostRequestHandler postRequestHandler = new PostRequestHandler(
                  //      Api.URL_LOGIN_DOCTOR_BASE_EMAIL_PASSWORD + "&email=j.hrncar@centrum.sk&password=1234",
                    //    null,
                      //  CODE_GET_REQUEST);
                //postRequestHandler.execute();
                //String s = postRequestHandler.execute();

                //Intent Book = new Intent("vaclav.tomas.doctororderterminapp.DoctorTerminsActivity");
                //startActivity(Book);

            }
        });

    }

    // login variant with hash coding - unfinished
    private void Log(final String email, final String password){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://wasaox.ddns.net/dentistapp/v1/Api.php?apicall=getDoctorByEmailAndPassword&email=j.hrncar@centrum.sk&password=1234", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                     String success = jsonObject.getString("success");
                     JSONArray jsonArray = jsonObject.getJSONArray("login");

                     if(success.equals("1")){

                         for (int i = 0; i < jsonArray.length(); i++){
                             JSONObject object = jsonArray.getJSONObject(i);

                             String name = object.getString("name").trim();
                             String email = object.getString("email").trim();

                             Toast.makeText(LoginDoctorActivity.this, "Success Login. \n Your name " + name + "\n Your email " + email,Toast.LENGTH_LONG).show();
                         }

                     }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginDoctorActivity.this, "Error 1 " + e.toString(),Toast.LENGTH_LONG).show();

                }
            }
        },
         new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginDoctorActivity.this, "Error " + error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // funciton for show info or warnings
    private void toast(String x){
        Toast.makeText(this,x,Toast.LENGTH_LONG).show();
    }

    // Log variant without hash encoding
    public void LoginVariant3(){

        username = (EditText)findViewById(R.id.UsernameEditText);
        password = (EditText)findViewById(R.id.PasswordEditText);


        loginButton = (ImageButton)findViewById(R.id.LoginButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Username = username.getText().toString().trim();
                String Password = password.getText().toString().trim();

                log2(Username, Password);


        }
    });

    }


    // log variant without hash encoding
    private void log2(final String username, final String password){

        progressDialog.setMessage("Prebieha prihlasovanie...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                //"http://192.168.1.145/DoctorOrderTerminApp/androidstudio/DBConnection/v1/Api.php?apicall=getDoctorByEmailAndPassword&email=j.hrncar@centrum.sk&password=1234",
                //"http://192.168.1.145/DoctorOrderTerminApp/androidstudio/DBConnection/includes/login.php",
                //Ludmanska
                //"http://192.168.1.145/dentistapp/DBConnection/includes/login.php",
                //Ruzova
                //"http://192.168.0.106/dentistapp/DBConnection/includes/login.php",
                //Bondanovce
                //"http://192.168.1.13/dentistapp/DBConnection/includes/login.php",
                Api.URL_LOGIN,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // Now store the user in SQLite
                        JSONObject user = jObj.getJSONObject("user");
                        String email = user.getString("email");
                        String uName = user.getString("name");
                        String uSurname = user.getString("surname");


                        //doc.setEmail(email);
                        //doc.setName(uName);
                        doctor.setKeyUsername(uName);
                        doctor.setKeySurname(uSurname);
                        //toast("Login username is " + uName + " and email is " + email);

                        //test shared preferencies
                        //SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(N,uName);
                        editor.putString(S,uSurname);
                        editor.commit();

                        doctorSession.setLoggedin(true);

                        Intent Book = new Intent("vaclav.tomas.doctororderterminapp.DoctorTerminsActivity");
                        startActivity(Book);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        //String errorMsg = jObj.getString("error_msg");
                        toast("Nesprávne meno alebo heslo, prosím skúste to znovu.");
                        progressDialog.hide();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    toast("Json error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                toast("Unknown Error occurred");
                progressDialog.hide();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("email", username);
                params.put("password", password);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);

    }

    // prevent leak activity and go to next activity until progress dialog is not closed
    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }

}
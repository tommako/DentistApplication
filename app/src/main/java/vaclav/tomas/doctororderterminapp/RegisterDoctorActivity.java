package vaclav.tomas.doctororderterminapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static vaclav.tomas.doctororderterminapp.Api.URL_BASE_FOR_GET_DOCTOR_TERMIN;
import static vaclav.tomas.doctororderterminapp.MainActivity.CODE_POST_REQUEST;

public class RegisterDoctorActivity extends AppCompatActivity {

    private static final String TAG = RegisterDoctorActivity.class.getSimpleName();
    private static EditText name, surname, specialization, email, security_question, security_answer, password, confirm_password;
    private static Button register;
    boolean doctorName_in_DB, doctorSurname_in_DB, doctorEmail_in_DB, doctor_in_DB;
    private ProgressDialog progressDialog;
    //public String doc_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        progressDialog = new ProgressDialog(this);

        //Initialize EditText View
        name = (EditText) findViewById(R.id.NameEditText);
        surname = (EditText) findViewById(R.id.SurnameEditText);
        specialization = (EditText) findViewById(R.id.SpecializationEditText);
        email = (EditText) findViewById(R.id.EmailEditText);
        security_question = (EditText) findViewById(R.id.SecurityQuestionEditText);
        security_answer = (EditText) findViewById(R.id.SecurityAnswerEditText);
        password = (EditText) findViewById(R.id.PasswordEditText);
        confirm_password = (EditText) findViewById(R.id.ConfirmPasswordEditText);

        //doctorName_in_DB = false;
        //doctorSurname_in_DB = false;
        //doctorEmail_in_DB = false;

        //createDoktorPostRequestHandler();
        createDoktorVoley();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    private void toast(String x) {
        Toast.makeText(this, x, Toast.LENGTH_LONG).show();
    }

    public void createDoktorPostRequestHandler() {

        register = (Button) findViewById(R.id.RegisterButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputedName = name.getText().toString();
                String inputedSurname = surname.getText().toString();
                String inputedSpecialization = specialization.getText().toString();
                String inputedEmail = email.getText().toString();
                String inputedSecurity_Question = security_question.getText().toString();
                String inputedSecurity_Answer = security_answer.getText().toString();
                String inputedPassword = password.getText().toString().trim();
                String inputedConfirm_Password = confirm_password.getText().toString().trim();
                System.out.println("Zadane heslo " + inputedPassword);

                //CheckIfDoctorDintExistInDB(inputedName, inputedSurname, inputedEmail);

               // System.out.println("doctor boolean " + doctor_in_DB +  CheckIfDoctorDintExistInDB(inputedName, inputedSurname, inputedEmail));

                if (inputedName.isEmpty() ||
                        inputedSurname.isEmpty() ||
                        inputedSpecialization.isEmpty() ||
                        inputedEmail.isEmpty() ||
                        inputedSecurity_Question.isEmpty() ||
                        inputedSecurity_Answer.isEmpty() ||
                        inputedPassword.equals("") ||
                        inputedConfirm_Password.equals("")
                        ) {
                    toast("Jedno z poli je prazdne, prosim skontrolujte ci su vsetky polia vyplnene ");
                } else if (!inputedPassword.equals(inputedConfirm_Password)) {
                    toast("Hesla niesu zhodne !! ");
                /*} else if (doctorName_in_DB == true && doctorSurname_in_DB == true && doctorEmail_in_DB == true) {
                    toast("Už ste registrovany !!");
                    return;*/
                } else {


                    HashMap<String, String> requestedParams = new HashMap<>();
                    requestedParams.put("specialization", inputedSpecialization);
                    requestedParams.put("name", inputedName);
                    requestedParams.put("surname", inputedSurname);
                    //requestedParams.put("specialization", inputedSpecialization);
                    requestedParams.put("email", inputedEmail);
                    requestedParams.put("password", inputedPassword);
                    requestedParams.put("security_question", inputedSecurity_Question);
                    requestedParams.put("security_answer", inputedSecurity_Answer);
                    //requestedParams.put("password", inputedPassword);

                   // CheckIfDoctorDintExistInDB(inputedName, inputedSurname, inputedEmail);

                    //System.out.println("doctor boolean " + doctor_in_DB);

                    //if(doctorName_in_DB == true && doctorSurname_in_DB == true && doctorEmail_in_DB == true){

                    //if (!CheckIfDoctorDintExistInDB(inputedName, inputedSurname, inputedEmail)) {
                    if(doctor_in_DB == false){
                        System.out.println("doctor boolean " + doctor_in_DB);
                        //toast("Už ste registrovany !!");
                        PostRequestHandler postRequestHandler = new PostRequestHandler(Api.URL_CREATE_DOCTOR, requestedParams, CODE_POST_REQUEST);
                        postRequestHandler.execute();
                    } else{
                        //Calling the create doctor API
                       // PostRequestHandler postRequestHandler = new PostRequestHandler(Api.URL_CREATE_DOCTOR, requestedParams, CODE_POST_REQUEST);
                    //postRequestHandler.execute();
                        System.out.println("doctor boolean " + doctor_in_DB);
                        toast("Už ste registrovany !!");
                }
                    // }else{
                    //   toast("Už ste registrovany !!");
                    //doctorName_in_DB = false;
                    //doctorSurname_in_DB = false;
                    //doctorEmail_in_DB = false;
                    //}

                }
                // System.out.print( "Status result" + postRequestHandler.execute().getStatus().toString());

            }
        });

    }

    //Test with voley insert
    public void createDoktorVoley() {

        register = (Button) findViewById(R.id.RegisterButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputedName = name.getText().toString();
                String inputedSurname = surname.getText().toString();
                String inputedSpecialization = specialization.getText().toString();
                String inputedEmail = email.getText().toString();
                String inputedSecurity_Question = security_question.getText().toString();
                String inputedSecurity_Answer = security_answer.getText().toString();
                String inputedPassword = password.getText().toString().trim();
                String inputedConfirm_Password = confirm_password.getText().toString().trim();
                System.out.println("Zadane heslo " + inputedPassword);

                //boolean s = CheckIfDoctorDintExistInDB(inputedName, inputedSurname, inputedEmail);
                //System.out.println("Result check doctor " + s );

                if (inputedName.isEmpty() ||
                        inputedSurname.isEmpty() ||
                        inputedSpecialization.isEmpty() ||
                        inputedEmail.isEmpty() ||
                        inputedSecurity_Question.isEmpty() ||
                        inputedSecurity_Answer.isEmpty() ||
                        inputedPassword.equals("") ||
                        inputedConfirm_Password.equals("")
                        ) {
                    toast("Jedno z poli je prazdne, prosim skontrolujte ci su vsetky polia vyplnene ");
                } else if (!inputedPassword.equals(inputedConfirm_Password)) {
                    toast("Hesla niesu zhodne !! ");
                    //} else if (doctorName_in_DB == true && doctorSurname_in_DB == true && doctorEmail_in_DB == true) {
                //} else if (doctor_in_DB == false) {
                   // toast("Už ste registrovany !!");
                   // CreateDoctorVoley(inputedSpecialization, inputedName, inputedSurname, inputedEmail, inputedPassword, inputedSecurity_Question, inputedSecurity_Answer);


                } else {

                    //RequestQueue requestQueue= Volley.newRequestQueue(this);
                    //RegisterDoctorVoley(inputedSpecialization, inputedName, inputedSurname, inputedEmail, inputedPassword, inputedSecurity_Question, inputedSecurity_Answer);
                    //toast("Už ste registrovany !!");
                    CheckIfDoctorDintExistInDB(inputedSpecialization, inputedName, inputedSurname, inputedEmail, inputedPassword, inputedSecurity_Question, inputedSecurity_Answer);
                }

            }
        });

    }

    // vyskusat: https://stackoverflow.com/questions/30920457/execute-a-volley-request-after-another-one-is-finished

    public void RegisterDoctorVoley(final String specialization,final String name,final String surname,final String email,final String password,final String security_question,final String security_answer){

        //CheckIfDoctorDintExistInDB(name, surname, email);
        progressDialog.setMessage("Prebieha registracia....");
        progressDialog.show();
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                //Utils.REGISTER_URL,
                //"http://192.168.1.145/dentistapp/DBConnection/includes/doctorRegister.php",
                Api.URL_CREATE_DOCTOR,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try{
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        //JSONObject doctor = jObj.getJSONObject("doctors");
                        progressDialog.cancel();
                        toast("Doktor úspešne registrovaný.");

                    } else {
                        // Error in register. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        toast("Chyba pri registrácii " + errorMsg);
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
                //Log.e(TAG, "Login Error: " + error.getMessage());
                toast("Unknown Error occurred");
                //progressDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("specialization", specialization);
                params.put("name", name);
                params.put("surname", surname);
                params.put("email", email);
                params.put("password", password);
                params.put("security_question", security_question);
                params.put("security_answer", security_answer);

                return params;
            }

        };

        //if(doctor_in_DB == false) {

            requestQueue.add(strReq);

        /*}else {
            toast("Už ste registrovany !!");
        }*/
    }


    // Checking if same record is not in database
    //public boolean CheckIfDoctorDintExistInDB(final String doc_name, final String doc_surname, final String doc_email){
    public boolean CheckIfDoctorDintExistInDB(final String specialization,final String name,final String surname,final String email,final String password,final String security_question,final String security_answer){

        //String doc_name;

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                //"http://192.168.1.145/dentistapp/DBConnection/v1/Api.php?apicall=getDoctorTermins&username=" + sel_doctor, new Response.Listener<String>() {
                //Api.URL_CHECK_IF_DOCTOR_EXIST + "&name="+ doc_name + "&surname=" + doc_surname, new Response.Listener<String>() {
                Api.URL_CHECK_IF_DOCTOR_EXIST + "&name="+ name + "&surname=" + surname, new Response.Listener<String>() {
                //Api.URL_READ_DOCTORS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    //JSONArray doctors  = object.getJSONArray("doctors");
                    doctor_in_DB = object.getBoolean("error");
                    String errorMessage = object.getString("message");

                    if(!doctor_in_DB){
                        System.out.println("Doctor is not in db " + errorMessage + doctor_in_DB);
                        RegisterDoctorVoley(specialization, name, surname, email, password, security_question, security_answer);

                        //Send email notification that doctor is added to DB
                        //Creating SendMail object
                        SendMail sm = new SendMail(RegisterDoctorActivity.this,
                                                    //"tomas.mako@centrum.sk",
                                                            email,
                                                    "Registracia noveho doktora",
                                                    //"sd");
                                                    "Doktor " + name + " " + surname + " sa prave registroval v aplikacii.");

                        //Executing sendmail to send email
                        sm.execute();

                    }else {
                        System.out.println("Doctor is in db " + errorMessage + doctor_in_DB);
                        toast("Doktor už je registrovaný!!");
                    }

                    //return doctor_in_DB;

                   /* System.out.println("Doctors " + doctors);
                    System.out.println("String response " + response);

                    String doc1_name = doc_name;
                    String doc1_surname = doc_surname;
                    String doc1_email = doc_email;

                    for(int i=0;i<doctors.length();i++) {


                        JSONObject jsonObject = doctors.getJSONObject(i);

                        doctorName_in_DB  = jsonObject.getString("name").matches(doc1_name);
                        doctorSurname_in_DB = jsonObject.getString("surname").matches(doc1_surname);
                        doctorEmail_in_DB = jsonObject.getString("email").matches(doc1_email);
                        System.out.println("Status doctor Name" + doctorName_in_DB + " Surname " + doctorSurname_in_DB + " doctor email " + doctorEmail_in_DB);



                    }*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Unable to parse json array");
                    }
                });


        requestQueue.add(stringRequest);

        return doctor_in_DB;

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

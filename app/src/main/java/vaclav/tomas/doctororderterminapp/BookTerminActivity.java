package vaclav.tomas.doctororderterminapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static vaclav.tomas.doctororderterminapp.Api.URL_BASE_FOR_GET_DOCTOR_TERMIN;
import static vaclav.tomas.doctororderterminapp.MainActivity.CODE_POST_REQUEST;

//import static vaclav.tomas.doctororderterminapp.BookTerminActivity.sendEmail;

public class BookTerminActivity extends AppCompatActivity {

    private static Button bookTerminButton, selectDate, bookTerminToDatabaseButton;
    private static EditText dateEditText, nameEditText, surnameEditText, emailEditText;
    public int day, month, year;
    DatePickerDialog datePicker;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private static Spinner hourSpinner, minuteSpinner, doctorSpinner;
    public static String hour;
    public String selected_hour, selected_minute, selected_doctor, selMonth, selDay,selYear;
    public boolean patientName_in_DB, patientSurname_in_DB, terminTime_in_DB, terminDate_in_DB, tetmin_in_DB;
    private static TextView choosenDateTextView;
    private ProgressDialog progressDialog;

    StringBuilder stringBuilder = new StringBuilder();

    //ProgressDialog progressDialog = new ProgressDialog(this);

    public String test;
    public String[] str;
    public String[] items;

    JSONObject jsonObject;
    List<Doctor> doctorsList;


    // spinner tutorials
    // http://abhiandroid.com/ui/custom-spinner-examples.html
    //https://android--code.blogspot.sk/2015/08/android-spinner-get-selected-item-text.html
    //https://developer.android.com/guide/topics/ui/controls/spinner.html
    //https://stackoverflow.com/questions/1947933/how-to-get-spinner-value

    String[] selectHourSpinner = new String[]{
            "--","8","9","10","11","13","14","15","16"
    };

    String[] selectMinuteSpinner = new String[]{
            "--","00","15","30","45"
    };

    String[] selectedTmHoursForSpinner = new String[] {"--","8","9","10","11","13","14","15","16"};
    String[] selectedTmMinuteForSpinner = new String[] {"--","00","15","30","45"};
    String[] selectedAmHoursForSpinner = new String[] {"--","8","9","10","11","13","14","15","16"};
    String[] selectedAmMinuteForSpinner = new String[] {"--","00","30"};
    String[] selectedInyHoursForSpinner = new String[] {"--","8","9"};
    String[] selectedInyMinuteForSpinner = new String[] {"--", "00", "15"};
    //String[] selectedFinalMinuteSpinner = new String[] {};

    String[] selectDoctorSpinner = new String[]{
            "--","mako_tomas","buzinsky_igor"
            //"--",stringBuilder.append("")
    };
    //List<String> minuteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_termin);

        progressDialog  = new ProgressDialog(this);

        patientName_in_DB = false;
        patientSurname_in_DB = false;
        terminTime_in_DB = false;
        terminDate_in_DB = false;

        doctorsList = new ArrayList<Doctor>();
        //minuteList = new ArrayList<>();

        //getDate();
        //sendEmail();
        getHour();
        getMinute();
        getDateWithButton();
        //getDoctor();
        //getDoctorNameSpinner(); test
        //bookTerminToDBPostRequestHandler();
        bookTermintoDBVoley();

        //onStart();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    private void toast(String x){
        Toast.makeText(this,x,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("loading started");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, Api.URL_READ_DOCTORS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject object = new JSONObject(response);

                    JSONArray doctors  = object.getJSONArray("doctors");

                    System.out.println("Termins " + doctors);
                    System.out.println("String response " + response);

                    for(int i=0;i<doctors.length();i++) {

                        jsonObject = doctors.getJSONObject(i);

                        doctorsList.add(new Doctor(
                                jsonObject.getString("name"),
                                jsonObject.getString("surname")
                        ));

  //                      System.out.println("Loaded list of doctors from DB " + doctorsList);

                        getDoctorNameSpinner();


                    }

                }catch (JSONException e){
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

    }


    // function for get doctor from spiner
    public void getDoctorNameSpinner(){

        doctorSpinner = (Spinner)findViewById(R.id.doctorSpinner);

        //final List<String> DoctorList = new ArrayList<>(Arrays.asList(selectDoctorSpinner));


        //final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
        final ArrayAdapter<Doctor> spinnerArrayAdapter = new ArrayAdapter<Doctor>(
               // this,R.layout.hour_spinner,DoctorList){
            this,R.layout.hour_spinner, doctorsList){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
               /* if(position%2 == 1) {
                    // Set the item background color
                    //tv.setBackgroundColor(Color.parseColor("#FFC9A3FF"));
                }
                else {
                    // Set the alternate item background color
                    //tv.setBackgroundColor(Color.parseColor("#FFAF89E5"));
                }*/
                return view;
            }
        };

        doctorSpinner.setAdapter(spinnerArrayAdapter);

        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //Performing action onItemSelected and onNothing selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
                //selected_doctor = (String) parent.getItemAtPosition(position);
                Doctor doctor  = (Doctor) ( (Spinner) findViewById(R.id.doctorSpinner) ).getSelectedItem();
                selected_doctor = doctor.toString();
                System.out.println("Vybrany doktor: " + selected_doctor);
                //Toast.makeText(getApplicationContext(), hourSpinnerValues[position], Toast.LENGTH_LONG).show();
                //System.out.println("Vybrany doktor: " + selected_doctor);

                changeHourSpinnerBaseOfDoctorName(selected_doctor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

    }

    // function for checking doctor name and on base this set hour and minute input range
    public void changeHourSpinnerBaseOfDoctorName(String s) {

        switch (s) {
            case "mako_tomas":
                selectHourSpinner = selectedTmHoursForSpinner;
                selectMinuteSpinner = selectedTmMinuteForSpinner;
                getHour();
                getMinute();
                break;
            case "mihalova_anika":
                selectHourSpinner = selectedAmHoursForSpinner;
                selectMinuteSpinner = selectedAmMinuteForSpinner;
                getHour();
                getMinute();
                break;
            case "hrncar_jozef":
                selectHourSpinner = selectedInyHoursForSpinner;
                selectMinuteSpinner = selectedInyMinuteForSpinner;
                getHour();
                getMinute();
                break;
            default:
                System.out.println("No match for selected doctor");
        }


    }

    // function for get minute from spinner
    public void getMinute(){
    //public void getMinute(String m){

        minuteSpinner = (Spinner)findViewById(R.id.minuteSpinner);

        //if (m.equals("mako_tomas")) {
          //  selectedFinalMinuteSpinner = selectMinuteSpinner;
        //}

        final List<String> minuteList = new ArrayList<>(Arrays.asList(selectMinuteSpinner));
        //final List<String> minuteList = new ArrayList<>(Arrays.asList(selectedFinalMinuteSpinner));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.hour_spinner,minuteList){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
               /* if(position%2 == 1) {
                    // Set the item background color
                    //tv.setBackgroundColor(Color.parseColor("#FFC9A3FF"));
                }
                else {
                    // Set the alternate item background color
                    //tv.setBackgroundColor(Color.parseColor("#FFAF89E5"));
                }*/
                return view;
            }
        };

        minuteSpinner.setAdapter(spinnerArrayAdapter);

        minuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //Performing action onItemSelected and onNothing selected
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
                selected_minute = (String) parent.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(), hourSpinnerValues[position], Toast.LENGTH_LONG).show();
                System.out.println("Vybrana minuta: " + selected_minute);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });


    }

    // function for get hour from spinner
    public void getHour(){

        hourSpinner = (Spinner)findViewById(R.id.hourSpinner);

        final List<String> hourList = new ArrayList<>(Arrays.asList(selectHourSpinner));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.hour_spinner,hourList){
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                /*if(position%2 == 1) {
                    // Set the item background color
                    //tv.setBackgroundColor(Color.parseColor("#FFC9A3FF"));
                }
                else {
                    // Set the alternate item background color
                    //tv.setBackgroundColor(Color.parseColor("#FFAF89E5"));
                }*/
                return view;
            }
        };

        //hourSpinner.setAdapter(adapter);
        hourSpinner.setAdapter(spinnerArrayAdapter);

        hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        //Performing action onItemSelected and onNothing selected
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
            selected_hour = (String) parent.getItemAtPosition(position);
            //Toast.makeText(getApplicationContext(), hourSpinnerValues[position], Toast.LENGTH_LONG).show();
            System.out.println("Vybrana hodina: " + selected_hour);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

        });


    }



    /*public void getDate(){

        dateEditText = (EditText)findViewById(R.id.DateEditText);
        //test = (TextView)findViewById(R.id.textView3);
        choosenDateTextView = (TextView)findViewById(R.id.ChoosenDatetextView);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                year = c.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(BookTerminActivity.this, new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        int rightMonth = selectedMonth + 1;
                        //dateEditText.setText("Vybrali ste" + selectedDay + " " + rightMonth + " " + selectedYear);
                        choosenDateTextView.setText("Vybrali ste: " + Integer.toString(selectedDay) + " " + Integer.toString(selectedMonth) + " " + Integer.toString(selectedYear));
                        //test.setText(selectedDay);
                    }
                },month,day,year);
                datePicker.show();
            }
        });
    }*/

    // function for get date from spinner
    public void getDateWithButton(){

        selectDate = (Button)findViewById(R.id.SelectDateButton);

        choosenDateTextView = (TextView)findViewById(R.id.ChoosenDatetextView);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                year = c.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(BookTerminActivity.this, new DatePickerDialog.OnDateSetListener() {
                //datePicker = new DatePickerDialog(BookTerminActivity.this, (DatePickerDialog.OnDateSetListener) BookTerminActivity() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        int rightMonth = selectedMonth + 1;
                        //dateEditText.setText("Vybrali ste" + selectedDay + " " + rightMonth + " " + selectedYear);

                        selDay = Integer.toString(selectedDay);
                        selMonth = Integer.toString(rightMonth);
                        selYear = Integer.toString(selectedYear);
                        choosenDateTextView.setText("Vybrali ste: " + Integer.toString(selectedDay) + " " + Integer.toString(rightMonth) + " " + Integer.toString(selectedYear));
                        //test.setText(selectedDay);
                    }
                },month,day,year);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.show();

            }
        });

    }

    // function for send email
    /*public void sendEmail(){

        //checknut
        //https://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a/2033124#2033124
        //https://github.com/enrichman/androidmail

        bookTerminButton = (Button)findViewById(R.id.TestSendEmailButton);

        bookTerminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selHour = selected_hour;
                String selMinute = selected_minute;
                String selDoctor = selected_doctor;

                String[] TO = {"tomas.mako@centrum.sk"};
                String[] CC = {""};
                //Intent emailIntent = new Intent(Intent.ACTION_SEND);
                Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);


                emailIntent.setData(Uri.parse("mailto:"));
                //emailIntent.setData(Uri.parse("tomas.mako@centrum.sk"));
               // emailIntent.setType("text/plain");
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT,"Vybrany termin na osetrenie je  " + month + " " + day + " o " + selHour + " : " + selMinute + " u doktora " + selDoctor);

                try {
                    //startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    startActivity(emailIntent);
                    finish();
                    Log.i("Finished sending email.", "");
                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(BookTerminActivity.this, "There is no email client instaled", Toast.LENGTH_LONG).show();
                }

            }
        });

    }*/

    // function for book termin by patient
    public void bookTerminToDBPostRequestHandler(){

        bookTerminToDatabaseButton = (Button)findViewById(R.id.bookTerminToDatabaseButton);

        bookTerminToDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Initialize EditText View
                nameEditText = (EditText)findViewById(R.id.NameEditText);
                surnameEditText = (EditText)findViewById(R.id.SurnameEditText);
                emailEditText = (EditText)findViewById(R.id.EmailEditText);

                String selectedDoctorBySpinner = selected_doctor;

                String selectedDayBySpinner = selDay;
                String selectedMonthBySpinner = selMonth;
                String selectedYearBySpinner = selYear;
                String selectedDateBySpinner = selectedDayBySpinner + "." + selectedMonthBySpinner + "." + selectedYearBySpinner;

                String selectedHourBySpinner = selected_hour;
                String selectedMinuteBySpinner = selected_minute;
                String selectedTimeBySpinner = selectedHourBySpinner + " : " + selectedMinuteBySpinner;

                String inputedName = nameEditText.getText().toString();
                String inputedSurname = surnameEditText.getText().toString();
                String emailForConfirm = emailEditText.getText().toString();

                System.out.println("Vybrany datum " + selectedDateBySpinner);

                if(inputedName.isEmpty() ||
                        inputedSurname.isEmpty() ||
                        selectedDateBySpinner.contains("null.null") ||
                        selectedTimeBySpinner.contains("--") ||
                        selectedDoctorBySpinner.contains("--") ||
                        emailForConfirm.isEmpty()){
                    toast("Nevyplnili ste vsetky polia !!");
                }else {

                   // progressDialog.setMessage("Prebieha registrovanie terminu.");
                   // progressDialog.show();

                    HashMap<String, String> requestedParams = new HashMap<>();
                    requestedParams.put("patientName", inputedName);
                    requestedParams.put("patientSurname", inputedSurname);
                    requestedParams.put("terminDate", selectedDateBySpinner);
                    requestedParams.put("terminTime", selectedTimeBySpinner);

                    System.out.println("Selected doctor " + selectedDoctorBySpinner + "Patient name: " + inputedName + " patient surname: " +inputedSurname + " termin Date " + selectedDateBySpinner + " termin time " + selectedTimeBySpinner);


                    CheckIfTerminIsFree(selectedDoctorBySpinner, inputedName, inputedSurname, emailForConfirm, selectedDateBySpinner, selectedTimeBySpinner);

                    if(patientName_in_DB == false && patientSurname_in_DB == false && terminTime_in_DB == false && terminTime_in_DB == false){

                        //Calling the book termin API
                        PostRequestHandler postRequestHandler = new PostRequestHandler(Api.URL_BASE_FOR_BOOK_TERMIN + selectedDoctorBySpinner, requestedParams, CODE_POST_REQUEST);
                        postRequestHandler.execute();

                    } else {

                        toast("Termín už je obsadený, vyberte prosím iný termín.");
                    }

                    //progressDialog.hide();

                }
            }
        });
    }

    public void bookTermintoDBVoley() {

        bookTerminToDatabaseButton = (Button) findViewById(R.id.bookTerminToDatabaseButton);

        bookTerminToDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Initialize EditText View
                nameEditText = (EditText) findViewById(R.id.NameEditText);
                surnameEditText = (EditText) findViewById(R.id.SurnameEditText);
                emailEditText = (EditText)findViewById(R.id.EmailEditText);

                String selectedDoctorBySpinner = selected_doctor;

                String selectedDayBySpinner = selDay;
                String selectedMonthBySpinner = selMonth;
                String selectedYearBySpinner = selYear;
                String selectedDateBySpinner = selectedDayBySpinner + "." + selectedMonthBySpinner;
                //String selectedDateBySpinner = selectedDayBySpinner + "." + selectedMonthBySpinner + "." + selectedYearBySpinner;

                String selectedHourBySpinner = selected_hour;
                String selectedMinuteBySpinner = selected_minute;
                String selectedTimeBySpinner = selectedHourBySpinner + " : " + selectedMinuteBySpinner;

                String inputedName = nameEditText.getText().toString();
                String inputedSurname = surnameEditText.getText().toString();
                String inputedEmailForConfirm = emailEditText.getText().toString();

                System.out.println("Vybrany datum " + selectedDateBySpinner);

                //CheckIfTerminIsFree(selectedDoctorBySpinner,inputedName,inputedSurname,selectedDateBySpinner,selectedTimeBySpinner);

                if (inputedName.isEmpty() ||
                        inputedSurname.isEmpty() ||
                        inputedEmailForConfirm.isEmpty() ||
                        selectedDateBySpinner.contains("null.null") ||
                        selectedTimeBySpinner.contains("--") ||
                        selectedDoctorBySpinner.contains("--")) {
                    toast("Nevyplnili ste vsetky polia !!");
                } else {

                    // progressDialog.setMessage("Prebieha registrovanie terminu.");
                    // progressDialog.show();

                   /* HashMap<String, String> requestedParams = new HashMap<>();
                    requestedParams.put("patientName", inputedName);
                    requestedParams.put("patientSurname", inputedSurname);
                    requestedParams.put("terminDate", selectedDateBySpinner);
                    requestedParams.put("terminTime", selectedTimeBySpinner);*/

                    System.out.println("Selected doctor " + selectedDoctorBySpinner + "Patient name: " + inputedName + " patient surname: " + inputedSurname + " termin Date " + selectedDateBySpinner + " termin time " + selectedTimeBySpinner);

                    //RegisterTerminForDoctorVoley(selectedDoctorBySpinner, inputedName, inputedSurname, selectedDateBySpinner, selectedTimeBySpinner);

                    //variant1 checking and registering termin
                    checkifDoctorHaveFreeChosenTermin(selectedDoctorBySpinner, inputedName, inputedSurname, inputedEmailForConfirm, selectedDateBySpinner, selectedTimeBySpinner);

                    //variant2 checking and registering termin
                    //CheckIfTerminIsFree(selectedDoctorBySpinner,inputedName,inputedSurname,selectedDateBySpinner,selectedTimeBySpinner);

                }
            }
        });

    }

    public void RegisterTerminForDoctorVoley(String sel_doctor, final String t_name, final String t_surname,final String inputedEmailForConfirm, final String t_date, final String t_time){

        //System.out.println(" vybrany doktor " + sel_doctor);
        progressDialog.setMessage("Prebieha rezervácia termínu...");
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Api.URL_BASE_FOR_BOOK_TERMIN + sel_doctor, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){

                        progressDialog.cancel();

                        SendMail sm = new SendMail(BookTerminActivity.this,
                                inputedEmailForConfirm,
                                "Rezervácia termínu na vyšetrenie",
                                "Termin rezervovaný dňa " + t_date + " v čase " + t_time +
                                        " . Termin bude potvrdený alebo zamietnutý lekárom. Čakajte na potvrdzujúci email."
                                );
                        sm.execute();

                        toast("Termín úspešne registrovaný.");

                    }else {
                        // Error in register. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        toast("Chyba pri registrácii termínu " + errorMsg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", "Unable to parse json array");
                }}) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("patientName", t_name);
                params.put("patientSurname", t_surname);
                params.put("terminDate", t_date);
                params.put("terminTime", t_time);
                // test posting stavTerminu
                params.put("stavTerminu", "nepotvrdeny");
                params.put("emailForConfirm", inputedEmailForConfirm);

                return params;
            }

        };

        requestQueue.add(stringRequest);


    }

    // prevent leak activity and go to next activity until progress dialog is not closed
    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }

    // Checking if same record is not in database
    public void CheckIfTerminIsFree(final String sel_doctor, final String t_name, final String t_surname, final String inputedEmailForConfirm, final String t_date, final String t_time){

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                //"http://192.168.1.145/dentistapp/DBConnection/v1/Api.php?apicall=getDoctorTermins&username=" + sel_doctor, new Response.Listener<String>() {
                URL_BASE_FOR_GET_DOCTOR_TERMIN + sel_doctor, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    JSONArray termins  = object.getJSONArray("doctorTermin");

                    System.out.println("Termins " + termins);
                    System.out.println("String response " + response);



                    for(int i=0;i<termins.length();i++) {


                        JSONObject jsonObject = termins.getJSONObject(i);

                        patientName_in_DB  = jsonObject.getString("patientName").matches(t_name);
                        patientSurname_in_DB = jsonObject.getString("patientSurname").matches(t_surname);
                        terminTime_in_DB = jsonObject.getString("terminTime").matches(t_time);
                        terminDate_in_DB = jsonObject.getString("terminDate").matches(t_date);
                        System.out.println("Status patient Name" + patientName_in_DB + " Surname " + patientSurname_in_DB + " Termin time" + terminTime_in_DB + " Termin date" + terminDate_in_DB);

                    }

                    if(terminDate_in_DB == false && terminTime_in_DB == false){
                        RegisterTerminForDoctorVoley(sel_doctor, t_name, t_surname, inputedEmailForConfirm, t_date, t_time);
                    }else{
                        toast("Termín už je obsadený.");
                    }




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

    }

    //Check if termin is free second variant
    public void checkifDoctorHaveFreeChosenTermin(final String sel_doctor, final String t_name, final String t_surname, final String inputedEmailForConfirm, final String t_date, final String t_time){

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Api.URL_CHECK_IF_TERMIN_IS_FREE + sel_doctor + "&terminDate="+ t_date + "&terminTime=" + t_time, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    tetmin_in_DB = object.getBoolean("error");
                    String errorMessage = object.getString("message");

                    if(!tetmin_in_DB){
                        System.out.println("Termin is not in db " + errorMessage + tetmin_in_DB);
                        //RegisterDoctorVoley(specialization, name, surname, email, password, security_question, security_answer);
                        RegisterTerminForDoctorVoley(sel_doctor,t_name,t_surname, inputedEmailForConfirm, t_date,t_time);
                    }else {
                        System.out.println("Termin is in db " + errorMessage + tetmin_in_DB);
                        toast("Termín už je obsadený. ");
                    }

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
    }

}

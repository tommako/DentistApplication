package vaclav.tomas.doctororderterminapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class DoctorTerminsActivity extends AppCompatActivity {

    private TextView doctorName, updateTextView, selectedDateForFilter;
    private Button logOutButton, dateForFiler, filterButton, resetFilterButton;
    private Doctor doctor;
    public static int day, month, year;
    DatePickerDialog datePicker;
    private DoctorSession doctorSession;
    public String DoctorName, DoctorSurname, DoctorTableForSelectData, selMonth, selDay,selYear, dateForFilter;;
    private ListView patientsTermins;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    List<Patient> doctorTerminList;
    RecyclerView recyclerView;

    Doctor doc = new Doctor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_termins);


        doctor = new Doctor(this);
        doctorSession = new DoctorSession(this);
        doctorName = (TextView)findViewById(R.id.DoctorNameTextView);
        //updateTextView = (TextView)findViewById(R.id.textViewUpdate);
        //updateTextView.setText("abrakadabra");

        if(!doctorSession.isDoctorLoggedin()){
            startActivity(new Intent(this, LoginDoctorActivity.class));
            finish();
        }


        DoctorName = doctor.getKeyUsername();
        DoctorSurname = doctor.getKeySurname();
        doctorName.setText(DoctorName + " " + DoctorSurname + " ste prihlaseny.");

        DoctorTableForSelectData = DoctorSurname.toLowerCase() + "_" + DoctorName.toLowerCase();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());

        recyclerView = findViewById(R.id.terminsRecyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        doctorTerminList = new ArrayList<Patient>();

        LogOut();

        getDate();
        filterRecords();
        resetFilter();

        //onStart();
        sysDate();

    }


    // function for show toast warning or information
    private void toast(String x){
        Toast.makeText(this,x,Toast.LENGTH_LONG).show();
    }

    // load data from database after start activity if doctor is succesfully log in
    @Override
    protected void onStart()
    {
        super.onStart();

        //System.out.println("link request" + Api.URL_BASE_FOR_GET_DOCTOR_TERMIN + DoctorTableForSelectData );


        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                //"http://192.168.1.145/dentistapp/DBConnection/v1/Api.php?apicall=getDoctorTermins&username=" + DoctorTableForSelectData, new Response.Listener<String>() {
                Api.URL_BASE_FOR_GET_DOCTOR_TERMIN + DoctorTableForSelectData, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                    JSONObject object = new JSONObject(response);

                    JSONArray termins  = object.getJSONArray("doctorTermin");

                System.out.println("Termins " + termins);
                System.out.println("String response " + response);



                for(int i=0;i<termins.length();i++) {


                    JSONObject jsonObject = termins.getJSONObject(i);

                    doctorTerminList.add(new Patient(
                            jsonObject.getString("id"),
                            jsonObject.getString("patientName"),
                            jsonObject.getString("patientSurname"),
                            jsonObject.getString("emailForConfirm"),
                            jsonObject.getString("terminDate"),
                            jsonObject.getString("terminTime"),
                            jsonObject.getString("stavTerminu")

                    ));


                        }

                        //test sorting
                            /*Collections.sort(doctorTerminList, new Comparator<Patient>() {
                                @Override
                                public int compare(Patient patient, Patient p1) {

                                    return  (patient.getTerminDate().compareTo(p1.getTerminDate()));
                                }
                            });*/

                            //Collections.sort(doctorTerminList ,Collections.reverseOrder());


                        DoctorTerminAdapter doctorTerminAdapter = new DoctorTerminAdapter(DoctorTerminsActivity.this, doctorTerminList);


                recyclerView.setAdapter(doctorTerminAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Unable to parse json array1");
                    }
                });


        requestQueue.add(stringRequest);

    }

    //get system date
    public int sysDate(){

        Calendar c = Calendar.getInstance();
        //SimpleDateFormat sdf = new SimpleDateFormat("dd.MMM.yyyy hh:mm:ss aa");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.M.yyyy hh:mm:ss aa");
        String date = sdf.format(c.getTime());
        int monday = Calendar.MONDAY;
        int dayOFWeek = c.get(Calendar.DAY_OF_MONTH);
        System.out.println("System time is: " + date + " day of month " + dayOFWeek + " monday: " + monday);

        return monday;
    }

    // Function for get date for filtering records
    public void getDate(){

        dateForFiler = (Button)findViewById(R.id.DateForFilterButton);

        selectedDateForFilter = (TextView)findViewById(R.id.textViewForFilter);

        dateForFiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                year = c.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(DoctorTerminsActivity.this, new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        int rightMonth = selectedMonth + 1;
                        //dateEditText.setText("Vybrali ste" + selectedDay + " " + rightMonth + " " + selectedYear);
                        selDay = Integer.toString(selectedDay);
                        selMonth = Integer.toString(rightMonth);
                        selYear = Integer.toString(selectedYear);
                        selectedDateForFilter.setText("Vybrali ste: " + Integer.toString(selectedDay) + "." + Integer.toString(rightMonth));
                        dateForFilter = Integer.toString(selectedDay) + "." + Integer.toString(rightMonth);
                        //test.setText(selectedDay);
                    }
                },month,day,year);
                //datePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) - 205);
                datePicker.updateDate(year, month, day);
                //datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                //datePicker.getDatePicker().setMinDate(System.currentTimeMillis() + 1000);

                //.getDatePicker()
                        //.setMinDate(System.currentTimeMillis() - 1000);
                        //.setMaxDate(new Date().getTime());
                  //      .setMinDate(System.currentTimeMillis() - 604800000);
                datePicker.show();

            }
        });

    }

    //filter function
    public void filterRecords(){

        filterButton = (Button)findViewById(R.id.FilterButton);

        RequestQueue requestQueue= Volley.newRequestQueue(this);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toast("selected date for filter: " + selectedDateForFilter.getText().toString());

                if(selectedDateForFilter.getText().toString().contains("Vybraný termín pre filter: ")){
                    toast("Nevybrali ste žiadny termín pre filter.");
                }else {


                   filteringRecordunderSelectedDate(dateForFilter);

                }

            }
        });

    }

    // executing function for filtering data under termin
    public void filteringRecordunderSelectedDate (final String filteringDate){

System.out.println("filter link " + Api.URL_FILTER_TERMINS + DoctorName + DoctorSurname + "&terminDate=" + filteringDate);

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Api.URL_FILTER_TERMINS + DoctorName + DoctorSurname + "&terminDate=" + filteringDate, new Response.Listener<String>() {
            // Api.URL_BASE_FOR_GET_DOCTOR_TERMIN + DoctorTableForSelectData, new Response.Listener<String>() {
            // "http://192.168.1.145/dentistapp/DBConnection/v1/Api.php?apicall=getDoctorTerminsBaseOfDate"
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject object = new JSONObject(response);

                    JSONArray termins  = object.getJSONArray("doctorTermin");

                    System.out.println("Termins " + termins);
                    System.out.println("String response " + response);

                    doctorTerminList.clear();

                    for(int i=0;i<termins.length();i++) {


                        JSONObject jsonObject = termins.getJSONObject(i);

                        doctorTerminList.add(new Patient(
                               // jsonObject.getString("id"),
                                jsonObject.getString("patientName"),
                                jsonObject.getString("patientSurname"),
                                //jsonObject.getString("emailForConfirm"),
                                jsonObject.getString("terminDate"),
                                jsonObject.getString("terminTime"),
                                jsonObject.getString("stavTerminu")

                        ));


                    }

                    //test sorting
                            /*Collections.sort(doctorTerminList, new Comparator<Patient>() {
                                @Override
                                public int compare(Patient patient, Patient p1) {

                                    return  (patient.getTerminDate().compareTo(p1.getTerminDate()));
                                }
                            });*/

                    //Collections.sort(doctorTerminList ,Collections.reverseOrder());


                    DoctorTerminAdapter doctorTerminAdapter = new DoctorTerminAdapter(DoctorTerminsActivity.this, doctorTerminList);



                    recyclerView.setAdapter(doctorTerminAdapter);

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

    public void resetFilter(){

        resetFilterButton = (Button)findViewById(R.id.resetFilterButton);

        resetFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doctorTerminList.clear();
                selectedDateForFilter.setText("Vybraný termín pre filter: ");
                onStart();
            }
        });

    }

    public void confirmOrDenyTermin(final String id, final String patientName, final String patientSurname, final String emailForConfirm, final String terminDate, final String terminTime, final String stavTerminu){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Api.URL_UPDATE_TERMIN + DoctorName + DoctorSurname, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){

                       // progressDialog.cancel();
                        //resetFilter();
                        toast("Termín úspešne zmenený.");
                        //resume();
                        //refreshPatientListAfterUpdate();
                        //resetFilter();

                    }else {
                        // Error in register. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        toast("Chyba pri zmene termínu " + errorMsg);
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
                params.put("id", id);
                params.put("patientName", patientName);
                params.put("patientSurname", patientSurname);
                params.put("emailForConfirm", emailForConfirm);
                params.put("terminDate", terminDate);
                params.put("terminTime", terminTime);
                params.put("stavTerminu", stavTerminu);

                return params;
            }


        };


        requestQueue.add(stringRequest);

        if(stavTerminu.matches("potvrdený")) {
            SendMail sm = new SendMail(DoctorTerminsActivity.this,
                    //"tomas.mako@centrum.sk",
                    emailForConfirm,
                    "Potvrdenie terminu vysetrenia",
                    //"sd");
                    "Doktor potvrdil Vas termin ");
            sm.execute();
        } else {
            SendMail sm = new SendMail(DoctorTerminsActivity.this,
                    //"tomas.mako@centrum.sk",
                    emailForConfirm,
                    "Potvrdenie terminu vysetrenia",
                    //"sd");
                    "Doktor zamietol Vas termin, prosim vyberte si iny termin a objednajte sa znovu ");
            sm.execute();
        }

        this.recreate();


    }


    public String loginDocor(){
        String returnString = doctorName + DoctorSurname;
        return returnString;
        //LinearLayout linearLayout;
        //linearLayout = itemView.findViewById(R.id.terminAdapterLayout);
    }

    /*public SharedPreferences sp(){
        SharedPreferences sharedpreferences = getSharedPreferences(LoginDoctorActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences;
    }*/

    // Function for logout doctor
    public void LogOut(){

        logOutButton = (Button)findViewById(R.id.buttonLogOut);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doctorName = (TextView)findViewById(R.id.DoctorNameTextView);


                doctorSession.setLoggedin(false);
                doctor.clearDoctor();
                startActivity(new Intent(DoctorTerminsActivity.this, LoginDoctorActivity.class));
                finish();


            }
        });

    }
}

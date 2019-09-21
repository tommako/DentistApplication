package vaclav.tomas.doctororderterminapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private static ImageButton logiButton;
    private static ImageButton regisButton;
    private static ImageButton bookButton;

    public static final int CODE_GET_REQUEST = 1024;
    public static final int CODE_POST_REQUEST = 1025;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OpenLoginActivity();
        //OpenRegisterActivity();
        OpenBookPatientActivity();

    }

    public void OpenLoginActivity(){

        logiButton = (ImageButton)findViewById(R.id.LoginBytton);

        logiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Login = new Intent ("vaclav.tomas.doctororderterminapp.LoginDoctorActivity");
                startActivity(Login);
            }
        });

    }

    /*public void OpenRegisterActivity(){

        regisButton = (Button)findViewById(R.id.RegisterButton);

        regisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Register = new Intent("vaclav.tomas.doctororderterminapp.RegisterDoctorActivity");
                startActivity(Register);
            }
        });

    }*/

    public void OpenBookPatientActivity(){

        bookButton = (ImageButton)findViewById(R.id.BookButton);

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Book = new Intent("vaclav.tomas.doctororderterminapp.BookTerminActivity");
                startActivity(Book);
            }
        });
    }

    // inner class to perform network request extending an AsyncTask
    /*public class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        // the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        // the request code to define where it is a get or post
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progress bar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressBar.setVisibility(View.VISIBLE);
        }

        // this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(GONE);

            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    //Toast.makeText(getA, object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }*/

}

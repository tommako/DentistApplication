package vaclav.tomas.doctororderterminapp;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static vaclav.tomas.doctororderterminapp.MainActivity.CODE_GET_REQUEST;
import static vaclav.tomas.doctororderterminapp.MainActivity.CODE_POST_REQUEST;

public class PostRequestHandler extends AsyncTask<Void,Void,String> {

    // the url where we need to send the request
    String url;

    //the parameters
    HashMap<String, String> params;

    // the request code to define where it is a get or post
    int requestCode;



    //constructor to initialize values
    PostRequestHandler(String url, HashMap<String, String> params, int requestCode) {
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
               // Toast.makeText(PostRequestHandler.this,"asdad", Toast.LENGTH_LONG).show();
                System.out.println("ok boolean message "+ object.getString("message"));

                //refreshDoctorList(object.getJSONArray("doctors"));
            }else{
                System.out.println("error boolean message "+ object.getString("message"));
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

    private void refreshDoctorList(JSONArray doctors) throws JSONException {
        //heroList.clear();
        List<Doctor> doctorList = new ArrayList<>();

        for (int i = 0; i < doctors.length(); i++) {
            JSONObject obj = doctors.getJSONObject(i);

            doctorList.add(new Doctor(
                    obj.getInt("id"),
                    obj.getString("specialization"),
                    obj.getString("name"),
                    obj.getString("surname"),
                    obj.getString("email"),
                    obj.getString("password"),
                    obj.getString("security_question"),
                    obj.getString("security_answer")
            ));
        }
    }
}

//}

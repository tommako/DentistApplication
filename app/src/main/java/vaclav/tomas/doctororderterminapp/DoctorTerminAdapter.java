package vaclav.tomas.doctororderterminapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorTerminAdapter extends RecyclerView.Adapter<DoctorTerminAdapter.DoctorTerminViewHolder> {

    private Context context;
    private List<Patient> patientList;


    public DoctorTerminAdapter(Context context, List<Patient> patientList){
        this.context = context;
        this.patientList = patientList;
        //this.doctor = doctor;
    }

    @Override
    public  DoctorTerminViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.doctor_termin_list, null);
        return  new DoctorTerminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DoctorTerminAdapter.DoctorTerminViewHolder holder, int position) {
        final Patient patient = patientList.get(position);

        //holder.id.setText(patient.getId());
        holder.patientName.setText(patient.getPatientName());
        holder.patientSurname.setText(patient.getPatientSurname());
        //holder.
        holder.terminDate.setText(patient.getTerminDate());
        holder.terminTime.setText(patient.getTerminTime());
        holder.stavTerminu.setText(patient.getStavTerminu());

        /*holder.updateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "test"  + patient.getPatientName(), Toast.LENGTH_LONG).show();

                // alert dialog with own layout
                // https://www.youtube.com/watch?v=Zd0TUuoPP-s

                final AlertDialog.Builder aDialog = new AlertDialog.Builder(context);
                aDialog.setMessage("Pacient " + patient.getPatientName() + " " + patient.getPatientSurname() + " na termin: ")
                        .setCancelable(false)
                        .setPositiveButton("Potvrdit termin pre: " + patient.getPatientName() + " " + patient.getPatientSurname(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // test

                                // ((DoctorTerminsActivity) context).confirmOrDenyTermin(patient.getId(),patient.getPatientName(), patient.getPatientSurname(), patient.getEmailForConfirm(), patient.getTerminTime(), patient.getTerminDate(), "potvrdený");
                                ((DoctorTerminsActivity) context).confirmOrDenyTermin(patient.getId(),patient.getPatientName(), patient.getPatientSurname(), patient.getEmailForConfirm(), patient.getTerminDate(), patient.getTerminTime(), "potvrdený");

                                // prestudovat
                                //https://www.androiddesignpatterns.com/2012/08/implementing-loaders.html
                                //https://developer.android.com/guide/components/activities/activity-lifecycle#onresume
                                //https://developer.android.com/guide/components/activities/state-changes
                                //https://android.jlelse.eu/8-ways-to-do-asynchronous-processing-in-android-and-counting-f634dc6fae4e

                                // test

                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Zamietnut/zrusit termin", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // ((DoctorTerminsActivity) context).confirmOrDenyTermin(patient.getId(),patient.getPatientName(), patient.getPatientSurname(), patient.getEmailForConfirm(), patient.getTerminTime(), patient.getTerminDate(), "zamietnutý");
                                ((DoctorTerminsActivity) context).confirmOrDenyTermin(patient.getId(),patient.getPatientName(), patient.getPatientSurname(), patient.getEmailForConfirm(), patient.getTerminDate(), patient.getTerminTime(), "zamietnutý");


                                dialogInterface.cancel();
                            }

                        //});
                        }).setNeutralButton("Zatvoriť", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                //}) {

                });
                AlertDialog alertDialog = aDialog.create();
                //alertDialog.setTitle("Potvrdenie terminu");
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    class DoctorTerminViewHolder extends RecyclerView.ViewHolder{

        TextView patientName, patientSurname, terminDate, terminTime, updateTextView, id, stavTerminu;
        // variable for select item from view
        LinearLayout linearLayout;

        public DoctorTerminViewHolder(View itemView){
            super(itemView);

            //id = itemView;
            patientName = itemView.findViewById(R.id.textViewPatientName);
            patientSurname = itemView.findViewById(R.id.textViewPatientSurname);
            terminDate = itemView.findViewById(R.id.textViewTerminDate);
            terminTime = itemView.findViewById(R.id.textViewTerminTime);
            stavTerminu = itemView.findViewById(R.id.textViewStavTerminu);

            // ceknut ako select item from recycleView
            // https://www.youtube.com/watch?v=dmIfFIHnKsk&list=PLk7v1Z2rk4hjHrGKo9GqOtLs1e2bglHHA&index=8

            linearLayout = itemView.findViewById(R.id.terminAdapterLayout);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });*/

            //updateTextView = itemView.findViewById(R.id.textViewUpdate);

            /*updateTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });*/

        }

    }

}

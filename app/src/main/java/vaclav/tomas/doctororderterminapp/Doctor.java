package vaclav.tomas.doctororderterminapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Doctor {

    private int id;
    private String specialization;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String security_question;
    private String security_answer;

    // for login share information and session
    private static final String PREF_NAME = "login";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_EMAIL = "email";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public Doctor(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setKeyUsername(String username){
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public void setKeyEmail (String email){
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public void setKeySurname (String surname){
        editor.putString(KEY_SURNAME, surname);
        editor.apply();
    }

    public void clearDoctor (){
        editor.clear();
        editor.commit();
    }

    public String getKeyUsername (){ return sharedPreferences.getString(KEY_USERNAME, "");}
    public String getKeyEmail () {return  sharedPreferences.getString(KEY_EMAIL, "");}
    public String getKeySurname () {return  sharedPreferences.getString(KEY_SURNAME, "");}

    public Doctor (int id, String specialization, String name, String surname, String email, String password, String security_question, String security_answer){
        this.id = id;
        this.specialization = specialization;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.security_question = security_question;
        this.security_answer = security_answer;
    }

    public Doctor (String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    public Doctor (){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurity_question() {
        return security_question;
    }

    public void setSecurity_question(String security_question) {
        this.security_question = security_question;
    }

    public String getSecurity_answer() {
        return security_answer;
    }

    public void setSecurity_answer(String security_answer) {
        this.security_answer = security_answer;
    }

    @Override
    public String toString() {
        return this.surname.toLowerCase() + "_" + this.name.toLowerCase();
    }
}

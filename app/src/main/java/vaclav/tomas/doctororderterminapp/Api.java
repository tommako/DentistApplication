package vaclav.tomas.doctororderterminapp;

public class Api {
//145
    // Ludmanska
    //private static final String ROOT_URL = "http://192.168.1.145/dentistapp/DBConnection/v1/Api.php?apicall=";
    //private static final String ROOT_URL_LOGIN = "http://192.168.1.145/dentistapp/DBConnection/includes/";
    // Ruzova
    //private static final String ROOT_URL = "http://192.168.0.105/dentistapp/DBConnection/v1/Api.php?apicall=";
    //private static final String ROOT_URL_LOGIN = "http://192.168.0.105/dentistapp/DBConnection/includes/";
    //Bohdanovce
    //private static final String ROOT_URL = "http://192.168.1.12/dentistapp/DBConnection/v1/Api.php?apicall=";
    //private static final String ROOT_URL_LOGIN = "http://192.168.1.12/dentistapp/DBConnection/includes/";
    // Rausberry PI
    //private static final String ROOT_URL = "http://194.1.228.173/dentistapp/DBConnection/v1/Api.php?apicall=";
    //private static final String ROOT_URL_LOGIN = "http://194.1.228.173/dentistapp/DBConnection/includes/";
    // Rausberry PI with dynamic adress
    private static final String ROOT_URL = "http://wasaox.ddns.net/dentistapp/v1/Api.php?apicall=";
    private static final String ROOT_URL_LOGIN = "http://wasaox.ddns.net/dentistapp/includes/";


    public static final String URL_CREATE_DOCTOR = ROOT_URL + "createDoctor";
    public static final String URL_READ_DOCTORS = ROOT_URL + "getDoctors";
    public static final String URL_BASE_FOR_BOOK_TERMIN = ROOT_URL + "book_termin_in_";
    //public static final String URL_LOGIN_DOCTOR_BASE_EMAIL_PASSWORD = ROOT_URL +"getDoctorByEmailAndPassword";
    public static final String URL_BASE_FOR_GET_DOCTOR_TERMIN = ROOT_URL + "getDoctorTermins&username=";
    public static final String URL_CHECK_IF_DOCTOR_EXIST = ROOT_URL + "checkIfDoctorAlreadyExistByNameAndSurname";
    public static final String URL_CHECK_IF_TERMIN_IS_FREE = ROOT_URL + "checkifDoctorHaveFreeChosenTermin_";
    public static final String URL_LOGIN = ROOT_URL_LOGIN + "login.php";
    public static final String URL_FILTER_TERMINS = ROOT_URL + "getDoctorTerminsBaseOfDate";
    public static final String URL_UPDATE_TERMIN = ROOT_URL + "updateTerminInDoctorFor";


    //http://localhost/DoctorOrderTerminApp/androidstudio/DBConnection/v1/Api.php?apicall=getDoctorTermins&username=mako_tomas
}

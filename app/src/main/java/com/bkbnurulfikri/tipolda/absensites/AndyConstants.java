package com.bkbnurulfikri.tipolda.absensites;

public class AndyConstants {
    // web service url constants
    public class ServiceType {
        public static final String BASE_URL = "https://kb.nurulfikri.id/api/android/absensi_tes/";
        public static final String LOGIN = BASE_URL + "login.php";
        public static final String REGISTER =  BASE_URL + "register.php";
        public static final String INPUT_HADIR =  BASE_URL + "inputhadir.php";
        public static final String UPDATE_HADIR =  BASE_URL + "updatehadir.php";

   }
    // webservice key constants
    public class Params {

        public static final String NAMA = "nama";
        public static final String KODE = "kodelokasi";
        public static final String LEVEL = "level";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
       }

    public class Hadirs {

        public static final String IDNF = "idnf";
        //public static final String NAMAH = "nama";
        public static final String KODEH = "kodelokasi";
    }
}


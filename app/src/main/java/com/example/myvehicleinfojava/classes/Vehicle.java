package com.example.myvehicleinfojava.classes;

public class Vehicle {
    public String brand, model,userID, vehicleID, plateID;
    public int year, kivika, hp;
    public String gasTypeIDStr;
    public int gasTypeIDInt;
    public int capacity;

    public String arKikloforias,  vin ;
    public String logoLocalPath ;



    @Override
    public String toString() {
        return brand + " " + model;
    }


    public static class colNames{
        public static final String BRAND = "brand";
        public static final String MODEL = "model";
        public static final String USER_ID = "userID";
        public static final String KIVIKA = "kivika";
        public static final String HP = "hp";
        public static final String YEAR = "year";
        public static final String PLATE_ID = "plateID";

        public static final String GAS_TYPE_ID_STR = "gasTypeIDStr";
        public static final String GAS_TYPE_ID_INT= "gasTypeIDInt";
        public static final String CAPACITY = "capacity";
        public static final String ARITHMOS_KIKLOFORIAS = "arithmos_kikloforias";
        public static final String VIN = "vin";
        public static final String LOGO_LOCAL_PATH = "logoLocalPath";
    }
}

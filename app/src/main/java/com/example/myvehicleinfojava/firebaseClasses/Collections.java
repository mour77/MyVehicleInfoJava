package com.example.myvehicleinfojava.firebaseClasses;

public enum Collections {




    GAS("Gas", "Καύσιμο"),
    BRANDS("Brands", "Χρήστες"),
    MODELS("Models", "Μοντέλο"),
    REPAIRS("Repairs", "Επισκευή"),
    USERS("Users", "Χρήστης"),
    VEHICLES("Vehicles", "Όχημα")

            ;


    public final String column;
    public final String text;

    Collections(String column, String text) {
        this.column = column;
        this.text = text;
    }
}

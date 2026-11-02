package com.example.clinicalmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Local SQLite storage for a classroom prototype.
 * Do not store real patient information in this demonstration app.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "clinic_manager.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE patients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "full_name TEXT NOT NULL," +
                "age INTEGER NOT NULL," +
                "sex TEXT NOT NULL," +
                "phone TEXT," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
        db.execSQL("CREATE TABLE appointments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "patient_name TEXT NOT NULL," +
                "visit_date TEXT NOT NULL," +
                "reason TEXT," +
                "status TEXT DEFAULT 'Scheduled')");
        db.execSQL("CREATE TABLE clinical_notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "patient_id INTEGER NOT NULL," +
                "note TEXT NOT NULL," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(patient_id) REFERENCES patients(id))");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For a classroom project, preserve existing records when upgrading.
        // Add explicit ALTER TABLE migrations here as the schema evolves.
    }

    public long addPatient(String name, int age, String sex, String phone) {
        ContentValues values = new ContentValues();
        values.put("full_name", name.trim());
        values.put("age", age);
        values.put("sex", sex);
        values.put("phone", phone.trim());
        return getWritableDatabase().insert("patients", null, values);
    }

    public Cursor getPatients() {
        return getReadableDatabase().rawQuery(
                "SELECT id, full_name, age, sex, phone FROM patients ORDER BY full_name", null);
    }

    public long addAppointment(String patient, String date, String reason) {
        ContentValues values = new ContentValues();
        values.put("patient_name", patient.trim());
        values.put("visit_date", date.trim());
        values.put("reason", reason.trim());
        return getWritableDatabase().insert("appointments", null, values);
    }

    public Cursor getAppointments() {
        return getReadableDatabase().rawQuery(
                "SELECT id, patient_name, visit_date, reason, status FROM appointments ORDER BY visit_date", null);
    }

    public long addClinicalNote(int patientId, String note) {
        ContentValues values = new ContentValues();
        values.put("patient_id", patientId);
        values.put("note", note.trim());
        return getWritableDatabase().insert("clinical_notes", null, values);
    }

    public int count(String table) {
        Cursor c = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM " + table, null);
        try { return c.moveToFirst() ? c.getInt(0) : 0; }
        finally { c.close(); }
    }
}

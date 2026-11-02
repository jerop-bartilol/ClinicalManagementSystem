package com.example.clinicalmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Student-level Clinical Management System prototype.
 * Features: dashboard, patient registration/list, appointment booking/list.
 * Demo sign-in is intentionally local and is NOT real authentication.
 */
public class MainActivity extends Activity {
    private LinearLayout root;
    private DatabaseHelper db;
    private final int teal = Color.rgb(7, 111, 121);
    private final int dark = Color.rgb(28, 48, 55);
    private final int muted = Color.rgb(95, 115, 121);
    private final int pale = Color.rgb(235, 246, 247);

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.rootLayout);
        showWelcome();
    }

    private void reset(String title, String subtitle) {
        root.removeAllViews();
        root.setPadding(20, 24, 20, 28);
        TextView heading = new TextView(this);
        heading.setText(title);
        heading.setTextSize(27);
        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        heading.setTextColor(dark);
        root.addView(heading);
        if (subtitle != null && !subtitle.isEmpty()) {
            TextView sub = new TextView(this);
            sub.setText(subtitle);
            sub.setTextSize(14);
            sub.setTextColor(muted);
            sub.setPadding(0, 5, 0, 18);
            root.addView(sub);
        }
    }

    private TextView text(String value, int size, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(dark);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setPadding(0, 8, 0, 8);
        return t;
    }

    private Button button(String label, Runnable action) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(teal));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, 6, 0, 6);
        b.setLayoutParams(p);
        b.setOnClickListener(v -> action.run());
        return b;
    }

    private EditText field(String hint, int inputType) {
        EditText e = new EditText(this);
        e.setSingleLine(true);
        e.setHint(hint);
        e.setTextSize(16);
        e.setInputType(inputType);
        e.setPadding(12, 10, 12, 10);
        e.setBackgroundTintList(android.content.res.ColorStateList.valueOf(teal));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, 4, 0, 4);
        e.setLayoutParams(p);
        return e;
    }

    private void showWelcome() {
        reset("Clinic Manager", "Clinical Management System • Student project");
        TextView mark = text("CMS", 36, true);
        mark.setTextColor(teal);
        mark.setGravity(Gravity.CENTER);
        mark.setPadding(0, 38, 0, 18);
        root.addView(mark);
        TextView intro = text("Manage patient records and appointments in one simple place.", 17, false);
        intro.setGravity(Gravity.CENTER);
        root.addView(intro);
        root.addView(button("Open demo dashboard", this::showDashboard));
        root.addView(text("Demo access only — no account or password is required.", 13, false));
        root.addView(text("Use fictional information only. This prototype is not for real patient care.", 13, false));
    }

    private void showDashboard() {
        reset("Clinic dashboard", "Overview of locally saved demo records");
        addStat("Registered patients", db.count("patients"));
        addStat("Appointments", db.count("appointments"));
        root.addView(text("Quick actions", 18, true));
        root.addView(button("＋ Register patient", this::showAddPatient));
        root.addView(button("▤ View patients", this::showPatients));
        root.addView(button("＋ Book appointment", this::showAddAppointment));
        root.addView(button("▣ View appointments", this::showAppointments));
        root.addView(button("About this project", this::showAbout));
    }

    private void addStat(String label, int value) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(16, 10, 16, 10);
        card.setBackgroundTintList(android.content.res.ColorStateList.valueOf(pale));
        card.setBackgroundColor(pale);
        TextView n = text(String.valueOf(value), 25, true);
        n.setTextColor(teal);
        card.addView(n);
        card.addView(text(label, 14, false));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, 5, 0, 5);
        root.addView(card, p);
    }

    private void showAddPatient() {
        reset("Register patient", "Enter basic demographic details");
        EditText name = field("Full name *",  android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        EditText age = field("Age *", android.text.InputType.TYPE_CLASS_NUMBER);
        EditText sex = field("Sex (e.g. Female, Male, Other) *", android.text.InputType.TYPE_CLASS_TEXT);
        EditText phone = field("Phone (optional)", android.text.InputType.TYPE_CLASS_PHONE);
        root.addView(name); root.addView(age); root.addView(sex); root.addView(phone);
        root.addView(button("Save patient", () -> {
            String n = name.getText().toString().trim();
            String a = age.getText().toString().trim();
            String s = sex.getText().toString().trim();
            if (n.isEmpty() || a.isEmpty() || s.isEmpty()) {
                toast("Please complete all required fields."); return;
            }
            int years;
            try { years = Integer.parseInt(a); }
            catch (NumberFormatException ex) { toast("Enter a valid age."); return; }
            if (years < 0 || years > 130) { toast("Enter an age between 0 and 130."); return; }
            long id = db.addPatient(n, years, s, phone.getText().toString());
            if (id > 0) { toast("Patient saved."); showPatients(); }
            else toast("Could not save patient.");
        }));
        root.addView(button("Back to dashboard", this::showDashboard));
    }

    private void showPatients() {
        reset("Patients", "Patient records saved on this device");
        Cursor c = db.getPatients();
        if (c.getCount() == 0) root.addView(text("No patients yet. Register a patient to get started.", 15, false));
        while (c.moveToNext()) {
            String record = "Patient #" + c.getInt(0) + "\n" + c.getString(1) +
                    "\nAge: " + c.getInt(2) + " • " + c.getString(3) +
                    (c.getString(4) == null || c.getString(4).isEmpty() ? "" : "\nPhone: " + c.getString(4));
            addRecord(record);
        }
        c.close();
        root.addView(button("Register another patient", this::showAddPatient));
        root.addView(button("Back to dashboard", this::showDashboard));
    }

    private void showAddAppointment() {
        reset("Book appointment", "Use the patient's name as displayed in the patient list");
        EditText patient = field("Patient full name *", android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        EditText date = field("Date and time (e.g. 2026-11-02 10:30) *", android.text.InputType.TYPE_CLASS_TEXT);
        EditText reason = field("Visit reason (optional)", android.text.InputType.TYPE_CLASS_TEXT);
        root.addView(patient); root.addView(date); root.addView(reason);
        root.addView(button("Save appointment", () -> {
            String p = patient.getText().toString().trim();
            String d = date.getText().toString().trim();
            if (p.isEmpty() || d.isEmpty()) { toast("Patient name and date/time are required."); return; }
            long id = db.addAppointment(p, d, reason.getText().toString());
            if (id > 0) { toast("Appointment saved."); showAppointments(); }
            else toast("Could not save appointment.");
        }));
        root.addView(button("Back to dashboard", this::showDashboard));
    }

    private void showAppointments() {
        reset("Appointments", "Scheduled visits stored on this device");
        Cursor c = db.getAppointments();
        if (c.getCount() == 0) root.addView(text("No appointments yet.", 15, false));
        while (c.moveToNext()) {
            String record = "Appointment #" + c.getInt(0) + "\n" + c.getString(1) +
                    "\nWhen: " + c.getString(2) +
                    (c.getString(3) == null || c.getString(3).isEmpty() ? "" : "\nReason: " + c.getString(3)) +
                    "\nStatus: " + c.getString(4);
            addRecord(record);
        }
        c.close();
        root.addView(button("Book another appointment", this::showAddAppointment));
        root.addView(button("Back to dashboard", this::showDashboard));
    }

    private void addRecord(String value) {
        TextView row = text(value, 15, false);
        row.setBackgroundColor(Color.WHITE);
        row.setPadding(14, 12, 14, 12);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(0, 5, 0, 5);
        root.addView(row, p);
    }

    private void showAbout() {
        reset("About this system", "Project overview");
        root.addView(text("Purpose", 18, true));
        root.addView(text("A basic mobile application for organizing patient demographic records and clinic appointments.", 15, false));
        root.addView(text("Technology", 18, true));
        root.addView(text("Android Studio • Java • XML layouts • SQLite database", 15, false));
        root.addView(text("Current scope", 18, true));
        root.addView(text("Local prototype with demo access, patient registration, record listing, appointment booking, basic validation and dashboard counts.", 15, false));
        root.addView(text("Limitations", 18, true));
        root.addView(text("No secure user authentication, encryption, cloud sync, audit trail, backup workflow, role permissions or clinical decision support. Do not enter real patient data.", 15, false));
        root.addView(button("Back to dashboard", this::showDashboard));
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

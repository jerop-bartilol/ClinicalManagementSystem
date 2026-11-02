# Clinical Management System (Android / Java)

A third-year first-semester student project prototype built with Android Studio, Java, XML and SQLite.

## Features
- Demo welcome screen (not real authentication)
- Dashboard counts for patients and appointments
- Register patients with basic validation
- View patient records
- Book and view appointments
- SQLite local persistence
- About/limitations screen

## Open in Android Studio
1. Extract `ClinicalManagementSystem.zip`.
2. Open Android Studio.
3. Select **Open** and choose the `ClinicalManagementSystem` folder (the folder containing `settings.gradle`).
4. Allow Gradle sync to finish. Use JDK 17 if Android Studio asks.
5. Create/start an Android emulator (API 23 or later), or connect an Android phone with USB debugging enabled.
6. Press **Run ▶**.

## Demo walkthrough
1. Tap **Open demo dashboard**.
2. Select **Register patient** and enter fictional details.
3. Return to dashboard and open **View patients**.
4. Book an appointment with a fictional patient name and date/time.
5. View appointments and observe the dashboard counters.

## Important limitations
This is an educational prototype. It does not provide production-grade login, access control, encryption, audit logs, backups, remote synchronization or regulatory compliance. Do not enter real patient or sensitive health information. Clinical notes table is included as a starter schema but is not yet exposed through the UI.

## Suggested extensions
- Add patient search and update/delete workflows.
- Link appointments to patient IDs rather than names.
- Add clinical notes and visit history.
- Implement authentication and role-based permissions.
- Add encrypted storage, audit logging, secure backups and privacy review before any real-world use.

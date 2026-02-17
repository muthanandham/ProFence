1. Project Overview

ProFence is a high-precision, battery-optimized Android application designed to trigger automated alerts based on geographical boundaries. It focuses on four key transitions: Enter, Exit, Dwell, and Geofence (Stay).
Core Objective

To provide a premium, reliable alerting system that works in the background with zero cost for the developer and a professional "SaaS-style" UI for the user.
2. Tech Stack (AI-Friendly & 100% Free)

This stack is chosen because these tools have extensive documentation, making them easy for AI agents to write and debug.
Layer	Technology	Why?
Language	Kotlin	Native Android standard, most stable for Geofencing APIs.
Framework	Jetpack Compose	Modern UI toolkit. AI writes Compose code much better than XML.
Geofencing	Google Play Services	Native, $0 cost, and battery-optimized.
Maps	osmdroid (OpenStreetMap)	100% Free. No API keys or billing required.
Local Database	Room (SQLite)	Stores geofences locally on the device.
Backend/Sync	Supabase (Free Tier)	Easy PostgreSQL setup for remote alerts.
Notifications	Android Notif. Manager	Free, local, and instant.
3. UI/UX Design Specifications

The UI should feel like a premium "Dark Mode First" professional tool.
Visual Language

    Colors (Dark Theme): Background: #121212, Primary: #00E676 (Neon Green), Surface: #1E1E1E.

    Colors (Light Theme): Background: #F5F5F7, Primary: #00C853, Surface: #FFFFFF.

    Typography: Sans-serif (Inter or Roboto), clean and bold for status headers.

    Finish: Subtle glassmorphism (transparency) on cards and rounded corners (16dp).

Main Screens

    Dashboard (Home): A list of "Active Fences" with toggle switches and status indicators (Inside/Outside).

    Map View: A full-screen interactive map to long-press and drop a geofence.

    Fence Config: A bottom sheet to set:

        Radius (Slider: 100m - 10km).

        Transition Type (Checkbox: Enter, Exit, Dwell).

        Dwell Time (Dropdown: 1 min, 5 min, 10 min).

    Logs: A history of every time a fence was triggered.

4. Functional Requirements
R1: Geofence Transitions

    ENTER: Trigger alert immediately upon crossing the boundary.

    EXIT: Trigger alert when leaving the boundary.

    DWELL: Trigger only if the user stays inside the radius for X minutes (prevents false alerts from just driving by).

R2: Permission Handling

    The app must implement an "Incremental Permission Flow":

        Request Foreground Location.

        Show a "Why we need background access" custom dialog.

        Redirect to System Settings for "Allow all the time."

R3: Battery Management

    The app must use the GeofencingClient, which offloads tracking to the hardware level, ensuring less than 1% battery drain per hour.

5. Setup & Implementation Guide (For AI/Dev)

To build this app, follow these steps in order:
Phase 1: Environment Setup

    Open Android Studio and create a Empty Compose Activity.

    Add dependencies in build.gradle.kts:

        implementation("com.google.android.gms:play-services-location:21.1.0")

        implementation("org.osmdroid:osmdroid-android:6.1.18")

        implementation("androidx.room:room-runtime:2.6.1")

Phase 2: Manifest & Permissions

Add these to AndroidManifest.xml:
XML

<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.INTERNET" />

Phase 3: The Geofence Broadcast Receiver

Create a class GeofenceBroadcastReceiver.kt to handle the background event. This is the "brain" of the app that sends the notification when a user enters or exits.
6. Post-Development Setup (How to run)

    Clone/Open: Open the project folder in Android Studio Jellyfish (or newer).

    Sync Gradle: Let the dependencies download.

    Emulator Setup: * Use an emulator with Google Play Store installed.

        Go to Extended Controls (...) > Location.

    Run: Click the "Run" button.

    Test the Flow:

        Open the app and grant "While using app" location permission.

        Click "Enable Background Tracking" and select "Allow all the time" in the Android settings page that opens.

        Create a geofence on the map around your current "virtual" location.

        In the Emulator Location settings, "Teleport" your GPS coordinates outside the radius.

        Result: You should receive a "You have exited the zone" notification.
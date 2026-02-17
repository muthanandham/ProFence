# ProFence Testing Instructions

## Running the App (No Android Studio)
1. Go to your GitHub repository: [ProFence Actions](https://github.com/muthanandham/ProFence/actions)
2. Click on the latest **Android Build** workflow run.
3. Scroll down to the **Artifacts** section.
4. Download the **debug-apk** zip file.
5. Extract the zip file to find `app-debug.apk`.
6. Transfer this file to your Android phone and install it.

## Feature Testing

### 1. Permissions Flow
- On first launch, you should see the "Location Access Needed" screen.
- Click "Grant Location Access". Select "While using the app".
- Next, you should see "Background Location Required".
- Click "Open Settings". In the settings page, select "Permissions" > "Location" > "**Allow all the time**".
- Return to the app. You should see the Dashboard.

### 2. Creating a Geofence
- Tap the **Map** tab (center icon).
- **Long press** anywhere on the map.
- A bottom sheet will appear.
- Give it a name (e.g., "Home").
- Set radius (e.g., 200m).
- Check "Enter" and "Exit".
- Click "Create Fence".
- You should see a green circle on the map.

### 3. Simulating Geofence Triggers
- In the Emulator toolbar, click **Extended Controls** (`...`).
- Go to the **Location** tab.
- **Enter Trigger**: Set the latitude/longitude to match your geofence center. Click "Set Location".
  - You should receive a notification: "You have entered Home".
  - Dashboard should show status: "INSIDE".
- **Exit Trigger**: Change latitude significantly (e.g., +0.01). Click "Set Location".
  - You should receive a notification: "You have exited Home".
  - Dashboard should show status: "OUTSIDE".

### 4. Boot Persistence
- Create a geofence.
- Restart the emulator/device.
- Reopen the app.
- Verify the geofence is still listed in Dashboard.

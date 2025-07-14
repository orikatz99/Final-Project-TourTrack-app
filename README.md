<p align="left">
  <img src="docs/logo.png" alt="TourTrack Logo" width="120"/>
</p>

# TourTrack

**Developed by:** Shani Halali & Ori Katz  
**Partnered with:** Lower Galilee Regional Council  
**FINAL PROJECT GRADE: 98**

**Demo video:**

[![Demo Video](https://img.youtube.com/vi/oH4EgSAv1es/sddefault.jpg)](https://www.youtube.com/watch?v=oH4EgSAv1es)

## 📱 About the App

**TourTrack** is a social network for hikers – bringing everything they need into one place: trails, recommendations, nearby travelers, and real-time updates.  
Developed in collaboration with the Lower Galilee Regional Council, the app enhances the hiking experience through personalized suggestions and community connection.

---

## 🚀 Features
- 🔐 Secure authentication with private sign-up and optional Google login for quick access
- 👤 Role-based access control for travelers, local council members, and administrators
- 🧭 Smart trail recommendations based on user preferences, location, and weather conditions  
- 👥 Live display of nearby travelers and social connectivity  
- 🗺️ Interactive Google Maps integration with real-time weather display  
- 📌 Interactive trail markers pins - that open directly in Google Maps for seamless navigation and route discovery
- 📩 Reporting system – report issues or recommend trails
- 📞 Direct communication buttons for phone calling or messaging users via WhatsApp
- 🔎 Real-time display of connected users currently active in the app
- 📱 Responsive and user-friendly design 

---
### 🛠️ Tech Stack

**Frontend**: Android (Java, XML)  
**Backend**: Node.js + Express, deployed on **Vercel**  
**Database**: MongoDB (cloud-based)  
**Cloud Storage**: Firebase Storage (for uploading user files and images)  
**Authentication**: Google Sign-In via **Firebase Authentication** and custom email/password stored in **MongoDB**  
**APIs**: Google Maps API, OpenWeatherMap API, WhatsApp API  
**Other Tools**: Retrofit, SharedPreferences  
**Version Control**: Git & GitHub




---

## 📁 Project Structure

```
Final-Project-TourTrack-app/
├── app/                  # Android app source code  
│   ├── src/              # Java code and resources  
│   └── res/              # Layouts, images, strings (XML)  
├── backend/              # Node.js + Express backend  
│   └── routes/, models/  # API routes and DB models  
├── docs/                 # Project documents (STP, STD, presentation...)  
└── README.md             # Project overview file
```

---

## 🚀 Getting Started

### 📱 Running the Android App

1. Clone or download this repository:
   ```bash
   git clone https://github.com/orikatz99/Final-Project-TourTrack-app.git
   ```
2. Open the `app/` folder in **Android Studio**
3. Make sure your emulator or device is connected
4. Sync Gradle and run the app

> **Note:**  
> The file `google_maps_api.xml` is required for map functionality and is ignored in Git.  
> Make sure to add your own **Google Maps API key** inside `app/src/main/res/values/google_maps_api.xml`:
> ```xml
> <string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">YOUR_KEY_HERE</string>
> ```

### 🌐 Backend Hosting

The backend server is deployed on [Vercel](https://vercel.com) and is automatically available via: "https://tourtrack.vercel.app/"
>  The app is already configured to use this backend via Retrofit,  
> so no changes to the API base URL are required.


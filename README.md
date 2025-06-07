
<p align="left">
  <img src="docs/logo.png" alt="TourTrack Logo" width="120"/>
</p>

# TourTrack

**Developed by:** Shani Halali & Ori Katz  
**Partnered with:** Lower Galilee Regional Council

---

## 📱 About the App

**TourTrack** is a social network for hikers – bringing everything they need into one place: trails, recommendations, nearby travelers, and real-time updates.  
Developed in collaboration with the Lower Galilee Regional Council, the app enhances the hiking experience through personalized suggestions and community connection.

---

## 🚀 Features

- 🧭 Smart trail recommendations based on user preferences, location, and weather conditions  
- 👥 Live display of nearby travelers and social connectivity  
- 🗺️ Interactive Google Maps integration with real-time weather display  
- 📌 Clickable trail pins 
- 📩 Reporting system – report issues or recommend trails  
- 🔐 Role-based access for travelers, council members, and admins  
- 🔐 Google Sign-In integration for quick and secure login  
- 📢 In-app ads using AdMob SDK  
- 📱 Responsive and user-friendly design 

---

## 🛠️ Tech Stack

**Frontend:** Android (Java, XML)  
**Backend:** Node.js + Express  
**Database:** MongoDB  
**APIs:** Google Maps API, OpenWeatherMap API, WhatsApp API  
**Other Tools:** Retrofit, SharedPreferences  
**Version Control:** Git & GitHub

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

---

### 🌐 Running the Backend Server

1. Navigate to the `backend/` folder
2. Create a `.env` file in the root of the backend and define the required variables:
   ```env
   PORT=your_port
   MONGO_URI=MONGO_URI=mongodb+srv://your-username:your-password@your-cluster.mongodb.net/musicapp?retryWrites=true&w=majority
   JWT_SECRET=your_jwt_secret
   OPENWEATHER_API_KEY=your_openweather_api_key
   ```

3. Install dependencies:
   ```bash
   npm install
   ```
4. Start the server:
   ```bash
   npm run dev
   ```

> The backend uses `config/db.js` to connect to MongoDB using the `MONGO_URI` from your `.env` file.

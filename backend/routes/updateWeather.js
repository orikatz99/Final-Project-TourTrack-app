const mongoose = require("mongoose");
const axios = require("axios");
require("dotenv").config();
const Route = require("../models/Route");

const WEATHER_API_KEY = process.env.OPENWEATHER_API_KEY;
const BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

// ✅ Connect to MongoDB
mongoose.connect(process.env.MONGO_URI || "mongodb://localhost:27017/tourtrack", {
        useNewUrlParser: true,
        useUnifiedTopology: true,
    }).then(() => console.log("✅ Connected to MongoDB"))
    .catch(err => console.error("❌ MongoDB connection error:", err));

// 🔁 Weather update function
async function updateWeatherForAllRoutes() {
    try {
        const routes = await Route.find();
        let updatedCount = 0;

        for (const route of routes) {
            const { latitude, longitude } = route;
            if (!latitude || !longitude) continue;

            const url = `${BASE_URL}?lat=${latitude}&lon=${longitude}&appid=${WEATHER_API_KEY}&units=metric`;
            const response = await axios.get(url);
            const weatherType = response.data.weather[0].main;

            route.currentWeather = {
                type: weatherType,
                updatedAt: new Date()
            };

            await route.save();
            updatedCount++;
        }

        console.log(`✅ Weather updated for ${updatedCount} routes at ${new Date().toLocaleString()}`);
    } catch (err) {
        console.error("❌ Error updating weather:", err);
    }
}

// 🔁 Run the update once immediately (on script start)
updateWeatherForAllRoutes();

// ⏰ Schedule updates every hour (3600000 ms)
setInterval(() => {
    console.log("🔄 Running hourly weather update...");
    updateWeatherForAllRoutes();
}, 1000 * 60 * 60);
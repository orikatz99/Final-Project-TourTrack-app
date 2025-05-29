const Route = require('../models/Route');

exports.getRecommendations = async(req, res) => {
    try {
        const {
            region, // For future use (e.g., filtering by location)
            category, // Selected trail category (string)
            difficulty, // Selected difficulty level
            attraction, // Selected attraction (string)
            preferences, // User preferences (array of tags)
            weather, // Current weather at user's location (not used here)
            age, // Average age of the group (for future use)
            groupSize, // Number of people in the group (for future use)
            tripDate // The selected trip date (used for weather relevance)
        } = req.body;

        // Parse tripDate and check if it's today
        let isToday = false;
        if (tripDate) {
            const selected = new Date(tripDate);
            const today = new Date();
            isToday = selected.toDateString() === today.toDateString();
        }

        // Step 1: Fetch all routes from the database
        const allRoutes = await Route.find();

        // Step 2: Score each route
        const scoredRoutes = allRoutes.map(route => {
            let score = 0;

            // ✅ Category match (array)
            if (category && Array.isArray(route.category) && route.category.includes(category)) {
                score += 8;
            }

            // ✅ Attraction match (string)
            if (attraction && route.attraction === attraction) {
                score += 5;
            }

            // ✅ User preferences match route.tags
            if (preferences && Array.isArray(preferences)) {
                preferences.forEach(pref => {
                    if (route.tags.includes(pref)) {
                        score += 3;
                    }
                });
            }

            // ✅ Difficulty match
            if (difficulty && route.difficulty === difficulty) {
                score += 7;
            }

            // ✅ Weather-based scoring (only if trip is today)
            if (isToday && route.currentWeather && route.currentWeather.type === "Clear") {
                score += 6;
            }

            const acceptableWeather = ["Clear", "Clouds", "Mist", "Haze", "Fog", "Dust", "Sand", "Ash"];

            // ❌ Penalize bad weather for Trips/Accommodation only if trip is today
            if (
                isToday &&
                (route.attraction === "Trips" || route.attraction === "Accommodation") &&
                route.currentWeather &&
                !acceptableWeather.includes(route.currentWeather.type)
            ) {
                score -= 5;
            }

            return { route, score };
        });

        // Step 3: Sort and return top 3
        const sortedRoutes = scoredRoutes
            .sort((a, b) => b.score - a.score)
            .map(r => r.route);

        res.json(sortedRoutes);

    } catch (err) {
        console.error("❌ Recommendation error:", err);
        res.status(500).json({ message: 'Server error' });
    }
};
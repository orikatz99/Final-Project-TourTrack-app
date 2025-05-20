const mongoose = require("mongoose");
const fs = require("fs");
require("dotenv").config();

// התחברות למסד
mongoose.connect(process.env.MONGO_URI || "mongodb://localhost:27017/tourtrack", {
    useNewUrlParser: true,
    useUnifiedTopology: true,
});

const routeSchema = new mongoose.Schema({
    name: String,
    difficulty: String,
    lengthKm: Number,
    tags: String,
    imageUrl: String,
    description: String,
    latitude: Number,
    longitude: Number,
});

const Route = mongoose.model("Route", routeSchema);

const data = JSON.parse(fs.readFileSync("./routes.json", "utf8"));

Route.insertMany(data)
    .then(() => {
        console.log("✅ Data imported successfully");
        mongoose.disconnect();
    })
    .catch((err) => {
        console.error("❌ Error importing data:", err);
        mongoose.disconnect();
    });
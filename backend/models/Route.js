const mongoose = require("mongoose");

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

module.exports = mongoose.model("Route", routeSchema);
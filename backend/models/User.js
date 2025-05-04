const mongoose = require('mongoose');

// Temporary User schema – for testing preferences
const userSchema = new mongoose.Schema({
    name: String,
    email: String,
    password: String,
    preferences: [String] // ← the key field for Step 3
});

module.exports = mongoose.model('User', userSchema);
const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
    firstName: { type: String, required: true },
    lastName:  { type: String, required: true },
    email:     { type: String, required: true, unique: true },
    password:  { type: String, required: true },
    phone:     { type: String, required: true, unique: true },
    birthDate: { type: String }, // שמור כתאריך בפורמט מחרוזת כמו "01/01/2000"
    preferences: { type: [String], default: [] }
});

module.exports = mongoose.model('User', userSchema);

const mongoose = require('mongoose');

const recommendationSchema = new mongoose.Schema({
    userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    location: {type: String, required: true}, // store as a location name
    description: { type: String, required: true },
    photo: { type: String }, // URL to the photo
    createdAt: { type: Date, default: Date.now },
    updatedAt: { type: Date, default: Date.now }
});




module.exports = mongoose.model('Recommendation', recommendationSchema);

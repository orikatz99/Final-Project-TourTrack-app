const mongoose = require('mongoose');

const userLocationSchema = new mongoose.Schema({
    userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, unique: true },
    location: {
        type: {
            type: String,
            enum: ['Point'],
            required: true
        },
        coordinates: {
            type: [Number], // [lng, lat]
            required: true
        }
    },
    updatedAt: { type: Date, default: Date.now }
});

// Index for geospatial queries
userLocationSchema.index({ location: '2dsphere' });

module.exports = mongoose.model('UserLocation', userLocationSchema);
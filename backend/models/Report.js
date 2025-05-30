const mongoose = require('mongoose');

const reportSchema = new mongoose.Schema({
    userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    routeId: { type: mongoose.Schema.Types.ObjectId, ref: 'Route', required: true },
    description: { type: String, required: true },
    status: {
        type: String,
        enum: ['open', 'in_progress', 'resolved'],
        default: 'open'
    },
    location: {type: String, required: true}, // store as a location name
    photo: { type: String }, // URL to the photo
    type: {
        type: String,
        enum: ['Hazard', 'Dirty Path', 'Fallen Tree','Blocked Trail', 'Other'],
        required: true
    },
    createdAt: { type: Date, default: Date.now },
    updatedAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Report', reportSchema);

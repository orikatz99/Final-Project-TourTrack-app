const mongoose = require('mongoose');

const reportSchema = new mongoose.Schema(
  {
    userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
    description: { type: String, required: true },
    status: {
      type: String,
      enum: ['open', 'in_progress', 'resolved'],
      default: 'open'
    },
    location: { type: String, required: true },
    photo: { type: String },
    type: {
      type: String,
      enum: ['Hazard', 'Dirty Path', 'Fallen Tree', 'Blocked Trail', 'Other'],
      required: true
    }
  },
  { timestamps: true } // Automatically manage createdAt and updatedAt fields
);


module.exports = mongoose.model('Report', reportSchema);

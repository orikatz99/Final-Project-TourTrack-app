const User = require('../models/User'); // Import the User model

// Update user's preferences
exports.updatePreferences = async(req, res) => {
    console.log('Updating user preferences...'); // Log the request for debugging

    const { id } = req.params; // Get user ID from the URL
    const { preferences } = req.body; // Get preferences array from the request body

    try {
        const user = await User.findByIdAndUpdate(
            id, { preferences }, { new: true } // Return the updated user
        );

        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        res.json({ message: 'Preferences updated successfully', user });
    } catch (error) {
        res.status(500).json({ message: 'Server error', error: error.message });
    }
};
const express = require('express');
const router = express.Router();
const User = require('../models/User');

// ✅ Update user info by email (used after Google sign-in)
router.put('/auth/google/complete-by-email/:email', async(req, res) => {
    const { email } = req.params;
    const { phone, birthDate } = req.body;

    if (!phone || !birthDate) {
        return res.status(400).json({ message: 'Phone and birth date are required.' });
    }

    try {
        const updatedUser = await User.findOneAndUpdate({ email }, // search by email
            { phone, birthDate }, { new: true }
        );

        if (!updatedUser) {
            return res.status(404).json({ message: 'User not found.' });
        }

        res.json({ message: 'User info updated successfully', user: updatedUser });
    } catch (err) {
        res.status(500).json({ message: 'Server error', error: err.message });
    }
});

module.exports = router;
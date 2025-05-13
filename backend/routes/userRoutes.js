const express = require('express');
const router = express.Router();
const {
    updatePreferences,
    signUpUser,
    loginUser,
    getUserProfile,
    getPrivacySettings,
    savePrivacySettings
} = require('../controllers/userController');
const authMiddleware = require('../middlewares/authMiddleware');

// Update preferences
router.put('/preferences', authMiddleware, updatePreferences);

// Register
router.post('/signup', signUpUser);

// Login
router.post('/login', loginUser);

// Get user profile
router.get('/profile', authMiddleware, getUserProfile);

// Get privacy settings
router.get('/privacy', authMiddleware, getPrivacySettings);

// Save or update privacy settings
router.put('/privacy', authMiddleware, savePrivacySettings);

module.exports = router;

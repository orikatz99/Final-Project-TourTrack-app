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

// ✅ עדכון מיקום לפי מזהה משתמש
router.put('/location/:id', (req, res) => {
    const { id } = req.params;
    const { lat, lng } = req.body;

    console.log('📍 Location received for:', id);
    console.log('Coordinates:', lat, lng);

    // כאן אפשר לעדכן במסד הנתונים לפי ה-id

    res.sendStatus(200);
});

module.exports = router;
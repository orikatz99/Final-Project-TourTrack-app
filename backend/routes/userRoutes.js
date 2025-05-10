const express = require('express');
const router = express.Router();
const { updatePreferences, signUpUser, loginUser } = require('../controllers/userController'); // הוספנו loginUser

// route to update user preferences
router.put('/preferences/:id', updatePreferences);

// route to register a new user (sign up)
router.post('/signup', signUpUser);

// ✅ route to login user
router.post('/login', loginUser);

module.exports = router;

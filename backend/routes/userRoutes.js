const express = require('express');
const router = express.Router();
const { updatePreferences, signUpUser, loginUser } = require('../controllers/userController');
const authMiddleware = require('../middlewares/authMiddleware');

router.put('/preferences', authMiddleware, updatePreferences);

router.post('/signup', signUpUser);

router.post('/login', loginUser);

module.exports = router;
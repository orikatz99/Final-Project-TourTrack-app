const express = require('express');
const router = express.Router();
const { updatePreferences } = require('../controllers/userController'); // Import the controller

//route to update user preferences
router.put('/preferences/:id', updatePreferences);

module.exports = router;
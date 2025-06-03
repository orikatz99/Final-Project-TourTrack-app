const express = require('express');
const router = express.Router();
const { getAllRecommendations } = require('../controllers/userController');

// Route to get all public recommendations (no token required)
router.get('/recommendations', getAllRecommendations);

module.exports = router;

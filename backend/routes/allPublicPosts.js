const express = require('express');
const router = express.Router();
const { getAllRecommendations } = require('../controllers/userController');
const { getAllReports } = require('../controllers/userController');

// Route to get all public recommendations (no token required)
router.get('/recommendations', getAllRecommendations);
router.get('/reports',getAllReports);

module.exports = router;

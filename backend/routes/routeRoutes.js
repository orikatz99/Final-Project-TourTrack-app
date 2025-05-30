const express = require('express');
const router = express.Router();
const Route = require('../models/Route');
const { getRecommendations } = require('./recommendations');

// GET all routes
router.get('/', async(req, res) => {
    try {
        const routes = await Route.find({});
        res.json(routes);
    } catch (err) {
        res.status(500).json({ error: 'Failed to fetch routes' });
    }
});

// POST recommendations
router.post('/recommendations', getRecommendations);

module.exports = router;
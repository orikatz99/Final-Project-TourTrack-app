const User = require('../models/User');
const UserPrivacy = require('../models/userPrivacyModel');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

// Register a new user
exports.signUpUser = async (req, res) => {
    const { firstName, lastName, email, password, phone, birthDate } = req.body;

    if (!firstName || !lastName || !email || !password || !phone) {
        return res.status(400).json({ message: 'Missing required fields' });
    }

    try {
        const existingEmail = await User.findOne({ email });
        if (existingEmail) {
            return res.status(409).json({ message: 'Email already exists' });
        }

        const existingPhone = await User.findOne({ phone });
        if (existingPhone) {
            return res.status(409).json({ message: 'Phone already exists' });
        }

        const hashedPassword = await bcrypt.hash(password, 10);

        const newUser = new User({
            firstName,
            lastName,
            email,
            password: hashedPassword,
            phone,
            birthDate,
            preferences: []
        });

        await newUser.save();

        // Create default privacy + notification settings for this user
        await UserPrivacy.create({
            userId: newUser._id,
            privacySettings: {},
            notificationsSettings: {}
        });

        const token = jwt.sign({ id: newUser._id }, process.env.JWT_SECRET, {
            expiresIn: '7d'
        });

        return res.status(201).json({
            message: 'User registered successfully',
            token,
            user: {
                id: newUser._id,
                email: newUser.email,
                firstName: newUser.firstName,
                preferences: newUser.preferences
            }
        });
    } catch (error) {
        console.error('Error registering user:', error);
        return res.status(500).json({ message: 'Server error' });
    }
};

// Update user's preferences
exports.updatePreferences = async (req, res) => {
    try {
        const user = await User.findById(req.user._id);
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        user.preferences = req.body.preferences;
        await user.save();

        res.json({ message: 'Preferences updated successfully', user });
    } catch (error) {
        res.status(500).json({ message: 'Server error', error: error.message });
    }
};

// Login user
exports.loginUser = async (req, res) => {
    const { email, password } = req.body;

    try {
        const user = await User.findOne({ email });
        if (!user) {
            return res.status(401).json({ message: 'Email not found' });
        }

        const isMatch = await bcrypt.compare(password, user.password);
        if (!isMatch) {
            return res.status(401).json({ message: 'Incorrect password' });
        }

        const token = jwt.sign({ id: user._id }, process.env.JWT_SECRET, {
            expiresIn: '7d'
        });

        res.status(200).json({
            message: 'Login successful',
            token,
            user: {
                id: user._id,
                email: user.email,
                firstName: user.firstName,
                preferences: user.preferences
            }
        });
    } catch (error) {
        res.status(500).json({ message: 'Server error', error: error.message });
    }
};

// Get user profile
exports.getUserProfile = async (req, res) => {
    try {
        const authHeader = req.headers.authorization;
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
            return res.status(401).json({ message: 'No token provided' });
        }

        const token = authHeader.split(' ')[1];
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        const user = await User.findById(decoded.id);

        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        res.json({
            email: user.email,
            phone: user.phone,
            name: user.firstName + ' ' + user.lastName,
        });
    } catch (error) {
        res.status(401).json({ message: 'Invalid token' });
    }
};

// Save or update user privacy & notification settings
exports.savePrivacySettings = async (req, res) => {
    try {
        const userId = req.user._id;

        const existingSettings = await UserPrivacy.findOne({ userId });

        if (existingSettings) {
            existingSettings.privacySettings = req.body.privacySettings || existingSettings.privacySettings;
            existingSettings.notificationsSettings = req.body.notificationsSettings || existingSettings.notificationsSettings;
            await existingSettings.save();
            return res.json({ message: 'Settings updated successfully', data: existingSettings });
        } else {
            const newSettings = new UserPrivacy({
                userId,
                privacySettings: req.body.privacySettings || {},
                notificationsSettings: req.body.notificationsSettings || {}
            });
            await newSettings.save();
            return res.status(201).json({ message: 'Settings saved successfully', data: newSettings });
        }
    } catch (err) {
        res.status(500).json({ message: 'Server error', error: err.message });
    }
};

// Get user privacy & notification settings
exports.getPrivacySettings = async (req, res) => {
    try {
        const userId = req.user._id;

        const settings = await UserPrivacy.findOne({ userId });

        if (!settings) {
            return res.status(404).json({ message: 'Settings not found' });
        }

        res.json({ data: settings });
    } catch (err) {
        res.status(500).json({ message: 'Server error', error: err.message });
    }
};

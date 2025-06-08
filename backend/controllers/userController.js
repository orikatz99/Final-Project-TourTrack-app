const User = require('../models/User');
const UserPrivacy = require('../models/userPrivacyModel');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const Report = require('../models/Report');
const Recommendation = require('../models/Recommendation');

// Register a new user
exports.signUpUser = async(req, res) => {
    const { firstName, lastName, email, password, phone, birthDate } = req.body;

    if (!firstName || !lastName || !email || !password || !phone) {
        return res.status(400).json({ message: 'Missing required fields' });
    }

    try {
        const existingEmail = await User.findOne({ email });
        const existingPhone = await User.findOne({ phone });

        if (existingEmail && existingPhone) {
            return res.status(409).json({ success: false, message: 'This email and phone number are already in use.' });
        }

        if (existingEmail) {
            return res.status(409).json({ success: false, message: 'This email is already registered.' });
        }

        if (existingPhone) {
            return res.status(409).json({ success: false, message: 'This phone number is already registered.' });
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
exports.updatePreferences = async(req, res) => {
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
exports.loginUser = async(req, res) => {
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
exports.getUserProfile = async(req, res) => {
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
            firstName: user.firstName,
            lastName: user.lastName,
            email: user.email,
            phone: user.phone,
            preferences: user.preferences
        });
    } catch (error) {
        res.status(401).json({ message: 'Invalid token' });
    }
};


// Save or update user privacy & notification settings
exports.savePrivacySettings = async(req, res) => {
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
exports.getPrivacySettings = async(req, res) => {
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

//Get all users

exports.getAllUsers = async (req, res) => {
    try {
        const currentUserId = req.user._id;

        // 1. Get all users except current
        const users = await User.find({ _id: { $ne: currentUserId } })
            .select('firstName lastName phone');

        // 2. For each user, get privacy info
        const result = await Promise.all(users.map(async (user) => {
            const privacy = await UserPrivacy.findOne({ userId: user._id }).select('privacySettings.AllowPhoneCalls privacySettings.EnableWhatsapp');

            return {
                _id: user._id,
                firstName: user.firstName,
                lastName: user.lastName,
                phone: user.phone,
                allowPhoneCalls: privacy?.privacySettings?.AllowPhoneCalls ?? true,
                enableWhatsapp: privacy?.privacySettings?.EnableWhatsapp ?? true,
            };
        }));

        res.status(200).json(result);
    } catch (err) {
        console.error('❌ Error fetching users:', err);
        res.status(500).json({ message: 'Server error' });
    }
};

// Get user reports

exports.getUserReports = async(req, res) => {
    try {
        const userId = req.user._id;
        console.log('Fetching reports for userId:', userId);

        const reports = await Report.find({ userId })
            .select('description status location photo type createdAt updatedAt');

        if (!reports) {
            return res.status(404).json({ message: 'No reports found for this user' });
        }

        res.status(200).json(reports);
    } catch (err) {
        console.error('❌ Error fetching user reports:', err.message);
        console.error(err.stack);
        res.status(500).json({ message: 'Server error' });
    }
};


// post user report
exports.postUserReport = async(req, res) => {
    const {  description, status, location, photo, type } = req.body;

    if ( !description || !location || !type) {
        return res.status(400).json({ message: 'Missing required fields' });
    }

    try {
        const userId = req.user._id;

        const newReport = new Report({
            userId,
            description,
            status: status || 'open',
            location,
            photo,
            type
        });

        await newReport.save();

        res.status(201).json({ message: 'Report created successfully', report: newReport });
    } catch (error) {
        console.error('❌ Error fetching user reports:', err.message);
        console.error(err.stack);
        res.status(500).json({ message: 'Server error' });
    }
};

// Get all reports (admin or global view)
exports.getAllReports = async (req, res) => {
    try {
        const reports = await Report.find()
            .select('description status location photo type createdAt updatedAt userId');

        res.status(200).json(reports);
    } catch (err) {
        console.error('❌ Error fetching all reports:', err);
        res.status(500).json({ message: 'Server error' });
    }
};

// GET /api/recommendations
exports.getAllRecommendations = async (req, res) => {
    try {
        const recommendations = await Recommendation.find()
            .select('location description photo updatedAt userId')     
            .populate({ path: 'userId', select: 'firstName lastName' });   

        const formatted = recommendations.map(rec => ({
            recommend_id: rec._id,
            location: rec.location,
            description: rec.description,
            updatedAt: rec.updatedAt, 
            photo: rec.photo,
            user: {
                firstName: rec.userId?.firstName || '',
                lastName: rec.userId?.lastName || ''
            }
        }));

        res.status(200).json(formatted);
    } catch (err) {
        console.error('❌ Error fetching recommendations:', err);
        res.status(500).json({ message: 'Server error' });
    }
};







// Get user recommendations
exports.getUserRecommendations = async(req, res) => {
    try {
        const userId = req.user._id;

        const recommendation = await Recommendation.find({ userId })
            .select('location description photo createdAt updatedAt');

            /*
        if (!recommendation) {
            return res.status(404).json({ message: 'No recommendations found for this user' });
        }*/

        res.status(200).json(recommendation);
    } catch (err) {
        console.error('❌ Error fetching user recommendations:', err);
        res.status(500).json({ message: 'Server error' });
    }
};



// Post user recommendation
exports.postUserRecommendation = async(req, res) => {
    const { location, description, photo } = req.body;

    if (!location || !description) {
        return res.status(400).json({ message: 'Missing required fields' });
    }

    try {
        const userId = req.user._id;

        const newRecommendation = new Recommendation({
            userId,
            location,
            description,
            photo
        });

        await newRecommendation.save();

        res.status(201).json({ message: 'Recommendation created successfully', recommendation: newRecommendation });
    } catch (error) {
        console.error('❌ Error creating recommendation:', error);
        res.status(500).json({ message: 'Server error' });
    }
};

// DELETE /api/users/report/:id
exports.deleteUserReport = async (req, res) => {
    try {
        const reportId = req.params.id;

        const deletedReport = await Report.findByIdAndDelete(reportId);

        if (!deletedReport) {
            return res.status(404).json({ message: 'Report not found' });
        }

        res.status(200).json({ message: 'Report deleted successfully' });
    } catch (err) {
        console.error('❌ Error deleting report:', err.message);
        res.status(500).json({ message: 'Server error' });
    }
};

// PUT /api/users/report/:id
exports.updateReport = async (req, res) => {
  try {
    const reportId = req.params.id;
    const { description, location, type, photo } = req.body;

    const updatedReport = await Report.findByIdAndUpdate(
      reportId,
      { description, location, type, photo },
      { new: true }
    );

    if (!updatedReport) {
      return res.status(404).json({ message: 'Report not found' });
    }

    res.status(200).json({
      message: 'Report updated successfully',
      report: updatedReport 
    });
  } catch (err) {
    console.error('❌ Error updating report:', err.message);
    res.status(500).json({ message: 'Server error' });
  }
};

// PUT /api/users/recommendation/:id
exports.updateUserRecommendation = async (req, res) => {
    try {
        const { id } = req.params;
        const { description, location, photo } = req.body;

        const updatedRecommendation = await Recommendation.findByIdAndUpdate(
            id,
            { description, location, photo, updatedAt: new Date() },
            { new: true }
        );

        if (!updatedRecommendation) {
            return res.status(404).json({ message: 'Recommendation not found' });
        }

        res.status(200).json({
            message: 'Recommendation updated successfully',
            recommend: updatedRecommendation
        });
    } catch (err) {
        console.error('❌ Error updating recommendation:', err.message);
        res.status(500).json({ message: 'Server error' });
    }
};
// DELETE /api/users/recommendation/:id
exports.deleteUserRecommendation = async (req, res) => {
    try {
        const { id } = req.params;

        const deleted = await Recommendation.findByIdAndDelete(id);

        if (!deleted) {
            return res.status(404).json({ message: 'Recommendation not found' });
        }

        res.status(200).json({ message: 'Recommendation deleted successfully' });
    } catch (err) {
        console.error('❌ Error deleting recommendation:', err.message);
        res.status(500).json({ message: 'Server error' });
    }
};

// ✅ Check if user exists by email (for Google sign-in flow)
exports.checkUserExistsByEmail = async (req, res) => {
  const { email } = req.params;

  if (!email) {
    return res.status(400).json({ message: 'Email is required' });
  }

  try {
    const user = await User.findOne({ email });
    res.status(200).json(!!user); // true או false
  } catch (err) {
    console.error('Error checking user existence:', err.message);
    res.status(500).json({ message: 'Server error' });
  }
};

// Complete Google sign-up by email
exports.completeGoogleSignupByEmail = async (req, res) => {
    const { firstName, lastName, phone, birthDate } = req.body;
    const { email } = req.params;

    if (!email || !firstName || !lastName || !phone || !birthDate) {
        return res.status(400).json({ message: 'Missing required fields' });
    }

    try {
        const existingUser = await User.findOne({ email });
        if (existingUser) {
            return res.status(409).json({ message: 'User already exists' });
        }

        // Hash a default password for the new Google user
        const bcrypt = require('bcryptjs');
        const defaultPassword = 'defaultGooglePassword123';
        const hashedPassword = await bcrypt.hash(defaultPassword, 10);

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

        // Create default privacy & notification settings for this user
        await UserPrivacy.create({
            userId: newUser._id,
            privacySettings: {},
            notificationsSettings: {}
        });

        // Generate JWT token
        const jwt = require('jsonwebtoken');
        const token = jwt.sign({ id: newUser._id }, process.env.JWT_SECRET, {
            expiresIn: '7d'
        });

        // Return token and user info
        res.status(201).json({
            message: 'Google sign-up completed successfully',
            token,
            user: {
                id: newUser._id,
                email: newUser.email,
                firstName: newUser.firstName,
                preferences: newUser.preferences
            }
        });
    } catch (err) {
        console.error('❌ Error in completeGoogleSignupByEmail:', err.message);
        res.status(500).json({ message: 'Server error' });
    }
};

const fs = require('fs');
const path = require('path');
const shapefile = require('shapefile');
const mongoose = require('mongoose');

// טען משתני סביבה (כדי להשתמש בקובץ .env)
require('dotenv').config();

// התחברות למונגו
mongoose.connect(process.env.MONGO_URI, {
        useNewUrlParser: true,
        useUnifiedTopology: true
    })
    .then(() => console.log("✅ Connected to MongoDB"))
    .catch(err => console.error("❌ MongoDB connection error:", err));

// הגדרת סכמת Trail
const trailSchema = new mongoose.Schema({
    name: { type: String, default: "Unnamed Trail" },
    geometry: { type: Object, required: true }
});

const Trail = mongoose.model('Trail', trailSchema);

// קריאת כל קבצי .shp בתיקייה
const shapefilesDir = path.join(__dirname, 'shapefiles');
fs.readdir(shapefilesDir, async(err, files) => {
    if (err) throw err;

    const shpFiles = files.filter(file => file.endsWith('.shp'));

    for (const shpFile of shpFiles) {
        const baseName = shpFile.replace('.shp', '');
        const shpPath = path.join(shapefilesDir, `${baseName}.shp`);
        const dbfPath = path.join(shapefilesDir, `${baseName}.dbf`);

        try {
            const source = await shapefile.open(shpPath, dbfPath);
            while (true) {
                const result = await source.read();
                if (result.done) break;

                const { geometry, properties } = result.value;
                const name = properties.NAME || "Unnamed Trail";

                const trail = new Trail({ name, geometry });
                await trail.save();
                console.log(`✅ Saved: ${name}`);
            }
        } catch (error) {
            console.error(`❌ Error reading ${shpFile}:`, error.message);
        }
    }

    console.log("✅ Import completed!");
    mongoose.disconnect();
});
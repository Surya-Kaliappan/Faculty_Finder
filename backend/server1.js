require("dotenv").config();
const express = require("express");
const mysql = require("mysql2");
const cors = require("cors");
const os = require('os');

const app = express();
const PORT = process.env.PORT || 5000;

// Middleware
app.use(cors());
app.use(express.json());

const db = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "",
    database: "college"
});

db.connect((err) => {
    if (err) {
        console.error("Database connection failed:", err);
        return;
    }
    console.log("Connected to MySQL Database");
});

app.get("/search", (req, res) => {
    const { name, department, specialization } = req.query;
    let sql = `
        SELECT e.emp_id, e.emp_name, d.dept_name, e.emp_spec, e.emp_ph, e.emp_email, e.emp_cabin
        FROM employee e
        JOIN department d ON e.emp_dept = d.dept_id
        WHERE 1=1
    `;
    let params = [];

    if (name) {
        sql += " AND e.emp_name LIKE ?";
        params.push(`%${name}%`);
    }
    if (department) {
        sql += " AND e.emp_dept = ?";
        params.push(department);
    }
    if (specialization) {
        sql += " AND e.emp_spec LIKE ?";
        params.push(`%${specialization}%`);
    }

    db.query(sql, params, (err, result) => {
        if (err) {
            console.error("Search Query Failed:", err);
            return res.status(500).json({ error: "Database query failed", details: err.message });
        }
        // res.json(result);
        res.json(params.length>0 ? result : []);
    });
});


function checkTimeInInterval(currentTime, currentDay) {
    // Convert current time to minutes
    const [curHours, curMinutes] = currentTime.split(":").map(Number);
    const curTotalMinutes = curHours * 60 + curMinutes;
    if (curTotalMinutes < 540 || curTotalMinutes > 1010 || currentDay == 'Sunday'){
        return "closed";
    }

    for (let [index, interval] of intervals.entries()) {
        let [start, end] = interval.split("-");

        if (curTotalMinutes >= start && curTotalMinutes <= end) {
            return time[index];
        }
    }
    return "Free"; // If not in any interval, return "Free"
}

// must data
const time = ["9.00-9.50", "10.00-10.50", "11.00-11.50", "12.00-12.50", "1.00-1.50", "2.00-2.50", "3.00-3.50", "4.00-4.50"];
const intervals = ['540-590', '600-650', '660-710', '720-770', '780-830',  '840-890', '900-950', '960-1010'];

// Get Faculty Schedule & Check Availability
app.get("/schedule", (req, res) => {
    const { emp_id } = req.query;
    if (!emp_id) return res.status(400).json({ error: "emp_id is required" });

    const currentDay = new Date().toLocaleString('en-US', { weekday: 'long' });
    const currentTime = new Date().toLocaleTimeString('en-US', { hour12: false });
    const scheduled = checkTimeInInterval(currentTime,currentDay);

    // const currentDay = "Tuesday";
    // const scheduled = "12.00-12.50";

    // if(scheduled == 'Free'){
    //     return res.json({message: "Not in Class"});
    // } else
     if(scheduled == 'closed') {
        return res.json({message: "Not in College"});
    }

    let sql = `
        SELECT * FROM timetable 
        WHERE emp_id = ? 
        AND day = ? 
        AND time = ?
        ORDER BY time LIMIT 1
    `;

    // AND (time <= ? AND ADDTIME(time, duration) >= ?)

    db.query(sql, [emp_id, currentDay, scheduled], (err, result) => {
        if (err) {
            console.error("Schedule Query Failed:", err);
            return res.status(500).json({ error: "Database query failed", details: err.message });
        }
        res.json(result.length > 0 ? {message: "In Class"} : { message: "No any Classes" });  //result[0]
    });
});

// Fetch departments
app.get("/departments", (req, res) => {
    db.query("SELECT DISTINCT dept_name, dept_id FROM department", (err, result) => {
        if (err) {
            console.error("Error fetching departments:", err);
            return res.status(500).json({ error: "Database query error" });
        }
        res.json(result);
    });
});

// Fetch specializations
app.get("/specializations", (req, res) => {
    db.query("SELECT DISTINCT emp_spec AS name FROM employee", (err, result) => {
        if (err) {
            console.error("Error fetching specializations:", err);
            return res.status(500).json({ error: "Database query error" });
        }
        res.json(result);
    });
});

app.listen(PORT, () => {
    let network = "localhost";
    const networkInterfaces = os.networkInterfaces();
    for (const interfaceName in networkInterfaces) {
        for (const interface of networkInterfaces[interfaceName]) {
            if (interface.family === 'IPv4' && !interface.internal) {
                network = interface.address;
            }
        }
    }
    console.log(`Server is running on http://${network}:${PORT}`);
});

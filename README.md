🚀 CiviTrack – AI Powered Civic Issue Reporting App

CiviTrack is a full-stack Android application built using Kotlin, Firebase, and Node.js, integrated with AI-powered severity classification to intelligently prioritize civic issues.

The system not only allows citizens to report problems, but also automatically analyzes each issue using AI and estimates how urgently it should be resolved.

🧠 AI-Based Severity Classification

When a user submits an issue:

The Android app sends the issue description to the deployed backend (hosted on Railway).

The backend (CivicAIBackend) processes the description using Gemini AI.

The AI strictly classifies the issue into predefined categories.

The backend returns:

🔴 Severity Level

⏱ Estimated Resolution Time (in hours)

The result is stored in Firebase Firestore and displayed in the app UI.

📊 Severity Logic Used

The AI follows strict classification rules:

Severity	Examples	Estimated Resolution Time
Critical	Gas leak, fire, explosion, building collapse	1 hour
High	Electrical hazard, major flooding	6 hours
Medium	Potholes, broken streetlight	24 hours
Low	Garbage, noise, minor issues	48 hours

The backend ensures the response is returned in strict JSON format:

{
  "severity": "Low | Medium | High | Critical",
  "hours": number
}

This ensures consistency, reliability, and easy integration with the Android frontend.

📱 Features

🔐 Firebase Authentication

📝 Issue reporting with description & category

🤖 AI-based automatic severity detection

⏳ Estimated resolution time display

📂 Real-time storage with Firebase Firestore

👤 Role-based dashboard (User / Resolver)

🌐 Deployed backend on Railway

🛠 Tech Stack

Frontend (Android)

Kotlin

XML + ViewBinding

RecyclerView

Material Design

Backend

Node.js + Express

Gemini AI API

Railway Deployment

Database & Auth

Firebase Authentication

Firebase Firestore

⚙️ Setup Instructions

Clone the repository

Add your own google-services.json inside app/

Replace backend base URL with your deployed backend

Run the app in Android Studio

🔐 Security Notes

.env file is excluded from backend repository.

Firebase configuration file is not included for security reasons.

API keys are stored securely in environment variables.

💡 Why CiviTrack?

This project demonstrates:

Full-stack mobile application development

AI integration in real-world use case

REST API communication

Cloud deployment

Role-based architecture

Clean modular design

👨‍💻 Developed By

Vedant Vashistha
B.Tech CSE | IIIT Kota

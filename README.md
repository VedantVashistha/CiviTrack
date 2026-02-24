# 🚀 CiviTrack – AI Powered Civic Issue Reporting App

CiviTrack is a full-stack Android application built using **Kotlin, Firebase, and Node.js**.  
It enables citizens to report civic issues while intelligently prioritizing them using AI-based severity detection and estimated resolution time prediction.

The system bridges the communication gap between citizens and authorities through automated risk classification and structured resolution timelines.

---

## 🧠 AI-Based Severity Classification

When a user submits an issue:

1. The Android app sends the issue description to the deployed backend (hosted on Railway).
2. The backend (**CivicAIBackend**) processes the description using **Gemini AI**.
3. The AI strictly classifies the issue into predefined severity categories.
4. The backend returns:
   - Severity Level
   - Estimated Resolution Time (in hours)
5. The result is stored in Firebase Firestore and displayed in the app UI.

This ensures intelligent prioritization instead of manual classification.

---

## ⚡ Severity Logic Used by AI

### 🔴 Critical
- Examples: Gas leak, fire, explosion, building collapse  
- Estimated Resolution Time: **1 hour**

### 🟠 High
- Examples: Electrical hazard, major flooding  
- Estimated Resolution Time: **6 hours**

### 🟡 Medium
- Examples: Potholes, broken streetlight  
- Estimated Resolution Time: **24 hours**

### 🟢 Low
- Examples: Garbage, noise, minor inconvenience  
- Estimated Resolution Time: **48 hours**

---

## 📦 Example Backend Response

```json
{
  "severity": "Critical",
  "hours": 1
}
```

The backend enforces strict JSON formatting to ensure consistency and seamless Android integration.

---

## 📱 Core Features

- 🔐 Firebase Authentication (Login / Signup)
- 📝 Issue reporting with description
- 🤖 AI-powered automatic severity detection
- ⏳ Predicted resolution time display
- 📂 Real-time cloud storage using Firestore
- 👤 Role-based dashboards (User / Resolver)
- 🌐 Railway-hosted backend integration
- 🔄 REST API communication

---

## 🏗 System Architecture

```
Android App
    ↓
Railway Hosted Backend (Node.js + Express)
    ↓
Gemini AI Classification
    ↓
Severity + Resolution Time
    ↓
Firebase Firestore Storage
    ↓
Displayed in Android UI
```

---

## 🛠 Technology Stack

### 📱 Android (Frontend)
- Kotlin
- XML Layouts
- ViewBinding
- RecyclerView
- Material Design Components

### 🌐 Backend
- Node.js
- Express.js
- Gemini AI API
- Railway Deployment

### ☁ Database & Authentication
- Firebase Authentication
- Firebase Firestore

---

## ⚙️ Setup Instructions

1. Clone the repository:
   ```
   git clone https://github.com/YOUR_USERNAME/CiviTrack.git
   ```

2. Open the project in Android Studio.

3. Add your own `google-services.json` file inside:
   ```
   app/
   ```

4. Replace the backend base URL with your deployed Railway URL.

5. Sync Gradle and run the app.

---

## 🔐 Security Practices

- `.env` file is excluded from backend repository
- Firebase configuration file is not included
- API keys are stored securely using environment variables
- No sensitive credentials are hardcoded

---

## 💡 Project Highlights

This project demonstrates:

- Full-stack mobile application development
- Real-world AI integration in civic tech
- REST API architecture
- Cloud deployment workflow
- Role-based system design
- Clean modular Android architecture

---

## 👨‍💻 Developed By

**Vedant Vashistha**  
B.Tech CSE | IIIT Kota  

---

⭐ If you found this project interesting, consider starring the repository.

# 🚀 NagarSetu – Civic Issue Reporting App

NagarSetu is a modern Android application built using **Kotlin and Firebase** that enables citizens to report civic issues in their locality and track their resolution status in real time.

Designed with a clean UI and structured architecture, the app ensures seamless communication between users and authorities.

---

## 📱 Features

- 🔐 User Authentication (Firebase Auth)
- 📝 Report civic issues with title, description & category
- 📂 Real-time issue storage using Firebase Firestore
- 🔄 Live status tracking (Pending / Resolved)
- 👤 Role-based access (User / Resolver)
- 📊 RecyclerView-based issue listing
- ⚡ Smooth and responsive Material UI

---

## 🛠 Tech Stack

- **Language:** Kotlin  
- **Architecture:** XML + ViewBinding  
- **Backend:** Firebase Authentication & Firestore  
- **UI Components:** RecyclerView, Material Design  
- **IDE:** Android Studio  

---

## 📦 Project Structure
com.gcv.civicissue
│
├── ui.auth → Login & Signup
├── ui.main → Issue Listing
├── ui.resolver → Resolver Dashboard
├── model → Data classes
└── utils → Helper classes

---

## ⚙️ Setup Instructions (For Developers)

1. Clone the repository: git clone https://github.com/VedantVashistha/CiviTrack.git
   
2. Open in Android Studio.

3. Add your own `google-services.json` file inside:
   
You can download it from Firebase Console after creating your own project.

4. Sync Gradle and run the app.

---

## 🔐 Important

The `google-services.json` file is not included for security reasons.  
Please configure your own Firebase project before running.

---

## 💡 Why CiviTrack?

This project demonstrates:

- Full-stack Android development
- Firebase integration
- Clean architecture implementation
- Role-based logic handling
- Real-time cloud database operations

---

## 📷 Future Improvements

- Image upload using Firebase Storage
- Push notifications
- Location-based issue tagging
- Admin analytics dashboard

---

## 👨‍💻 Developed By

Vedant Vashistha  
B.Tech CSE | IIIT Kota  

---

⭐ If you like the project, feel free to star the repository!







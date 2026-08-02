package com.example.data.model

enum class AppLanguage {
    HINDI, ENGLISH
}

object LanguageStrings {
    fun getString(key: String, isHindi: Boolean): String {
        return if (isHindi) hindiStrings[key] ?: key else englishStrings[key] ?: key
    }

    private val hindiStrings = mapOf(
        "app_title" to "मिस्त्री कनेक्ट",
        "hero_heading" to "विश्वसनीय मिस्त्री और काम देने वालों का एक प्लेटफॉर्म",
        "become_mistry" to "मिस्त्री बनें",
        "post_job" to "काम पोस्ट करें",
        "login" to "लॉगिन",
        "register" to "रजिस्टर करें",
        "mistry_role" to "मैं मिस्त्री हूँ",
        "employer_role" to "मैं काम देने वाला हूँ",
        "full_name" to "पूरा नाम",
        "mobile_no" to "मोबाइल नंबर (OTP)",
        "email" to "ईमेल (ऐच्छिक)",
        "password" to "पासवर्ड",
        "aadhaar" to "आधार नंबर (ऐच्छिक)",
        "experience" to "अनुभव (वर्षों में)",
        "daily_wage" to "एक दिन की मजदूरी (₹)",
        "hourly_rate" to "प्रति घंटा रेट (₹)",
        "monthly_salary" to "मासिक सैलरी (₹)",
        "select_skills" to "कौशल (कम से कम एक चुनें)",
        "city" to "शहर",
        "district" to "जिला",
        "state" to "राज्य",
        "work_radius" to "कितनी दूरी तक काम करेंगे (km)",
        "online_status" to "अभी उपलब्ध हैं (Online)",
        "offline_status" to "अभी व्यस्त हैं (Offline)",
        "bio" to "बायोडाटा / विवरण",
        "bank_upi" to "बैंक / UPI आईडी (ऐच्छिक)",
        "company_name" to "कंपनी का नाम",
        "search" to "खोजें",
        "verified" to "वेरीफाइड",
        "unverified" to "अन-वेरीफाइड",
        "call" to "कॉल करें",
        "whatsapp" to "व्हाट्सएप",
        "hire" to "काम दें",
        "admin_panel" to "एडमिन पैनल",
        "dashboard" to "डैशबोर्ड",
        "home" to "होम",
        "my_jobs" to "मेरे काम",
        "applications" to "आवेदन",
        "rating_reviews" to "रेटिंग और समीक्षाएं",
        "dark_mode" to "डार्क मोड",
        "language" to "भाषा (Language)"
    )

    private val englishStrings = mapOf(
        "app_title" to "Mistri Connect",
        "hero_heading" to "A Trusted Platform for Skilled Workers & Employers",
        "become_mistry" to "Become Mistry",
        "post_job" to "Post Job",
        "login" to "Login",
        "register" to "Register",
        "mistry_role" to "I am a Mistry (Worker)",
        "employer_role" to "I am an Employer",
        "full_name" to "Full Name",
        "mobile_no" to "Mobile Number (OTP)",
        "email" to "Email (Optional)",
        "password" to "Password",
        "aadhaar" to "Aadhaar Number (Optional)",
        "experience" to "Experience (Years)",
        "daily_wage" to "Daily Wage (₹)",
        "hourly_rate" to "Hourly Rate (₹)",
        "monthly_salary" to "Monthly Salary (₹)",
        "select_skills" to "Skills (Select at least one)",
        "city" to "City",
        "district" to "District",
        "state" to "State",
        "work_radius" to "Work Distance Radius (km)",
        "online_status" to "Currently Available (Online)",
        "offline_status" to "Currently Busy (Offline)",
        "bio" to "Bio / Description",
        "bank_upi" to "Bank / UPI ID (Optional)",
        "company_name" to "Company Name",
        "search" to "Search",
        "verified" to "Verified",
        "unverified" to "Unverified",
        "call" to "Call Now",
        "whatsapp" to "WhatsApp",
        "hire" to "Hire Worker",
        "admin_panel" to "Admin Panel",
        "dashboard" to "Dashboard",
        "home" to "Home",
        "my_jobs" to "My Jobs",
        "applications" to "Applications",
        "rating_reviews" to "Rating & Reviews",
        "dark_mode" to "Dark Mode",
        "language" to "Language"
    )
}

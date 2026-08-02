package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class SkillItem(
    val hindiName: String,
    val englishName: String,
    val icon: ImageVector
)

object SkillCategories {
    val ALL_SKILLS = listOf(
        SkillItem("एल्युमिनियम कांच", "Aluminium Glass", Icons.Default.Window),
        SkillItem("कारपेंटर फर्नीचर", "Furniture Carpenter", Icons.Default.Chair),
        SkillItem("कुली", "Coolie / Loader", Icons.Default.Luggage),
        SkillItem("इलेक्ट्रिशियन", "Electrician", Icons.Default.ElectricBolt),
        SkillItem("फैब्रिकेटर", "Fabricator", Icons.Default.Handyman),
        SkillItem("फर्श-टाइल या IPS", "Tile / Flooring", Icons.Default.GridView),
        SkillItem("हेल्पर सिविल", "Helper Civil", Icons.Default.Engineering),
        SkillItem("हेल्पर इलेक्ट्रिशियन", "Helper Electrician", Icons.Default.Power),
        SkillItem("हेल्पर प्लम्बर", "Helper Plumber", Icons.Default.WaterDrop),
        SkillItem("हेल्पर कारपेंटर", "Helper Carpenter", Icons.Default.Carpenter),
        SkillItem("JCB ऑपरेटर", "JCB Operator", Icons.Default.PrecisionManufacturing),
        SkillItem("मेसन", "Mason / Rajmistri", Icons.Default.Foundation),
        SkillItem("प्लम्बर", "Plumber", Icons.Default.Plumbing),
        SkillItem("पेंटर", "Painter", Icons.Default.FormatPaint),
        SkillItem("RMC ऑपरेटर", "RMC Operator", Icons.Default.AirportShuttle),
        SkillItem("स्कैफोल्डर", "Scaffolder", Icons.Default.Architecture),
        SkillItem("कारपेंटर शटरिंग", "Shuttering Carpenter", Icons.Default.HomeWork),
        SkillItem("सरिया", "Steel Fixer / Rebar", Icons.Default.LineStyle),
        SkillItem("स्टोन मेसन", "Stone Mason", Icons.Default.Landscape),
        SkillItem("क्रेन ऑपरेटर", "Crane Operator", Icons.Default.LocalShipping),
        SkillItem("वेल्डर", "Welder", Icons.Default.FlashOn)
    )

    val STATES_AND_CITIES = mapOf(
        "उत्तर प्रदेश (Uttar Pradesh)" to listOf("लखनऊ", "कानपुर", "वाराणसी", "आगरा", "नोएडा", "गोरखपुर"),
        "दिल्ली NCR (Delhi NCR)" to listOf("नई दिल्ली", "दक्षिण दिल्ली", "पूर्वी दिल्ली", "गुड़गांव", "फरीदाबाद"),
        "बिहार (Bihar)" to listOf("पटना", "मुजफ्फरपुर", "भागलपुर", "गया", "दरभंगा"),
        "मध्य प्रदेश (Madhya Pradesh)" to listOf("भोपाल", "इंदौर", "ग्वालियर", "जबलपुर"),
        "राजस्थान (Rajasthan)" to listOf("जयपुर", "जोधपुर", "कोटा", "उदयपुर"),
        "हरियाणा (Haryana)" to listOf("अंबाला", "हिसार", "पानीपत", "करनाल"),
        "पंजाब (Punjab)" to listOf("लुधियाना", "अमृतसर", "जालंधर")
    )
}

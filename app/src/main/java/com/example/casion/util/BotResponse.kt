package com.example.casion.util

object BotResponse {
    val skinAndNailsSymptoms = hashMapOf(
        "itching" to "Gatal",
        "skin_rash" to "Ruam Kulit",
        "nodal_skin_eruptions" to "Eruptions Kulit Nodal",
        "yellowish_skin" to "Kulit Kekuningan",
        "redness_of_eyes" to "Kemerahan pada Mata",
        "bruising" to "Memar",
        "swollen_legs" to "Kaki Bengkak",
        "puffy_face_and_eyes" to "Wajah dan Mata Bengkak",
        "brittle_nails" to "Kuku Rapuh",
        "swollen_extremities" to "Ekstremitas Bengkak",
        "dischromic_patches" to "Bercak Diskromik",
        "pus_filled_pimples" to "Jerawat Bernanah",
        "blackheads" to "Komedo",
        "scurring" to "Bekas Luka",
        "skin_peeling" to "Pengelupasan Kulit",
        "silver_like_dusting" to "Debu Seperti Perak",
        "small_dents_in_nails" to "Lekukan Kecil di Kuku",
        "inflammatory_nails" to "Kuku Meradang",
        "blister" to "Lepuh",
        "red_sore_around_nose" to "Luka Merah di Sekitar Hidung",
        "yellow_crust_ooze" to "Kerak Kuning yang Mengeluarkan Cairan"
    )

    val respiratorySymptoms = hashMapOf(
        "continuous_sneezing" to "Bersin Terus-menerus",
        "cough" to "Batuk",
        "breathlessness" to "Sesak Napas",
        "phlegm" to "Dahak",
        "throat_irritation" to "Iritasi Tenggorokan",
        "sinus_pressure" to "Tekanan Sinus",
        "runny_nose" to "Hidung Berair",
        "congestion" to "Kongesti",
        "chest_pain" to "Nyeri Dada",
        "blood_in_sputum" to "Darah dalam Dahak",
        "mucoid_sputum" to "Dahak Mukoid",
        "rusty_sputum" to "Dahak Berkarat"
    )

    val gastrointestinalSymptoms = hashMapOf(
        "stomach_pain" to "Sakit Perut",
        "acidity" to "Asam Lambung",
        "ulcers_on_tongue" to "Luka di Lidah",
        "vomiting" to "Muntah",
        "diarrhoea" to "Diare",
        "abdominal_pain" to "Nyeri Perut",
        "constipation" to "Sembelit",
        "yellow_urine" to "Urin Kuning",
        "yellowing_of_eyes" to "Menguningnya Mata",
        "fluid_overload" to "Overload Cairan",
        "swelling_of_stomach" to "Pembengkakan Perut",
        "belly_pain" to "Nyeri Perut",
        "stomach_bleeding" to "Pendarahan Perut",
        "distention_of_abdomen" to "Distensi Abdomen",
        "pain_during_bowel_movements" to "Nyeri Saat Buang Air Besar",
        "pain_in_anal_region" to "Nyeri di Daerah Anus",
        "bloody_stool" to "Tinja Berdarah",
        "irritation_in_anus" to "Iritasi di Anus",
        "loss_of_appetite" to "Hilangnya Nafsu Makan",
        "indigestion" to "Gangguan Pencernaan",
        "increased_appetite" to "Nafsu Makan Meningkat",
        "polyuria" to "Poliuria",
        "passage_of_gases" to "Pengeluaran Gas",
        "internal_itching" to "Gatal Internal",
        "nausea" to "Mual",
        "dehydration" to "Dehidrasi"
    )

    val generalSymptoms = hashMapOf(
        "shivering" to "Menggigil",
        "chills" to "Dingin",
        "joint_pain" to "Nyeri Sendi",
        "fatigue" to "Kelelahan",
        "weight_gain" to "Penambahan Berat Badan",
        "anxiety" to "Kecemasan",
        "cold_hands_and_feets" to "Tangan dan Kaki Dingin",
        "mood_swings" to "Perubahan Suasana Hati",
        "weight_loss" to "Penurunan Berat Badan",
        "restlessness" to "Gelisah",
        "lethargy" to "Lesu",
        "irregular_sugar_level" to "Kadar Gula Tidak Teratur",
        "high_fever" to "Demam Tinggi",
        "mild_fever" to "Demam Ringan",
        "headache" to "Sakit Kepala",
        "obesity" to "Kegemukan",
        "malaise" to "Lesu",
        "fast_heart_rate" to "Detak Jantung Cepat",
        "toxic_look_(typhos)" to "Penampilan Toksik (Tifoid)",
        "depression" to "Depresi",
        "irritability" to "Iritabilitas",
        "muscle_pain" to "Nyeri Otot",
        "red_spots_over_body" to "Bintik Merah di Seluruh Tubuh",
        "blurred_and_distorted_vision" to "Penglihatan Kabur dan Terganggu",
        "visual_disturbances" to "Gangguan Penglihatan",
        "lack_of_concentration" to "Kurang Konsentrasi",
        "coma" to "Koma",
        "history_of_alcohol_consumption" to "Riwayat Konsumsi Alkohol",
        "prominent_veins_on_calf" to "Pembuluh Darah Menonjol di Betis",
        "palpitations" to "Palpitasi"
    )

    val neurologicalSymptoms = hashMapOf(
        "muscle_wasting" to "Penyusutan Otot",
        "slurred_speech" to "Ucapan Tidak Jelas"
    )

    val urinarySymptoms = hashMapOf(
        "burning_micturition" to "Buang Air Kecil Terbakar",
        "spotting_urination" to "Bercak Urin"
    )

    val cardiovascularSymptoms = hashMapOf(
        "prominent_veins_on_calf" to "Pembuluh Darah Menonjol di Betis"
    )

    val endocrineSymptoms = hashMapOf(
        "enlarged_thyroid" to "Tiroid Membesar",
        "excessive_hunger" to "Rasa Lapar Berlebihan"
    )

    fun responses(_message: String, pickedSymptoms: ArrayList<String>): String {
        val random = (0..2).random()
        val message = _message.toLowerCase()

        return when {
            message.contains("halo") || message.contains("hai") -> {
                when (random) {
                    0 -> "Halo! apakah ada yang bisa aku bantu?"
                    1 -> "Hai juga, gimana kabarmu?"
                    else -> "Hai!"
                }
            }

            else -> {
                // Create response message including the list of picked symptoms as bullet points
                if (pickedSymptoms.isNotEmpty()) {
                    val symptomsList = pickedSymptoms.joinToString("\n") { "• $it" }
                    "Gejala yang sudah kamu sebutkan:\n$symptomsList"
                } else {
                    "Maaf, saya tidak mengerti gejalanya."
                }
            }
        }
    }
}

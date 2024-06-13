package com.example.casion.util

object BotResponse {

    fun responses(_message: String): String {

        val random = (0..2).random()
        val message = _message.toLowerCase()

        // Define kemungkinan gejala dan penyakit yang sesuai
        val symptomDiseaseMap = mapOf(
            "pusing" to listOf("dehidrasi", "anemia", "migrain"),
            "migren" to listOf("migrain", "stres"),
            "sakit kepala" to listOf("tension headache", "migrain", "flu"),
            "mual" to listOf("gangguan pencernaan", "keracunan makanan", "gastritis"),
            "kram" to listOf("gastritis", "kolik", "dispepsia"),
            "sakit perut" to listOf("gastritis", "kolik", "dispepsia")
        )

        // Define penanganan awal untuk penyakit
        val diseaseTreatmentMap = mapOf(
            "dehidrasi" to "Minum air yang cukup dan istirahat.",
            "anemia" to "Konsumsi makanan tinggi zat besi dan konsultasikan ke dokter.",
            "migrain" to "Hindari pemicu, istirahat di tempat gelap dan minum obat pereda nyeri.",
            "stres" to "Lakukan relaksasi dan manajemen stres.",
            "tension headache" to "Istirahat dan konsumsi obat pereda nyeri.",
            "flu" to "Istirahat yang cukup dan minum banyak cairan.",
            "gangguan pencernaan" to "Hindari makanan berlemak dan pedas.",
            "keracunan makanan" to "Minum banyak air dan konsultasikan ke dokter.",
            "gastritis" to "Hindari makanan pedas dan minum obat antasida.",
            "kolik" to "Kompres hangat dan minum obat pereda nyeri.",
            "dispepsia" to "Hindari makanan berlemak dan pedas."
        )

        return when {
            message.contains("halo") || message.contains("hai") -> {
                when (random) {
                    0 -> "Halo! apakah ada yang bisa aku bantu?"
                    1 -> "Hai juga, gimana kabarmu?"
                    else -> "Hai!"
                }
            }

            message.contains("pusing") || message.contains("migren") || message.contains("sakit kepala") ||
                    message.contains("mual") || message.contains("kram") || message.contains("sakit perut") -> {

                val symptoms = message.split(" ").filter { symptomDiseaseMap.containsKey(it) }
                val possibleDiseases = symptoms.flatMap { symptomDiseaseMap[it] ?: emptyList() }.groupingBy { it }.eachCount()
                val mostLikelyDisease = possibleDiseases.maxByOrNull { it.value }?.key
                val treatment = diseaseTreatmentMap[mostLikelyDisease] ?: "Silakan konsultasikan dengan dokter."

                "Berdasarkan gejala yang kamu sebutkan, kemungkinan besar kamu mengalami $mostLikelyDisease. Penanganan awal yang disarankan: $treatment"
            }

            else -> {
                when (random) {
                    0 -> "Maaf, saya tidak paham"
                    1 -> "Maaf saya memiliki batasan untuk memahami pesan"
                    else -> "Maaf, saya tidak mengerti"
                }
            }
        }
    }
}

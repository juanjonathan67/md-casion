package com.example.casion.util

object BotResponse {

    fun responses(_message: String) : String{

        val random = (0..3).random()
        val message = _message.toLowerCase()

        return when {
            message.contains("sakit") -> {
                when (random) {
                    0 -> "Apa keluhanmu?"
                    1 -> "Apa yang sakit?"
                    else -> "sakit ap?"
                }
            }

            message.contains("halo") || message.contains("hai") -> {
                when (random) {
                    0 -> "Halo! apakah ada yang bisa aku bantu?"
                    1 -> "Hai juga, gimana kabarmu?"
                    else -> "G ush sok asik, km knp?"
                }
            }

            message.contains("mual") || message.contains("pusing") || message.contains("pilek") -> {
                when (random) {
                    0 -> "Apa yang kamu rasakan?"
                    1 -> "Bagaimana rasanya?"
                    else -> "trs?"
                }
            }
            else -> {
                when (random) {
                    0 -> "Oh"
                    1 -> "Yodah"
                    2 -> "au ah, bodo amat"
                    3 -> "yg bener ajg, lg kerja ini"
                    else -> "Gtw ah, cape gw"
                }
            }
        }
    }
}
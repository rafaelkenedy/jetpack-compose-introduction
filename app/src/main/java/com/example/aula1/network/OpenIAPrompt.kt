package com.example.aula1.network

class OpenIAPrompt(
    val prompt: String,
    val model: String = "text-davinci-003",
    val max_tokens: Int = 15,
    val n: Int = 1,
    val temperature: Double = 0.7
)
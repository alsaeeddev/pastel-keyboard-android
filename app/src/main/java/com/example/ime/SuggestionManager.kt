package com.example.ime

object SuggestionManager {
    private val corpus = listOf(
        "the", "be", "to", "of", "and", "a", "in", "that", "have", "i", "it", "for", "not", "on", "with", 
        "he", "as", "you", "do", "at", "this", "but", "his", "by", "from", "they", "we", "say", "her", "she", 
        "or", "an", "will", "my", "one", "all", "would", "there", "their", "what", "so", "up", "out", "if", 
        "about", "who", "get", "which", "go", "me", "hello", "beautiful", "pastel", "keyboard", "aesthetic", 
        "cute", "sweet", "love", "thanks", "thank", "happy", "smile", "please", "sorry", "wonderful", 
        "amazing", "awesome", "sparkle", "cherry", "blossom", "flower", "princess", "angel", "magic", 
        "dream", "dreamy", "fairy", "glitter", "unicorn", "ribbon", "heart", "pink", "lavender", "peach", 
        "rose", "midnight", "today", "tomorrow", "good", "morning", "night", "sweetheart", "bubble", "tea", 
        "coffee", "cake", "strawberry", "bunny", "kitty"
    )

    fun getSuggestions(prefix: String): List<String> {
        if (prefix.isBlank()) return emptyList()
        val lowerPrefix = prefix.lowercase()
        return corpus.filter { it.startsWith(lowerPrefix) && it != lowerPrefix }
            .take(3)
    }
}

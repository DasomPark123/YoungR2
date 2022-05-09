package com.example.youngr2

import java.util.regex.Pattern

fun removeKorean(string: String) : String {
    val regex = "[\\uAC00-\\uD7A3]".toRegex()
    return string.replace(regex, "")
}

fun findWordBetweenBracket(str : String) : String {
    var word : String? = ""
    val pattern = Pattern.compile("[(](.*?)[)]")
    val matcher = pattern.matcher(str)
    while (matcher.find()) {
        word = matcher.group(1)
        if (word == null) break
    }
    return word ?: ""
}
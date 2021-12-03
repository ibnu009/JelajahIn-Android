package com.ibnu.jelajahin.ui.user.login

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.regex.Pattern

class LoginFragmentTest {

    @Test
    fun testEmailIsNotEmpty() {
        val mockCorrectEmail = "ibnubatutah001@gmail.com"
        assertEquals(mockCorrectEmail.length, 24)
    }

    @Test
    fun testPasswordIsNotEmpty() {
        val mockpassword = "ibnubatutah001@gmail.com"
        assertEquals(true, mockpassword.isNotEmpty())
    }

    @Test
    fun testPasswordCorrectFormat() {
        val mockPassword = "Ibnubatutah001"
        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        )
        val actualPassword = passwordREGEX.matcher(mockPassword).matches()
        assertEquals(true, actualPassword)
    }

    @Test
    fun testPasswordIncorrectFormat() {
        val mockPassword = "ibnu1"
        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        )
        val actualPassword = passwordREGEX.matcher(mockPassword).matches()
        assertEquals(false, actualPassword)
    }
}
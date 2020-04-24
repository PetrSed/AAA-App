package services

import Wrapper
import ExitCodes.*
import domains.*
import java.math.BigInteger
import java.security.MessageDigest

class AuthenticationService(val wrapper: Wrapper) {
    fun authenticate(login: String, password: String): Int {
        if (!checkLoginValidity(login)) {
            return InvalidLoginFormat.code
        }
        if (checkLoginPresenceInBase(login)) {
            return UnknownLogin.code
        }
        val nowHash = getPasswordHash(password)
        val user = getUser(login, wrapper)
        if (!checkPasswordsHashs(user!!.hash, nowHash)) {
            return InvalidPassword.code
        }

        return -1
    }

    fun checkLoginValidity(login: String): Boolean = login.matches(Regex("[a-z]{1,10}"))
    fun checkLoginPresenceInBase(login: String): Boolean = wrapper.loginExists(login)
    fun getUser(login: String, wrapper: Wrapper): User? = wrapper.getUser(login)
    fun getPasswordHash(password: String): String = password.md5()
    fun checkPasswordsHashs(baseHash: String, nowHash: String): Boolean = baseHash == nowHash


    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }
}



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
        if (checkLoginPresenceInBase(login, wrapper)) {
            return UnknownLogin.code
        }
        val nowHash = getPasswordHash(password)
        val user = getUser(login, wrapper)
        if (!checkPasswordsHashs(user.hash, nowHash)) {
            return InvalidPassword.code
        }

        return -1
    }

    private fun checkLoginValidity(login: String): Boolean = login.matches(Regex("[a-z]{1,10}"))
    private fun checkLoginPresenceInBase(login: String, wrapper: Wrapper): Boolean = wrapper.loginExists(login)
    private fun getUser(login: String, wrapper: Wrapper): User = wrapper.getUser(login)
    private fun getPasswordHash(password: String): String = password.md5()
    private fun checkPasswordsHashs(baseHash: String, nowHash: String): Boolean = baseHash == nowHash


    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }
}



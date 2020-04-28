import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import services.AuthenticationService
import domains.User

object AuthenticationTest : Spek({

    val users = listOf(
        User("admin", "21232f297a57a5a743894a0e4a801fc3"),
        User("petr", "5685dc8ca490fb3399ed2ddeb5faddca"),
        User("vasya", "202cb962ac59075b964b07152d234b70"),
        User("q", "3a4d92a1200aad406ac50377c7d863aa")
    )

    val wrapper = mockk<Wrapper> {
        every { getUser(ofType()) } answers { users.find { it.login == firstArg() } }
        every { loginExists(ofType()) } answers { users.find { it.login == firstArg() } != null }
    }

    val authenService = AuthenticationService(wrapper)

    Feature("Authentication Service verification") {
        Scenario("Check the validity of the login") {
            Then("Valid format") {
                assertTrue(authenService.checkLoginValidity("petr"))
            }
            Then("Invalid format") {
                assertFalse(authenService.checkLoginValidity("petr!"))
            }
        }
        Scenario("Check the presence of the login") {
            Then("Login is presence in the database") {
                assertTrue(authenService.checkLoginPresenceInBase("petr"))
            }
            Then("Login is not presence in the database") {
                assertFalse(authenService.checkLoginPresenceInBase("petr!"))
            }
        }
        Scenario("Getting the hash and verifying the password") {
            Then("Correct password") {
                val nowHash = authenService.getPasswordHash("petr101")
                assertTrue(authenService.checkPasswordsHashs("5685dc8ca490fb3399ed2ddeb5faddca", nowHash))
            }
            Then("Incorrect password") {
                val nowHash = authenService.getPasswordHash("petr1")
                assertFalse(authenService.checkPasswordsHashs("5685dc8ca490fb3399ed2ddeb5faddca", nowHash))
            }
        }
    }

})
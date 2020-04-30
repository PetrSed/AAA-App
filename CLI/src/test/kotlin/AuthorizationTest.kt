import io.mockk.every
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import services.AuthorizationService
import domains.*
import kotlin.test.assertEquals
import ExitCodes.*

object AuthorizationTest : Spek({

    val wrapper = mockk<Wrapper> {
        every { hasPermission("petr", "EXECUTE", "^A(\\.BB)?\$") } answers { true }
        every { hasPermission("petr", "WRITE", "^A(\\.BB)?\$") } answers { false }
        every { hasPermission("petr!", "EXECUTE", "^A(\\.BB)?\$") } answers { false }
        every { hasPermission("petr", "EXECUTE", "^A(\\.BB.С)?\$" ) } answers { true }
        every { hasPermission("petr", "EXECUTE", "^A\$" ) } answers { false }

    }

    val authorService = AuthorizationService(wrapper)

    Feature("Authorization Service verification") {
        Scenario("Checking the access role") {
            Then("Have access") {
                assertEquals(-1, authorService.authorize(Resource("petr", Role.EXECUTE, "A.BB")))
            }
            Then("No access") {
                assertEquals(NoAccess.code, authorService.authorize(Resource("petr", Role.WRITE, "A.BB")))
            }
            Then("No access") {
                assertEquals(NoAccess.code, authorService.authorize(Resource("petr!", Role.EXECUTE, "A.BB")))
            }
        }
        Scenario("Сhecking access to the parent") {
            Then("Have access") {
                assertEquals(-1, authorService.authorize(Resource("petr", Role.EXECUTE, "A.BB.С")))
            }
            Then("No access") {
                assertEquals(NoAccess.code, authorService.authorize(Resource("petr", Role.EXECUTE, "A")))
            }
        }
    }

})
package services

import Wrapper
import domains.Session

class AccountingService(val wrapper: Wrapper) {
    fun accounting(session: Session): Int {
        addActivity(session)
        return -1
    }
    fun addActivity(session: Session) {
        wrapper.addSession(session)
    }
}
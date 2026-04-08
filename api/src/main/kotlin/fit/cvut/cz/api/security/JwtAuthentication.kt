package fit.cvut.cz.api.security

import fit.cvut.cz.api.models.Role
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JwtAuthentication : Authentication {

    private var authenticated = false
    var userId: Long? = null
    var login: String? = null
    var role: Role? = null

    override fun getName(): String {
        return login!!
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(role!!)
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getDetails(): Any? {
        return null
    }

    override fun getPrincipal(): Any? {
        return null
    }

    override fun isAuthenticated(): Boolean {
        return authenticated
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }
}
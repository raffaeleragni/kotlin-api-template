package app.config.security

import org.springframework.security.core.GrantedAuthority

enum class Roles: GrantedAuthority {
  ROLE1 {
    override fun getAuthority(): String = "ROLE1"
  }
}


package tda.core.apirest

package object domain {
  type UserId = String
  type AuthToken = String

  final case class AuthTokenContent(userId: UserId)

  final case class AuthData(id: UserId, username: String, email: String, password: String) {
    require(id.nonEmpty, "id.empty")
    require(username.nonEmpty, "username.empty")
    require(email.nonEmpty, "email.empty")
    require(password.nonEmpty, "password.empty")
  }

  final case class UserProfile(id: UserId, firstName: String, lastName: String, identity: String) {
    require(id.nonEmpty, "firstName.empty")
    require(firstName.nonEmpty, "firstName.empty")
    require(lastName.nonEmpty, "lastName.empty")
    require(identity.nonEmpty, "identity.empty")
  }

  final case class UserProfileUpdate(firstName: Option[String] = None, lastName: Option[String] = None
                                     , identity: Option[String] = None) {
    def merge(profile: UserProfile): UserProfile =
      UserProfile(profile.id, firstName.getOrElse(profile.firstName), lastName.getOrElse(profile.lastName)
        , identity.getOrElse(profile.identity))
  }
}

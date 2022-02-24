package tda.core.apirest.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import tda.core.apirest.domain.profiles.UserProfileService
import tda.core.apirest.domain.auth.AuthService
import tda.core.apirest.http.routes.{ AuthRoute, ProfileRoute }
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.concurrent.ExecutionContext

class HttpRoute(
    userProfileService: UserProfileService,
    authService: AuthService,
    secretKey: String
)(implicit executionContext: ExecutionContext) {

  private val usersRouter = new ProfileRoute(secretKey, userProfileService)
  private val authRouter  = new AuthRoute(authService)

  val route: Route =
    cors() {
      pathPrefix("v1") {
        usersRouter.route ~
        authRouter.route
      } ~
      pathPrefix("healthcheck") {
        get {
          complete("OK")
        }
      }
    }

}

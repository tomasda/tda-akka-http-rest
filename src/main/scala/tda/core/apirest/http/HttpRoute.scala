package tda.core.apirest.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import tda.core.apirest.domain.profiles.UserProfileService
import tda.core.apirest.domain.auth.AuthService
import tda.core.apirest.http.routes.{AuthRoute, ProfileRoute, SessionRoute}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import tda.core.apirest.session.SessionController

import scala.concurrent.ExecutionContext

class HttpRoute(
                sessionController: SessionController,
                userProfileService: UserProfileService,
                authService: AuthService,
                secretKey: String
            )(implicit executionContext: ExecutionContext) {

  private val usersRouter = new ProfileRoute(secretKey, sessionController, userProfileService)
  private val authRouter  = new AuthRoute(sessionController, authService)
  private val sessionRouter = new SessionRoute(sessionController)

  val route: Route =
    cors() {
      pathPrefix("v1") {
        sessionRouter.route ~
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

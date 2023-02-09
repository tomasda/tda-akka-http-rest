package tda.core.apirest.http.routes

import akka.http.scaladsl.server.Directives.{complete, get, pathEndOrSingleSlash, pathPrefix}
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto.exportEncoder
import io.circe.syntax.EncoderOps
import tda.core.apirest.session.SessionController

import scala.concurrent.ExecutionContext

class SessionRoute(sessionController: SessionController)
                  (implicit executionContext: ExecutionContext)
  extends FailFastCirceSupport {


    val route: Route = pathPrefix("sessions") {
      pathEndOrSingleSlash {
        get {
          complete(sessionController.getProfiles.map(_.asJson))
        }
      } //~
//        pathPrefix("me") {
//          pathEndOrSingleSlash {
//            authenticate(secretKey) { userId =>
//              get {
//                complete(getProfile(userId))
//              } ~
//                post {
//                  entity(as[UserProfileUpdate]) { userUpdate =>
//                    complete(updateProfile(userId, userUpdate).map(_.asJson))
//                  }
//                }
//            }
//          }
//        } ~
//        pathPrefix(Segment) { id =>
//          pathEndOrSingleSlash {
//            get {
//              complete(getProfile(id).map {
//                case Some(profile) =>
//                  OK -> profile.asJson
//                case None =>
//                  BadRequest -> None.asJson
//              })
//            } ~
//              post {
//                entity(as[UserProfileUpdate]) { userUpdate =>
//                  complete(updateProfile(id, userUpdate).map {
//                    case Some(profile) =>
//                      OK -> profile.asJson
//                    case None =>
//                      BadRequest -> None.asJson
//                  })
//                }
//              }
//          }
//        }
    }

}

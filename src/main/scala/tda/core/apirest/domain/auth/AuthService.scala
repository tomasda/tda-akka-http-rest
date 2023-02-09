package tda.core.apirest.domain.auth

import java.util.UUID
import tda.core.apirest.domain.{AuthData, AuthToken, AuthTokenContent, UserId}
import tda.core.apirest.utils.MonadTransformers._
import com.roundeights.hasher.Implicits._
import pdi.jwt.{Jwt, JwtAlgorithm}
import io.circe.syntax._
import io.circe.generic.auto._
import tda.core.apirest.session.SessionController

import scala.concurrent.{ExecutionContext, Future}

class AuthService(sessionController: SessionController, authDataStorage: AuthDataStorage,secretKey: String)
                 (implicit executionContext: ExecutionContext) {

  def signIn(login: String, password: String): Future[Option[AuthToken]] =
    authDataStorage
      .findAuthData(login)
      .filterT(_.password == password.sha256.hex)
      .mapT(authData => encodeToken(authData.id))

  def signUp(login: String, email: String, password: String): Future[AuthToken] =
    authDataStorage
      .saveAuthData(AuthData(UUID.randomUUID().toString, login, email, password.sha256.hex))
      .map(authData => encodeToken(authData.id))

  private def encodeToken(userId: UserId): AuthToken = {
    val session = Jwt.encode(userId, secretKey, JwtAlgorithm.HS256)
    sessionController.addSession(userId, session)
    session
  }

}

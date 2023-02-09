package tda.core.apirest.utils


import akka.http.javadsl.server.Directives.reject
import akka.http.scaladsl.server.Directive1
import io.circe.jawn.decode
import pdi.jwt._
import tda.core.apirest.domain.{AuthTokenContent, UserId}

object SecurityDirectives {

  import akka.http.scaladsl.server.directives.BasicDirectives._
  import akka.http.scaladsl.server.directives.HeaderDirectives._

  def authenticate(secretKey: String): Directive1[UserId] =
    headerValueByName("Token")
      .map(Jwt.decodeRaw(_, secretKey, Seq(JwtAlgorithm.HS256)))
      .map(_.toOption.flatMap(decode[AuthTokenContent](_).toOption))
      .flatMap {
        case Some(result) =>
          provide(result.userId)
        case None =>
          reject
      }

}



package me.archdev.http

import akka.http.scaladsl.server.Route
import me.archdev.BaseServiceTest
import tda.core.apirest.domain.auth.AuthService
import tda.core.apirest.domain.profiles.UserProfileService
import tda.core.apirest.http.HttpRoute

class HttpRouteTest extends BaseServiceTest {

  "HttpRoute" when {

    "GET /healthcheck" should {

      "return 200 OK" in new Context {
        Get("/healthcheck") ~> httpRoute ~> check {
          responseAs[String] shouldBe "OK"
          status.intValue() shouldBe 200
        }
      }

    }

  }

  trait Context {
    val secretKey = "secret"
    val userProfileService: UserProfileService = mock[UserProfileService]
    val authService: AuthService = mock[AuthService]

    val httpRoute: Route = new HttpRoute(userProfileService, authService, secretKey).route
  }

}

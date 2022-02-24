package tda.core.apirest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import tda.core.apirest.domain.auth.{AuthService, JdbcAuthDataStorage}
import tda.core.apirest.domain.profiles.{UserProfileService, JdbcUserProfileStorage}
import tda.core.apirest.http.HttpRoute
import tda.core.apirest.utils.Config
import tda.core.apirest.utils.db.{DatabaseConnector, DatabaseMigrationManager}

import scala.concurrent.{ExecutionContext, Future}

object Boot extends App {

  def startApplication(): Future[Http.ServerBinding] = {
    implicit val actorSystem: ActorSystem = ActorSystem()
    implicit val executor: ExecutionContext      = actorSystem.dispatcher

    val config = Config.load()

    new DatabaseMigrationManager(
      config.database.jdbcUrl,
      config.database.username,
      config.database.password
    ).migrateDatabaseSchema()

    val databaseConnector = new DatabaseConnector(
      config.database.jdbcUrl,
      config.database.username,
      config.database.password
    )

    val userProfileStorage = new JdbcUserProfileStorage(databaseConnector)
    val authDataStorage    = new JdbcAuthDataStorage(databaseConnector)

    val usersService = new UserProfileService(userProfileStorage)
    val authService  = new AuthService(authDataStorage, config.secretKey)


    val httpRoute    = new HttpRoute(usersService, authService, config.secretKey)

    Http().newServerAt(config.http.host, config.http.port).bind(httpRoute.route)
  }

  startApplication()

}

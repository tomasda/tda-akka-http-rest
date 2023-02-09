package tda.core.apirest

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import tda.core.apirest.domain.UserId
import tda.core.apirest.domain.auth.{AuthService, JdbcAuthDataStorage}
import tda.core.apirest.domain.profiles.{JdbcUserProfileStorage, UserProfileService}
import tda.core.apirest.http.HttpRoute
import tda.core.apirest.session.{SessionController, SessionParams}
import tda.core.apirest.utils.Config
import tda.core.apirest.utils.db.{DatabaseConnector, DatabaseMigrationManager}

import scala.concurrent.duration.DurationInt
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

    val sessionController = new SessionController()
    val userProfileStorage = new JdbcUserProfileStorage(databaseConnector, sessionController)
    val authDataStorage    = new JdbcAuthDataStorage(databaseConnector)

    val usersService = new UserProfileService(userProfileStorage)
    val authService  = new AuthService(sessionController, authDataStorage, config.secretKey)

    val httpRoute    = new HttpRoute(sessionController, usersService, authService, config.secretKey)

    //actorSystem.scheduler.schedule(0.microsecond, 300.microsecond, sessionController.schedule , "tick")
    val schedulerActorSystem = ActorSystem("akka-scheduler-system")
    val greeter = schedulerActorSystem.actorOf(Props(classOf[Greetings]))
    val greeting = Greet("Detective","Lucifer")
    //schedulerActorSystem.scheduler.scheduleOnce(5.seconds, greeter, greeting)
    schedulerActorSystem.scheduler.scheduleWithFixedDelay(5.seconds,30.seconds, greeter, greeting)

    Http().newServerAt(config.http.host, config.http.port).bind(httpRoute.route)
  }

  startApplication()

}
case class Greet(to: String, by: String)
case class Greeted(msg: String){
  println(msg)
}
class Greetings extends Actor {
  override def receive: Receive = {
    case greet: Greet =>
      sender ! Greeted(s"${greet.by}: Hello, ${greet.to}")

  }
}
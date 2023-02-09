package tda.core.apirest.session


import tda.core.apirest.domain.UserId

object sessions{
  var sessions_1:Array[SessionParams] = Array()
  /** Este método sólo debe estar accesible en modo depuración */
  def getSessions: Array[SessionParams] ={
    sessions_1
  }

  /**
   * TODO: Falta verificación de session ya abierta.
   * @param s
   */
  def addSession(s:SessionParams): Unit ={
    sessions_1 = sessions_1 :+ s

  }

  /**
   * TODO: Falta verificación de session ya cerrada por exceder el tiempo de uso.
   * @param s
   */
  def removeSession(s:SessionParams):Unit ={
    sessions_1.dropWhile(_ == s)

  }

  def exists(s:String):Boolean =
    !sessions_1.exists(p => p.AuthToken.equals(s))
}

case class SessionParams(UserId: String, AuthToken: String) {}

/**
 * Interface of Session Controller
 */
sealed trait SessionControllerInterface {

  def addSession(idUsuario:UserId, token:String)

  def checkSession(token: String):Boolean

  def removeSession(idUsuario:UserId)

  def getUserIdByToken(token: String):Option[String]

}

/**
 * Session Controller
 */
class SessionController() extends SessionControllerInterface {


  override def addSession(idUsuario: UserId, token: String): Unit = {
    sessions.addSession(SessionParams(idUsuario,token))
  }

  override def checkSession(token: String): Boolean = sessions.exists(token)

  override def removeSession(idUsuario: UserId): Unit = ???

  override def getUserIdByToken(token: String): Option[String] = sessions.sessions_1.find(s => s.AuthToken.equals(token)) match {
    case Some(id) => Some(id.UserId)
    case None => None
  }

  def listSessions(): Unit ={
    sessions.getSessions.foreach( a => println("Session: "+a.UserId+" "+a.AuthToken))
  }

  def getProfiles: Seq[SessionParams] = sessions.getSessions


  object schedule{
    println("Se ha accedido al método schedule")
  }


}





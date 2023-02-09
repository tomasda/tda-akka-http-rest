package tda.core.apirest.domain.profiles

import tda.core.apirest.domain.UserProfile
import tda.core.apirest.utils.db.DatabaseConnector

private[profiles] trait UserProfileTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class Profiles(tag: Tag) extends Table[UserProfile](tag, "profiles") {
    def id        = column[String]("id", O.PrimaryKey)
    def firstName = column[String]("first_name")
    def lastName  = column[String]("last_name")
    def identity  = column[String]("identity")

    def * = (id, firstName, lastName, identity) <> ((UserProfile.apply _).tupled, UserProfile.unapply)
  }

  protected val
  profiles = TableQuery[Profiles]

}

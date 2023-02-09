package tda.core.apirest.domain.profiles

import tda.core.apirest.domain.{UserProfile, UserProfileUpdate}
import tda.core.apirest.utils.MonadTransformers.FutureOptionMonadTransformer

import scala.concurrent.{ExecutionContext, Future}

class UserProfileService(
    userProfileStorage: UserProfileStorage
)(implicit executionContext: ExecutionContext) {

  def getProfiles: Future[Seq[UserProfile]] =
    userProfileStorage.getProfiles

  def getProfile(id: String): Future[Option[UserProfile]] =
    userProfileStorage.getProfile(id)
//  def getProfile(token: String): Future[Option[UserProfile]] =
//    userProfileStorage.getProfile(token)

  def createProfile(profile: UserProfile): Future[UserProfile] =
    userProfileStorage.saveProfile(profile)

  def updateProfile(id: String, profileUpdate: UserProfileUpdate): Future[Option[UserProfile]] =
    userProfileStorage
      .getProfile(id)
      .mapT(profileUpdate.merge)
      .flatMapTOuter(userProfileStorage.saveProfile)

}

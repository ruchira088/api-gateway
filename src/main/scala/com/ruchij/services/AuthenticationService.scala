package com.ruchij.services

import javax.inject.{Inject, Singleton}

import com.ruchij.constants.ConfigurationValues
import com.ruchij.exceptions.{ExpiredAuthTokenException, InvalidAuthTokenException, InvalidCredentials, RedisClientException}
import com.ruchij.models.{AuthToken, User}
import com.ruchij.services.kv.KeyValueStore
import com.ruchij.services.hashing.PasswordHashingService
import com.ruchij.utils.GeneralUtils._
import org.joda.time.DateTime

import scalaz.std.scalaFuture.futureInstance
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthenticationService @Inject()(
       userService: UserService,
       passwordHashingService: PasswordHashingService,
       keyValueStore: KeyValueStore
)(implicit executionContext: ExecutionContext) {
  def login(username: String, password: String): Future[AuthToken] =
    for {
      user <- userService.getByUsername(username).getOrElseF(Future.failed(InvalidCredentials))
      hashedPassword <- Future.fromTry(toTry(user.password))

      isMatch <- passwordHashingService.checkPassword(password, hashedPassword)

      _ <- predicate(isMatch, InvalidCredentials)

      authToken = AuthToken(user)

      _ <- keyValueStore.put(authToken.id, authToken)
    }
      yield authToken

  def authenticate(tokenId: String): Future[User] =
    for {
      authToken <- keyValueStore.get[AuthToken](tokenId).getOrElseF(Future.failed(InvalidAuthTokenException))

      diff = authToken.issuedAt.plus(ConfigurationValues.SESSION_DURATION.length).getMillis - DateTime.now().getMillis

      _ <- predicate(diff > 0, ExpiredAuthTokenException)

      newToken <- renewToken(authToken)
    }
    yield newToken.user

  private def renewToken(authToken: AuthToken): Future[AuthToken] =
    keyValueStore.put(authToken.id, authToken.copy(issuedAt = DateTime.now()))
}
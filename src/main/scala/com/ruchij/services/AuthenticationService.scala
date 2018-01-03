package com.ruchij.services

import javax.inject.{Inject, Singleton}

import com.ruchij.exceptions.InvalidCredentials
import com.ruchij.models.User
import com.ruchij.services.kv.KeyValueStore
import com.ruchij.services.hashing.PasswordHashingService
import com.ruchij.utils.GeneralUtils._

import scalaz.std.scalaFuture.futureInstance
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthenticationService @Inject()(
       userService: UserService,
       passwordHashingService: PasswordHashingService,
       cachingService: KeyValueStore
)(implicit executionContext: ExecutionContext)
{
  def login(username: String, password: String): Future[User] =
    for {
      user <- userService.getByUsername(username).getOrElseF(Future.failed(InvalidCredentials))
      hashedPassword <- Future.fromTry(toTry(user.password))

      isMatch <- passwordHashingService.checkPassword(password, hashedPassword)

      _ <- predicate(isMatch, InvalidCredentials)
    }
    yield user.sanitize
}
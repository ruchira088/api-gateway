package com.ruchij.services

import javax.inject.{Inject, Singleton}

import com.ruchij.daos.UserDao
import com.ruchij.exceptions.UsernameAlreadyExistsException
import com.ruchij.models.{Applicant, User}
import com.ruchij.services.hashing.PasswordHashingService
import com.ruchij.utils.GeneralUtils._
import com.ruchij.web.requests.RegisterUser
import org.joda.time.DateTime

import scalaz.std.scalaFuture.futureInstance
import scala.concurrent.{ExecutionContext, Future}
import scalaz.OptionT

@Singleton
class UserService @Inject()(userDao: UserDao, passwordHashingService: PasswordHashingService)
                           (implicit executionContext: ExecutionContext)
{
  def getByUsername(username: String): OptionT[Future, User] =
    userDao.getByUsername(username)

  def create(registerUser: RegisterUser): Future[User] =
    for {
      isAvailable <- userDao.getByUsername(registerUser.username).isEmpty

      _ <- predicate(isAvailable, UsernameAlreadyExistsException(registerUser.username))

      user <- newUser(registerUser)

      _ <- userDao.create(user)
    }
    yield user

  private def newUser(registerUser: RegisterUser): Future[User] =
    passwordHashingService.hashPassword(registerUser.password)
      .map(hashedPassword =>
        User(
          uuid(),
          DateTime.now(),
          registerUser.username,
          Some(hashedPassword),
          Applicant,
          registerUser.email,
          None,
          None
        )
      )
}

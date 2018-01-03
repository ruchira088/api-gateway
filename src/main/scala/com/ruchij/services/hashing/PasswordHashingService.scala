package com.ruchij.services.hashing

import scala.concurrent.Future

trait PasswordHashingService
{
  def hashPassword(password: String): Future[String]

  def checkPassword(candidate: String, hashedPassword: String): Future[Boolean]
}

package com.ruchij.services.hashing

import javax.inject.{Inject, Singleton}

import com.ruchij.contexts.BlockingExecutionContext
import org.mindrot.jbcrypt.BCrypt

import scala.concurrent.Future

@Singleton
class BcryptPasswordHashing @Inject()(implicit blockingExecutionContext: BlockingExecutionContext)
  extends PasswordHashingService
{
  override def hashPassword(password: String): Future[String] =
    Future {
      BCrypt.hashpw(password, BCrypt.gensalt())
    }

  override def checkPassword(candidate: String, hashedPassword: String): Future[Boolean] =
    Future {
      BCrypt.checkpw(candidate, hashedPassword)
    }
}

package com.ruchij.modules

import akka.actor.ActorSystem
import com.google.inject.AbstractModule
import com.ruchij.constants.EnvVariableNames.MONGO_DB_URL
import com.ruchij.contexts.{BlockingExecutionContext, BlockingExecutionContextImpl}
import com.ruchij.daos.{MongoUserDao, UserDao}
import com.ruchij.mongo.MongoCollection
import com.ruchij.services.hashing.{BcryptPasswordHashing, PasswordHashingService}
import com.ruchij.utils.ConfigUtils
import reactivemongo.api.MongoConnection

import scala.concurrent.ExecutionContext
import scala.util.Try

class GuiceModule()(implicit actorSystem: ActorSystem, executionContext: ExecutionContext) extends AbstractModule
{
  override def configure(): Unit =
  {
    initializationBindings()

    bind(classOf[ActorSystem]).toInstance(actorSystem)
    bind(classOf[ExecutionContext]).toInstance(executionContext)

    bind(classOf[BlockingExecutionContext]).to(classOf[BlockingExecutionContextImpl])

    bind(classOf[PasswordHashingService]).to(classOf[BcryptPasswordHashing])
    bind(classOf[UserDao]).to(classOf[MongoUserDao])
  }

  private def initializationBindings() =
  {
    mongoDbConnection().fold(
      exception => throw exception,
      mongoConnection => {
        bind(classOf[MongoConnection]).toInstance(mongoConnection)
      }
    )
  }

  private def mongoDbConnection(): Try[MongoConnection] =
    for {
      mongoDbUrl <- ConfigUtils.getEnvValue(MONGO_DB_URL)
      mongoConnection <- MongoCollection.mongoConnection(mongoDbUrl)
    }
    yield mongoConnection
}

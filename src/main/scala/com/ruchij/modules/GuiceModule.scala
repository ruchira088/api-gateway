package com.ruchij.modules

import akka.actor.ActorSystem
import com.google.inject.AbstractModule
import com.ruchij.constants.EnvVariableNames._
import com.ruchij.contexts.{BlockingExecutionContext, BlockingExecutionContextImpl}
import com.ruchij.daos.{MongoUserDao, UserDao}
import com.ruchij.mongo.MongoCollection
import com.ruchij.services.hashing.{BcryptPasswordHashing, PasswordHashingService}
import com.ruchij.services.kv.{KeyValueStore, RedisKeyValueStore}
import com.ruchij.services.product.{ProductService, ProductServiceImpl}
import com.ruchij.utils.ConfigUtils.getEnvValue
import com.ruchij.utils.GeneralUtils
import reactivemongo.api.MongoConnection
import redis.RedisClient

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._
import scala.sys.addShutdownHook
import scala.language.postfixOps
import scala.util.Try

class GuiceModule()(implicit actorSystem: ActorSystem, executionContext: ExecutionContext) extends AbstractModule
{
  override def configure(): Unit =
  {
    initializationBindings().fold(exception => throw exception, _ => {})

    bind(classOf[ActorSystem]).toInstance(actorSystem)
    bind(classOf[ExecutionContext]).toInstance(executionContext)

    bind(classOf[BlockingExecutionContext]).to(classOf[BlockingExecutionContextImpl])

    bind(classOf[PasswordHashingService]).to(classOf[BcryptPasswordHashing])
    bind(classOf[ProductService]).to(classOf[ProductServiceImpl])
    bind(classOf[UserDao]).to(classOf[MongoUserDao])
    bind(classOf[KeyValueStore]).to(classOf[RedisKeyValueStore])
  }

  private def initializationBindings(): Try[Unit] =
    for {
      mongoConnection <- mongoDbConnection()
      redis <- redisClient()

      _ = println("Successfully connected to MongoDB and Redis")

      _ = addShutdownHook {
        mongoConnection.close()
        Await.ready(redis.shutdown(), 30 seconds)
      }
    }
    yield  {
      bind(classOf[MongoConnection]).toInstance(mongoConnection)
      bind(classOf[RedisClient]).toInstance(redis)
    }

  private def mongoDbConnection(): Try[MongoConnection] =
    for {
      mongoDbUrl <- getEnvValue(MONGO_DB_URL)
      mongoConnection <- MongoCollection.mongoConnection(mongoDbUrl)
    }
    yield mongoConnection

  private def redisClient(): Try[RedisClient] =
    for {
      redisHost <- getEnvValue(REDIS_HOST)
      redisPort <- getEnvValue(REDIS_PORT).flatMap(GeneralUtils.parse[String, Int](_, string => string.toInt))
    }
    yield RedisClient(host = redisHost, port = redisPort)
}

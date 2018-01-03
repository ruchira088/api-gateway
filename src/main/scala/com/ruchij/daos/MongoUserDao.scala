package com.ruchij.daos

import javax.inject.{Inject, Singleton}

import com.ruchij.models.User
import com.ruchij.mongo.MongoCollection
import play.api.libs.json.Json
import reactivemongo.api.MongoConnection
import reactivemongo.api.commands.WriteResult

import scala.concurrent.{ExecutionContext, Future}
import scalaz.OptionT

@Singleton
class MongoUserDao @Inject()(val mongoConnection: MongoConnection)(implicit executionContext: ExecutionContext)
  extends UserDao with MongoCollection[User]
{
  import MongoUserDao._

  override type InsertionResult = WriteResult

  override def create(user: User): Future[WriteResult] = insert(user)

  override def getByUsername(username: String): OptionT[Future, User] =
    OptionT {
      find(Json.obj("username" -> username))
        .map(_.headOption)
    }

  override def collectionName: String = COLLECTION_NAME
}

object MongoUserDao
{
  val COLLECTION_NAME = "users"
}
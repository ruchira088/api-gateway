package com.ruchij.mongo

import play.api.libs.json.{JsObject, OWrites, Reads}
import reactivemongo.play.json._
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, MongoConnection, MongoDriver}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

trait MongoCollection[T]
{
  import MongoCollection._

  def mongoConnection: MongoConnection

  def collectionName: String

  def insert(item: T)(implicit oWrites: OWrites[T], executionContext: ExecutionContext): Future[WriteResult] =
    for {
      collection <- jsonCollection
      writeResult <- collection.insert(item)
    }
    yield writeResult

  def jsonCollection(implicit executionContext: ExecutionContext): Future[JSONCollection] =
    for {
      connection <- Future.successful(mongoConnection)
      database <- connection.database(MONGO_DATABASE_NAME)
      itemCollection = database.collection[JSONCollection](collectionName)
    }
    yield itemCollection

  def find(query: JsObject)(implicit reads: Reads[T], executionContext: ExecutionContext): Future[List[T]] =
    for {
      collection <- jsonCollection
      items <- collection.find(query).cursor[T]().collect(MAX_DOCS, Cursor.FailOnError[List[T]]())
    }
    yield items
}

object MongoCollection
{
  val MONGO_DATABASE_NAME = "asp-rubber"

  val MAX_DOCS = 30

  def mongoConnection(mongoUri: String): Try[MongoConnection] =
    for {
      parsedUri <- MongoConnection.parseURI(mongoUri)
      connection = MongoDriver().connection(parsedUri)
    }
    yield connection
}

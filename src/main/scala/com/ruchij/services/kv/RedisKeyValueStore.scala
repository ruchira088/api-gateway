package com.ruchij.services.kv

import javax.inject.Inject

import com.ruchij.exceptions.RedisClientException
import com.ruchij.utils.GeneralUtils
import play.api.libs.json.{Json, Reads, Writes}
import redis.RedisClient

import scalaz.std.scalaFuture.futureInstance
import scala.concurrent.{ExecutionContext, Future}
import scalaz.OptionT

class RedisKeyValueStore @Inject()(redisClient: RedisClient)(implicit executionContext: ExecutionContext) extends KeyValueStore
{
  override def put[T](key: String, item: T)(implicit Writes: Writes[T]): Future[T] =
    for {
      isSuccess <- redisClient.set(key, Json.stringify(Json.toJson(item)))

      _ <- GeneralUtils.predicate(isSuccess, RedisClientException)
    }
    yield item

  override def get[T](key: String)(implicit reads: Reads[T]): OptionT[Future, T] =
    for {
      value <- OptionT(redisClient.get[String](key))
      item <- OptionT(Future.successful(Json.parse(value).asOpt[T]))
    }
    yield item

  override def remove(keys: String*): Future[Long] =
    redisClient.del(keys: _*)
}

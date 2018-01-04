package com.ruchij.services.kv

import play.api.libs.json.{Reads, Writes}

import scala.concurrent.Future
import scalaz.OptionT

trait KeyValueStore
{
  def put[T](key: String, item: T)(implicit Writes: Writes[T]): Future[T]

  def get[T](key: String)(implicit reads: Reads[T]): OptionT[Future, T]

  def remove(keys: String*): Future[Long]
}
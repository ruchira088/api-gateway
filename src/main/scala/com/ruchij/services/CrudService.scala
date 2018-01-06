package com.ruchij.services

import scala.concurrent.Future
import scalaz.OptionT

trait CrudService[T]
{
  type NewItem

  def create(newItem: NewItem): Future[T]

  def getById(id: String): OptionT[Future, T]

  def update(id: String, item: T): Future[T]

  def delete(id: String): Future[T]
}

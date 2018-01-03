package com.ruchij.daos

import com.ruchij.models.User

import scala.concurrent.Future
import scalaz.OptionT

trait UserDao
{
  type InsertionResult

  def create(user: User): Future[InsertionResult]

  def getByUsername(username: String): OptionT[Future, User]
}
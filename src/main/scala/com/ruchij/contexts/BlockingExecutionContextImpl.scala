package com.ruchij.contexts

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.dispatch.MessageDispatcher

@Singleton
class BlockingExecutionContextImpl @Inject()(actorSystem: ActorSystem) extends BlockingExecutionContext
{
  val dispatcher: MessageDispatcher = actorSystem.dispatchers.lookup("blocking-execution-context")

  override def execute(runnable: Runnable): Unit = dispatcher.execute(runnable)

  override def reportFailure(cause: Throwable): Unit = dispatcher.reportFailure(cause)
}

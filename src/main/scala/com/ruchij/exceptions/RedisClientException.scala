package com.ruchij.exceptions

object RedisClientException extends Exception
{
  override def getMessage: String = "Redis client exception."
}

package com.ruchij.exceptions

case class EnvValueUndefinedException(name: String) extends Exception
{
  override def getMessage: String = s""""$name" is NOT defined in the environment"""
}

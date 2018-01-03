package com.ruchij.exceptions

case class UsernameAlreadyExistsException(username: String) extends Exception
{
  override def getMessage: String = s""""$username" is ALREADY assigned to a user."""
}

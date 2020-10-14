package com.balbilo.registration.json

import com.balbilo.registration.model.ValueClasses._
import io.circe.Decoder.{decodeLocalDate, decodeString}
import io.circe.Encoder.{encodeLocalDate, encodeString}
import io.circe.{Decoder, Encoder}

trait ValueClasses {

  implicit val fullNameEncoder: Encoder[FullName] = encodeString.contramap(_.value)
  implicit val fullNameDecoder: Decoder[FullName] = decodeString.map(FullName.apply)

  implicit val emailEncoder: Encoder[Email] = encodeString.contramap(_.value)
  implicit val emailDecoder: Decoder[Email] = decodeString.map(Email.apply)

  implicit val passwordEncoder: Encoder[Password] = encodeString.contramap(_.value)
  implicit val passwordDecoder: Decoder[Password] = decodeString.map(Password)

  implicit val dateOfBirthEncoder: Encoder[DateOfBirth] = encodeLocalDate.contramap(_.value)
  implicit val dateOfBirthDecoder: Decoder[DateOfBirth] = decodeLocalDate.map(DateOfBirth.apply)

}
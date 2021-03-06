package com.balbilo.user.json

import com.balbilo.user.model.ServerError
import com.balbilo.user.testkit.{AnyWordSpecBase, PropertySpecBase}
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, Encoder}

class ServerErrorJsonSpec extends AnyWordSpecBase with PropertySpecBase with FromJson {

  "ServerErrorJson" should {

    "encode and decode Server error with code and message" in forAll { serverError: ServerError =>
      val json = s"""{"code":"${serverError.code}","message":"${serverError.message}"}"""
      decoder[ServerError](json) shouldBe Right(serverError)
      encoder(serverError).noSpaces shouldBe json.trim
    }

    "perform a roundTrip" in forAll { serverError: ServerError =>
      decoder[ServerError](encoder(serverError).noSpaces) shouldBe Right(serverError)
    }

  }

  private def encoder[A : Encoder](value: A) = value.asJson

  private def decoder[A : Decoder](json: String) = decode[A](json)
}

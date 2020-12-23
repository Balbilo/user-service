package com.balbilo.user.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, entity, onComplete, path, post}
import akka.http.scaladsl.server.Route
import com.balbilo.user.json.{serverErrorEncoder, userTokenDecoder, userTokenEncoder}
import com.balbilo.user.model.RegisterError.UserRegistered
import com.balbilo.user.model.{RegisterError, UserToken}
import com.balbilo.user.repository.RegistrationRepository
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.util.{Failure, Success, Try}

final case class Register(repository: RegistrationRepository)(implicit system: ActorSystem) {

  private implicit val dispatcher = system.dispatcher

  val route =
    (path("register") & post & entity(as[UserToken])) { userToken =>
      val register = repository.registerUser(userToken)
      onComplete(register)(handleResponse)
    }

  private def handleResponse(register: Try[Either[RegisterError, UserToken]]): Route =
    register match {
      case Success(success)   => handleResponse(success)
      case Failure(exception) => complete(exception)
    }

  private def handleResponse(register: Either[RegisterError, UserToken]): Route =
    register match {
      case Right(userToken) => complete(StatusCodes.OK -> userToken)
      case Left(error)      => handlerError(error)
    }

  private def handlerError(error: RegisterError): Route =
    error match {
      case UserRegistered => complete(StatusCodes.AlreadyReported -> UserRegistered)
    }
}
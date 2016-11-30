package com.datiobd.demo.services

import com.datiobd.demo.model.{Question, QuestionUpdate}
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by AlvaroNav on 24/11/16.
  */
class QuestionService(implicit val executionContext: ExecutionContext) {

  var questions = Vector.empty[Question]

  def create(question: Question): Future[Option[String]] = Future {
    questions.find(_.id == question.id) match {
      case Some(q) => None // Conflict! id is already taken
      case None =>
        questions = questions :+ question
        Some(question.id)
    }
  }

  def get(id: String): Future[Option[Question]] = Future {
    questions.find(_.id == id)
  }

  def update(id: String, update: QuestionUpdate): Future[Option[Question]] = {

    def updateEntity(question: Question): Question = {
      val title = update.title.getOrElse(question.title)
      val text = update.text.getOrElse(question.text)
      Question(id, title, text)
    }

    get(id).flatMap { maybeQuestion =>
      maybeQuestion match {
        case None => Future {
          None
        } // No question found, nothing to update
        case Some(question) =>
          val updatedQuestion = updateEntity(question)
          delete(id).flatMap { _ =>
            create(updatedQuestion).map(_ => Some(updatedQuestion))
          }
      }
    }
  }

  def delete(id: String): Future[Unit] = Future {
    questions = questions.filterNot(_.id == id)
  }
}

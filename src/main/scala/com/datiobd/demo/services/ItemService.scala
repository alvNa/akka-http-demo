package com.datiobd.demo.services

import com.datiobd.demo.model.{Item}
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by AlvaroNav on 24/11/16.
  */
class ItemService(implicit val executionContext: ExecutionContext) {

  var items = Vector.empty[Item]

  def create(item: Item): Future[Option[Integer]] = Future {
    items.find(_.id == item.id) match {
      case Some(q) => None // Conflict! id is already taken
      case None =>
        items = items :+ item
        Some(item.id)
    }
  }

  def get(id: Integer): Future[Option[Item]] = Future {
    items.find(_.id == id)
  }

  def update(id: Integer, update: Item): Future[Option[Item]] = {

    def updateEntity(item: Item): Item = {
      Item(id, item.name)
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

  def delete(id: Integer): Future[Unit] = Future {
    items = items.filterNot(_.id == id)
  }
}

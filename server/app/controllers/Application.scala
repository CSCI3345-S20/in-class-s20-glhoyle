package controllers

import javax.inject._
import scala.concurrent.Future

import edu.trinity.videoquizreact.shared.SharedMessages
import play.api.mvc._

@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def index = Action {
    Ok(views.html.index(SharedMessages.itWorks))
  }

  def tempPage = Action {
    Ok(views.html.tempPage(models.TempModel.data.map(_.year).distinct))
  }

  def temps(month: Int, year: Int) =  Action {
    Ok(views.html.tempMonth(month, year, models.TempModel.data.
      filter(td => td.year == year && td.month == month)))
  }

  def tempsPost = Action.async { request => Future {
    val oparams = request.body.asFormUrlEncoded
    oparams.map { params =>
      try {
        val month = params("month")(0).toInt
        val year = params("year")(0).toInt
        Ok(views.html.tempMonth(month, year, models.TempModel.data.
           filter(td => td.year == year && td.month == month)))
      } catch {
        case ex: NumberFormatException => Ok("NaN")
      }
    }.getOrElse(Ok("You screwed up.")) }
  }

}

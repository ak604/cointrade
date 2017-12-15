package controllers


import javax.inject._
import play.api._
import play.api.mvc._
import javax.inject.Inject
import score._
import scala.concurrent.ExecutionContext


@Singleton
class CoinScore @Inject()(cc: ControllerComponents, val cal : Calc) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def topn() = Action.async { implicit request: Request[AnyContent] =>
    val ret = cal.topN(3)
    implicit val ec: ExecutionContext = cc.executionContext
    ret.map{f=>
      Ok(f)
    }
  }
}
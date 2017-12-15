package controllers


import javax.inject._
import play.api._
import play.api.mvc._
import javax.inject.Inject
import score._
import scala.concurrent.ExecutionContext
import models._


@Singleton
class CoinTransactions @Inject()(cc: ControllerComponents, val cal : Calc) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def purchase( userId : Long,  coinId: String,  amount: Long,  unitPrice: Long, exchangeId:String)
    = Action { implicit request: Request[AnyContent] =>
      val userPurchase=new UserPurchase(userId,coinId,amount,unitPrice,exchangeId)
      userPurchase.save()
      Ok("saved")
  }
}
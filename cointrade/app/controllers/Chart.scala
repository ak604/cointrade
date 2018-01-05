package controllers


import javax.inject._
import play.api._
import play.api.mvc._
import javax.inject.Inject
import score._
import scala.concurrent.ExecutionContext
import services._
import play.api.libs.json._


@Singleton
class Chart @Inject()(cc: ControllerComponents, val coinPriceService : CoinPriceService) extends AbstractController(cc) {
  
  implicit val ec: ExecutionContext = cc.executionContext
  def price(coin:String) = Action { implicit request: Request[AnyContent] =>
     Ok(views.html.chart(coin))
  }
  
   def priceJson(coin:String) = Action.async { implicit request: Request[AnyContent] =>
     val prices= coinPriceService.lastNBlocks(coin, 18, 0)
     val ret= prices.map{ lst=>
       Json.toJson(lst)
     }
    ret.map{retVal =>
      Ok(retVal)
    }
  }
}
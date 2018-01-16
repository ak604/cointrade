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
class Chart @Inject()(cc: ControllerComponents, val marketPriceService : MarketPriceService) extends AbstractController(cc) {
  
  implicit val ec: ExecutionContext = cc.executionContext
  
   def prices(market:String) = Action.async { implicit request: Request[AnyContent] =>
     val prices= marketPriceService.lastNBlocks(market, 18, 0)
     val ret= prices.map{ lst=>
       Json.toJson(lst)
     }
    ret.map{retVal =>
      Ok(retVal)
    }
  }
}
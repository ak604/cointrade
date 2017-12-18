package controllers


import javax.inject._
import play.api._
import play.api.mvc._
import javax.inject.Inject
import score._
import scala.concurrent.ExecutionContext
import models._
import services._
import scala.concurrent._
import constants._


@Singleton
class CoinTransactions @Inject()(cc: ControllerComponents, val coinPriceService : CoinPriceService) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def purchase( userId : Long,  coinId: String,  amount: Long,  unitprice: Long, exchangeId:String)
    = Action { implicit request: Request[AnyContent] =>
      
      implicit val ExecutionContext = cc.executionContext
      
       val price= 
         if(unitprice==0)
             coinPriceService.getLastPrice(coinId,AppConstants.granularity)
         else
             Future{ unitprice}
      val amnt = if(amount==0) 1 else amount
      price.onComplete{ prc=>
      val userPurchase=new UserPurchase(userId,coinId,amnt,prc.get,exchangeId)
      userPurchase.save()

      }
       Ok("saved")
    
  }
  
}
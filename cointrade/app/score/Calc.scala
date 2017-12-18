package score

import data._
import javax.inject._
import services._
import scala.concurrent.ExecutionContext
import play.api.Logger
import scala.concurrent.Future
import scala.util._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import models._
import collection.mutable._
import scala.collection.JavaConverters._
import scala.collection.immutable.Range
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions.asScalaBuffer
import constants._

@Singleton
class Calc @Inject()(val coinPrice : CoinPriceService) {

	implicit val ec= ExecutionContext.global
			def topN(n :Int)={
					var futLst :List[Future[(String,Long)]]  = Nil
							Coins.lst.take(100).foreach{coin=>
							futLst=  futLst++List(score(coin))
	}

	val resFut = Future.sequence(futLst)
			val result= resFut.map{lst=>
			val  sorted =lst.sortBy{ case(coin,score) => score}
			sorted.reverse.map{ case(coin,score) =>
			coin + "  => "+ score
			}.mkString("\n")
	}
	
  val sellFut = sellScore()
 sellFut.zipWith(result){case(x,y) =>
   x +"\n"+y
 }

	}

	def sellScore()={
	  
	  val lst : List[UserPurchase]= asScalaBuffer(UserPurchase.allBought()).toList
	  val futLst =lst.map{up=>
	    	val values = coinPrice.lastNHours(up.coinId, 48, AppConstants.granularity,up.timestamp);
	      values.map{ lst=>
	     val sellScore =Calc.sellScore(lst,up.unitprice)
	     val currentProfit = (100*(lst.last - up.unitprice))/up.unitprice
	     up.coinId + " => " + up.amount+ " => "+
	     up.exchangeId + " => " + up.timestamp+ " => " + up.unitprice + " => "+ sellScore +" => "+ currentProfit+"%"
	    }
	  }
	  val resFut = Future.sequence(futLst)
	  val result= resFut.map{lst=>
	    lst.mkString("\n")
	  }
	  result
	}

	def score(coin:String)={
			val values = coinPrice.lastNHours(coin, 18, AppConstants.granularity,0);
			values.map{lst =>
			(coin,Calc.buyScore(lst))
			}
	}
}

object Calc{
			val weightByHour = Array(1,2,4,8,10,8,6,4,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)
			val div:Long=3600/AppConstants.granularity
			val normalizeCnt=5
			val percentFactor =1000
			val toleranceFactor =2
			
			val tolProfit=40
			val tolLoss=20

			def percent(first:Long, second:Long)={
	      ((first-second)*Calc.percentFactor)/second
	    }
			def repalceZeroWithLastElement(lst : List[Long]):List[Long]={
					lst.scanLeft(0L){(x,y) =>
					  val xx=x
					  val yy=y
					if(y==0) x else y
					}.drop(1)
			}
	
			def normalize(lst:List[Long])={ 		
					val x =repalceZeroWithLastElement(lst).zipWithIndex
					x.filter { case(value,i) => ((i+div)%div < normalizeCnt)  }
					.map{ case(e,_)=> e}
					.grouped(normalizeCnt)
					.map{ lst5 => 
					(lst5.sum/lst5.length)
					}.toList
			}

			def positiveHike(normalized:List[Long])={  	  
					val hikes =normalized.dropRight(1).zip(normalized.drop(1)).map{
					case(x,y) => ((Calc.percentFactor*(y-x))/x)
					}
					val res = (hikes.reverse.span(hike=> hike> - Calc.toleranceFactor)._1)
					res
			}


			def buyScore(lst:List[Long])={
					val normalized= normalize(lst)
							val positiveHikes= positiveHike(normalized)
					buyScoreGivenHikes(positiveHikes)
			}
			
			def buyScoreGivenHikes(positiveHikes:List[Long])={
			  	val nHours = positiveHikes.length
				if(nHours>0){(positiveHikes.sum * weightByHour(nHours-1) )/ nHours}else 0
			}
			
			def sellScore(lst: List[Long], unitprice:Long)={
			   val localMax=lst.max
	      val currentPrice= lst.last
	      
	      val profitWhenMax = Calc.percent(localMax,unitprice)
	      val currProfit = Calc.percent(currentPrice,unitprice)
	      val profitScore =  Calc.tolProfit-Calc.percent(localMax, currentPrice) 
	      
	      val lossScore = currProfit+Calc.tolLoss
	      val sellScore = if(lossScore>0) profitScore else lossScore
	      sellScore
			}

}
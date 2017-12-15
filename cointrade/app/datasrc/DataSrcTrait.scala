package datasrc

import scala.concurrent._

trait DataSrcTrait {
 
  def priceInTimeRange(coin:String,startTime : Long, endTime : Long) :Future[List[Long]]
}
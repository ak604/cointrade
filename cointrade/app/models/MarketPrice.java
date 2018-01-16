package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.Model;

@Entity
@Table(name="market_prices")
public class MarketPrice extends Model {
	
	@Id
	public Long marketPriceId;
    public String marketName;
	public Double bid;
    public Double ask;
    public Double last;
    public Long timestamp;
    
    public MarketPrice(String marketName, Double bid, Double ask, Double last, Long timestamp) {
 		this.marketName = marketName;
 		this.bid = bid;
 		this.ask = ask;
 		this.last = last;
 		this.timestamp = timestamp;
 	}
    
   public static final Finder<Long, MarketPrice> find = new Finder<>(MarketPrice.class);  
   
   public static List<MarketPrice> valuesBetween(String marketName,Long startTime, Long endTime) {
	
	  return find.query().where()
	   .ge("timestamp", startTime)
	   .le("timestamp", endTime)
	   .eq("market_name", marketName)
       .orderBy("timestamp asc")
      .findList();
   }
   
   public Double getPrice() {
	   return last;
   }
   
}
package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.Model;
import play.Logger;

@Entity
@Table(name="coin_prices")
public class CoinPrice extends Model {
	
	@Id
	public Long coinPriceId;
    public String coinId;
    
    public Long price;
    public Long timestamp;
    
   public static final Finder<Long, CoinPrice> find = new Finder<>(CoinPrice.class);
    
   public CoinPrice( Long userId, String coinId,  Long price,  Long timestamp) {
	   this.coinId=coinId;
	   this.timestamp= timestamp;
	   this.price=price;
   }
   
   public static List<CoinPrice> valuesBetween(String coinId,Long startTime, Long endTime) {
	
	  return find.query().where()
	   .ge("timestamp", startTime)
	   .le("timestamp", endTime)
	   .eq("coin_id", coinId)
       .orderBy("timestamp desc")
      .findList();
   }
   
}
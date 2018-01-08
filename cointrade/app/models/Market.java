package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.Model;

@Entity
@Table(name="markets")
public class Market extends Model {
	
	@Id
	public String marketName;
    public String marketCurrency;
    public String baseCurrency;
    public Double minTradeSize;
    public Boolean isActive; 
    
    public Market(String marketName, String marketCurrency, String baseCurrency, Double minTradeSize,
			Boolean isActive) {
		this.marketName = marketName;
		this.marketCurrency = marketCurrency;
		this.baseCurrency = baseCurrency;
		this.minTradeSize = minTradeSize;
		this.isActive = isActive;
	}

    public static final Finder<String, Market> find = new Finder<>(Market.class);
    
    
    public static void truncate() {
    		Ebean.createSqlUpdate("delete from markets").execute();
    }
    public static List<Market> allActiveMarkets(String coinId,Long startTime, Long endTime) {
	  return find.query().where()
	   .eq("isActive", true)
      .findList();
   }
    
}
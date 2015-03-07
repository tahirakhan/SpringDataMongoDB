package com.tahir.project.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.tahir.project.dao.StockDao;
import com.tahir.project.model.Stock;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by Tahir on 3/7/15.
 */
@Repository
public class StockDaoImpl extends MongoDaoImpl implements StockDao {

  private void ensureIndex() {
    DBCollection coll = this.mongoOperations.getCollection(COLLECTION_STOCK);
    BasicDBObject obj = new BasicDBObject();
    obj.put("stockId", 1);
    coll.ensureIndex(obj, "stockId_idx", false);
  }
  @Override
  public Stock save(Stock stock) {

    ensureIndex();
    insert(stock, COLLECTION_STOCK);

    return stock;
  }

  @Override
  public Stock update(Stock stock) {
    final Query query = new Query(where("_id").is(stock.getStockId()).and("deleted").is(false));

    modify(query, stock, Stock.class, COLLECTION_STOCK);
    return stock;
  }

  @Override
  public boolean delete(String stockId) {
    final Query query = new Query(where("_id").in(stockId).and("deleted").is(false));

    int status =
        updateMulti(query, Update.update("deleted", new Boolean(true)), COLLECTION_STOCK);

    return (status == 1);
  }

  @Override
  public Stock findByStockId(String stockId) {
    final Query query = new Query(where("_id").is(stockId).and("deleted").is(false));

    List list = find(query, Stock.class, COLLECTION_STOCK);

    if (list != null && list.size() > 0) {
      return (Stock) list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Stock> findAll() {
    final Query query =
        new Query(where("deleted").is(false));

    return find(query, Stock.class, COLLECTION_STOCK);
  }
}

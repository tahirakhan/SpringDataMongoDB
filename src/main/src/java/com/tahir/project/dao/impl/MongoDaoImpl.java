package com.tahir.project.dao.impl;

/**
 * Created by Tahir on 3/8/15.
 */

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.WriteResult;
import com.tahir.project.dao.MongoDao;

/**
 * User: SURYA MANDADAPU Date: 9/23/12 Time: 5:03 PM
 */
public abstract class MongoDaoImpl implements MongoDao {
  protected final static Logger LOG = LoggerFactory.getLogger(MongoDaoImpl.class);

  @Autowired
  protected MongoOperations mongoOperations;

  public void insert(final Object object, final String collection) {
    mongoOperations.insert(object, collection);
  }

  public void batchInsert(List basicDBList, final String collection) {
    mongoOperations.insert(basicDBList, collection);
  }

  public long count(Query query, String collection) {
    return mongoOperations.count(query, collection);
  }


  // this returns previous version of the object, do not use the return value unless
  // you need the old version before the update
  @SuppressWarnings("unchecked")
  public Object modify(Query query, final Object object, Class claz, String collection) {
    BasicDBObject dbDoc = new BasicDBObject();
    mongoOperations.getConverter().write(object, dbDoc);
    dbDoc.remove("_id");

    return mongoOperations.findAndModify(query, new BasicUpdate(dbDoc), claz, collection);
  }

  public List findAll(Class claz, String collection) {
    return mongoOperations.findAll(claz, collection);
  }

  public List find(Query query, Class claz, String collection) {
    return mongoOperations.find(query, claz, collection);
  }

  public List find(Query query, Class claz, String collection, String[] excludeFileds) {
    return mongoOperations.find(query, claz, collection);
  }

  public Object findById(String id, Class claz, String collection) {
    return mongoOperations.findById(id, claz, collection);
  }

  public Object findModify(Query query, Update update, Class claz, String collection) {
    return mongoOperations.findAndModify(query, update, claz, collection);
  }

  public void save(final Object object, String collection) {
    mongoOperations.save(object, collection);
  }

  public int modify(Query query, String[] columns, Object[] values, String collection) {
    int i = 0;
    BasicDBObject newValues = new BasicDBObject(columns[i], values[i]);
    for (String column : columns) {
      if (column != null && !"".equals(column.trim())) {
        newValues.append(column, values[i]);
      }
      i++;
    }
    BasicDBObject set = new BasicDBObject("$set", newValues);
    Update bupdate = new BasicUpdate(set);
    return this.updateMulti(query, bupdate, collection);
  }

  public int updateMulti(Query query, Update update, String collection) {
    WriteResult wr = mongoOperations.updateMulti(query, update, collection);
    return getWriteResultStatus(wr);
  }

  private int getWriteResultStatus(WriteResult writeResult) {
    CommandResult commandResult = writeResult.getLastError();
    if (commandResult != null && commandResult.ok()) {
      return 1;
    }
    return 0;
  }


}
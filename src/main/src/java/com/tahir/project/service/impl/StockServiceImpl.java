package com.tahir.project.service.impl;


import com.tahir.project.dao.StockDao;
import com.tahir.project.model.Stock;
import com.tahir.project.service.StockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



/**
 * Created by Tahir on 3/7/15.
 */
@Transactional
@Service
public class StockServiceImpl implements StockService{

  @Autowired
  StockDao dao;

  @Override
  public Stock save(Stock stock) {
    return dao.save(stock);
  }

  @Override
  public Stock update(Stock stock) {
    return dao.update(stock);
  }

  @Override
  public boolean delete(String stockId) {
    return dao.delete(stockId);
  }

  @Override
  public Stock findByStockId(String stockId) {
    return dao.findByStockId(stockId);
  }

  @Override
  public List<Stock> findAll() {
    return dao.findAll();
  }
}

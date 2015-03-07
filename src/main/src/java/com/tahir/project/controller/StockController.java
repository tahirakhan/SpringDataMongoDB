package com.tahir.project.controller;

/**
 * Created by Tahir on 3/7/15.
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tahir.project.model.Stock;
import com.tahir.project.service.StockService;

@Controller
@RequestMapping("/")
public class StockController {

  @Autowired
  StockService service;
  /*
   * This method will list all existing employees.
   */
  @RequestMapping(value = { "/list" }, method = RequestMethod.GET)
  @ResponseBody
  public List<Stock> listStocks(ModelMap model) {

    List<Stock> stocks = service.findAll();
    model.addAttribute("stocks", stocks);
    return stocks;
  }

  /*
     * This method will provide the medium to add a new employee.
     */
  @RequestMapping(value = { "/save" }, method = RequestMethod.POST)
  public @ResponseBody Stock  saveStock(@RequestBody Stock stock) {
    service.save(stock);
    return stock;
  }

  /*
   * This method will provide the medium to add a new employee.
   */
  @RequestMapping(value = { "/update" }, method = RequestMethod.PUT)
  public @ResponseBody Stock  updateStock(@RequestBody Stock stock) {
    service.update(stock);
    return stock;
  }

  /*
   * This method will provide the medium to add a new employee.
   */
  @RequestMapping(produces = "application/json", value = { "/stock/{stock_id}" }, method = RequestMethod.GET)
  public @ResponseBody Stock  getStock(@PathVariable("stock_id") String stock_id) {
    return service.findByStockId(stock_id);
  }

  /*
  * This method will provide the medium to add a new employee.
  */
  @RequestMapping(value = { "/stock/{stock_id}" }, method = RequestMethod.DELETE)
  public @ResponseBody void  deleteStock(@PathVariable("stock_id") String stock_id) {
    service.delete(stock_id);
  }

}

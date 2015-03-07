package com.tahir.project.model;

/**
 * Created by Tahir on 3/7/15.
 */
import java.io.Serializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import lombok.Data;

public @Data
class Stock implements java.io.Serializable {

  @Id
  private String stockId;
  private String unitId;
  private String stockCode;

  private String stockName;
  boolean deleted;

}
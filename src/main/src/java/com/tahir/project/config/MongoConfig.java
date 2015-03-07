package com.tahir.project.config;

/**
 * Created by Tahir on 3/7/15.
 */

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.util.Assert;

import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;


@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

  /**
   * This controls whether the system retries automatically on connection errors.
   */
  @Value("${mongo.server.autoConnectRetry}")
  private String autoConnectRetry;

  /**
   * The number of connections allowed per host (the pool size, per host)
   */
  @Value("${mongo.server.connectionsPerHost}")
  private String connectionsPerHost;

  /**
   * The connection timeout in milliseconds; this is for establishing the socket connections (open).
   */
  @Value("${mongo.server.connectTimeout}")
  private String connectTimeout;

  /**
   * The max wait time for a blocking thread for a connection from the pool
   */
  @Value("${mongo.server.maxWaitTime}")
  private String maxWaitTime;

  /**
   *
   */
  @Value("${mongo.server.socketTimeout}")
  private String socketTimeout;

  /**
   * multiplier for connectionsPerHost for # of threads that can block if connectionsPerHost is 10,
   * and threadsAllowedToBlockForConnectionMultiplier is 5, then 50 threads can block more than that
   * and an exception will be thrown
   */
  @Value("${mongo.server.threadsAllowedToBlockForConnectionMultiplier}")
  private String threadsAllowedToBlockForConnectionMultiplier;

  @Value("${mongo.server.replicaSets}")
  private String mongoreplicaSets;

  private List<ServerAddress> serverAddresses = new ArrayList<>();

  public boolean addServerAddress(final ServerAddress serverAddress) {
    Assert.notNull(serverAddress, "serverAddress is required");
    return serverAddresses.add(serverAddress);
  }

  public void init() {
    if (mongoreplicaSets != null && !"".equals(mongoreplicaSets.trim())) {
      StringTokenizer tokens = new StringTokenizer(mongoreplicaSets, ",");
      while (tokens.hasMoreTokens()) {
        String token = tokens.nextToken();
        if (token != null && !"".equalsIgnoreCase(token.trim())) {
          StringTokenizer hostPort = new StringTokenizer(token, ":");
          try {
            addServerAddress(new ServerAddress(hostPort.nextToken(), Integer.parseInt(hostPort
                                                                                          .nextToken())));
          } catch (UnknownHostException e) {
            e.printStackTrace();
          }
        }
      }
    }

  }


  @Bean
  public MongoFactoryBean mongoFactoryBean() {

    init();

    MongoFactoryBean mongoFactoryBean = new MongoFactoryBean();
    if (serverAddresses.size() == 1) {
      final ServerAddress serverAddress = serverAddresses.iterator().next();
      mongoFactoryBean.setHost(serverAddress.getHost());
      mongoFactoryBean.setPort(serverAddress.getPort());
    } else {
      final com.mongodb.ServerAddress[] mongoServerAddresses =
          new com.mongodb.ServerAddress[serverAddresses.size()];
      int i = 0;
      for (final ServerAddress serverAddress : serverAddresses) {
        mongoServerAddresses[i++] = serverAddress;
      }
      mongoFactoryBean.setReplicaSetSeeds(mongoServerAddresses);
    }
    mongoFactoryBean.setMongoOptions(createMongoOptions());
    mongoFactoryBean.setWriteConcern(WriteConcern.FSYNC_SAFE);

    return mongoFactoryBean;


  }

  public MongoOptions createMongoOptions() {
    final MongoOptions options = new MongoOptions();
    options.autoConnectRetry = Boolean.getBoolean(autoConnectRetry);
    options.connectionsPerHost = Integer.parseInt(connectionsPerHost);
    options.connectTimeout = Integer.parseInt(connectTimeout);
    options.maxWaitTime = Integer.parseInt(maxWaitTime);
    options.socketTimeout = Integer.parseInt(socketTimeout);
    options.threadsAllowedToBlockForConnectionMultiplier =
        Integer.parseInt(threadsAllowedToBlockForConnectionMultiplier);
    return options;
  }


  @Override
  public String getDatabaseName() {
    return "test";
  }


  public String getConnectionsPerHost() {
    return connectionsPerHost;
  }

  public String getConnectTimeout() {
    return connectTimeout;
  }

  public String getMaxWaitTime() {
    return maxWaitTime;
  }

  public List<ServerAddress> getServerAddresses() {
    return serverAddresses;
  }

  public String getSocketTimeout() {
    return socketTimeout;
  }

  public String getThreadsAllowedToBlockForConnectionMultiplier() {
    return threadsAllowedToBlockForConnectionMultiplier;
  }



  public String isAutoConnectRetry() {
    return autoConnectRetry;
  }

  public void setAutoConnectRetry(final String autoConnectRetry) {
    this.autoConnectRetry = autoConnectRetry;
  }

  public void setConnectionsPerHost(final String connectionsPerHost) {
    this.connectionsPerHost = connectionsPerHost;
  }

  public void setConnectTimeout(final String connectTimeout) {
    this.connectTimeout = connectTimeout;
  }


  public void setMaxWaitTime(final String maxWaitTime) {
    this.maxWaitTime = maxWaitTime;
  }

  public void setServerAddresses(final List<ServerAddress> serverAddresses) {
    this.serverAddresses = serverAddresses;
  }

  public void setSocketTimeout(final String socketTimeout) {
    this.socketTimeout = socketTimeout;
  }

  public void setThreadsAllowedToBlockForConnectionMultiplier(
      final String threadsAllowedToBlockForConnectionMultiplier) {
    this.threadsAllowedToBlockForConnectionMultiplier =
        threadsAllowedToBlockForConnectionMultiplier;
  }


  @Override
  public Mongo mongo() throws Exception {
    return mongoFactoryBean().getObject();
  }


  // We need _class field for polymorphism in Spring Data Mongo, 01-03-15, Stefan
  //
  // @Bean
  // public MongoOperations mongoOperations() throws Exception {
  // //remove _class
  // MappingMongoConverter converter =
  // new MappingMongoConverter(mongoDbFactory(), new MongoMappingContext());
  // converter.setTypeMapper(new DefaultMongoTypeMapper(null));
  //
  // MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(), converter);
  // return mongoTemplate;
  // }


}
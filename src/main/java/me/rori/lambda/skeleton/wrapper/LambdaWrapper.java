package me.rori.lambda.skeleton.wrapper;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import lombok.Data;

import java.util.Date;

@Data
public abstract class LambdaWrapper<I> {

  public abstract String handleRequest(I input, Context context);

  private LambdaLogger logger;
  private String lambdaName;
  private long startTime =  new Date().getTime();

  public String processRequest(I input, Context context) {
    try {
      Date date = new Date();
      long timeMillisecs = date.getTime();

      this.lambdaName = context.getFunctionName();
      String returnValue = handleRequest(input, context);

      timeMillisecs = date.getTime() - timeMillisecs;
      getLogger().log(String.format("Processing message TODO, completed in %d milliseconds", timeMillisecs));

      return returnValue;
    } catch (Exception e) {
      getLogger().log(e.getMessage());
      return LambdaStatus.ERROR;
    }
  }

  public void logInfo(String message) {
    getLogger().log(message);
  }

  public void logException(String message, Exception e) {
    getLogger().log(message + e);
  }
}
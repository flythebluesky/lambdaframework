package me.rori.lambda.skeleton;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import me.rori.lambda.skeleton.wrapper.LambdaSQSWrapper;

public class LambdaSQSFunctionExample extends LambdaSQSWrapper {

  @Override
  public void processSQSMessage(final SQSEvent.SQSMessage sqsMessage) throws Exception {
    // This is where an message is handled.
    // A message is logged before and after each queue event is processed
    // If the message is processed successfully, it is deleted and will not be queued again
    // If the message cannot be process, throw an exception and the wrapper will log it the message will queued again

    getLogger().log("hello");
  }
}

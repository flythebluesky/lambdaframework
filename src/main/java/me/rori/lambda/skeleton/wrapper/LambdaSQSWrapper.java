package me.rori.lambda.skeleton.wrapper;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;

import java.util.Date;

public abstract class LambdaSQSWrapper extends LambdaWrapper<SQSEvent>
        implements RequestHandler<SQSEvent, String> {

  private final AmazonSQS sqsClient;

  public LambdaSQSWrapper() {
    this.sqsClient = AmazonSQSClientBuilder.defaultClient();
  }

  public abstract void processSQSMessage(SQSEvent.SQSMessage sqsMessage) throws Exception;

  @Override
  public String handleRequest(final SQSEvent input, final Context context) {
    try {
      input.getRecords().forEach(sqsMessage -> {
        String messageId = sqsMessage.getMessageId();
        try {
          getLogger().log(String.format("Processing message id='%s', starting", messageId));
          processSQSMessage(sqsMessage);
          markCompleted(sqsMessage);
        } catch (Exception e) {
          getLogger().log(String.format("Processing message id='%s', failed in %d milliseconds ",
                  messageId, getStartTime() - new Date().getTime()) + e);
        }
      });
      return LambdaStatus.OK;
    } catch (Exception e) {
      throw (e);
    }
  }

  private void markCompleted(SQSEvent.SQSMessage sqsMessage) {

    DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest();
    deleteMessageRequest.withQueueUrl(sqsMessage.getEventSource());
    deleteMessageRequest.setReceiptHandle(sqsMessage.getReceiptHandle());
    sqsClient.deleteMessage(deleteMessageRequest);
  }
}

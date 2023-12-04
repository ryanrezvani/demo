package com.example;
import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.logging.Payload.StringPayload;
import com.google.cloud.logging.Severity;
import java.util.Collections;
import io.grpc.LoadBalancerRegistry;
import io.grpc.internal.PickFirstLoadBalancerProvider;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
/**
 * Hello world!
 *
 */
public class App {  
    public static void main(String[] args) throws Exception {
    // The name of the log to write to
    String logName = "my-java-log"; // "my-log";
    String textPayload = "mysearchkeywordjava";
    LoadBalancerRegistry.getDefaultRegistry().register(new PickFirstLoadBalancerProvider());
    
    // Instantiates a client
    try (Logging logging = LoggingOptions.getDefaultInstance().getService()) {

      LogEntry entry =
          LogEntry.newBuilder(StringPayload.of(textPayload))
              .setSeverity(Severity.ERROR)
              .setLogName(logName)
              .setResource(MonitoredResource.newBuilder("global").build())
              .build();

      // Writes the log entry asynchronously
      logging.write(Collections.singleton(entry));

      // Optional - flush any pending log entries just before Logging is closed
      logging.flush();
    }
    System.out.printf("Logged: %s%n", textPayload);
    }

    public OpenTelemetry openTelemetry() {
      return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
    }

}

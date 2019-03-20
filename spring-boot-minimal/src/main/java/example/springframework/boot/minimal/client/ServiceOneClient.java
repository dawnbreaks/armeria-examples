package example.springframework.boot.minimal.client;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import me.lubin.proto.serviceone.ServiceOneGrpc;
import me.lubin.proto.serviceone.ServiceOneGrpc.ServiceOneBlockingStub;
import me.lubin.proto.serviceone.ServiceOneGrpc.ServiceOneStub;
import me.lubin.proto.serviceone.ServiceOneProto.HelloOneReq;
import me.lubin.proto.serviceone.ServiceOneProto.HelloOneRsp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 *author: lubin
 *Date:    2019/2/28
 */
public class ServiceOneClient {
    private final static Logger logger = LoggerFactory.getLogger(ServiceOneClient.class);
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
          .usePlaintext()
          .build();

        ServiceOneBlockingStub blockingServiceStub = ServiceOneGrpc.newBlockingStub(channel);
        System.out.println(blockingServiceStub.sayHello(HelloOneReq.newBuilder().setGreeting("hello world !").build()));
        ServiceOneStub serviceOneStub = ServiceOneGrpc.newStub(channel);
        for (int i = 0; i< 100; i ++) {
            HelloOneReq helloReq = HelloOneReq.newBuilder().setGreeting("hello world " + i).build();
            serviceOneStub.sayHello(helloReq, new StreamObserver<HelloOneRsp>() {
                @Override
                public void onNext(HelloOneRsp value) {
                    System.out.println(value.getGreeting());
                }

                @Override
                public void onError(Throwable t) {
                    logger.error("serviceOneStub.sayHello",t );
                }
                @Override
                public void onCompleted() {

                }
            });
        }
    }
}

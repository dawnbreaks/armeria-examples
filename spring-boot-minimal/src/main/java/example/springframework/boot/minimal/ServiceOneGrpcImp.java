package example.springframework.boot.minimal;


import io.grpc.stub.StreamObserver;
import me.lubin.proto.serviceone.ServiceOneGrpc.ServiceOneImplBase;
import me.lubin.proto.serviceone.ServiceOneProto.HelloOneReq;
import me.lubin.proto.serviceone.ServiceOneProto.HelloOneRsp;


/*
 *author: lubin
 *Date:    2019/2/28
 */
public class ServiceOneGrpcImp extends ServiceOneImplBase {
    @Override
    public void sayHello(HelloOneReq request, StreamObserver<HelloOneRsp> responseObserver) {
        responseObserver.onNext(HelloOneRsp.newBuilder().setGreeting("reply from ServiceOneImp: " + request.getGreeting() + "!!! haha").build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<HelloOneReq> streamTest(StreamObserver<HelloOneRsp> responseObserver) {
        return new StreamObserver<HelloOneReq>() {
            @Override
            public void onNext(HelloOneReq value) {
                responseObserver.onNext(HelloOneRsp.newBuilder().setGreeting(value.getGreeting()).build());
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}

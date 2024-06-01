package com.finbots.services;

import com.finbots.job.JobReply;
import com.finbots.job.JobRequest;
import com.finbots.job.JobServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class JobServiceClient {

    private final JobServiceGrpc.JobServiceBlockingStub blockingStub;

    public JobServiceClient() {
        String host = "localhost";
        int port = 9090;
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        blockingStub = JobServiceGrpc.newBlockingStub(channel);
    }

    public String createJob(String param) {
        JobRequest request = JobRequest.newBuilder()
                .setParam(param)
                .build();

        JobReply response = blockingStub.createJob(request);

        return response.getStatus();
    }
}

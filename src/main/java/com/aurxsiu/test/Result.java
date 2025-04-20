package com.aurxsiu.test;

public class Result<S,R>{
    private final S status;
    private final R result;

    public Result(S status, R result) {
        this.status = status;
        this.result = result;
    }

    public S getStatus() {
        return status;
    }

    public R getResult() {
        return result;
    }
}

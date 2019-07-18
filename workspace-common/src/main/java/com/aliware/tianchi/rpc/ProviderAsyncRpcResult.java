package com.aliware.tianchi.rpc;

import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.Result;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ProviderAsyncRpcResult extends AsyncRpcResult {
    private static final long serialVersionUID = -6925924956850004727L;

    public ProviderAsyncRpcResult(CompletableFuture<Object> future) {
        super(future);
    }

    public ProviderAsyncRpcResult(CompletableFuture<Object> future, boolean registerCallback) {
        super(future, registerCallback);
    }

    public ProviderAsyncRpcResult(CompletableFuture<Object> future, CompletableFuture<Result> rFuture, boolean registerCallback) {
        super(future, rFuture, registerCallback);
    }

    @Override
    public Object getValue() {
        return super.getValue();
    }

    @Override
    public Throwable getException() {
        return super.getException();
    }

    @Override
    public boolean hasException() {
        return super.hasException();
    }

    @Override
    public Object getResult() {
        return super.getResult();
    }

    @Override
    public CompletableFuture getValueFuture() {
        return super.getValueFuture();
    }

    @Override
    public CompletableFuture<Result> getResultFuture() {
        return super.getResultFuture();
    }

    @Override
    public void setResultFuture(CompletableFuture<Result> resultFuture) {
        super.setResultFuture(resultFuture);
    }

    @Override
    public Result getRpcResult() {
        return super.getRpcResult();
    }

    @Override
    public Object recreate() throws Throwable {
        return super.recreate();
    }

    @Override
    public void thenApplyWithContext(Function<Result, Result> fn) {
        super.thenApplyWithContext(fn);
    }

    @Override
    public Map<String, String> getAttachments() {
        return super.getAttachments();
    }

    @Override
    public void setAttachments(Map<String, String> map) {
        super.setAttachments(map);
    }

    @Override
    public void addAttachments(Map<String, String> map) {
        super.addAttachments(map);
    }

    @Override
    public String getAttachment(String key) {
        return super.getAttachment(key);
    }

    @Override
    public String getAttachment(String key, String defaultValue) {
        return super.getAttachment(key, defaultValue);
    }

    @Override
    public void setAttachment(String key, String value) {
        super.setAttachment(key, value);
    }
}

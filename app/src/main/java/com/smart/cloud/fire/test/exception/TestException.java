package com.smart.cloud.fire.test.exception;

public class TestException extends Exception {

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        System.out.print("own test");
    }
}

package com.example.kingsecurehub;

public class RequestThread extends Thread {

    private RequestController requestController;

    public RequestThread(RequestController rq){
        requestController=rq;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(10000);
                System.out.println("soy un hilo");
                requestController.notifyServer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

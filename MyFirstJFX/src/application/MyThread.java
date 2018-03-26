package application;


class MyThread extends Thread{
        @Override
        public void run() {
            for (;;){
                try { Thread.sleep(30);
                } catch (InterruptedException e) {    }
            }  
        } 
}
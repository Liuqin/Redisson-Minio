package org.qin.request_more;


import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @title: multiplenode
 * @decription:
 * @author: liuqin
 * @date: 2020/7/8 09:25
 */


@Component
@Slf4j
public class MultipleNode {

    @Autowired
    private MultipleNodeRequest multipleNodeRequest;


    public String getResult(List<PostData> nodes) {
        SynchronousQueue<String> queue = new SynchronousQueue<>();
        for (PostData postData : nodes) {
            new ThreadRequest(postData, queue).start();
        }
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void doneWork(ThreadRequest threadRequest) {
        String result = multipleNodeRequest.PostJson(threadRequest);
        if (result != null) {
            threadRequest.setCanceled(true);
            log.info("result=>:" + result);
            threadRequest.queue.offer(result);
        } else {
            System.out.println("当前线程没有拿到数据");
        }

    }


    @Data
    public class ThreadRequest extends Thread {
        private AtomicBoolean canceled;
        private SynchronousQueue<String> queue;
        private PostData postData;


        public ThreadRequest(PostData postData, SynchronousQueue<String> queue) {
            this.queue = queue;
            this.postData = postData;
            this.postData = postData;
            this.canceled = new AtomicBoolean(false);
        }


        @SneakyThrows
        @Override
        public void run() {
            doneWork(this);
        }


        public void setCanceled(boolean isCanceled) {
            canceled.set(isCanceled);
        }


        public boolean isCanceled() {
            return canceled.get();
        }
    }


}

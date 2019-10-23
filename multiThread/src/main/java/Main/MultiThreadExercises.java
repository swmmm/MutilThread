package Main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName: MultiThreadExercises
 * @description:
 * @author: swm
 * @create: 2019-10-10 14:28
 **/
public class MultiThreadExercises {
        public static void main(String[] args) throws InterruptedException {
//            AtomicReference<Integer> ref = new AtomicReference<>(new Integer(1000));
//
//            List<Thread> list = new ArrayList<>();
//            for (int i = 0; i < 1000; i++) {
//                Thread t = new Thread(new Task(ref), "Thread-" + i);
//                list.add(t);
//                t.start();
//            }
//
//            for (Thread t : list) {
//                t.join();
//            }
//
//            System.out.println(ref.get());    // 打印2000
            int a=1;
            a = ~a;
            System.out.println(a);
            a = ~a;
            System.out.println(~a);
        }

    }

    class Task implements Runnable {
        private AtomicReference<Integer> ref;

        Task(AtomicReference<Integer> ref) {
            this.ref = ref;
        }

        @Override
        public void run() {
            for (; ; ) {    //自旋操作
                Integer oldV = ref.get();
                if (ref.compareAndSet(oldV, oldV + 1))  // CAS操作
                    break;
            }
        }
}



package Main;

import java.io.*;
import java.net.HttpURLConnection;
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
            AtomicReference<Integer> ref = new AtomicReference<>(new Integer(1000));

            List<Thread> list = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                Thread t = new Thread(new Task(ref), "Thread-" + i);
                list.add(t);
                t.start();
            }

            for (Thread t : list) {
                t.join();
            }

            System.out.println(ref.get());    // 打印2000
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

    class IoTest{
        public static void main(String[] args) {
            new Thread(()->{
                InputStream is2 = null;
                try {
                    char bWrite[] = { 'a', '2', '3', '4', '6' };
                    OutputStream os = new FileOutputStream("E:\\bolo\\test.txt");
                    for (int x = 0; x < bWrite.length; x++) {
                        os.write(bWrite[x]); // writes the bytes
                        System.out.println("新线程休眠");
                        Thread.sleep(2000);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            try {
                InputStream is = new FileInputStream("E:\\bolo\\test.txt");
                int size = is.available();

                for (int i = 0; i < size; i++) {
                    System.out.print((char) is.read() + "  ");
                    System.out.println("开始休眠");
                    Thread.sleep(2000);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class NetTest{
        public static void main(String[] args) throws InterruptedException {
            Thread.sleep(5000);
            for (int i =0;i<100;i++){
                new Thread(()->{
                    System.out.println(Thread.currentThread().getName()+"开始访问");
                    for (int i1=0;i1<200;i1++) {
                        try {
                            URL url = new URL("http://112.74.174.246:8080/F_VideoManage/back/login/");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            InputStream inputStream = connection.getInputStream();
                            if (i1%10==0){
                                System.out.println(Thread.currentThread().getName()+"访问"+i1+"次了");
                                System.out.println(inputStream.read());
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }



package Main;

import entity.Res;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
class He{
    public static void main(String[] args) {
        List<String>strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());

        System.out.println("筛选列表: " + filtered);
        String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);
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

class IoTest {
    public static void main(String[] args) {
        new Thread(() -> {
            InputStream is2 = null;
            try {
                char bWrite[] = {'a', '2', '3', '4', '6'};
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class NetTest {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(5000);
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "开始访问");
                for (int i1 = 0; i1 < 200; i1++) {
                    try {
                        URL url = new URL("http://112.74.174.246:8080/F_VideoManage/back/login/");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = connection.getInputStream();
                        if (i1 % 10 == 0) {
                            System.out.println(Thread.currentThread().getName() + "访问" + i1 + "次了");
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

class ClassInitTest {
    private static Integer aa = new Integer(init("静态类成员赋值", 2));

    static {
        System.out.println("静态代码块执行");
    }

    private Integer a = new Integer(init("类成员赋值", 1));

    {
        System.out.println("普通代码块执行");
    }

    public ClassInitTest() {
        System.out.println("构造函数执行");
    }

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public static Integer init(String s, Integer i) {
        System.out.println(s);
        return i;
    }

    public static void main(String[] args) {
        long startTime=System.currentTimeMillis();
        for (int i=1;i<=10;i++){
            BiliSpiderMain.connectGetRes("https://api.bilibili.com/pgc/season/index/result?season_type=1&pagesize=20&type=1&page="+i);
        }
        long endTime=System.currentTimeMillis();
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

//        System.out.println(ClassInitTest.aa);//静态类成员赋值 静态代码块执行
//        ClassInitTest a = new ClassInitTest();//类成员赋值 普通代码块执行 构造函数执行
//        System.out.println(a.getA());
    }
}

//synchronized修饰静态成员变量
class SynTest {
    private Object lock = new Object();
    private static Object staticLock = new Object();

    public static void main(String[] args) throws InterruptedException {
        SynTest synTest = new SynTest();
        SynTest synTest1 = new SynTest();
        Thread t1 = new Thread(() -> {
            synchronized (staticLock) {
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + "----" + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (staticLock) {
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + "----" + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }
}

class SetTest{
    public static void main(String[] args) {
        Set<Map<String,String>> set = new HashSet<>();
        Map<String,String> map=new HashMap<>();
        map.put("aaa","aaa");
        Map<String,String> map1=new HashMap<>();
        map1.put("aaa","aaa");
        set.add(map);
        set.add(map1);
        System.out.println(set.size());
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        List<String> sublist = list.subList(0,5);
        System.out.println(sublist.size());
    }
}



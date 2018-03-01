import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.CountDownLatch;

public class ReadMultiFilesByThread {

    /**
     * @param args
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        final int thNum = 4;
        final String filePath3 = "E:\\新建文件夹.txt"; //
        final String filePath2 = "E:\\新建文件夹1.txt"; //
        final String filePath = "E:\\新建文件夹2.txt"; //
        final String filePath4 = "E:\\KuGou\\Temp\\baf41bdf5223ec2ef14b3acef23c0a77.txt"; //

        CountDownLatch doneSignal = new CountDownLatch(thNum);
        ReadFileThread2 r1 = new ReadFileThread2(doneSignal,filePath);
        ReadFileThread2 r2 = new ReadFileThread2(doneSignal,filePath2);
        ReadFileThread2 r3 = new ReadFileThread2(doneSignal,filePath3);
        ReadFileThread2 r4 = new ReadFileThread2(doneSignal,filePath4);
        r1.start();
        r2.start();
        r3.start();
        r4.start();
        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("===============================");
        System.out.println("The totally executed time: "+(endTime-startTime));
    }

}

class ReadFileThread2 extends Thread{
    private RandomAccessFile raf;
    private CountDownLatch doneSignal;
    private final int bufLen = 256;
    private String path;

    public ReadFileThread2(CountDownLatch doneSignal,String path){
        this.doneSignal = doneSignal;
        this.path = path;
    }


    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            raf = new RandomAccessFile(path,"rw");
            raf.seek(0);
            long contentLen = new File(path).length();
            long times = contentLen / bufLen +1;
            byte []buff = new byte[bufLen];
            int hasRead = 0;
            String result = null;
            for(int i=0;i<times;i++){
                hasRead = raf.read(buff);
                if(hasRead < 0){
                    break;
                }
                result = new String(buff,"gb2312");
            }
            doneSignal.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(getName() + " " + path +" total Time: " + (end - start));
    }
}
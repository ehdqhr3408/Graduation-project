package capstone.foodscouter.image;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class ImageController {
    @PostMapping("/image")
    public String upload(@RequestParam(value = "image") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String msg = null;
        String storageName = "C:\\Users\\ehdqh\\OneDrive\\사진\\스크린샷";
        try (Socket client = new Socket()) {
            System.out.println("[연결 요청]");
            InetSocketAddress serverIp = new InetSocketAddress("127.0.0.1", 9999);
            client.connect(serverIp);
            System.out.println("[연결 성공]");
            try {
                //송신부
                OutputStream fos = client.getOutputStream();
                File imagefile = new File(fileName);
                imagefile.createNewFile();
                FileOutputStream fileOutStream = new FileOutputStream(imagefile);
                fileOutStream.write(file.getBytes());
                byte[] imageBytes = Files.readAllBytes(Paths.get(String.valueOf(imagefile)));
                fos.write(imageBytes);
                fos.flush();
                System.out.println("[데이터 보내기 성공]");


                //수신부
                byte[] getdata = null;
                InputStream fis = client.getInputStream();
                getdata = new byte[100];
                int readByteCount = fis.read(getdata);
                msg = new String(getdata, 0 , readByteCount, "UTF-8");
                System.out.println("데이터 수신 성공"+msg);
                fis.close();


            } catch (Exception e) {
            }
            if (!client.isClosed()) {
                try {
                    client.close();
                } catch (IOException e1) {
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
}